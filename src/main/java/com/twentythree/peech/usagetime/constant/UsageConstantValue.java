package com.twentythree.peech.usagetime.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsageConstantValue {
    public static final Long DEFAULT_USAGE_TIME = 9000L;
    public static final Long MAX_AUDIO_TIME = 1500L;
    public static final Long BUFFER_TIME = 5L;
}
