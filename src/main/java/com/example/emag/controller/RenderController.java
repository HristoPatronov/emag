package com.example.emag.controller;

import com.example.emag.model.dao.PictureDAO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

@RestController
public class RenderController {


    @Autowired
    private PictureDAO pictureDAO;

    @Value("${image.path}")
    String pathDir;

    @RequestMapping(value = "/render/{productId}", produces = {MediaType.IMAGE_JPEG_VALUE, "application/json"}, method = RequestMethod.GET)
    public void renderImage(@PathVariable(name = "productId") long productId, HttpServletResponse response) throws IOException, SQLException {
        String pictureUrl = pictureDAO.getPictureUrl(productId);
        ClassPathResource imgFile = new ClassPathResource("static"+ "\\" + pictureUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        StreamUtils.copy(imgFile.getInputStream(), outputStream);
    }

    @SneakyThrows
    @PostMapping("/uploadImg")
    public void uploadImage(MultipartFile file, Long productId) {
        String uploadDir = System.getProperty("user.dir") + pathDir;
        Path copyLocation = null;
        try {
            copyLocation = Paths
                    .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pictureDAO.addPicture(productId, file.getOriginalFilename());
    }
}
