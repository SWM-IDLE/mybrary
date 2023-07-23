package kr.mybrary.bookservice.tag.domain;

import java.util.List;
import kr.mybrary.bookservice.tag.domain.dto.MeaningTagDtoMapper;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagFindPageServiceRequest;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import kr.mybrary.bookservice.tag.presentation.dto.response.MeaningTagElementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeaningTagService {

    private final MeaningTagRepository meaningTagRepository;
    private final MyBookMeaningTagRepository myBookMeaningTagRepository;

    @Transactional(readOnly = true)
    public List<MeaningTagElementResponse> findAll() {
        return meaningTagRepository.findAll()
                .stream()
                .map(MeaningTagDtoMapper.INSTANCE::entityToMeaningTagElementResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MeaningTagElementResponse> findPageOrderByRegisteredCount(
            MeaningTagFindPageServiceRequest request) {

        PageRequest pageRequest = PageRequest.of(0, request.getSize());

        return meaningTagRepository.findAllByOrderByRegisteredCountDesc(pageRequest)
                .map(MeaningTagDtoMapper.INSTANCE::entityToMeaningTagElementResponse)
                .toList();
    }

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
