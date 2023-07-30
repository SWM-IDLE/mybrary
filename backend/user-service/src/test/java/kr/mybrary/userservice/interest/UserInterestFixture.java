package kr.mybrary.userservice.interest;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.persistence.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserInterestFixture {

    COMMON_USER_INTEREST_1(1L, UserFixture.COMMON_USER.getUser(), InterestFixture.DOMESTIC_NOVEL.getInterest()),
    COMMON_USER_INTEREST_2(2L, UserFixture.COMMON_USER.getUser(), InterestFixture.FOREIGN_NOVEL.getInterest());

    private final Long id;
    private final User user;
    private final Interest interest;

    public UserInterest getUserInterest() {
        return UserInterest.builder()
                .id(id)
                .user(user)
                .interest(interest)
                .build();
    }
}
