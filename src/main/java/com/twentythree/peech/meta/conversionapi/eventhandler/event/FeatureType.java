package com.twentythree.peech.meta.conversionapi.eventhandler.event;

import lombok.Getter;

@Getter
public enum FeatureType {

    STT("STT","ViewContent"),
    EXPECTED_TIME("ExpectedTime","ViewContent"),
    EXPECTED_QUESTION_LIST("ExpectedQuestionList","ViewContent"),
    LOGIN("LogIn","ViewContent"),
    SIGNUP("SignUp","CompleteRegistration");

    private final String eventName;
    private final String originalEventData;

    FeatureType(String eventName, String originalEventData) {
        this.eventName = eventName;
        this.originalEventData = originalEventData;
    }
}
