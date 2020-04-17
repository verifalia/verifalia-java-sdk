package com.verifalia.api.common.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public abstract class ListSegment<T> {
    private ListSegmentMeta meta;
    private List<T> data;
}