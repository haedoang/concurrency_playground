package io.haedoang.step1;

import io.haedoang.step1.coupon.domain.Coupon;
import io.haedoang.step1.coupon.infra.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Profile("!test")
    @Transactional
    @Bean
    CommandLineRunner commandLineRunner(CouponRepository couponRepository) {
        return args -> {
            String couponId = UUID.randomUUID().toString();
            couponRepository.save(Coupon.from(couponId, 10));
            log.info("coupon created! {}", couponId);
        };
    }
}

