package io.poussard.mark.spring.isolationlevelstesting.domain.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.noidxuser.NoIdxUser;

public class CreateUserOutputMessage {
    private final String idempotencyKey;
    private final String error;
    private final NoIdxUser user;

    @JsonCreator
    public CreateUserOutputMessage(@JsonProperty("idempotencyKey") String idempotencyKey, @JsonProperty("error") String error, @JsonProperty("user") NoIdxUser user) {
        this.idempotencyKey = idempotencyKey;
        this.error = error;
        this.user = user;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getError() {
        return error;
    }

    public NoIdxUser getUser() {
        return user;
    }
}
