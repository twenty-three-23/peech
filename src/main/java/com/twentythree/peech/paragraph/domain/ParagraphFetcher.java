package com.twentythree.peech.paragraph.domain;


public interface ParagraphFetcher {

    ParagraphsDomain fetchParagraphs(Long scriptId);
    ParagraphsInformationDomain fetchParagraphsInformation(Long scriptId);
}
