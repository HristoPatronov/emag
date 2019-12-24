package model;


import java.util.ArrayList;
import java.util.List;

public class Category {

    private Integer id;
    private String name;
    private List<SubCategory> subCategories;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.subCategories = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

}
