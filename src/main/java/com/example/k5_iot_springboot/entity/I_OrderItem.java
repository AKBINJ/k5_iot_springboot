package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "order_items",
        indexes = {
                @Index(name = "idx_orders_items_order", columnList = "order_id"),
                @Index(name = "idx_orders_items_product", columnList = "product_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_order_product", columnNames = {"order_id", "porduct_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class I_OrderItem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_order_items_order"))
    private I_Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_items_product"))
    private I_Product product;

    @Min(1)
    @Column(nullable = false)
    private int quantity;

    void setOrder(I_Order order) {
        this.order = order;
    }
}
