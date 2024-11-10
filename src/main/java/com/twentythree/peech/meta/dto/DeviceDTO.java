package com.twentythree.peech.meta.dto;

import com.twentythree.peech.meta.conversionapi.eventhandler.event.MetaSecret;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeviceDTO {
    private String deviceType;
    private MetaSecret metaSecret;
}
