package com.BTL_JAVA.BTL.Entity.Product;

import com.BTL_JAVA.BTL.Entity.Orders.OrderDetail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Entity
@Table(name = "product_variation")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variation_id")
    int id;

    @Column(name = "image")
    String image;

    @Column(name = "size")
    String size;

    @Column(name ="color")
    String color;

    @Column(name = "stock_quantity")
    Integer stockQuantity;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    Product product;

    @OneToMany(mappedBy = "productVariation",
                fetch = FetchType.LAZY,
          cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    List<Cart> carts;
    
    @OneToMany(mappedBy = "productVariation",
                fetch = FetchType.LAZY,
          cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    List<OrderDetail> orderDetails;

}
