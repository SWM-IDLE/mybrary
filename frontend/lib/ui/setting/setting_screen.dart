import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/profile/profile_edit/profile_edit_screen.dart';
import 'package:url_launcher/url_launcher.dart';

class SettingScreen extends StatelessWidget {
  const SettingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    const secureStorage = FlutterSecureStorage();

    return DefaultLayout(
      appBar: AppBar(
        backgroundColor: commonWhiteColor,
        elevation: 0,
        title: const Text('설정'),
        titleTextStyle: appBarTitleStyle.copyWith(
          fontSize: 17.0,
        ),
        centerTitle: true,
        foregroundColor: commonBlackColor,
      ),
      child: Column(
        children: [
          Expanded(
            child: SingleChildScrollView(
              physics: const BouncingScrollPhysics(),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '설정',
                      style: settingTitleStyle,
                    ),
                    const SizedBox(height: 12.0),
                    _settingTab(
                      tabTitle: '프로필 편집',
                      onTap: () {
                        Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (context) => const ProfileEditScreen(),
                          ),
                        );
                      },
                    ),
                    _settingTab(
                      tabTitle: '로그인 정보',
                      onTap: () {},
                    ),
                    const SizedBox(height: 26.0),
                    const Text(
                      '서비스 안내',
                      style: settingTitleStyle,
                    ),
                    const SizedBox(height: 12.0),
                    _settingTab(
                      tabTitle: '공지사항 / 업데이트 소식',
                      onTap: () async {
                        await launchUrl(
                          Uri.parse(
                              'https://ribbon-soil-e35.notion.site/dcec4dc01b2b4ab5bbb7ffba3cf24a6a?pvs=4'),
                        );
                      },
                    ),
                    _settingTab(
                      tabTitle: '마이브러리 가이드',
                      onTap: () async {
                        await launchUrl(
                          Uri.parse(
                              'https://ribbon-soil-e35.notion.site/feaaf88e77474c4b83b7fb2da9f683d8?pvs=4'),
                        );
                      },
                    ),
                    _settingTab(
                      tabTitle: '1:1 문의 및 요청',
                      onTap: () async {
                        String url = 'https://open.kakao.com/me/mybrary';
                        if (await canLaunchUrl(Uri.parse(url))) {
                          await launchUrl(
                            Uri.parse(url),
                            mode: LaunchMode.externalApplication,
                            webOnlyWindowName: '_self',
                          );
                        }
                      },
                    ),
                    const SizedBox(height: 26.0),
                    const Text(
                      '법적 고지 및 정책',
                      style: settingTitleStyle,
                    ),
                    const SizedBox(height: 12.0),
                    _settingTab(
                      tabTitle: '마이브러리 이용약관',
                      onTap: () async {
                        await launchUrl(
                          Uri.parse(
                              'https://ribbon-soil-e35.notion.site/21ae953b6edd455e8ed77d5bb16fa4c4?pvs=4'),
                        );
                      },
                    ),
                    _settingTab(
                      tabTitle: '개인정보 처리방침',
                      onTap: () async {
                        await launchUrl(
                          Uri.parse(
                              'https://ribbon-soil-e35.notion.site/847e95af04bb4ffb9c08163672cb990a?pvs=4'),
                        );
                      },
                    ),
                    _settingTab(
                      tabTitle: '오픈소스 라이선스',
                      onTap: () async {
                        await launchUrl(
                          Uri.parse(
                              'https://ribbon-soil-e35.notion.site/f98bad671bcb4e759e194922c3d787bf?pvs=4'),
                        );
                      },
                    ),
                    const SizedBox(height: 26.0),
                    const Text(
                      '기타',
                      style: settingTitleStyle,
                    ),
                    const SizedBox(height: 12.0),
                    _settingTab(
                      tabTitle: '리뷰로 응원하기',
                      onTap: () {},
                    ),
                    _settingTab(
                      tabTitle: '로그아웃',
                      onTap: () async {
                        await showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return AlertDialog(
                              title: const Text(
                                '로그아웃',
                                style: commonSubBoldStyle,
                                textAlign: TextAlign.center,
                              ),
                              content: Wrap(
                                alignment: WrapAlignment.center,
                                children: [
                                  const Text(
                                    '정말',
                                    style: confirmButtonTextStyle,
                                  ),
                                  Text(
                                    ' 로그아웃 ',
                                    style: confirmButtonTextStyle.copyWith(
                                      color: commonRedColor,
                                    ),
                                  ),
                                  const Text(
                                    '하시겠습니까?',
                                    style: confirmButtonTextStyle,
                                  ),
                                ],
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
                                    _confirmButton(
                                      onTap: () {
                                        Navigator.of(context).pop();
                                      },
                                      buttonText: '취소',
                                      isCancel: true,
                                    ),
                                    _confirmButton(
                                      onTap: () async {
                                        await secureStorage.deleteAll();

                                        if (context.mounted) {
                                          Navigator.of(context)
                                              .pushNamedAndRemoveUntil(
                                            '/signin',
                                            (Route<dynamic> route) => false,
                                          );
                                        }
                                      },
                                      buttonText: '삭제',
                                      isCancel: false,
                                    ),
                                  ],
                                ),
                              ],
                            );
                          },
                        );
                      },
                    ),
                    const SizedBox(height: 6.0),
                    Container(
                      width: double.infinity,
                      alignment: Alignment.centerRight,
                      padding: const EdgeInsets.only(right: 4.0),
                      child: const Text(
                        '탈퇴하기',
                        style: settingTitleStyle,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Container _settingTab({
    required String tabTitle,
    required GestureTapCallback onTap,
  }) {
    return Container(
      padding: const EdgeInsets.all(12.0),
      child: InkWell(
        onTap: onTap,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              tabTitle,
              style: settingSubTitleStyle,
            ),
            SvgPicture.asset(
              'assets/svg/icon/profile_menu_arrow.svg',
            ),
          ],
        ),
      ),
    );
  }

  Widget _confirmButton({
    required GestureTapCallback? onTap,
    required String buttonText,
    required bool isCancel,
  }) {
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 4.0),
        child: InkWell(
          onTap: onTap,
          child: Container(
            height: 46.0,
            decoration: BoxDecoration(
              color: isCancel ? greyF1F2F5 : primaryColor,
              borderRadius: BorderRadius.circular(4.0),
            ),
            child: Center(
              child: Text(
                buttonText,
                style: commonSubBoldStyle.copyWith(
                  color: isCancel ? commonBlackColor : commonWhiteColor,
                  fontSize: 14.0,
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
