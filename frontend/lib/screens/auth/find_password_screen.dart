import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';

const String RESET_VERIFY_TEST_ID = 'test123';
const String RESET_VERIFY_TEST_CODE = 'abcdef';

class FindPasswordScreen extends StatefulWidget {
  const FindPasswordScreen({super.key});

  @override
  State<FindPasswordScreen> createState() => _FindPasswordScreenState();
}

class _FindPasswordScreenState extends State<FindPasswordScreen> {
  final GlobalKey<FormState> resetVerifyKey = GlobalKey();

  String? loginId;
  String? code;
  bool? _isValid;
  bool? _isFormValid;
  bool? _isValidLoginId;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: SafeArea(
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.0),
          child: Form(
            key: resetVerifyKey,
            onChanged: () {
              final isFormValid = resetVerifyKey.currentState!.validate();
              if (isFormValid != _isFormValid) {
                setState(() {
                  _isFormValid = isFormValid;
                });
              }
            },
            child: Column(
              children: [
                Logo(
                  logoText: '비밀번호 찾기',
                ),
                Expanded(
                  child: _FindPasswordForm(
                    isValid: _isValid ?? false,
                    isValidLoginId: _isValidLoginId ?? false,
                    loginId: loginId ?? '',
                    code: code ?? '',
                    onSignUpSaved: (String? val) {
                      loginId = val;
                    },
                    isVerifyEnabled: _isFormValid ?? false,
                    onIdVerifyPressed: onIdVerifyPressed,
                    onConfirmPressed: () {
                      Navigator.of(context).pop();
                    },
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void onIdVerifyPressed() async {
    // formKey는 생성을 했는데, Form 위젯과 결합을 안했을 때는 null
    if (resetVerifyKey.currentState == null) {
      return;
    }

    if (resetVerifyKey.currentState!.validate()) {
      resetVerifyKey.currentState!.save();
      if (loginId == RESET_VERIFY_TEST_ID) {
        setState(() {
          _isValid = true;
          _isValidLoginId = true;
        });
      } else {
        setState(() {
          _isValid = true;
          _isValidLoginId = false;
        });
      }
    } else {
      print('에러가 있습니다.');
    }
  }
}

class _FindPasswordForm extends StatelessWidget {
  final String loginId;
  final String code;
  final FormFieldSetter<String> onSignUpSaved;
  final bool isValid;
  final bool isVerifyEnabled;
  final bool isValidLoginId;
  final VoidCallback onIdVerifyPressed;
  final VoidCallback onConfirmPressed;

  const _FindPasswordForm({
    required this.loginId,
    required this.code,
    required this.onSignUpSaved,
    required this.isValid,
    required this.isVerifyEnabled,
    required this.onIdVerifyPressed,
    required this.onConfirmPressed,
    required this.isValidLoginId,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _IdVerifyForm(
          loginId: loginId,
          code: code,
          onSignUpSaved: onSignUpSaved,
          isValid: isValid,
          isValidLoginId: isValidLoginId,
        ),
        Padding(
          padding: const EdgeInsets.only(bottom: 16.0),
          child: LoginButton(
            onPressed: isValid && isValidLoginId
                ? onConfirmPressed
                : onIdVerifyPressed,
            isEnabled: isVerifyEnabled,
            isOAuth: false,
            btnText: isValid && isValidLoginId ? '확인' : '임시 비밀번호 받기',
            btnBackgroundColor: LOGIN_PRIMARY_COLOR,
            textColor: BLACK_COLOR,
          ),
        ),
      ],
    );
  }
}

class _IdVerifyForm extends StatelessWidget {
  final String loginId;
  final String code;
  final FormFieldSetter<String> onSignUpSaved;
  final bool isValid;
  final bool isValidLoginId;

  const _IdVerifyForm({
    required this.loginId,
    required this.onSignUpSaved,
    required this.isValid,
    required this.code,
    required this.isValidLoginId,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          '아이디',
          style: TextStyle(
            color: !(isValid && isValidLoginId) ? BLACK_COLOR : DISABLED_COLOR,
          ),
        ),
        LoginInput(
          isEnabled: !(isValid && isValidLoginId),
          initialValue: loginId,
          onSaved: onSignUpSaved,
          hintText: '가입하신 아이디를 입력해주세요.',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '아이디를 입력해 주세요.';
            }

            // 영어 소문자와 숫자를 포함, 영어 대문자와 특수문자를 포함하지 않는 6~20자의 문자열
            RegExp regExp = RegExp(r'^(?=.*[a-z])(?=.*\d)(?!.*[A-Z!@#$&*])');
            if (val.length < 6 || val.length > 20 || !(regExp.hasMatch(val))) {
              return '아이디는 영소문자/숫자 혼합 6자 이상으로 입력해 주세요.';
            }

            // 존재하지 않는 아이디입니다에 대한 로직 추가 예정

            return null;
          },
        ),
        SizedBox(
          height: isValid && !isValidLoginId ? 15.0 : 30.0,
        ),
        if (isValid && isValidLoginId) _ConfirmNotification(),
        if (isValid && !isValidLoginId)
          // 추후 토스트 메세지로 변경 예정입니다.
          Text(
            '존재하지 않는 아이디입니다.',
            style: TextStyle(color: Colors.red[500]),
          ),
      ],
    );
  }
}

class _ConfirmNotification extends StatelessWidget {
  const _ConfirmNotification({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            _notificationText('가입하신 이메일로 '),
            _notificationText(
              '임시 비밀번호를 발송',
              isBold: true,
            ),
            _notificationText('했습니다.'),
          ],
        ),
        SizedBox(
          height: 5.0,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            _notificationText('임시 비밀번호로 로그인 후, '),
            _notificationText(
              '비밀번호를 변경',
              isBold: true,
            ),
            _notificationText('해주세요.'),
          ],
        ),
      ],
    );
  }

  Text _notificationText(String text, {bool isBold = false}) {
    return Text(
      text,
      style: TextStyle(
        fontSize: 16.0,
        fontWeight: isBold ? FontWeight.w700 : FontWeight.w500,
      ),
    );
  }
}

// 추후 비밀번호 재설정 관련해서 논의 후 재사용 예정
// class _VerifyForm extends StatelessWidget {
//   final String code;
//   final FormFieldSetter<String> onSignUpSaved;
//   const _VerifyForm({
//     required this.onSignUpSaved,
//     required this.code,
//     Key? key,
//   }) : super(key: key);
//
//   @override
//   Widget build(BuildContext context) {
//     final boxDecorationStyle = BoxDecoration(
//       border: Border(
//         bottom: BorderSide(
//           color: LESS_BLACK_COLOR,
//           width: 0.5,
//         ),
//       ),
//     );
//
//     return Column(
//       crossAxisAlignment: CrossAxisAlignment.start,
//       children: [
//         Text('인증코드'),
//         LoginInput(
//           suffixText: '02:00',
//           initialValue: code,
//           onSaved: onSignUpSaved,
//           hintText: '이메일로 전송된 인증코드를 입력해주세요.',
//           validator: (String? val) {
//             // 인증코드는 숫자로만? 영문/숫자 혼합?
//             // 추후 검증 로직이 추가될 예정입니다.
//             return null;
//           },
//         ),
//         SizedBox(height: 15.0),
//         Row(
//           mainAxisAlignment: MainAxisAlignment.end,
//           children: [
//             Container(
//               padding: EdgeInsets.symmetric(vertical: 5.0),
//               decoration: boxDecorationStyle,
//               child: Text(
//                 '인증코드 재발송',
//                 style: TextStyle(
//                   color: LESS_BLACK_COLOR,
//                 ),
//                 textAlign: TextAlign.right,
//               ),
//             ),
//           ],
//         ),
//       ],
//     );
//   }
// }
