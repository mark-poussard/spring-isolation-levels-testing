package io.poussard.mark.spring.isolationlevelstesting.domain.transaction.util;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
