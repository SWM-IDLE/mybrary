package kr.mybrary.bookservice.tag.presentation;

import kr.mybrary.bookservice.global.dto.response.SuccessResponse;
import kr.mybrary.bookservice.tag.domain.MeaningTagService;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagFindPageServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meaning-tags")
public class MeaningTagController {

    private final MeaningTagService meaningTagService;

    @GetMapping
    public ResponseEntity getAllMeaningTags(@RequestHeader("USER-ID") String loginId) {

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "전체 의미 태그 조회 결과입니다.",
                meaningTagService.findAll()));
    }

    @GetMapping("/most")
    public ResponseEntity getPageMeaningTags(@RequestHeader("USER-ID") String loginId,
            @RequestParam int size) {

        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK.toString(), "등록된 수가 큰 순서대로 의미 태그 페이징 조회 결과입니다.",
                meaningTagService.findPageOrderByRegisteredCount(MeaningTagFindPageServiceRequest.of(size))));
    }
}
