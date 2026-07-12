package kr.mybrary.user.domain;

import kr.mybrary.user.domain.dto.request.UserInfoServiceRequest;
import kr.mybrary.user.domain.dto.response.UserInfoServiceResponse;

/**
 * book 도메인이 user 정보를 조회할 때 사용하는 포트 인터페이스.
 * 모놀리식 환경에서는 UserServicePortImpl이 UserService를 직접 호출한다.
 * book-service의 UserServiceClient(Feign)를 대체한다.
 */
public interface UserServicePort {

    /**
     * loginId 목록으로 사용자 정보(닉네임, 프로필 이미지)를 일괄 조회한다.
     *
     * @param request loginId 리스트를 담은 요청 객체
     * @return UserInfo 리스트를 담은 응답 객체
     */
    UserInfoServiceResponse getUsersInfo(UserInfoServiceRequest request);

}
