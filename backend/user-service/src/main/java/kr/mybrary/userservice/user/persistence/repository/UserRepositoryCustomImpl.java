package kr.mybrary.userservice.user.persistence.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.mybrary.userservice.user.persistence.model.UserInfoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.mybrary.userservice.user.persistence.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserInfoModel> findAllUserInfoByLoginIds(List<String> loginIds) {
        return queryFactory
                .select(Projections.fields(UserInfoModel.class,
                        user.loginId,
                        user.nickname,
                        user.profileImageUrl))
                .from(user)
                .where(user.loginId.in(loginIds))
                .fetch();
    }
}
