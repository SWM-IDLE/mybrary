package kr.mybrary.userservice.user.domain.impl;

import jakarta.transaction.Transactional;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.ProfileResponse;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Override
    public ProfileResponse getProfile(String loginId) {
        return null;
    }

}
