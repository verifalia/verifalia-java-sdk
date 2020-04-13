package com.verifalia.api.emailvalidations;

import com.verifalia.api.emailvalidations.models.ValidationOverview;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class WaitingStrategy {
    boolean waitForCompletion;

    public void waitForNextPoll(@NonNull ValidationOverview validationOverview) {
    }
}
