package kr.mybrary.bookservice.tag.domain;

import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeaningTagService {

    private final MeaningTagRepository meaningTagRepository;
    private final MyBookMeaningTagRepository myBookMeaningTagRepository;

    public void assignMeaningTag(MeaningTagAssignServiceRequest request) {
        MeaningTag meaningTag = meaningTagRepository.findByQuote(request.getQuote())
                .orElseGet(() -> saveAndGetMeaningTag(request.getLoginId(), request.getQuote()));

        meaningTag.increaseRegisteredCount();

        MyBookMeaningTag myBookMeaningTag = myBookMeaningTagRepository.findByMyBook(request.getMyBook())
                .orElseGet(() -> saveAndGetMyBookMeaningTag(request, meaningTag));

        myBookMeaningTag.assignMeaningTag(meaningTag, request.getColorCode());
        myBookMeaningTagRepository.save(myBookMeaningTag);
    }

    private MeaningTag saveAndGetMeaningTag(String loginId, String quote) {

        MeaningTag meaningTag = MeaningTag.builder()
                .quote(quote)
                .createdBy(loginId)
                .registeredCount(0)
                .build();

        return meaningTagRepository.save(meaningTag);
    }

    private MyBookMeaningTag saveAndGetMyBookMeaningTag(MeaningTagAssignServiceRequest request,
            MeaningTag meaningTag) {

        MyBookMeaningTag myBookMeaningTag = MyBookMeaningTag.builder()
                .myBook(request.getMyBook())
                .meaningTag(meaningTag)
                .meaningTagColor(request.getColorCode())
                .build();

        return myBookMeaningTagRepository.save(myBookMeaningTag);
    }
}
