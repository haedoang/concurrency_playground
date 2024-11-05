package io.haedoang.step1.coupon.domain;

import io.haedoang.step1.base.entity.BaseEntity;
import io.haedoang.step1.base.error.ErrorCode;
import io.haedoang.step1.base.error.ServiceException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

@DynamicUpdate
@SQLRestriction("deleted=false")
@Getter
@EqualsAndHashCode(of = {"key"}, callSuper = false)
@Table(name = "COUPON")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @Column(name = "COUPON_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(name = "COUPON_ID", unique = true)
    private String couponId;

    @Column(name = "USED", nullable = false)
    private Integer used = 0;

    @Column(name = "REMAINED", nullable = false)
    private Integer remained;

    private Coupon(String couponId, Integer remained) {
        validate(couponId, remained);
        this.couponId = couponId;
        this.remained = remained;
    }

    private void validate(String couponId, Integer remained) {
        if (!StringUtils.hasLength(couponId) || remained <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Coupon from(String couponId, int remained) {
        return new Coupon(couponId, remained);
    }

    public void used() {
        if (isSoldOut()) {
            throw new ServiceException(ErrorCode.COUPON_OUT_OF_STOCK);
        }

        this.used = used + 1;
    }

    public boolean isSoldOut() {
        return used.equals(remained);
    }
}
