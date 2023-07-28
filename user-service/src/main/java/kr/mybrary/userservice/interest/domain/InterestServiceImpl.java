package kr.mybrary.userservice.interest.domain;

import jakarta.validation.constraints.NotNull;
import kr.mybrary.userservice.interest.domain.dto.InterestCategoryMapper;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public InterestCategoryServiceResponse getInterestCategories() {
        return InterestCategoryServiceResponse.builder()
                .interestCategories(getInterestCategoryResponses())
                .build();
    }

    @NotNull
    private List<InterestCategoryResponse> getInterestCategoryResponses() {
        return interestCategoryRepository.findAll()
                .stream()
                .map(interestCategory -> InterestCategoryMapper.INSTANCE.toInterestCategoryResponse(interestCategory))
                .toList();
    }

}
