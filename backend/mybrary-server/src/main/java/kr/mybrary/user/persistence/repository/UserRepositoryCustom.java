package kr.mybrary.user.persistence.repository;

import kr.mybrary.user.persistence.model.FollowUserInfoModel;
import kr.mybrary.user.persistence.model.UserInfoModel;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserInfoModel> findAllUserInfoByLoginIds(List<String> loginIds);

    List<FollowUserInfoModel> findAllFollowings(Long sourceId);

    List<FollowUserInfoModel> findAllFollowers(Long targetId);

}
