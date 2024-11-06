package io.haedoang.step1.coupon.application.service;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class SynchronizedService {

    public <T> T synchronize(Supplier<T> supplier) {
        synchronized (this) {
            return supplier.get();
        }
    }
}
