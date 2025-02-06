package io.poussard.mark.spring.isolationlevelstesting.domain.messaging;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.GenericUser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SyncMessagingService {
    private final Map<String, Data<GenericUser>> responses = new HashMap<>();

    private Data<GenericUser> getData(String idempotencyKey){
        if(!responses.containsKey(idempotencyKey)){
            CompletableFuture<GenericUser> future = new CompletableFuture<>();
            responses.put(idempotencyKey, new Data<>(new Object(), future));
            future.thenApply(_ -> responses.remove(idempotencyKey));
        }
        return responses.get(idempotencyKey);
    }

    public CompletableFuture<GenericUser> get(String idempotencyKey){
        return getData(idempotencyKey).getFuture();
    }

    public void set(String idempotencyKey, GenericUser user){
        Data<GenericUser> data = getData(idempotencyKey);
        synchronized (data.getLock()){
            if(!data.getFuture().isDone()){
                data.getFuture().complete(user);
            }
        }
    }

    public void set(String idempotencyKey, Exception e){
        Data<GenericUser> data = getData(idempotencyKey);
        synchronized (data.getLock()){
            if(!data.getFuture().isDone()){
                data.getFuture().completeExceptionally(e);
            }
        }
    }

    private static class Data<T> {
        private Object lock;
        private CompletableFuture<T> future;

        public Data(Object lock, CompletableFuture<T> future) {
            this.lock = lock;
            this.future = future;
        }

        public Object getLock() {
            return lock;
        }

        public CompletableFuture<T> getFuture() {
            return future;
        }
    }
}
