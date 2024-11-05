package io.haedoang.step1.coupon.domain;

import io.haedoang.step1.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

@SQLRestriction("deleted=false")
@Getter
@EqualsAndHashCode(of = {"key"}, callSuper = false)
@Table(name = "COUPON_DOWNLOAD")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponDownload extends BaseEntity {

    @Id
    @Column(name = "COUPON_DOWNLOAD_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_KEY", nullable = false)
    private Coupon coupon;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    private CouponDownload(Coupon coupon) {
        this.coupon = Objects.requireNonNull(coupon);
    }

    public static CouponDownload from(Coupon coupon) {
        return new CouponDownload(coupon);
    }

    public CouponDownload by(String userId) {
        this.userId = userId;
        return this;
    }
}
