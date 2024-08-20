package com.twentythree.peech.paragraph.infrastructure;

import com.twentythree.peech.paragraph.domain.ParagraphFetcher;
import com.twentythree.peech.paragraph.domain.ParagraphsDomain;
import com.twentythree.peech.paragraph.valueobject.Paragraph;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class ParagraphFetcherImplTest {

    private static final Logger log = LoggerFactory.getLogger(ParagraphFetcherImplTest.class);
    @Autowired private ParagraphFetcher paragraphFetcher;

    @Test
    public void 문장을_문단으로_치환() throws Exception {
        //Given

        Long scriptId = 1L;

        //When

        ParagraphsDomain paragraphsDomain = paragraphFetcher.fetchParagraphs(scriptId);
        List<Paragraph> paragraphs = paragraphsDomain.getParagraphs();

        Paragraph paragraph1 = paragraphs.get(0);
        Paragraph paragraph2 = paragraphs.get(1);
        //Then

        assertThat(paragraphsDomain.getScriptId()).isEqualTo(scriptId);

        assertThat(paragraph1.getParagraphContent()).isEqualTo("사업가 김씨가 신한은행 자산 관리 센터에서 50억짜리 해외 부동산 펀드에 가입한 건 지난 2017년입니다.해외 국가 기간이 세들어 있어 무조건 안전하다는 은행 직원 설득이 결정적이었다고 합니다.");
        assertThat(paragraph2.getParagraphContent()).isEqualTo("이렇게 때마다 금리를 웃도는 배당을 주고, 5년 후 건물의 증권을 팔아 투자자에게 나눠주는 상품, 인데 당시엔 없어서 못팔 정도였습니다.");


    }

}