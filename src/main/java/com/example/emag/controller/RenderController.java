package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.model.dao.PictureDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import static com.example.emag.utils.UserUtil.SESSION_KEY_LOGGED_USER;

@RestController
public class RenderController extends AbstractController {

    @Autowired
    private PictureDAO pictureDao;

    @Autowired
    private ProductDAO productDao;

    @Value("${image.path}")
    String pathDir;

    @RequestMapping(value = "/render/{productId}", produces = {MediaType.IMAGE_JPEG_VALUE, "application/json"}, method = RequestMethod.GET)
    public void renderImage(@PathVariable(name = "productId") long productId, HttpServletResponse response) throws IOException, SQLException {
        String pictureUrl = pictureDao.getPictureUrl(productId);
        ClassPathResource imgFile = new ClassPathResource("static"+ "\\" + pictureUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        StreamUtils.copy(imgFile.getInputStream(), outputStream);
    }

    @PostMapping("/products/uploadImg")
    public void uploadImage(@RequestParam("file") MultipartFile file,
                            @RequestParam("productId") Long productId,
                            HttpSession session) throws SQLException, AuthorizationException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForAdminRights(user);
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new BadRequestException("You cannot upload picture for product, which don't exist!");
        }
        if (product.isDeleted()) {
            throw new BadRequestException("You cannot upload picture for inactive product!");
        }
        String uploadDir = System.getProperty("user.dir") + pathDir;
        Path copyLocation = null;
        try {
            copyLocation = Paths
                    .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        pictureDao.addPicture(productId, file.getOriginalFilename());
    }

    private void checkForAdminRights(User user) throws AuthorizationException {
        if (user == null) throw new AuthorizationException();
        if (!user.isAdmin()) throw new AuthorizationException("You need to be admin to perform this!");
    }
}
