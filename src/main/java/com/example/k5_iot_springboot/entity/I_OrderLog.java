package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "order_logs",
        indexes = {
                @Index(name = "idx_orders_logs_order", columnList = "order_id"),
                @Index(name = "idx_orders_logs_created_at", columnList = "created_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class I_OrderLog extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_order_logs_order"))
    private I_Order order;

    @Size(max = 255)
    @Column(length = 255)
    private String message;

}
