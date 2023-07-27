package kr.mybrary.userservice.interest.domain;

import jakarta.validation.constraints.NotNull;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    @Override
    public InterestCategoryServiceResponse getInterestsGroupByCategory() {
        return InterestCategoryServiceResponse.builder()
                .interestsGroupByCategory(getCategoryMap())
                .build();
    }

    @NotNull
    private Map<InterestCategory, List<Interest>> getCategoryMap() {
        return interestCategoryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        interestCategory -> interestRepository.findAllByCategory(interestCategory)
                ));
    }


}
