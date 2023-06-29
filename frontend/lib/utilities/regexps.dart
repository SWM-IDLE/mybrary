class LoginRegExp {
  // 영어 소문자와 숫자를 포함, 영어 대문자와 특수문자를 포함하지 않는 6~20자의 문자열
  static RegExp idRegExp = RegExp(r'^(?=.*[a-z])(?=.*\d)(?!.*[A-Z!@#$&*])');

  // 영문/숫자/하이픈 검증 + @ + 도메인 검증 (최소 2자, 최대 7자)
  // 이메일은 최소 7자에서 최대 40자까지 입력 가능
  static RegExp emailRegExp =
      RegExp(r'^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$');

  // 영문, 숫자, 특수문자를 포함하는 8~16자의 문자열
  static RegExp passwordRegExp =
      RegExp(r'^(?=.*[a-z])(?=.*\d)(?=.*[A-Z!@#$&*])');

  // 영문, 숫자, 특수문자를 포함하는 6~20자 이상의 문자열
  static RegExp nicknameRegExp = RegExp(r'^[a-zA-Z0-9가-힣]+$');
}
