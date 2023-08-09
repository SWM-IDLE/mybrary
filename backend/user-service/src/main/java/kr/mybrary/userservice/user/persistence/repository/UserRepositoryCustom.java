package kr.mybrary.userservice.user.persistence.repository;

import kr.mybrary.userservice.user.persistence.model.UserInfoModel;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserInfoModel> findAllUserInfoByLoginIds(List<String> loginIds);

}
