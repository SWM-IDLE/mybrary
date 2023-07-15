import 'dart:convert';

Map<String, dynamic> parseJwt(String token) {
  final jwt = token.split('.');
  if (jwt.length != 3) {
    throw Exception('유효하지 않은 jwt 토큰입니다.');
  }

  final payload = _decodeBase64(jwt[1]);
  final payloadMap = json.decode(payload);
  if (payloadMap is! Map<String, dynamic>) {
    throw Exception('유효하지 않은 jwt의 페이로드입니다.');
  }

  return payloadMap;
}

String _decodeBase64(String str) {
  // '+', '/' 특수 문자를 전송하면 정상적으로 해당 값이 전송되지 않음
  // '+'와 '/'를 '-'와 '_'로 바꾸어 URL-safe 적용
  String output = str.replaceAll('-', '+').replaceAll('_', '/');
  //  character가 하나 혹은 두 개라면 나머지 공간을 채워주어야 함.
  //  이 때, 패딩 공간에 사용되는 것이 '=', '=='
  switch (output.length % 4) {
    case 0:
      break;
    case 2:
      output += '==';
      break;
    case 3:
      output += '=';
      break;
    default:
      throw Exception('잘못된 base64url의 문자열입니다.');
  }
  return utf8.decode(base64Url.decode(output));
}
