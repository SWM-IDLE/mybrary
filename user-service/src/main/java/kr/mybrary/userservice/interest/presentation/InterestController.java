package kr.mybrary.userservice.interest.presentation;

import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import kr.mybrary.userservice.interest.domain.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/interest-categories")
    public ResponseEntity<SuccessResponse> getInterestCategories() {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "카테고리별 관심사를 모두 조회했습니다.", interestService.getInterestCategories())
        );
    }

}