bool checkAuthValidator(
    String val, RegExp regExp, int minLength, int maxLength) {
  bool authValidator = val.length < minLength ||
      val.length > maxLength ||
      !(regExp.hasMatch(val));
  return authValidator;
}

class LoginRegExp {
  // 적어도 하나의 영어 소문자와 숫자를 포함합니다.
  // 영어 대문자와 특수문자는 포함하지 않습니다.
  static RegExp idRegExp = RegExp(r'^(?=.*[a-z])(?=.*\d)(?!.*[A-Z!@#$&*])');

  // 영문/숫자/하이픈 검증 + @ + 도메인 검증으로 이루어져있습니다.
  static RegExp emailRegExp = RegExp(
      r"^[a-zA-Z\d.a-zA-Z\d.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z\d]+\.[a-zA-Z]+");

  // 적어도 하나의 영문, 숫자, 특수문자를 포함합니다.
  static RegExp passwordRegExp =
      RegExp(r'^(?=.*[a-z])(?=.*\d)(?=.*[A-Z!@#$&*])');

  // 특수문자를 제외한 영문, 숫자, 한글을 포함합니다.
  static RegExp nicknameRegExp = RegExp(r'^[a-zA-Z\d가-힣]+$');
}
