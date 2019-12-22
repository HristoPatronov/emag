package model;

import java.time.LocalDate;
import java.util.Collection;

public class Order {

    private Long id;
    private Double totalPrice;
    private LocalDate date;
    private Long userId;
    private Long paymentTypeId;
    private Long statusId;
    private Collection<Product> orderedProducts;

}
