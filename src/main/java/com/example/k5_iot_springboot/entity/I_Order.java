package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.common.enums.OrderStatus;
import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_user", columnList = "user_id"),
                @Index(name = "idx_orders_status", columnList = "order_status"),
                @Index(name = "idx_orders_created_at", columnList = "created_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class I_Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Many(Order)ToOne(G_User)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_orders_user"))
    private G_User user;

    // 자바 Enum 타입은 DB에서 VARCHAR + CHECK 제약조건 사용
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 16)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // I_Order (주문) 엔티티와 I_OrderItem (주문 상세) 엔티티 간 1:N 관계를 명시
    // - mappedBy: 주인 관계 지정 (양방향 매핑에서 연관관계의 주인을 I_OrderItem으로 지정 - FK 설정을 하는 엔티티 지정)
    //              >> "order"는 I_OrderItem의 order 필드명을 가리킴
    // - cascade = CascadeType.ALL
    //      : 영속성 전이를 의미 (Order 저장 / 삭제 시 OrderItem도 같이 저장 / 삭제)
    // - orPhanRemoval = true
    //      : 고아 객체 제거 기능
    //      >> items 리스트에서 요소 제거 시, 해당 요소의 DB에서 OrderItem 레코드가 삭제됨
    // @Builder.Default
    private List<I_OrderItem> items = new ArrayList<>();

    @Builder
    public I_Order(@NotNull G_User user, OrderStatus orderStatus) {
        this.user = user;
        this.orderStatus = (orderStatus != null) ? orderStatus : OrderStatus.PENDING;
    }

    public void addItem(I_OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(I_OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
