import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';

const String RESET_VERIFY_TEST_ID = 'test123';
const String RESET_VERIFY_TEST_CODE = 'abcdef';

class ResetVerifyScreen extends StatefulWidget {
  const ResetVerifyScreen({super.key});

  @override
  State<ResetVerifyScreen> createState() => _ResetVerifyScreenState();
}

class _ResetVerifyScreenState extends State<ResetVerifyScreen> {
  final GlobalKey<FormState> resetVerifyKey = GlobalKey();

  String? loginId;
  String? code;
  bool? _isValid;
  bool? _isFormValid;

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
                  logoText: '비밀번호 재설정',
                ),
                Expanded(
                  child: _ResetVerifyForm(
                    isValid: _isValid ?? false,
                    loginId: loginId ?? '',
                    code: code ?? '',
                    onSignUpSaved: (String? val) {
                      loginId = val;
                    },
                    isVerifyEnabled: _isFormValid ?? false,
                    onIdVerifyPressed: onIdVerifyPressed,
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
        });
      } else {
        print('아이디가 일치하지 않습니다.');
      }
    } else {
      print('에러가 있습니다.');
    }
  }
}

class _ResetVerifyForm extends StatelessWidget {
  final String loginId;
  final String code;
  final FormFieldSetter<String> onSignUpSaved;
  final bool isValid;
  final bool isVerifyEnabled;
  final VoidCallback onIdVerifyPressed;

  const _ResetVerifyForm({
    required this.loginId,
    required this.code,
    required this.onSignUpSaved,
    required this.isValid,
    required this.isVerifyEnabled,
    required this.onIdVerifyPressed,
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
        ),
        Padding(
          padding: const EdgeInsets.only(bottom: 16.0),
          child: LoginButton(
            onPressed: onIdVerifyPressed,
            isEnabled: isValid ? false : isVerifyEnabled,
            isOAuth: false,
            btnText: isValid ? '인증하기' : '인증번호 발송',
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

  const _IdVerifyForm({
    required this.loginId,
    required this.onSignUpSaved,
    required this.isValid,
    required this.code,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('아이디'),
        LoginInput(
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
          height: 20.0,
        ),
        if (isValid)
          _VerifyForm(
            code: code,
            onSignUpSaved: onSignUpSaved,
          ),
      ],
    );
  }
}

class _VerifyForm extends StatelessWidget {
  final String code;
  final FormFieldSetter<String> onSignUpSaved;
  const _VerifyForm({
    required this.onSignUpSaved,
    required this.code,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final boxDecorationStyle = BoxDecoration(
      border: Border(
        bottom: BorderSide(
          color: LESS_BLACK_COLOR,
          width: 0.5,
        ),
      ),
    );

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('인증코드'),
        LoginInput(
          suffixText: '02:00',
          initialValue: code,
          onSaved: onSignUpSaved,
          hintText: '이메일로 전송된 인증코드를 입력해주세요.',
          validator: (String? val) {
            // 인증코드는 숫자로만? 영문/숫자 혼합?
            // 추후 검증 로직이 추가될 예정입니다.
            return null;
          },
        ),
        SizedBox(height: 15.0),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Container(
              padding: EdgeInsets.symmetric(vertical: 5.0),
              decoration: boxDecorationStyle,
              child: Text(
                '인증코드 재발송',
                style: TextStyle(
                  color: LESS_BLACK_COLOR,
                ),
                textAlign: TextAlign.right,
              ),
            ),
          ],
        ),
      ],
    );
  }
}
