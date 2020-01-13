package com.example.emag.services;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;


public class AbstractService {

    protected void checkForProductExistence(Product product) throws NotFoundException {
        if (product == null) throw new NotFoundException("Product not found");
    }

    protected void checkForLoggedUser(User user) throws AuthorizationException {
        if (user == null) throw new AuthorizationException();
    }
}
