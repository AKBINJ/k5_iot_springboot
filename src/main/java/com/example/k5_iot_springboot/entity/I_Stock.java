package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "stocks",
        indexes = { @Index(name = "idx_stocks_product_id", columnList = "product_id") }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class I_Stock extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull // 참조되는 길이 PK 값이 기 때문에 비워질 수 X
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_stocks_product"))
    private I_Product product;

    @Min(0)
    @Column(nullable = false)
    private int quantity;

    @Builder
    private I_Stock(I_Product product, int quantity) {
        this.product = product;
        this.quantity = 0; // 재고 생성 시 - 수량 초기화 (0)
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
