package com.example.emag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWIthPicturesDTO {

    private ProductWithAllDTO productWithAllDTO;
    private File picture;
}
