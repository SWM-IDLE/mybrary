import 'package:flutter_web_auth/flutter_web_auth.dart';

enum OAuthType { naver, kakao, google }

const APP_DOMAIN_URI =
    "https://48d7-211-47-82-177.ngrok-free.app/oauth2/authorization";
const APP_LOGIN_URI =
    "https://48d7-211-47-82-177.ngrok-free.app/login/oauth2/code";
const APP_REDIRECT_URI = "app.ngrok-free.48d7-211-47-82-177";

Future<void> signInOAuth({required OAuthType oAuthType}) async {
  Uri? url;

  // 함수를 실행하는 타입에 맞추어 Uri를 생성합니다. (naver, kakao, google)
  switch (oAuthType) {
    case OAuthType.naver:
      url = Uri.parse('$APP_DOMAIN_URI/naver');
      break;
    case OAuthType.kakao:
      url = Uri.parse('$APP_DOMAIN_URI/kakao');
      break;
    case OAuthType.google:
      url = Uri.parse('$APP_DOMAIN_URI/google');
      break;
    default:
      break;
  }

  /*
  * 로그인 이후에 callback 데이터 반환을 받아 처리해야하는데,
  * callbackUrlScheme의 정규표현식에서 ngrok api가 유효하지 않은 것 같습니다.
  * nid.naver.com/~~~&redirect_url=~~ 부분에 대해 논의가 필요할 것 같습니다.
  * */
  final result = await FlutterWebAuth.authenticate(
      url: url.toString(), callbackUrlScheme: APP_REDIRECT_URI);

  print(result);

  // redirectUrl로부터 데이터를 받아 access/refresh token을 추출 가능
  // final accessToken = Uri.parse(result).queryParameters['Authorization'];
  // final refreshToken = Uri.parse(result).queryParameters['refresh-token'];
}

// Future authenticateWithOAuth2Naver() async {
//   const clientId = '8vGIEXZZlCtez_85OGb4';
//   const redirectUri =
//       'https://48d7-211-47-82-177.ngrok-free.app/login/oauth2/code/naver';
//   const authorizeUrl =
//       'https://48d7-211-47-82-177.ngrok-free.app/oauth2/authorization/naver';
//   // const tokenUrl =
//   //     'https://48d7-211-47-82-177.ngrok-free.app/login/oauth2/code/naver';
//
//   final authUrl = Uri.parse('$authorizeUrl?'
//       'client_id=$clientId&'
//       'redirect_uri=$redirectUri&'
//       'response_type=code');
//
//   final result = await FlutterWebAuth.authenticate(
//     url: authUrl.toString(),
//     callbackUrlScheme: redirectUri,
//   );
// }
