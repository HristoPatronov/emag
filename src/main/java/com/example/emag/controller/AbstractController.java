package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {

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

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleEmailException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }
}
