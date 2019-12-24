package model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {

    private Map<Product, Integer> products;

    public ShoppingCart(){
        this.products = new LinkedHashMap<>();
    }

    public void addProduct(Product product, Integer quantity){
        if (!this.products.containsKey(product)){
            this.products.put(product, 1);
        }
        this.products.put(product, quantity);
    }

    public void removeProduct(Product product){
        if (this.products.containsKey(product)){
            this.products.remove(product);
        }
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }
}
