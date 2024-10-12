package com.twentythree.peech.paragraph.infrastructure;

import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.paragraph.domain.ParagraphFetcher;
import com.twentythree.peech.paragraph.domain.ParagraphsDomain;
import com.twentythree.peech.paragraph.domain.ParagraphsInformationDomain;
import com.twentythree.peech.paragraph.valueobject.Paragraph;
import com.twentythree.peech.paragraph.valueobject.ParagraphInformation;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class ParagraphFetcherImpl implements ParagraphFetcher {

    private final SentenceRepository sentenceRepository;

    @Override
    public ParagraphsDomain fetchParagraphs(Long scriptId) {
        List<SentenceEntity> sentences = sentenceRepository.findBySentencesToScriptId(scriptId);

        sentences.sort(Comparator.comparing(SentenceEntity::getParagraphId)
                .thenComparing(SentenceEntity::getSentenceOrder));

        List<Paragraph> paragraphs = new ArrayList<>();
        Long latestParagraphOrder = null;
        String paragraphContent = "";

        for (SentenceEntity sentence : sentences) {
            Long paragraphOrder = sentence.getParagraphId();

            if (latestParagraphOrder == null) {
                latestParagraphOrder = paragraphOrder;
                paragraphContent = sentence.getSentenceContent();
            }
            else if (paragraphOrder > latestParagraphOrder) {

                paragraphs.add(new Paragraph(latestParagraphOrder, paragraphContent));

                latestParagraphOrder = paragraphOrder;
                paragraphContent = sentence.getSentenceContent();
            } else if (paragraphOrder.equals(latestParagraphOrder)) {
                paragraphContent += sentence.getSentenceContent();
            } else {
                throw new RuntimeException("문단 도메인 생성중 알 수 없는 오류 발생");
            }
        }
        paragraphs.add(new Paragraph(latestParagraphOrder, paragraphContent));

        return ParagraphsDomain.of(scriptId, paragraphs);
    }

    @Override
    public ParagraphsInformationDomain fetchParagraphsInformation(Long scriptId) {
        List<SentenceEntity> sentences = sentenceRepository.findBySentencesToScriptId(scriptId);

        sentences.sort(Comparator.comparing(SentenceEntity::getParagraphId)
                .thenComparing(SentenceEntity::getSentenceOrder));

        List<ParagraphInformation> paragraphInformations = new ArrayList<>();
        Long latestParagraphOrder = null;
        String paragraphContent = "";
        LocalTime expectedTimePerParagraph = LocalTime.of(0,0,0,0);
        LocalTime realTimePerParagraph = LocalTime.of(0,0,0,0);


        for (SentenceEntity sentence : sentences) {
            Long paragraphOrder = sentence.getParagraphId();

            if (latestParagraphOrder == null) {
                latestParagraphOrder = paragraphOrder;
                paragraphContent = sentence.getSentenceContent();
            }
            else if (paragraphOrder > latestParagraphOrder) {

                paragraphInformations.add(new ParagraphInformation(latestParagraphOrder, paragraphContent, expectedTimePerParagraph, realTimePerParagraph));

                latestParagraphOrder = paragraphOrder;
                paragraphContent = sentence.getSentenceContent();
            } else if (paragraphOrder.equals(latestParagraphOrder)) {
                paragraphContent += sentence.getSentenceContent();
                expectedTimePerParagraph = ScriptUtils.sumLocalTime(expectedTimePerParagraph, sentence.getSentenceExpectTime());
                realTimePerParagraph = ScriptUtils.sumLocalTime(realTimePerParagraph, sentence.getSentenceRealTime());
            } else {
                throw new RuntimeException("문단 도메인 생성중 알 수 없는 오류 발생");
            }
        }
        paragraphInformations.add(new ParagraphInformation(latestParagraphOrder, paragraphContent, expectedTimePerParagraph, realTimePerParagraph));

        return ParagraphsInformationDomain.of(scriptId, paragraphInformations);
    }
}
