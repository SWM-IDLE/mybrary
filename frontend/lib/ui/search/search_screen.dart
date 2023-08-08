import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/search/components/search_popular_keyword.dart';
import 'package:mybrary/ui/search/search_book_list/search_book_list.dart';
import 'package:permission_handler/permission_handler.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(
      SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarBrightness: Brightness.light,
        statusBarIconBrightness: Brightness.dark,
        systemNavigationBarColor: LESS_GREY_COLOR.withOpacity(0.2),
        systemNavigationBarIconBrightness: Brightness.dark,
      ),
    );
    super.initState();
  }

  final TextEditingController _bookSearchKeywordController =
      TextEditingController();

  @override
  void dispose() {
    _bookSearchKeywordController.clear();
    _bookSearchKeywordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: AppBar(
        toolbarHeight: 50.0,
        backgroundColor: WHITE_COLOR,
        elevation: 0,
        title: const Text('검색'),
        titleTextStyle: commonSubTitleStyle.copyWith(
          color: BLACK_COLOR,
        ),
        centerTitle: true,
        foregroundColor: BLACK_COLOR,
        actions: [
          IconButton(
            onPressed: () {},
            icon: SvgPicture.asset('assets/svg/icon/barcode_scan.svg'),
          ),
        ],
      ),
      child: SafeArea(
        bottom: false,
        child: SingleChildScrollView(
          physics: const BouncingScrollPhysics(),
          keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
          child: InkWell(
            onTap: () => FocusScope.of(context).unfocus(),
            child: SizedBox(
              height: MediaQuery.of(context).size.height * 0.97 -
                  kToolbarHeight -
                  kBottomNavigationBarHeight,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(
                      top: 6.0,
                      left: 18.0,
                      bottom: 12.0,
                      right: 18.0,
                    ),
                    child: TextField(
                      textInputAction: TextInputAction.search,
                      controller: _bookSearchKeywordController,
                      cursorColor: primaryColor,
                      onSubmitted: (value) {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => SearchBookList(
                              bookSearchKeyword: value,
                            ),
                          ),
                        ).then(
                          (value) => _bookSearchKeywordController.clear(),
                        );
                      },
                      decoration: InputDecoration(
                        contentPadding: const EdgeInsets.symmetric(
                          vertical: 6.0,
                        ),
                        hintText: '책, 저자, 회원을 검색해보세요.',
                        hintStyle: commonSubRegularStyle,
                        filled: true,
                        fillColor: GREY_COLOR_OPACITY_TWO,
                        focusedBorder: searchInputBorderStyle,
                        enabledBorder: searchInputBorderStyle,
                        focusColor: GREY_COLOR,
                        prefixIcon: SvgPicture.asset(
                          'assets/svg/icon/search_small.svg',
                          fit: BoxFit.scaleDown,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(
                    height: 8.0,
                  ),
                  SearchPopularKeyword(
                    bookSearchKeywordController: _bookSearchKeywordController,
                    onBookSearchBinding: getBookSearchPopularKeywordResponse,
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  void getBookSearchPopularKeywordResponse(bool isBinding) {
    final popularKeyword = _bookSearchKeywordController.text;
    setState(() {
      if (popularKeyword != "") {
        isBinding = true;
        // _bookSearchResponse = _searchDataSource.getBookSearchResponse(
        //     '${getBookServiceApi(API.getBookSearchKeyword)}?keyword=$popularKeyword');
        // _isSearching = true;
      }
    });
  }

  Future<dynamic> onIsbnScan() async {
    await Permission.camera.request();

    final permissionCameraStatus = await Permission.camera.status;

    switch (permissionCameraStatus) {
      case PermissionStatus.granted || PermissionStatus.provisional:
        if (!mounted) break;
        return Navigator.of(context).pushNamed('/search/barcode');
      case PermissionStatus.denied || PermissionStatus.permanentlyDenied:
        if (!mounted) break;
        return ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            action: SnackBarAction(
              label: '설정',
              textColor: PRIMARY_COLOR,
              onPressed: () {
                openAppSettings();
              },
            ),
            content: const Text(
              '카메라 권한이 없습니다.\n설정에서 권한을 허용해주세요.',
              style: commonSnackBarMessageStyle,
            ),
            duration: const Duration(seconds: 3),
          ),
        );
      default:
        return Permission.camera.request();
    }
  }
}
