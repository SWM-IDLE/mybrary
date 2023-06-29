bool valueRegExpValidation(
  String val,
  RegExp regExp,
  int minLength,
  int maxLength,
) {
  return val.length < minLength ||
      val.length > maxLength ||
      !(regExp.hasMatch(val));
}
