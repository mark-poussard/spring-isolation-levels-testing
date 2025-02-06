package io.poussard.mark.spring.isolationlevelstesting.domain.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.CreateUserRequest;

public class CreateUserMessage {
    private String idempotencyKey;
    private CreateUserRequest request;

    @JsonCreator
    public CreateUserMessage(@JsonProperty("idempotencyKey") String idempotencyKey, @JsonProperty("request") CreateUserRequest request) {
        this.idempotencyKey = idempotencyKey;
        this.request = request;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public CreateUserRequest getRequest() {
        return request;
    }
}
