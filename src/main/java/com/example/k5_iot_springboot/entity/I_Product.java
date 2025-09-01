package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "products",
        indexes = { @Index(name = "idx_products_name", columnList = "name") },
        uniqueConstraints = { @UniqueConstraint(name = "uq_products_name", columnNames = "name") }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class I_Product extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(nullable = false)
    private int price;

    private I_Product(String name, int price){
        this.name = name;
        this.price = price;
    }
}
