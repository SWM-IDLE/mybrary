import 'package:flutter_test/flutter_test.dart';
import 'package:mybrary/utils/logics/validate_utils.dart';

const CORRECT_ID = 'test123';
const CORRECT_PASSWORD = 'test123!@';
const CORRECT_EMAIL = 'test1@test.com';
const CORRECT_NICKNAME = '마이브러리ab12';

void main() {
  group('회원가입 요청 시 id 유효성 시나리오', () {
    test('회원가입 요청 시 인증 성공', () {
      // given

      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, true);
    });

    test('아이디의 길이가 6자 미만이면, 인증 실패', () {
      // given
      String loginId = 'test1';
      // when
      bool result = signUpValidatorTest(
          loginId, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('아이디의 길이가 20자 초과면, 인증 실패', () {
      // given
      String loginId = 'testtestte1234567890t';
      // when
      bool result = signUpValidatorTest(
          loginId, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('아이디에 영어 대문자가 포함되면, 인증 실패', () {
      // given
      String loginId = 'Test123';
      // when
      bool result = signUpValidatorTest(
          loginId, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('아이디에 특수 문자가 포함되면, 인증 실패', () {
      // given
      String loginId = 'test123!';
      // when
      bool result = signUpValidatorTest(
          loginId, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });
  });

  group('회원가입 요청 시 pw 유효성 시나리오', () {
    test('회원가입 요청 시 인증 성공', () {
      // given

      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, true);
    });

    test('패스워드의 길이가 8자 미만이면, 인증 실패', () {
      // given
      String loginPassword = 't123!@';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, loginPassword, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('패스워드의 길이가 16자 초과면, 인증 실패', () {
      // given
      String loginPassword = 'testtest12341234!@#';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, loginPassword, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('패스워드에 특수 문자를 포함하지 않으면, 인증 실패', () {
      // given
      String loginPassword = 'test1234';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, loginPassword, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('패스워드에 숫자를 포함하지 않으면, 인증 실패', () {
      // given
      String loginPassword = 'testtest!@';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, loginPassword, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('패스워드에 영어를 포함하지 않으면, 인증 실패', () {
      // given
      String loginPassword = '12341234!@';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, loginPassword, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });
  });

  group('회원가입 요청 시 email 유효성 시나리오', () {
    test('회원가입 요청 시 로그인 성공', () {
      // given

      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, true);
    });

    test('이메일이 10자 미만이면, 인증 실패', () {
      // given
      String loginEmail = 'te@st.co';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, loginEmail, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('이메일이 40자 초과면, 인증 실패', () {
      // given
      String loginEmail = 'test1test1test1test1@test1test1.comcomcom';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, loginEmail, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('이메일 형식에 일치하지 않으면(@미포함), 인증 실패', () {
      // given
      String loginEmail = 'test.com';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, loginEmail, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });

    test('이메일 형식이 일치하지 않으면(도메인), 인증 실패', () {
      // given
      String loginEmail = 'test1@gmail.';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, loginEmail, CORRECT_NICKNAME);
      // then
      expect(result, false);
    });
  });

  group('회원가입 요청 시 nickname 유효성 시나리오', () {
    test('회원가입 요청 시 로그인 성공', () {
      // given

      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, CORRECT_NICKNAME);
      // then
      expect(result, true);
    });

    test('닉네임이 2자 미만이면, 인증 실패', () {
      // given
      String loginNickname = 't';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, loginNickname);
      // then
      expect(result, false);
    });

    test('닉네임이 20자 초과면, 인증 실패', () {
      // given
      String loginNickname = 'test1test1test1test12';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, loginNickname);
      // then
      expect(result, false);
    });

    test('닉네임에 특수 문자가 들어가면, 인증 실패', () {
      // given
      String loginNickname = '마이브러리1!@';
      // when
      bool result = signUpValidatorTest(
          CORRECT_ID, CORRECT_PASSWORD, CORRECT_EMAIL, loginNickname);
      // then
      expect(result, false);
    });
  });
}

bool signUpValidatorTest(
  String loginId,
  String password,
  String email,
  String nickname,
) {
  if (checkAuthValidator(loginId, LoginRegExp.idRegExp, 6, 20)) {
    return false;
  }

  if (checkAuthValidator(email, LoginRegExp.emailRegExp, 10, 40)) {
    return false;
  }

  if (checkAuthValidator(password, LoginRegExp.passwordRegExp, 8, 16)) {
    return false;
  }

  if (checkAuthValidator(nickname, LoginRegExp.nicknameRegExp, 2, 20)) {
    return false;
  }

  return true;
}
