package kr.mybrary.userservice.user.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.domain.dto.request.*;
import kr.mybrary.userservice.user.domain.dto.response.FollowResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowStatusServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowerServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.FollowingServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileImageUrlServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SearchServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.userservice.user.domain.dto.response.UserInfoServiceResponse;
import kr.mybrary.userservice.user.persistence.Role;
import kr.mybrary.userservice.user.presentation.dto.request.FollowRequest;
import kr.mybrary.userservice.user.presentation.dto.request.FollowerRequest;
import kr.mybrary.userservice.user.presentation.dto.request.ProfileUpdateRequest;
import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.presentation.dto.request.UserInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "loginId_1", roles = {"USER"})
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/api/v1/users";
    private final String USER_ID = "userId";
    private final String STATUS_FIELD_DESCRIPTION = "응답 상태";
    private final String MESSAGE_FIELD_DESCRIPTION = "응답 메시지";

    @DisplayName("아이디, 비밀번호, 닉네임, 이메일을 입력해 회원 가입을 한다.")
    @Test
    void signUp() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@email.com")
                .build();

        SignUpServiceResponse signUpServiceResponse = SignUpServiceResponse.builder()
                .loginId(signUpRequest.getLoginId())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .role(Role.USER)
                .build();

        given(userService.signUp(any(SignUpServiceRequest.class))).willReturn(
                signUpServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(BASE_URL+"/sign-up")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.toString()))
                .andExpect(jsonPath("$.message").value("회원 가입에 성공했습니다."))
                .andExpect(jsonPath("$.data.loginId").value(signUpRequest.getLoginId()))
                .andExpect(jsonPath("$.data.nickname").value(signUpRequest.getNickname()))
                .andExpect(jsonPath("$.data.email").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("$.data.role").value(Role.USER.name()));

        verify(userService).signUp(any(SignUpServiceRequest.class));

        // docs
        actions.andDo(document("user-sign-up",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-sign-up")
                                .summary("아이디, 비밀번호, 닉네임, 이메일을 입력해 회원 가입을 한다.")
                                .requestSchema(Schema.schema("user_sign_up_request_body"))
                                .requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING)
                                                .description("회원 가입 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING)
                                                .description(
                                                        "회원 가입 비밀번호 (8~16자 영문 대 소문자, 숫자, 특수문자 사용)"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                                .description("회원 가입 닉네임 (특수문자를 제외한 2~20자 사용)"),
                                        fieldWithPath("email").type(JsonFieldType.STRING)
                                                .description("회원 가입 이메일(선택)")
                                )
                                .responseSchema(Schema.schema("user_sign_up_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.loginId").type(JsonFieldType.STRING)
                                                .description("가입된 아이디"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("가입된 닉네임"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                                                .description("가입된 이메일"),
                                        fieldWithPath("data.role").type(JsonFieldType.STRING)
                                                .description("가입된 권한")
                                )
                                .build()
                ))
        );
    }


    @DisplayName("사용자의 프로필 정보를 조회한다.")
    @Test
    void getProfile() throws Exception {
        // given
        ProfileServiceResponse profileServiceResponse = ProfileServiceResponse.builder()
                .nickname("nickname_1")
                .profileImageUrl("profileImageUrl_1")
                .introduction("introduction_1")
                .build();

        given(userService.getProfile(anyString())).willReturn(profileServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL+"/{userId}/profile", USER_ID)
                        .with(csrf()));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 프로필 정보입니다."))
                .andExpect(jsonPath("$.data.nickname").value(profileServiceResponse.getNickname()))
                .andExpect(jsonPath("$.data.profileImageUrl").value(
                        profileServiceResponse.getProfileImageUrl()))
                .andExpect(jsonPath("$.data.introduction").value(
                        profileServiceResponse.getIntroduction()));

        verify(userService).getProfile(anyString());

        // docs
        actions.andDo(document("get-user-profile",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-profile")
                                .summary("사용자의 프로필 정보를 조회한다.")
                                .requestSchema(Schema.schema("get_user_profile_request_body"))
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 아이디")
                                )
                                .responseSchema(Schema.schema("get_user_profile_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("사용자의 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(
                                                        JsonFieldType.STRING)
                                                .description("사용자의 프로필 이미지 URL"),
                                        fieldWithPath("data.introduction").type(
                                                JsonFieldType.STRING).description("사용자의 한 줄 소개")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("로그인 된 사용자의 프로필 정보를 수정한다.")
    @Test
    void updateProfile() throws Exception {
        // given
        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .nickname("new_nickname")
                .introduction("new_introduction")
                .build();

        ProfileServiceResponse profileServiceResponse = ProfileServiceResponse.builder()
                .nickname(profileUpdateRequest.getNickname())
                .profileImageUrl("profileImageUrl")
                .introduction(profileUpdateRequest.getIntroduction())
                .build();

        given(userService.updateProfile(any(ProfileUpdateServiceRequest.class))).willReturn(
                profileServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASE_URL+"/{userId}/profile", USER_ID)
                        .with(csrf())
                        .header("USER-ID", USER_ID)
                        .content(objectMapper.writeValueAsString(profileUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        verify(userService).updateProfile(any(ProfileUpdateServiceRequest.class));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("로그인 된 사용자의 프로필 정보를 수정했습니다."))
                .andExpect(jsonPath("$.data.nickname").value(profileServiceResponse.getNickname()))
                .andExpect(jsonPath("$.data.profileImageUrl").value(
                        profileServiceResponse.getProfileImageUrl()))
                .andExpect(jsonPath("$.data.introduction").value(
                        profileServiceResponse.getIntroduction()));

        // docs
        actions.andDo(document("update-user-profile",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-profile")
                                .summary("로그인한 사용자의 프로필 정보를 수정한다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 아이디")
                                )
                                .requestSchema(Schema.schema("update_user_profile_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                                .description("수정할 닉네임"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING)
                                                .description("수정할 한 줄 소개")
                                )
                                .responseSchema(Schema.schema("update_user_profile_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("수정된 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(
                                                JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("data.introduction").type(
                                                JsonFieldType.STRING).description("수정된 한 줄 소개")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자의 프로필 이미지 URL을 조회한다.")
    @Test
    void getProfileImageUrl() throws Exception {
        // given
        ProfileImageUrlServiceResponse profileImageUrlServiceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl("tiny_profileImageUrl")
                .build();

        given(userService.getProfileImageUrl(any(ProfileImageUrlServiceRequest.class))).willReturn(
                profileImageUrlServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL+"/{userId}/profile/image", USER_ID)
                        .param("size", "tiny"));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 프로필 이미지 URL입니다."))
                .andExpect(jsonPath("$.data.profileImageUrl").value(
                        profileImageUrlServiceResponse.getProfileImageUrl()));

        verify(userService).getProfileImageUrl(any(ProfileImageUrlServiceRequest.class));

        // docs
        actions.andDo(document("get-user-profile-image",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-profile")
                                .summary("사용자의 프로필 이미지 URL을 조회한다.")
                                .description("size는 tiny, small, original이 가능하다. 기본값은 original이다. 리사이징 된 이미지가 없으면 original 이미지 url을 반환한다.")
                                .requestSchema(Schema.schema("get_user_profile_image_request_body"))
                                .pathParameters(
                                        parameterWithName("userId").description("사용자의 아이디")
                                )
                                .queryParameters(
                                        parameterWithName("size").type(SimpleType.STRING).description("프로필 이미지의 크기")
                                )
                                .responseSchema(
                                        Schema.schema("get_user_profile_image_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.profileImageUrl").type(
                                                JsonFieldType.STRING).description("프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("로그인 된 사용자의 프로필 이미지를 등록한다.")
    @Test
    void updateProfileImage() throws Exception {
        // given
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "user1.png", "image/jpg",
                "new_profileImage".getBytes());

        ProfileImageUrlServiceResponse profileImageUrlServiceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl("new_profileImageUrl")
                .build();

        given(userService.updateProfileImage(any(ProfileImageUpdateServiceRequest.class))).willReturn(
                profileImageUrlServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.multipart(BASE_URL+"/{userId}/profile/image", USER_ID)
                        .file(profileImage)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .with(csrf())
                        .header("USER-ID", USER_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("로그인 된 사용자의 프로필 이미지를 등록했습니다."))
                .andExpect(jsonPath("$.data.profileImageUrl").value(
                        profileImageUrlServiceResponse.getProfileImageUrl()));

        verify(userService).updateProfileImage(any(ProfileImageUpdateServiceRequest.class));

        // docs
        actions.andDo(document("update-user-profile-image",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-profile")
                                .summary("로그인한 사용자의 프로필 이미지를 수정한다.")
                                .description("프로필 이미지는 5MB 이하의 jpg, png 파일만 등록할 수 있습니다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 아이디")
                                )
                                .requestSchema(
                                        Schema.schema("update_user_profile_image_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .responseSchema(
                                        Schema.schema("update_user_profile_image_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.profileImageUrl").type(
                                                JsonFieldType.STRING).description("수정된 프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("로그인 된 사용자의 프로필 이미지를 삭제한다.")
    @Test
    void deleteProfileImage() throws Exception {
        // given
        ProfileImageUrlServiceResponse profileImageUrlServiceResponse = ProfileImageUrlServiceResponse.builder()
                .profileImageUrl("default_profileImageUrl")
                .build();

        given(userService.deleteProfileImage(any(ProfileImageUpdateServiceRequest.class))).willReturn(
                profileImageUrlServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(BASE_URL+"/{userId}/profile/image", USER_ID)
                        .with(csrf())
                        .header("USER-ID", USER_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("로그인 된 사용자의 프로필 이미지를 삭제했습니다."))
                .andExpect(jsonPath("$.data.profileImageUrl").value(
                        profileImageUrlServiceResponse.getProfileImageUrl()));

        verify(userService).deleteProfileImage(any(ProfileImageUpdateServiceRequest.class));

        // docs
        actions.andDo(document("delete-user-profile-image",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-profile")
                                .summary("로그인한 사용자의 프로필 이미지를 삭제한다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 아이디")
                                )
                                .requestSchema(
                                        Schema.schema("delete_user_profile_image_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .responseSchema(
                                        Schema.schema("delete_user_profile_image_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.profileImageUrl").type(
                                                JsonFieldType.STRING).description("기본 프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자의 팔로워 목록을 조회한다.")
    @Test
    void getFollowers() throws Exception {
        // given
        List<FollowResponse> followers = new ArrayList<>();
        followers.add(FollowResponse.builder()
                .userId("followerId_1")
                .nickname("followerNickname_1")
                .profileImageUrl("followerProfileImageUrl_1")
                .build());
        followers.add(FollowResponse.builder()
                .userId("followerId_2")
                .nickname("followerNickname_2")
                .profileImageUrl("followerProfileImageUrl_2")
                .build());

        FollowerServiceResponse followerServiceResponse = FollowerServiceResponse.builder()
                .userId(USER_ID)
                .followers(followers)
                .build();

        given(userService.getFollowers(anyString())).willReturn(followerServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL+"/{userId}/followers", USER_ID)
                        .with(csrf()));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 팔로워 목록을 조회했습니다."))
                .andExpect(jsonPath("$.data.userId").value(
                        followerServiceResponse.getUserId()))
                .andExpect(jsonPath("$.data.followers[0].userId").value(
                        followers.get(0).getUserId()))
                .andExpect(jsonPath("$.data.followers[1].userId").value(
                        followers.get(1).getUserId()))
                .andExpect(jsonPath("$.data.followers[0].nickname").value(
                        followers.get(0).getNickname()))
                .andExpect(jsonPath("$.data.followers[1].nickname").value(
                        followers.get(1).getNickname()))
                .andExpect(jsonPath("$.data.followers[0].profileImageUrl").value(
                        followers.get(0).getProfileImageUrl()))
                .andExpect(jsonPath("$.data.followers[1].profileImageUrl").value(
                        followers.get(1).getProfileImageUrl()));

        verify(userService).getFollowers(anyString());

        // docs
        actions.andDo(document("get-user-followers",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-follow")
                                .summary("사용자의 팔로워 목록을 조회한다.")
                                .pathParameters(
                                        parameterWithName("userId").type(SimpleType.STRING).description("사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("get_user_followers_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.userId").type(
                                                JsonFieldType.STRING).description("요청한 사용자의 아이디"),
                                        fieldWithPath("data.followers[].userId").type(
                                                JsonFieldType.STRING).description("팔로워의 아이디"),
                                        fieldWithPath("data.followers[].nickname").type(
                                                JsonFieldType.STRING).description("팔로워의 닉네임"),
                                        fieldWithPath("data.followers[].profileImageUrl").type(
                                                        JsonFieldType.STRING)
                                                .description("팔로워의 프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자의 팔로잉 목록을 조회한다.")
    @Test
    void getFollowings() throws Exception {
        // given
        List<FollowResponse> followings = new ArrayList<>();
        followings.add(FollowResponse.builder()
                .userId("followingId_1")
                .nickname("followingNickname_1")
                .profileImageUrl("followingProfileImageUrl_1")
                .build());
        followings.add(FollowResponse.builder()
                .userId("followingId_2")
                .nickname("followingNickname_2")
                .profileImageUrl("followingProfileImageUrl_2")
                .build());

        FollowingServiceResponse followingServiceResponse = FollowingServiceResponse.builder()
                .userId(USER_ID)
                .followings(followings)
                .build();

        given(userService.getFollowings(anyString())).willReturn(followingServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL+"/{userId}/followings", USER_ID)
                        .with(csrf()));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자의 팔로잉 목록을 조회했습니다."))
                .andExpect(jsonPath("$.data.userId").value(
                        followingServiceResponse.getUserId()))
                .andExpect(jsonPath("$.data.followings[0].userId").value(
                        followings.get(0).getUserId()))
                .andExpect(jsonPath("$.data.followings[1].userId").value(
                        followings.get(1).getUserId()))
                .andExpect(jsonPath("$.data.followings[0].nickname").value(
                        followings.get(0).getNickname()))
                .andExpect(jsonPath("$.data.followings[1].nickname").value(
                        followings.get(1).getNickname()))
                .andExpect(jsonPath("$.data.followings[0].profileImageUrl").value(
                        followings.get(0).getProfileImageUrl()))
                .andExpect(jsonPath("$.data.followings[1].profileImageUrl").value(
                        followings.get(1).getProfileImageUrl()));

        verify(userService).getFollowings(anyString());

        // docs
        actions.andDo(document("get-user-followings",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-follow")
                                .summary("사용자의 팔로잉 목록을 조회한다.")
                                .pathParameters(
                                        parameterWithName("userId").type(SimpleType.STRING).description("사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("get_user_followings_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.userId").type(
                                                JsonFieldType.STRING).description("요청한 사용자의 아이디"),
                                        fieldWithPath("data.followings[].userId").type(
                                                JsonFieldType.STRING).description("팔로잉의 아이디"),
                                        fieldWithPath("data.followings[].nickname").type(
                                                JsonFieldType.STRING).description("팔로잉의 닉네임"),
                                        fieldWithPath("data.followings[].profileImageUrl").type(
                                                        JsonFieldType.STRING)
                                                .description("팔로잉의 프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자를 팔로우한다.")
    @Test
    void follow() throws Exception {
        // given
        doNothing().when(userService).follow(any(FollowServiceRequest.class));

        FollowRequest followRequest = FollowRequest.builder()
                .targetId("targetId")
                .build();

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(BASE_URL+"/follow")
                        .with(csrf())
                        .header("USER-ID", USER_ID)
                        .content(objectMapper.writeValueAsString(followRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자를 팔로우했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(userService).follow(any(FollowServiceRequest.class));

        // docs
        actions.andDo(document("follow-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-follow")
                                .summary("사용자를 팔로우한다.")
                                .requestSchema(Schema.schema("follow_user_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("targetId").type(JsonFieldType.STRING)
                                                .description("팔로우할 사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("follow_user_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("응답 데이터").optional()
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자를 언팔로우한다.")
    @Test
    void unfollow() throws Exception {
        // given
        doNothing().when(userService).unfollow(any(FollowServiceRequest.class));

        FollowRequest followRequest = FollowRequest.builder()
                .targetId("targetId")
                .build();

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(BASE_URL+"/follow")
                        .with(csrf())
                        .header("USER-ID", USER_ID)
                        .content(objectMapper.writeValueAsString(followRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자를 언팔로우했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(userService).unfollow(any(FollowServiceRequest.class));

        // docs
        actions.andDo(document("unfollow-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-follow")
                                .summary("사용자를 언팔로우한다.")
                                .requestSchema(Schema.schema("unfollow_user_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("targetId").type(JsonFieldType.STRING)
                                                .description("언팔로우할 사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("unfollow_user_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("응답 데이터").optional()
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자를 팔로워 목록에서 삭제한다.")
    @Test
    void deleteFollower() throws Exception {
        // given
        doNothing().when(userService).deleteFollower(any(FollowerServiceRequest.class));

        FollowerRequest followerRequest = FollowerRequest.builder()
                .sourceId("sourceId")
                .build();

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(BASE_URL+"/follower")
                        .with(csrf())
                        .header("USER-ID", USER_ID)
                        .content(objectMapper.writeValueAsString(followerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자를 팔로워 목록에서 삭제했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(userService).deleteFollower(any(FollowerServiceRequest.class));

        // docs
        actions.andDo(document("delete-follower",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-follow")
                                .summary("사용자를 팔로워 목록에서 삭제한다.")
                                .requestSchema(Schema.schema("delete_follower_request_body"))
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("sourceId").type(JsonFieldType.STRING)
                                                .description("팔로워 목록에서 삭제할 사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("delete_follower_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("응답 데이터").optional()
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자를 팔로우 중인지 확인한다.")
    @Test
    void isFollowing() throws Exception {
        // given
        FollowStatusServiceResponse followStatusServiceResponse = FollowStatusServiceResponse.builder()
                .userId(USER_ID)
                .targetId("targetId")
                .isFollowing(true)
                .build();

        given(userService.getFollowStatus(any(FollowServiceRequest.class))).willReturn(followStatusServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL+"/follow")
                        .param("targetId", "targetId")
                        .header("USER-ID", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("사용자를 팔로우 중인지 확인했습니다."))
                .andExpect(jsonPath("$.data.userId").value(USER_ID))
                .andExpect(jsonPath("$.data.targetId").value("targetId"))
                .andExpect(jsonPath("$.data.following").value(true));

        verify(userService).getFollowStatus(any(FollowServiceRequest.class));

        // docs
        actions.andDo(document("is-following",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-follow")
                                .summary("사용자를 팔로우 중인지 확인한다.")
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .queryParameters(
                                        parameterWithName("targetId").description("팔로우 중인지 확인할 사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("is_following_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.userId").type(JsonFieldType.STRING)
                                                .description("요청한 사용자의 아이디"),
                                        fieldWithPath("data.targetId").type(JsonFieldType.STRING)
                                                .description("팔로우 중인지 확인할 사용자의 아이디"),
                                        fieldWithPath("data.following").type(JsonFieldType.BOOLEAN)
                                                .description("팔로우 중인지 여부")
                                )
                                .build()
                ))
        );
    }


    @DisplayName("닉네임으로 사용자를 검색한다.")
    @Test
    void searchByNickname() throws Exception {
        // given
        SearchServiceResponse searchServiceResponse = SearchServiceResponse.builder()
                .searchedUsers(List.of(
                        SearchServiceResponse.SearchedUser.builder()
                                .userId("userId_1")
                                .nickname("nickname_1")
                                .profileImageUrl("profileImageUrl_1")
                                .build(),
                        SearchServiceResponse.SearchedUser.builder()
                                .userId("userId_2")
                                .nickname("nickname_2")
                                .profileImageUrl("profileImageUrl_2")
                                .build()
                )).build();

        given(userService.searchByNickname(any())).willReturn(searchServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_URL + "/search")
                        .param("nickname", "nickname"));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("닉네임으로 사용자를 검색했습니다."))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.searchedUsers[0].nickname").value(searchServiceResponse.getSearchedUsers().get(0).getNickname()))
                .andExpect(jsonPath("$.data.searchedUsers[1].nickname").value(searchServiceResponse.getSearchedUsers().get(1).getNickname()));

        verify(userService).searchByNickname(any());

        // docs
        actions.andDo(document("search-user-by-nickname",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user-search")
                                .summary("닉네임으로 사용자를 검색한다.")
                                .queryParameters(
                                        parameterWithName("nickname").type(SimpleType.STRING).description("검색할 닉네임")
                                )
                                .responseSchema(Schema.schema("search_user_by_nickname_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data.searchedUsers").type(JsonFieldType.ARRAY).description("검색된 사용자 목록"),
                                        fieldWithPath("data.searchedUsers[].userId").type(JsonFieldType.STRING).description("검색된 사용자의 아이디"),
                                        fieldWithPath("data.searchedUsers[].nickname").type(JsonFieldType.STRING).description("검색된 사용자의 닉네임"),
                                        fieldWithPath("data.searchedUsers[].profileImageUrl").type(JsonFieldType.STRING).description("검색된 사용자의 프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자를 탈퇴 처리한다.")
    @Test
    void deleteAccount() throws Exception {
        // given
        doNothing().when(userService).deleteAccount(any());

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(BASE_URL + "/account")
                        .with(csrf())
                        .header("USER-ID", USER_ID));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("회원 탈퇴에 성공했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(userService).deleteAccount(any());

        // docs
        actions.andDo(document("delete-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("user")
                                .summary("사용자를 탈퇴 처리한다.")
                                .requestHeaders(
                                        headerWithName("USER-ID").description("로그인 된 사용자의 아이디")
                                )
                                .responseSchema(Schema.schema("delete_user_response_body"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description(STATUS_FIELD_DESCRIPTION),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description(MESSAGE_FIELD_DESCRIPTION),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("응답 데이터").optional()
                                )
                                .build()
                ))
        );
    }

    @DisplayName("사용자 정보를 조회한다. (for book-service)")
    @Test
    void getUserInfoCalledByFeignClient() throws Exception {
        // given
        UserInfoRequest userInfoRequest = UserInfoRequest.builder()
                .userIds(List.of("userId_1", "userId_2"))
                .build();

        UserInfoServiceResponse userInfoServiceResponse = UserInfoServiceResponse.builder()
                .userInfoElements(List.of(
                        UserInfoServiceResponse.UserInfoElement.builder()
                                .userId("userId_1")
                                .nickname("nickname_1")
                                .profileImageUrl("profileImageUrl_1")
                                .build(),
                        UserInfoServiceResponse.UserInfoElement.builder()
                                .userId("userId_2")
                                .nickname("nickname_2")
                                .profileImageUrl("profileImageUrl_2")
                                .build()
                )).build();

        given(userService.getUserInfo(any(UserInfoServiceRequest.class))).willReturn(userInfoServiceResponse);

        // when
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(BASE_URL + "/info")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoRequest)));

        // then
        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.userInfoElements[0].userId").value(userInfoServiceResponse.getUserInfoElements().get(0).getUserId()))
                .andExpect(jsonPath("$.data.userInfoElements[0].nickname").value(userInfoServiceResponse.getUserInfoElements().get(0).getNickname()))
                .andExpect(jsonPath("$.data.userInfoElements[0].profileImageUrl").value(userInfoServiceResponse.getUserInfoElements().get(0).getProfileImageUrl()))
                .andExpect(jsonPath("$.data.userInfoElements[1].userId").value(userInfoServiceResponse.getUserInfoElements().get(1).getUserId()))
                .andExpect(jsonPath("$.data.userInfoElements[1].nickname").value(userInfoServiceResponse.getUserInfoElements().get(1).getNickname()))
                .andExpect(jsonPath("$.data.userInfoElements[1].profileImageUrl").value(userInfoServiceResponse.getUserInfoElements().get(1).getProfileImageUrl()));

        verify(userService).getUserInfo(any(UserInfoServiceRequest.class));

        // docs
        actions.andDo(document("get-user-info",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("feignClient API for book-service")
                                .summary("사용자 정보를 조회한다.")
                                .requestFields(
                                        fieldWithPath("userIds").type(JsonFieldType.ARRAY).description("조회할 사용자 아이디 목록")
                                )
                                .responseSchema(Schema.schema("get_user_info_response_body"))
                                .responseFields(
                                        fieldWithPath("data.userInfoElements").type(JsonFieldType.ARRAY).description("조회된 사용자 정보 목록"),
                                        fieldWithPath("data.userInfoElements[].userId").type(JsonFieldType.STRING).description("조회된 사용자의 아이디"),
                                        fieldWithPath("data.userInfoElements[].nickname").type(JsonFieldType.STRING).description("조회된 사용자의 닉네임"),
                                        fieldWithPath("data.userInfoElements[].profileImageUrl").type(JsonFieldType.STRING).description("조회된 사용자의 프로필 이미지 URL")
                                )
                                .build()
                ))
        );
    }

}