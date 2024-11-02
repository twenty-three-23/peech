package com.twentythree.peech.meta.conversionapi.eventhandler.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OriginalEventData {
    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("event_time")
    private long eventTime;
}
