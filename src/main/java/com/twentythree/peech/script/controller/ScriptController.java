package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.OrderToParagraph;
import com.twentythree.peech.script.dto.TimePerParagraph;
import com.twentythree.peech.script.dto.request.ScriptRequestDTO;
import com.twentythree.peech.script.dto.response.ProcessedScriptResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ScriptController implements SwaggerScriptInterface {
    @Override
    @GetMapping("/test")
    public ProcessedScriptResponseDTO processScript(@RequestBody ScriptRequestDTO paragraph) {
        List<TimePerParagraph> tpp = new ArrayList<>();
        LocalTime lt = LocalTime.of(0, 1, 29);
        List<OrderToParagraph> itp = new ArrayList<>();

        tpp.add(new TimePerParagraph(1L, LocalTime.of(0, 1, 29)));
        tpp.add(new TimePerParagraph(1L, LocalTime.of(0, 2, 33)));

        itp.add(new OrderToParagraph(1L, "qweqwe"));
        itp.add(new OrderToParagraph(1L, "asd"));

        ProcessedScriptResponseDTO processedScriptResponseDTO = new ProcessedScriptResponseDTO(lt, tpp, itp);
        return processedScriptResponseDTO;
    }
}
