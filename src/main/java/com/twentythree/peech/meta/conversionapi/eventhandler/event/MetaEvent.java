package com.twentythree.peech.meta.conversionapi.eventhandler.event;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MetaEvent {
    private EventDataList event;
    private MetaSecret metaSecret;
}
