package io.haedoang.step1.order.domain;

import io.haedoang.step1.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@EqualsAndHashCode(of = {"key"}, callSuper = false)
@Table(name = "TB_ORDER")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_KEY")
    private Long key;

    @Column(name = "ORDER_ID", unique = true, nullable = false)
    private String orderId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Version
    private Integer version;


    private Order(String orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = OrderStatus.ORDER_REQUEST;
    }

    public static Order of(String orderId, String userId) {
        return new Order(orderId, userId);
    }

    public void update(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
