package io.haedoang.step1.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrencyTest {

    @RepeatedTest(500)
    @DisplayName("스레드 safe 하지 못한 테스트")
    void threadUnSafeTest() {
        // given
        Counter given = new Counter();
        int incrementPerThread = 10;
        int threadCount = 10;

        // when
        executeMultiThread(threadCount, () -> {
            for (int i = 0; i < incrementPerThread; i++) {
                given.increment();
            }
        });

        // then
        assertThat(given.getCount()).isEqualTo(incrementPerThread * threadCount)
                .as("간헐적 실패가 발생한다");
    }


    @RepeatedTest(500)
    @DisplayName("CAS 알고리즘을 사용한 스레드 safe 테스트")
    void threadSafeTest() {
        // given
        AtomicInteger given = new AtomicInteger();
        int incrementPerThread = 10;
        int threadCount = 10;

        // when
        executeMultiThread(threadCount, () -> {
            for (int i = 0; i < incrementPerThread; i++) {
                given.incrementAndGet();
            }
        });

        // then
        assertThat(given.get()).isEqualTo(incrementPerThread * threadCount);

    }

    private static class Counter {
        private int count = 0;

        public Counter() {
        }

        public void increment() {
            this.count = count + 1;
        }

        public int getCount() {
            return this.count;
        }
    }


    private void executeMultiThread(int threadCount, Runnable runnable) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount + 1); //main thread 포함
        ExecutorService es = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            es.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " await");
                    cyclicBarrier.await();

                    //run
                    runnable.run();


                    System.out.println(Thread.currentThread().getName() + " finished");
                } catch (InterruptedException | BrokenBarrierException e) {
                    // error handling
                }
            });
        }

        try {
            cyclicBarrier.await(); //main thread 대기
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        es.shutdown();

        try {
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // error handling
            e.printStackTrace();
        }
    }
}
