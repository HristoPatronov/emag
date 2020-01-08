package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dto.ErrorDTO;
import com.example.emag.model.pojo.Address;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    protected void checkForLoggedUser(User user) throws SQLException {
        if (user == null) throw new AuthorizationException();
    }

    protected void checkForAdminRights(User user) throws SQLException {
        if (!user.isAdmin()) throw new AuthorizationException("You need to be admin to perform this!");
    }

    protected void checkForProductExistence(Product product) throws SQLException {
        if (product == null) throw new NotFoundException("Product not found");
    }

    protected void checkForAddressExistence(Address address) throws SQLException {
        if (address == null) throw new NotFoundException("Address not found");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO unauthorized(Exception e){
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleSQLExceptions(Exception e){
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequests(Exception e){
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }
}
