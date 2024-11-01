package com.twentythree.peech.meta.conversionapi.annotation;

import com.twentythree.peech.meta.conversionapi.eventhandler.event.FeatureType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaEventTrigger {
    FeatureType name();
}
