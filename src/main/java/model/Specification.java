package model;

import java.util.HashMap;

public class Specification {

    private Long id;

    private HashMap<String, HashMap<String, String>> description;
                    //title,        descTitle, desc


    public Specification(Long id) {
        this.id = id;
        this.description = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HashMap<String, HashMap<String, String>> getDescription() {
        return description;
    }
}
