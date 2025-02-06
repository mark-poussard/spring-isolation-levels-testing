package io.poussard.mark.spring.isolationlevelstesting.domain.transaction;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

@Service
public class ConcurrencyTestService {
    public <T> ConcurrencyTestResult<T> runConcurrently(Function<Integer, T> writeFn, int count){
        ConcurrentLinkedQueue<T> success = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> failures = new ConcurrentLinkedQueue<>();
        CompletableFuture[] tasks = new CompletableFuture[count];
        long start = System.nanoTime();
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
        long duration = System.nanoTime() - start;
        return new ConcurrencyTestResult<>(success.stream().toList(), failures.stream().toList(), duration);
    }
}
