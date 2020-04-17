package com.verifalia.api.emailvalidations;

import com.verifalia.api.common.ProgressProvider;
import com.verifalia.api.emailvalidations.models.ValidationOverview;
import com.verifalia.api.emailvalidations.models.ValidationProgress;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class WaitingStrategy {
    boolean waitForCompletion;
    ProgressProvider<ValidationOverview> progressProvider;

    public WaitingStrategy(@NonNull final Boolean waitForCompletion) {
        this(waitForCompletion, null);
    }

    public WaitingStrategy(@NonNull final Boolean waitForCompletion, final ProgressProvider<ValidationOverview> progressProvider) {
        this.waitForCompletion = waitForCompletion;
        this.progressProvider = progressProvider;
    }

    public void waitForNextPoll(@NonNull final ValidationOverview validationOverview) throws InterruptedException {
        // Observe the ETA if we have one, otherwise a delay given the formula: max(0.5, min(30, 2^(log(noOfEntries, 10) - 1)))

        Duration timeToSleep = null;
        ValidationProgress progress = validationOverview.getProgress();

        if (progress != null) {
            timeToSleep = progress.getEstimatedTimeRemaining();
        }

        if (timeToSleep == null) {
            // TODO: For better results, consider the job age while determining the polling delay

            timeToSleep = Duration.ofSeconds((long) Math.max(0.5, Math.min(30, Math.pow(2, Math.log10(validationOverview.getNoOfEntries()) - 1))));
        }

        Thread.sleep(timeToSleep.toMillis());
    }
}