package model;

public class SubCategory {

    private Integer id;
    private String name;
    private boolean isHeader;
    private Integer categoryId;

    public SubCategory(Integer id,  String name, Integer categoryId, boolean isHeader) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.isHeader = isHeader;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
