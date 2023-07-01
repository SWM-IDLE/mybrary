import 'package:flutter_web_auth/flutter_web_auth.dart';

enum OAuthType { naver, kakao, google }

// 추후, 서버 api 주소가 나오면 코드 리팩터링 진행하겠습니다.
const APP_DOMAIN_URI = "https://{서버_api_주소}/oauth2/authorization";
const APP_REDIRECT_URI = "kr.mybrary";

Future<void> signInOAuth({required OAuthType oAuthType}) async {
  Uri? url;

  // 함수를 실행하는 타입에 맞추어 Uri를 생성합니다. (naver, kakao, google)
  switch (oAuthType) {
    case OAuthType.naver:
      url = Uri.parse('$APP_DOMAIN_URI/naver?redirect_url=$APP_REDIRECT_URI');
      break;
    case OAuthType.kakao:
      url = Uri.parse('$APP_DOMAIN_URI/kakao?redirect_url=$APP_REDIRECT_URI');
      break;
    case OAuthType.google:
      url = Uri.parse('$APP_DOMAIN_URI/google?redirect_url=$APP_REDIRECT_URI');
      break;
    default:
      break;
  }

  print('로그인 클릭');

  /*
  * 로그인 이후에 callback 데이터 반환을 받아 처리해야하는데,
  * callbackUrlScheme의 정규표현식에서 ngrok api가 유효하지 않은 것 같습니다.
  * nid.naver.com/~~~&redirect_url=~~ 부분에 대해 논의가 필요할 것 같습니다.
  * */

  final result = await FlutterWebAuth.authenticate(
      url: url.toString(), callbackUrlScheme: APP_REDIRECT_URI);

  // redirectUrl로부터 데이터를 받아 access/refresh token을 추출 가능
  final accessToken = Uri.parse(result).queryParameters['Authorization'];
  final refreshToken =
      Uri.parse(result).queryParameters['Authorization-Refresh'];

  print('accessToken: $accessToken');
  print('refreshToken: $refreshToken');
}
