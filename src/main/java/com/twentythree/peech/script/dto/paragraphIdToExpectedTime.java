package com.twentythree.peech.script.dto;

import java.time.LocalTime;

public record paragraphIdToExpectedTime(Long paragraphId, LocalTime expectedTimePerParagraph) {
}
