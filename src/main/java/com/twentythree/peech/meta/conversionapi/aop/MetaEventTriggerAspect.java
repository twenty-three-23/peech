package com.twentythree.peech.meta.conversionapi.aop;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.meta.conversionapi.annotation.MetaEventTrigger;
import com.twentythree.peech.meta.conversionapi.eventhandler.event.*;
import com.twentythree.peech.meta.dto.DeviceDTO;
import com.twentythree.peech.user.util.UserEmailHolder;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.twentythree.peech.meta.util.MetaUtil.*;

@Component
@Aspect
public class MetaEventTriggerAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    public MetaEventTriggerAspect(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @AfterReturning("@annotation(metaEventTrigger)")
    public void afterLoginEvent(MetaEventTrigger metaEventTrigger) {
        if (metaEventTrigger.name() == FeatureType.LOGIN) {

            String serviceType = SecurityContextHolder.getServiceType();
            DeviceDTO result = getDeviceTypeAndMetaSecret(serviceType);

            UserData.UserDataBuilder userData = UserData.builder().em(UserEmailHolder.getUserEmail());
            OriginalEventData originalEventData = OriginalEventData.builder()
                    .eventName(metaEventTrigger.name().getOriginalEventData()).eventTime(getEventTime()).build();

            List<EventData> data = List.of(EventData.builder()
                    .eventName(metaEventTrigger.name().getEventName())
                    .eventTime(getEventTime())
                    .actionSource(result.getDeviceType())
                    .userData(userData.build())
                    .originalEventData(originalEventData).build());

            EventDataList eventDataList = EventDataList.builder().data(data).build();

            applicationEventPublisher.publishEvent(MetaEvent.builder()
                    .event(eventDataList).metaSecret(result.getMetaSecret()).build());
        }
    }

    @Before("@annotation(metaEventTrigger)")
    public void triggerMetaEvent(MetaEventTrigger metaEventTrigger) {
        if (metaEventTrigger.name() != FeatureType.LOGIN) {

            String email = SecurityContextHolder.getEmail();
            String sh256Email = encryptSHA256(email);

            UserData.UserDataBuilder userData = UserData.builder().em(sh256Email);
            OriginalEventData originalEventData = OriginalEventData.builder()
                    .eventName(metaEventTrigger.name().getOriginalEventData()).eventTime(getEventTime()).build();

            String serviceType = SecurityContextHolder.getServiceType();
            DeviceDTO result = getDeviceTypeAndMetaSecret(serviceType);

            List<EventData> data = List.of(EventData.builder()
                    .eventName(metaEventTrigger.name().getEventName())
                    .eventTime(getEventTime())
                    .actionSource(result.getDeviceType())
                    .userData(userData.build())
                    .originalEventData(originalEventData).build());

            EventDataList eventDataList = EventDataList.builder().data(data).build();

            applicationEventPublisher.publishEvent(MetaEvent.builder()
                    .event(eventDataList).metaSecret(result.getMetaSecret()).build());
        }
    }
}
