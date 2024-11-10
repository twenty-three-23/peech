package com.twentythree.peech.meta.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetaController {

    @Value("${meta.tag.content}")
    private String metaContent;

    @GetMapping("/")
    public String metaDomain(Model model) {
        model.addAttribute("metaContent", metaContent);
        return "index";
    }

}
