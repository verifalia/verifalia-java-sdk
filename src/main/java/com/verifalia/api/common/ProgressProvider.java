package com.verifalia.api.common;

import lombok.NonNull;

public interface ProgressProvider<T> {
    void report(@NonNull T value);
}
