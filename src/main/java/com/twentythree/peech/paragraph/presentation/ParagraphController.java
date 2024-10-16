package com.twentythree.peech.paragraph.presentation;

import com.twentythree.peech.common.dto.response.WrappedResponseBody;
import com.twentythree.peech.paragraph.application.ParagraphService;
import com.twentythree.peech.paragraph.dto.KeyWordsByParagraph;
import com.twentythree.peech.paragraph.dto.response.HistoryDetailResponseDTO;
import com.twentythree.peech.paragraph.dto.response.KeyWordsByParagraphsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ParagraphController {

    private final ParagraphService paragraphService;

    @GetMapping("api/v1.1/themes/{themeId}/scripts/{scriptId}/keywords")
    public WrappedResponseBody<KeyWordsByParagraphsResponseDTO> getKeyWordsByScriptId(@PathVariable Long scriptId) {
        List<KeyWordsByParagraph> keyWordsByScriptId = paragraphService.getKeyWordsByScriptId(scriptId);

        KeyWordsByParagraphsResponseDTO keyWordsByParagraphsResponseDTO = KeyWordsByParagraphsResponseDTO.of(keyWordsByScriptId);
        return new WrappedResponseBody<KeyWordsByParagraphsResponseDTO>(200, keyWordsByParagraphsResponseDTO);
    }

    @GetMapping("api/v2/theme/{themeId}/scripts/{scriptId}")
    public HistoryDetailResponseDTO getScriptDetail(@PathVariable Long scriptId) {
        return paragraphService.getScriptDetail(scriptId);
    }
}
