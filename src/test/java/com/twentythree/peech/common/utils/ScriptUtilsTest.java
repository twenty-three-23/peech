package com.twentythree.peech.common.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

class ScriptUtilsTest {

    @Test
    public void 초를_시간으로_변환하는_로직_테스트() throws Exception {
        //Given
        float second = 600.194328971244f;

        //When
        LocalTime localTime = ScriptUtils.transferSeoondToLocalTime(second);

        //Then
        Assertions.assertThat(localTime).isEqualTo(LocalTime.of(0, 10, 0, 190_000_000));
    }
}