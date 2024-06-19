package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ScriptRequestDTO;
import com.twentythree.peech.script.dto.response.ProcessedScriptResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.HashMap;

@RestController
public class ScriptController implements SwaggerScriptInterface {
    @Override
    @GetMapping("/test")
    public ProcessedScriptResponseDTO processScript(@RequestBody ScriptRequestDTO paragraph) {
        HashMap<Long, LocalTime> hm = new HashMap<>();
        LocalTime lt = LocalTime.of(0, 1, 29);
        HashMap<Long, String> hm2 = new HashMap<>();

        hm.put(1L, LocalTime.of(0, 1, 29));
        hm.put(2L, LocalTime.of(0, 1, 29));

        hm2.put(1L, "qweqwe");
        hm2.put(2L, "asdasdfsdfd");

        ProcessedScriptResponseDTO processedScriptResponseDTO = new ProcessedScriptResponseDTO(lt, hm, hm2);
        return processedScriptResponseDTO;
    }
}
