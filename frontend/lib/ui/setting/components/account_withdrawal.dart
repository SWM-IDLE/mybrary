import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/utils/logics/common_utils.dart';

class AccountWithdrawal extends StatefulWidget {
  const AccountWithdrawal({
    super.key,
  });

  @override
  State<AccountWithdrawal> createState() => _AccountWithdrawalState();
}

class _AccountWithdrawalState extends State<AccountWithdrawal> {
  final _profileRepository = ProfileRepository();

  bool _isChecked = false;

  final _userId = UserState.userId;

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '',
      child: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              '마이브러리 회원 탈퇴',
              style: commonSubMediumStyle.copyWith(
                fontSize: 18.0,
              ),
            ),
            const SizedBox(height: 8.0),
            Text(
              '회원 탈퇴 전 반드시 아래 내용을 확인해주세요.',
              style: commonSubRegularStyle.copyWith(
                color: commonRedColor,
              ),
            ),
            const SizedBox(height: 16.0),
            Container(
              color: grey262626,
              padding: const EdgeInsets.symmetric(
                horizontal: 24.0,
                vertical: 42.0,
              ),
              child: const Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '1. 회원 탈퇴 시, 계정 정보는 복구가 불가능합니다.',
                    style: settingInfoStyle,
                  ),
                  SizedBox(height: 12.0),
                  Text(
                    '2. 사용자 신고 등 부정 이용에 대한 조치를 위해 해당 정보는 6개월 간 보존됩니다.',
                    style: settingInfoStyle,
                  ),
                  SizedBox(height: 12.0),
                  Text(
                    '3. 등록된 리뷰 혹은 도서는 자동으로 삭제되지 않습니다. 탈퇴 전 개별적으로 삭제하시기 바랍니다.',
                    style: settingInfoStyle,
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24.0),
            InkWell(
              onTap: () {
                setState(() {
                  _isChecked = !_isChecked;
                });
              },
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Container(
                  padding: const EdgeInsets.all(16.0),
                  decoration: BoxDecoration(
                    color: greyF1F2F5,
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      SvgPicture.asset(
                          'assets/svg/icon/small/checkbox_${_isChecked ? 'green' : 'grey'}.svg'),
                      const SizedBox(width: 8.0),
                      Text(
                        '위 내용을 확인하였으며, 회원 탈퇴에 동의합니다.',
                        style: commonSubMediumStyle.copyWith(
                          height: 1.2,
                          fontSize: 14.0,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 12.0),
            InkWell(
              onTap: () async {
                if (_isChecked) {
                  await showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return AlertDialog(
                        title: const Text(
                          '회원탈퇴',
                          style: commonSubBoldStyle,
                          textAlign: TextAlign.center,
                        ),
                        content: const Text(
                          '정말 회원을 탈퇴하시겠습니까?',
                          style: confirmButtonTextStyle,
                          textAlign: TextAlign.center,
                        ),
                        contentPadding: const EdgeInsets.only(
                          top: 24.0,
                          bottom: 16.0,
                        ),
                        actionsAlignment: MainAxisAlignment.center,
                        buttonPadding:
                            const EdgeInsets.symmetric(horizontal: 8.0),
                        actions: [
                          Row(
                            children: [
                              confirmButton(
                                onTap: () {
                                  Navigator.of(context).pop();
                                },
                                buttonText: '취소',
                                isCancel: true,
                              ),
                              confirmButton(
                                onTap: () async {
                                  await _profileRepository.deleteAccount(
                                    context: context,
                                    userId: _userId,
                                  );

                                  Future.delayed(
                                      const Duration(milliseconds: 500), () {
                                    Navigator.of(context)
                                        .pushNamedAndRemoveUntil(
                                      '/signin',
                                      (Route<dynamic> route) => false,
                                    );
                                  });
                                },
                                buttonText: '탈퇴하기',
                                isCancel: false,
                              ),
                            ],
                          ),
                        ],
                      );
                    },
                  );
                }
              },
              child: Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 16.0,
                  vertical: 8.0,
                ),
                decoration: BoxDecoration(
                  color: _isChecked ? commonRedColor : greyACACAC,
                  borderRadius: BorderRadius.circular(4.0),
                ),
                child: Text(
                  '회원 탈퇴',
                  style: commonSubRegularStyle.copyWith(
                    color: commonWhiteColor,
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
