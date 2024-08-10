package com.twentythree.peech.paragraph.infrastructure;

import com.twentythree.peech.paragraph.domain.ParagraphFetcher;
import com.twentythree.peech.paragraph.domain.ParagraphsDomain;
import com.twentythree.peech.paragraph.valueobject.Paragraph;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        String paragraphContnet = "";

        for (SentenceEntity sentence : sentences) {
            Long paragraphOrder = sentence.getParagraphId();

            if (latestParagraphOrder == null) {
                latestParagraphOrder = paragraphOrder;
                paragraphContnet = sentence.getSentenceContent();
            }
            else if (paragraphOrder > latestParagraphOrder) {

                paragraphs.add(new Paragraph(latestParagraphOrder, paragraphContnet));

                latestParagraphOrder = paragraphOrder;
                paragraphContnet = sentence.getSentenceContent();
            } else if (paragraphOrder.equals(latestParagraphOrder)) {
                paragraphContnet += sentence.getSentenceContent();
            } else {
                throw new RuntimeException("문단 도메인 생성중 알 수 없는 오류 발생");
            }
        }
        paragraphs.add(new Paragraph(latestParagraphOrder, paragraphContnet));

        return ParagraphsDomain.of(scriptId, paragraphs);
    }
}
