package com.BTL_JAVA.BTL.Entity.Product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "parent_id")
    Integer parent_id;

    @Column(name="image_url")
    String imageUrl;

    @Column(name ="image_public_id")
    String imagePublicId;

    @OneToMany(mappedBy = "category",
               fetch = FetchType.LAZY,
    cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    private Set<Product> products;

}
