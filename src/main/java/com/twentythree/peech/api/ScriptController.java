package com.twentythree.peech.api;

import com.twentythree.peech.dto.ParagraphResponseDTO;

import com.twentythree.peech.dto.ScriptRequestDto;
import com.twentythree.peech.dto.DefaultTimeResponseDTO;
import com.twentythree.peech.dto.ParagraphRequestDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.ArrayList;

@RestController
public class ScriptController implements SwaggerScriptInterface {

    @PostMapping("/api/script/paraInfo")
    public ParagraphResponseDTO postParaInfo(@RequestBody ScriptRequestDto script) {

        ArrayList tmp = new ArrayList();

        tmp.add("qweqwe");
        tmp.add("asdasd");

        return new ParagraphResponseDTO(tmp);
    }

    @PostMapping("/api/script/paraDefaultTime")
    public DefaultTimeResponseDTO getDefaultTimePerParagraphResponseDTO(@RequestBody ParagraphRequestDTO paragraph) {

        LocalTime defaultTime = LocalTime.of(0, 1, 20);
        return new DefaultTimeResponseDTO(defaultTime);
    }
}
