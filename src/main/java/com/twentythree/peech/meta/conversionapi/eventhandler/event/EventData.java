package com.twentythree.peech.meta.conversionapi.eventhandler.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventData {
    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("event_time")
    private long eventTime;

    @JsonProperty("action_source")
    private String actionSource;

    @JsonProperty("user_data")
    private UserData userData;

    @JsonProperty("original_event_data")
    private OriginalEventData originalEventData;
}
