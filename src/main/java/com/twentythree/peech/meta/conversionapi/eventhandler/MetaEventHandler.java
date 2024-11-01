package com.twentythree.peech.meta.conversionapi.eventhandler;

import com.twentythree.peech.meta.conversionapi.client.MetaFeign;
import com.twentythree.peech.meta.conversionapi.eventhandler.event.MetaEvent;
import com.twentythree.peech.meta.conversionapi.eventhandler.event.MetaSecret;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetaEventHandler {

    private final MetaFeign metaFeign;

    @Async
    @EventListener
    public void sendConversionApi(MetaEvent metaEvent) {
        MetaSecret metaSecret = metaEvent.getMetaSecret();
        metaFeign.sendEvent(metaSecret.getPixelId(), metaSecret.getAccessToken(), metaEvent.getEvent());
    }
}
