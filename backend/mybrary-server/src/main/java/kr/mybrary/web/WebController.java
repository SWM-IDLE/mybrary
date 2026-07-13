package kr.mybrary.web;

import kr.mybrary.book.domain.BookInterestService;
import kr.mybrary.book.domain.dto.request.BookInterestServiceRequest;
import kr.mybrary.book.domain.dto.request.BookInterestStatusServiceRequest;
import kr.mybrary.book.domain.dto.request.BookMyInterestFindServiceRequest;
import kr.mybrary.book.persistence.BookOrderType;
import kr.mybrary.booksearch.domain.BookSearchRankingService;
import kr.mybrary.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.booksearch.presentation.dto.response.BookListByCategorySearchResultResponse;
import kr.mybrary.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.booksearch.presentation.dto.response.BookSearchRankingResponse;
import kr.mybrary.booksearch.presentation.dto.response.BookSearchResultResponse;
import kr.mybrary.interest.domain.InterestService;
import kr.mybrary.interest.domain.dto.request.UserInterestAndBookRecommendationsServiceRequest;
import kr.mybrary.interest.domain.dto.request.UserInterestUpdateServiceRequest;
import kr.mybrary.interest.domain.dto.response.InterestResponse;
import kr.mybrary.mybook.domain.MyBookService;
import kr.mybrary.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.mybook.domain.dto.request.MyBookDeleteServiceRequest;
import kr.mybrary.mybook.domain.dto.request.MyBookDetailServiceRequest;
import kr.mybrary.mybook.domain.dto.request.MyBookFindAllServiceRequest;
import kr.mybrary.mybook.domain.dto.request.MyBookRegisteredStatusServiceRequest;
import kr.mybrary.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.mybook.persistence.MyBookOrderType;
import kr.mybrary.mybook.persistence.ReadStatus;
import kr.mybrary.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.mybook.presentation.dto.response.MyBookRegistrationCountResponse;
import kr.mybrary.review.presentation.dto.response.MyReviewsOfBookGetResponse;
import kr.mybrary.review.domain.MyReviewReadService;
import kr.mybrary.review.domain.MyReviewWriteService;
import kr.mybrary.review.domain.dto.request.MyReviewCreateServiceRequest;
import kr.mybrary.review.domain.dto.request.MyReviewDeleteServiceRequest;
import kr.mybrary.review.domain.dto.request.MyReviewOfMyBookGetServiceRequest;
import kr.mybrary.review.domain.dto.request.MyReviewsOfBookGetServiceRequest;
import kr.mybrary.review.domain.dto.request.MyReviewUpdateServiceRequest;
import kr.mybrary.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import kr.mybrary.user.domain.UserService;
import kr.mybrary.user.domain.dto.request.FollowServiceRequest;
import kr.mybrary.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.user.domain.dto.response.FollowerServiceResponse;
import kr.mybrary.user.domain.dto.response.FollowingServiceResponse;
import kr.mybrary.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.user.presentation.dto.request.ProfileUpdateRequest;
import kr.mybrary.user.presentation.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Thymeleaf MPA 용 컨트롤러.
 * /web/** 경로를 담당하며, 서비스 레이어를 직접 호출한다 (HTTP 라운드트립 없음).
 * 세션 기반 인증 (Spring Security Form Login) 사용.
 */
@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final UserService userService;
    private final MyBookService myBookService;
    private final BookInterestService bookInterestService;
    private final PlatformBookSearchApiService platformBookSearchApiService;
    private final BookSearchRankingService bookSearchRankingService;
    private final InterestService interestService;
    private final MyReviewReadService myReviewReadService;
    private final MyReviewWriteService myReviewWriteService;

    // ===========================
    // 인증 (로그인 / 회원가입)
    // ===========================

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "web/login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "web/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequest signUpRequest,
                         RedirectAttributes redirectAttributes) {
        try {
            SignUpServiceResponse response = userService.signUp(SignUpServiceRequest.of(signUpRequest));
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/web/login";
        } catch (Exception e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/signup";
        }
    }

    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "web/find-password";
    }

    // ===========================
    // 홈
    // ===========================

    @GetMapping({"", "/"})
    public String home(Model model, Authentication auth) {
        String loginId = auth.getName();

        // 오늘 등록된 MyBook 수
        MyBookRegistrationCountResponse todayCount = myBookService.getBookRegistrationCountOfToday();
        model.addAttribute("todayCount", todayCount.getCount());

        // 베스트셀러 (Aladin ItemNewAll categoryId=0 활용)
        try {
            BookListByCategorySearchResultResponse bestsellers = platformBookSearchApiService
                    .searchBookListByCategory(BookListByCategorySearchServiceRequest.of("Bestseller", 0, 1));
            model.addAttribute("bestsellers", bestsellers.getBooks());
        } catch (Exception e) {
            log.warn("베스트셀러 조회 실패: {}", e.getMessage());
            model.addAttribute("bestsellers", List.of());
        }

        // 추천 도서 (사용자 관심사 기반)
        try {
            var recommendationsResponse = interestService.getInterestsAndBookRecommendations(
                    UserInterestAndBookRecommendationsServiceRequest.of(loginId, "ItemNewAll", 1)
            );
            model.addAttribute("recommendations", recommendationsResponse.getBookRecommendations());
            model.addAttribute("userInterests", recommendationsResponse.getUserInterests());
        } catch (Exception e) {
            log.warn("추천 도서 조회 실패: {}", e.getMessage());
            model.addAttribute("recommendations", List.of());
            model.addAttribute("userInterests", List.of());
        }

        model.addAttribute("loginId", loginId);
        return "web/home";
    }

    // ===========================
    // 도서 검색
    // ===========================

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "accuracy") String sort,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {

        // 인기 검색어
        BookSearchRankingResponse ranking = bookSearchRankingService.getBookSearchKeywordRankingList();
        model.addAttribute("ranking", ranking.getBookSearchKeywords());

        if (keyword != null && !keyword.isBlank()) {
            try {
                BookSearchResultResponse result = platformBookSearchApiService
                        .searchWithKeyword(BookSearchServiceRequest.of(keyword, sort, page));
                model.addAttribute("searchResults", result.getBookSearchResult());
                model.addAttribute("nextRequestUrl", result.getNextRequestUrl());
                bookSearchRankingService.increaseSearchRankingScore(keyword);
            } catch (Exception e) {
                log.warn("도서 검색 실패: keyword={}, error={}", keyword, e.getMessage());
                model.addAttribute("searchResults", List.of());
                model.addAttribute("searchError", "도서 검색 중 오류가 발생했습니다.");
            }
            model.addAttribute("keyword", keyword);
            model.addAttribute("sort", sort);
            model.addAttribute("page", page);
        }

        return "web/search";
    }

    @GetMapping("/search/detail")
    public String searchDetail(@RequestParam String isbn13,
                               @RequestParam(required = false) String isbn10,
                               Model model, Authentication auth) {
        String loginId = auth.getName();
        try {
            BookSearchDetailResponse detail = platformBookSearchApiService
                    .searchBookDetailWithISBN(BookSearchServiceRequest.of(isbn13));
            model.addAttribute("book", detail);

            // 도서 리뷰 (DB에 등록된 도서라면)
            try {
                MyReviewsOfBookGetResponse reviews = myReviewReadService.getReviewsFromBook(
                        MyReviewsOfBookGetServiceRequest.builder()
                                .isbn13(isbn13)
                                .build());
                model.addAttribute("reviews", reviews);
            } catch (Exception e) {
                model.addAttribute("reviews", null);
            }

            // 관심 도서 여부
            try {
                boolean interested = bookInterestService.getInterestStatus(
                        BookInterestStatusServiceRequest.builder()
                                .loginId(loginId)
                                .isbn13(isbn13)
                                .build()
                ).isInterested();
                model.addAttribute("interested", interested);
            } catch (Exception e) {
                model.addAttribute("interested", false);
            }

            // MyBook 등록 여부
            try {
                boolean registered = myBookService.getMyBookRegisteredStatus(
                        MyBookRegisteredStatusServiceRequest.builder()
                                .loginId(loginId)
                                .isbn13(isbn13)
                                .build()
                ).isRegistered();
                model.addAttribute("registered", registered);
            } catch (Exception e) {
                model.addAttribute("registered", false);
            }

        } catch (Exception e) {
            log.warn("도서 상세 조회 실패: isbn13={}, error={}", isbn13, e.getMessage());
            model.addAttribute("errorMessage", "도서 정보를 불러올 수 없습니다.");
        }
        model.addAttribute("loginId", loginId);
        return "web/search-detail";
    }

    // 관심 도서 토글 (POST)
    @PostMapping("/search/interest")
    public String toggleBookInterest(@RequestParam String isbn13,
                                     @RequestParam(required = false) String returnUrl,
                                     Authentication auth) {
        String loginId = auth.getName();
        try {
            bookInterestService.handleBookInterest(BookInterestServiceRequest.builder()
                    .loginId(loginId)
                    .isbn13(isbn13)
                    .build());
        } catch (Exception e) {
            log.warn("관심 도서 토글 실패: {}", e.getMessage());
        }
        return "redirect:" + (returnUrl != null ? returnUrl : "/web/search/detail?isbn13=" + isbn13);
    }

    // MyBook 등록 (POST)
    @PostMapping("/search/mybook")
    public String registerMyBook(@RequestParam String isbn13,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            myBookService.create(MyBookCreateServiceRequest.builder()
                    .userId(loginId)
                    .isbn13(isbn13)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "내 책으로 등록되었습니다.");
        } catch (Exception e) {
            log.warn("MyBook 등록 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/search/detail?isbn13=" + isbn13;
    }

    // ===========================
    // MyBook 목록
    // ===========================

    @GetMapping("/mybooks")
    public String mybooks(@RequestParam(defaultValue = "NONE") String orderType,
                          @RequestParam(required = false) String readStatus,
                          Model model, Authentication auth) {
        String loginId = auth.getName();

        MyBookOrderType myBookOrderType = parseMyBookOrderType(orderType);
        ReadStatus readStatusEnum = ReadStatus.of(readStatus);

        List<MyBookElementResponse> myBooks = myBookService.findAllMyBooks(
                MyBookFindAllServiceRequest.of(loginId, loginId, myBookOrderType, readStatusEnum));

        model.addAttribute("mybooks", myBooks);
        model.addAttribute("orderType", orderType);
        model.addAttribute("readStatus", readStatus);
        model.addAttribute("loginId", loginId);
        return "web/mybooks";
    }

    @GetMapping("/mybooks/{id}")
    public String mybookDetail(@PathVariable Long id, Model model, Authentication auth) {
        String loginId = auth.getName();

        MyBookDetailResponse detail = myBookService.findMyBookDetail(
                MyBookDetailServiceRequest.builder()
                        .mybookId(id)
                        .loginId(loginId)
                        .build());

        MyReviewOfMyBookGetResponse review = myReviewReadService.getReviewFromMyBook(
                MyReviewOfMyBookGetServiceRequest.of(id));

        model.addAttribute("mybook", detail);
        model.addAttribute("review", review);
        model.addAttribute("loginId", loginId);
        model.addAttribute("readStatusValues", ReadStatus.values());
        return "web/mybook-detail";
    }

    // MyBook 업데이트 (POST)
    @PostMapping("/mybooks/{id}/update")
    public String updateMyBook(@PathVariable Long id,
                               @RequestParam(defaultValue = "false") boolean showable,
                               @RequestParam(defaultValue = "false") boolean exchangeable,
                               @RequestParam(defaultValue = "false") boolean shareable,
                               @RequestParam(required = false) String readStatus,
                               @RequestParam(required = false) String meaningTagQuote,
                               @RequestParam(required = false) String meaningTagColorCode,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            MybookUpdateServiceRequest request = MybookUpdateServiceRequest.builder()
                    .loginId(loginId)
                    .myBookId(id)
                    .showable(showable)
                    .exchangeable(exchangeable)
                    .shareable(shareable)
                    .readStatus(ReadStatus.of(readStatus))
                    .startDateOfPossession(LocalDateTime.now())
                    .meaningTag(MybookUpdateServiceRequest.MeaningTag.builder()
                            .quote(meaningTagQuote != null ? meaningTagQuote : "")
                            .colorCode(meaningTagColorCode != null ? meaningTagColorCode : "19C568")
                            .build())
                    .build();
            myBookService.updateMyBookProperties(request);
            redirectAttributes.addFlashAttribute("successMessage", "내 책 정보가 수정되었습니다.");
        } catch (Exception e) {
            log.warn("MyBook 수정 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/mybooks/" + id;
    }

    // MyBook 삭제 (POST)
    @PostMapping("/mybooks/{id}/delete")
    public String deleteMyBook(@PathVariable Long id,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            myBookService.deleteMyBook(MyBookDeleteServiceRequest.builder()
                    .mybookId(id)
                    .loginId(loginId)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "내 책이 삭제되었습니다.");
        } catch (Exception e) {
            log.warn("MyBook 삭제 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/mybooks";
    }

    // ===========================
    // 리뷰 작성 / 수정 / 삭제
    // ===========================

    @PostMapping("/mybooks/{id}/review")
    public String createReview(@PathVariable Long id,
                               @RequestParam String content,
                               @RequestParam Double starRating,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            myReviewWriteService.create(MyReviewCreateServiceRequest.builder()
                    .myBookId(id)
                    .loginId(loginId)
                    .content(content)
                    .starRating(starRating)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "리뷰가 등록되었습니다.");
        } catch (Exception e) {
            log.warn("리뷰 작성 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/mybooks/" + id;
    }

    @PostMapping("/mybooks/{id}/review/{reviewId}/update")
    public String updateReview(@PathVariable Long id,
                               @PathVariable Long reviewId,
                               @RequestParam String content,
                               @RequestParam Double starRating,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            myReviewWriteService.update(MyReviewUpdateServiceRequest.builder()
                    .myReviewId(reviewId)
                    .loginId(loginId)
                    .content(content)
                    .starRating(starRating)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "리뷰가 수정되었습니다.");
        } catch (Exception e) {
            log.warn("리뷰 수정 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/mybooks/" + id;
    }

    @PostMapping("/mybooks/{id}/review/{reviewId}/delete")
    public String deleteReview(@PathVariable Long id,
                               @PathVariable Long reviewId,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            myReviewWriteService.delete(MyReviewDeleteServiceRequest.builder()
                    .myReviewId(reviewId)
                    .loginId(loginId)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "리뷰가 삭제되었습니다.");
        } catch (Exception e) {
            log.warn("리뷰 삭제 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/mybooks/" + id;
    }

    // ===========================
    // 관심 도서 목록
    // ===========================

    @GetMapping("/mybooks/interest")
    public String mybooksInterest(@RequestParam(defaultValue = "NONE") String orderType,
                                  Model model, Authentication auth) {
        String loginId = auth.getName();
        BookOrderType bookOrderType = parseBookOrderType(orderType);

        var interestBooks = bookInterestService.getBookInterestList(
                BookMyInterestFindServiceRequest.of(loginId, bookOrderType));

        model.addAttribute("interestBooks", interestBooks);
        model.addAttribute("orderType", orderType);
        model.addAttribute("loginId", loginId);
        return "web/mybooks-interest";
    }

    // ===========================
    // 프로필
    // ===========================

    @GetMapping("/profile")
    public String myProfile(Model model, Authentication auth) {
        return profilePage(auth.getName(), model, auth);
    }

    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable String userId, Model model, Authentication auth) {
        return profilePage(userId, model, auth);
    }

    private String profilePage(String targetUserId, Model model, Authentication auth) {
        String loginId = auth.getName();
        try {
            ProfileServiceResponse profile = userService.getProfile(targetUserId);
            FollowerServiceResponse followers = userService.getFollowers(targetUserId);
            FollowingServiceResponse followings = userService.getFollowings(targetUserId);

            boolean isMyProfile = loginId.equals(targetUserId);
            boolean isFollowing = false;
            if (!isMyProfile) {
                isFollowing = userService.getFollowStatus(
                        FollowServiceRequest.of(loginId, targetUserId)).isFollowing();
            }

            model.addAttribute("profile", profile);
            model.addAttribute("targetUserId", targetUserId);
            model.addAttribute("followerCount", followers.getFollowers().size());
            model.addAttribute("followingCount", followings.getFollowings().size());
            model.addAttribute("isMyProfile", isMyProfile);
            model.addAttribute("isFollowing", isFollowing);
            model.addAttribute("loginId", loginId);
        } catch (Exception e) {
            log.warn("프로필 조회 실패: userId={}, error={}", targetUserId, e.getMessage());
            model.addAttribute("errorMessage", "프로필을 불러올 수 없습니다.");
        }
        return "web/profile";
    }

    @GetMapping("/profile/edit")
    public String profileEditPage(Model model, Authentication auth) {
        String loginId = auth.getName();
        ProfileServiceResponse profile = userService.getProfile(loginId);
        model.addAttribute("profile", profile);
        model.addAttribute("loginId", loginId);
        return "web/profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@RequestParam String nickname,
                                @RequestParam(required = false) String introduction,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            ProfileUpdateRequest updateRequest = ProfileUpdateRequest.builder()
                    .nickname(nickname)
                    .introduction(introduction != null ? introduction : "")
                    .build();

            userService.updateProfile(ProfileUpdateServiceRequest.of(updateRequest, loginId, loginId));
            redirectAttributes.addFlashAttribute("successMessage", "프로필이 수정되었습니다.");
        } catch (Exception e) {
            log.warn("프로필 수정 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/profile";
    }

    // 팔로우 / 언팔로우
    @PostMapping("/profile/{userId}/follow")
    public String follow(@PathVariable String userId, Authentication auth) {
        String loginId = auth.getName();
        try {
            userService.follow(FollowServiceRequest.of(loginId, userId));
        } catch (Exception e) {
            log.warn("팔로우 실패: {}", e.getMessage());
        }
        return "redirect:/web/profile/" + userId;
    }

    @PostMapping("/profile/{userId}/unfollow")
    public String unfollow(@PathVariable String userId, Authentication auth) {
        String loginId = auth.getName();
        try {
            userService.unfollow(FollowServiceRequest.of(loginId, userId));
        } catch (Exception e) {
            log.warn("언팔로우 실패: {}", e.getMessage());
        }
        return "redirect:/web/profile/" + userId;
    }

    // 팔로워 / 팔로잉 목록
    @GetMapping("/profile/{userId}/followers")
    public String followerList(@PathVariable String userId, Model model, Authentication auth) {
        FollowerServiceResponse followers = userService.getFollowers(userId);
        model.addAttribute("followers", followers.getFollowers());
        model.addAttribute("targetUserId", userId);
        model.addAttribute("loginId", auth.getName());
        return "web/follow-list";
    }

    @GetMapping("/profile/{userId}/followings")
    public String followingList(@PathVariable String userId, Model model, Authentication auth) {
        FollowingServiceResponse followings = userService.getFollowings(userId);
        model.addAttribute("followings", followings.getFollowings());
        model.addAttribute("targetUserId", userId);
        model.addAttribute("loginId", auth.getName());
        return "web/follow-list";
    }

    // ===========================
    // 관심사
    // ===========================

    @GetMapping("/interests")
    public String interestsPage(Model model, Authentication auth) {
        String loginId = auth.getName();
        var allCategories = interestService.getInterestCategories();
        var userInterests = interestService.getUserInterests(loginId);

        model.addAttribute("categories", allCategories.getInterestCategories());
        model.addAttribute("userInterestIds",
                userInterests.getUserInterests().stream()
                        .map(InterestResponse::getId)
                        .toList());
        model.addAttribute("loginId", loginId);
        return "web/interests";
    }

    @PostMapping("/interests")
    public String updateInterests(@RequestParam(required = false) List<Long> interestIds,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            List<Long> ids = interestIds != null ? interestIds : List.of();
            interestService.updateUserInterests(
                    UserInterestUpdateServiceRequest.builder()
                            .userId(loginId)
                            .loginId(loginId)
                            .interestIds(ids)
                            .build());
            redirectAttributes.addFlashAttribute("successMessage", "관심사가 저장되었습니다.");
        } catch (Exception e) {
            log.warn("관심사 업데이트 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/web/interests";
    }

    // ===========================
    // 설정
    // ===========================

    @GetMapping("/settings")
    public String settingsPage(Model model, Authentication auth) {
        model.addAttribute("loginId", auth.getName());
        return "web/settings";
    }

    @PostMapping("/settings/delete-account")
    public String deleteAccount(Authentication auth,
                                RedirectAttributes redirectAttributes) {
        String loginId = auth.getName();
        try {
            userService.deleteAccount(loginId);
            return "redirect:/web/login?accountDeleted=true";
        } catch (Exception e) {
            log.warn("계정 탈퇴 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "계정 탈퇴에 실패했습니다.");
            return "redirect:/web/settings";
        }
    }

    // ===========================
    // 유틸리티
    // ===========================

    private MyBookOrderType parseMyBookOrderType(String orderType) {
        try {
            return MyBookOrderType.valueOf(orderType.toUpperCase());
        } catch (Exception e) {
            return MyBookOrderType.NONE;
        }
    }

    private BookOrderType parseBookOrderType(String orderType) {
        try {
            return BookOrderType.valueOf(orderType.toUpperCase());
        } catch (Exception e) {
            return BookOrderType.NONE;
        }
    }
}
