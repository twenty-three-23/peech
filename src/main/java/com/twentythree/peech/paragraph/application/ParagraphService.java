package com.twentythree.peech.paragraph.application;

import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.paragraph.domain.KeywordExtractor;
import com.twentythree.peech.paragraph.domain.ParagraphFetcher;
import com.twentythree.peech.paragraph.domain.ParagraphsDomain;
import com.twentythree.peech.paragraph.domain.ParagraphsInformationDomain;
import com.twentythree.peech.paragraph.dto.HistoryParagraphDTO;
import com.twentythree.peech.paragraph.dto.KeyWordsByParagraph;
import com.twentythree.peech.paragraph.dto.response.HistoryDetailResponseDTO;
import com.twentythree.peech.paragraph.valueobject.Paragraph;
import com.twentythree.peech.paragraph.valueobject.ParagraphInformation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.twentythree.peech.common.utils.ScriptUtils.*;

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

    public HistoryDetailResponseDTO getScriptDetail(Long scriptId) {
        ParagraphsInformationDomain paragraphsInformationDomain = paragraphFetcher.fetchParagraphsInformation(scriptId);
        List<ParagraphInformation> paragraphInformations = paragraphsInformationDomain.getParagraphInformations();
        LocalTime totalRealTime = LocalTime.of(0, 0, 0, 0);

        List<HistoryParagraphDTO> historyParagraphs = new ArrayList<>();

        for(ParagraphInformation paragraphInformation : paragraphInformations) {
            String measurement = measurementSpeedResult(paragraphInformation.getParagraphRealTime(),
                    paragraphInformation.getParagraphExpectedTime());
            totalRealTime = sumLocalTime(totalRealTime, paragraphInformation.getParagraphRealTime());

            historyParagraphs.add(new HistoryParagraphDTO(paragraphInformation.getParagraphOrder(), measurement,
                    paragraphInformation.getParagraphRealTime(), paragraphInformation.getParagraphContent()));
        }

        return new HistoryDetailResponseDTO(totalRealTime, historyParagraphs);
    }
}
