/*
 * Verifalia - Email list cleaning and real-time email verification service
 * https://verifalia.com/
 * support@verifalia.com
 *
 * Copyright (c) 2005-2020 Cobisi Research
 *
 * Cobisi Research
 * Via Prima Strada, 35
 * 35129, Padova
 * Italy - European Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.verifalia.api.emailvalidations;

import com.verifalia.api.common.ProgressProvider;
import com.verifalia.api.emailvalidations.models.ValidationOverview;
import com.verifalia.api.emailvalidations.models.ValidationProgress;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Duration;

/**
 * A strategy to use while waiting for the completion of an email validation job.
 */
@Getter
public class WaitingStrategy {
    /**
     * Gets a value that controls whether to wait for the completion of an email validation job.
     */
    boolean waitForCompletion;

    /**
     * Gets a {@link ProgressProvider} instance which eventually receives completion progress updates for an email
     * validation job.
     */
    ProgressProvider<ValidationOverview> progressProvider;

    /**
     * Initializes a {@link WaitingStrategy} according to specified options.
     * @param waitForCompletion If <tt>true</tt>, the strategy will wait for the job completion.
     */
    public WaitingStrategy(@NonNull final Boolean waitForCompletion) {
        this(waitForCompletion, null);
    }

    /**
     * Initializes a {@link WaitingStrategy} according to specified options.
     * @param waitForCompletion If <tt>true</tt>, the strategy will wait for the job completion.
     * @param progressProvider A {@link ProgressProvider} instance which eventually receives completion progress updates
     *                        for an email validation job.
     */
    public WaitingStrategy(@NonNull final Boolean waitForCompletion, final ProgressProvider<ValidationOverview> progressProvider) {
        this.waitForCompletion = waitForCompletion;
        this.progressProvider = progressProvider;
    }

    /**
     * Waits for the next polling interval of the specified {@link ValidationOverview}.
     * @param validationOverview
     * @throws InterruptedException
     */
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