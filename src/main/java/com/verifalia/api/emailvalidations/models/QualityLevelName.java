package com.verifalia.api.emailvalidations.models;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class QualityLevelName {
    /// <summary>
    /// The Standard quality level. Suitable for most businesses, provides good results for the vast majority of email addresses;
    /// features a single validation pass and 5 second anti-tarpit time; less suitable for validating email addresses with temporary
    /// issues (mailbox over quota, greylisting, etc.) and slower mail exchangers.
    /// </summary>
    public static QualityLevelName Standard = new QualityLevelName("Standard");
    /// <summary>
    /// The High quality level. Much higher quality, featuring 3 validation passes and 50 seconds of anti-tarpit time, so you can
    /// even validate most addresses with temporary issues, or slower mail exchangers.
    /// </summary>
    public static QualityLevelName High = new QualityLevelName("High");
    /// <summary>
    /// The Extreme quality level. Unbeatable, top-notch quality for professionals who need the best results the industry can offer:
    /// performs email validations at the highest level, with 9 validation passes and 2 minutes of anti-tarpit time.
    /// </summary>
    public static QualityLevelName Extreme = new QualityLevelName("Extreme");
    private final String nameOrGuid;

    public QualityLevelName(@NonNull final String nameOrGuid) {
        this.nameOrGuid = nameOrGuid;
    }
}
