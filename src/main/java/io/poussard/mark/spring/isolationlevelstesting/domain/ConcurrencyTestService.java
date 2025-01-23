package io.poussard.mark.spring.isolationlevelstesting.domain;

import io.poussard.mark.spring.isolationlevelstesting.infrastructure.ConcurrencyTestResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class ConcurrencyTestService {
    public <T> ConcurrencyTestResult<T> runConcurrently(Function<Integer, T> writeFn, int count){
        ConcurrentLinkedQueue<T> success = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> failures = new ConcurrentLinkedQueue<>();
        CompletableFuture[] tasks = new CompletableFuture[count];
        long start = System.currentTimeMillis();
        for(int i=0; i<count; i++){
            final int finalI = i;
            tasks[i] = CompletableFuture.runAsync(() -> {
                try{
                    success.add(writeFn.apply(finalI));
                } catch(Exception e){
                    failures.add(e.getMessage());
                }
            });
        }
        CompletableFuture.allOf(tasks).join();
        long duration = System.currentTimeMillis() - start;
        return new ConcurrencyTestResult<T>(success.stream().toList(), failures.stream().toList(), duration);
    }
}
