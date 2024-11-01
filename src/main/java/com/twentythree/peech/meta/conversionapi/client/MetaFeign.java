package com.twentythree.peech.meta.conversionapi.client;

import com.twentythree.peech.meta.conversionapi.eventhandler.event.EventDataList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "MetaFeign",
        url = "https://graph.facebook.com/${meta.api-version}/"
)
public interface MetaFeign {

    @PostMapping(value ="{pixelId}/events", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendEvent(
            @PathVariable("pixelId") String pixelId,
            @RequestParam("access_token") String accessToken,
            @RequestBody EventDataList eventData
    );
}
