package kr.mybrary.userservice.authentication.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

     void reIssueToken(HttpServletRequest request, HttpServletResponse response);

}
