package com.twentythree.peech.dto;


import lombok.Data;

import java.sql.Array;
import java.util.ArrayList;

@Data
public class ParagraphResponseDTO {
    private final ArrayList paragraphs;

    public ParagraphResponseDTO(ArrayList paragraph) {

        ArrayList arr = new ArrayList<String>();

        for (Object p : paragraph) {
            arr.add(p);
        }

        this.paragraphs = arr;
    }
}
