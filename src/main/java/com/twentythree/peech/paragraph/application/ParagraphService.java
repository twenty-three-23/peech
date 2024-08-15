package com.twentythree.peech.paragraph.application;

import com.twentythree.peech.paragraph.domain.KeywordExtractor;
import com.twentythree.peech.paragraph.domain.ParagraphFetcher;
import com.twentythree.peech.paragraph.domain.ParagraphsDomain;
import com.twentythree.peech.paragraph.dto.KeyWordsByParagraph;
import com.twentythree.peech.paragraph.valueobject.Paragraph;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ParagraphService {

    private static final Logger log = LoggerFactory.getLogger(ParagraphService.class);
    private final ParagraphFetcher paragraphFetcher;
    private final KeywordExtractor keywordExtractor;

    public List<KeyWordsByParagraph> getKeyWordsByScriptId(Long scriptId) {
        ParagraphsDomain paragraphsDomain = paragraphFetcher.fetchParagraphs(scriptId);
        List<Paragraph> paragraphs = paragraphsDomain.getParagraphs();

        List<KeyWordsByParagraph> keyWordsByParagraphs = new ArrayList<>();

        for (Paragraph paragraph : paragraphs) {
            String paragraphContent = paragraph.getParagraphContent();
            List<String> keyWords = keywordExtractor.extractKeywords(paragraphContent);

            keyWordsByParagraphs.add(KeyWordsByParagraph.of(paragraph.getParagraphOrder(), keyWords));
        }
        return keyWordsByParagraphs;
    }
}
