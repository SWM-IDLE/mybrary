import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/search/components/search_popular_keyword.dart';
import 'package:mybrary/ui/search/search_book_list/search_book_list.dart';
import 'package:mybrary/utils/logics/permission_utils.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  late bool _isClearButtonVisible = false;

  final TextEditingController _bookSearchKeywordController =
      TextEditingController();

  @override
  void initState() {
    super.initState();
    SystemChrome.setSystemUIOverlayStyle(
      SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarBrightness: Brightness.light,
        statusBarIconBrightness: Brightness.dark,
        systemNavigationBarColor: LESS_GREY_COLOR.withOpacity(0.2),
        systemNavigationBarIconBrightness: Brightness.dark,
      ),
    );

    _bookSearchKeywordController.addListener(_isClearText);
  }

  void _isClearText() {
    if (_bookSearchKeywordController.text.isEmpty) {
      _isClearButtonVisible = false;
    }
  }

  @override
  void dispose() {
    _bookSearchKeywordController.clear();
    _bookSearchKeywordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: GestureDetector(
        onTap: () {
          FocusScope.of(context).unfocus();
        },
        child: DefaultLayout(
          appBar: AppBar(
            toolbarHeight: 60.0,
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
                onPressed: () => onIsbnScan(context),
                icon: SvgPicture.asset('assets/svg/icon/barcode_scan.svg'),
              ),
            ],
          ),
          child: Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  keyboardDismissBehavior:
                      ScrollViewKeyboardDismissBehavior.onDrag,
                  physics: const BouncingScrollPhysics(
                      parent: AlwaysScrollableScrollPhysics()),
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(
                          top: 0.0,
                          left: 18.0,
                          bottom: 12.0,
                          right: 18.0,
                        ),
                        child: TextField(
                          textInputAction: TextInputAction.search,
                          controller: _bookSearchKeywordController,
                          cursorColor: primaryColor,
                          onChanged: (value) {
                            setState(() {
                              _isClearButtonVisible = value.isNotEmpty;
                            });
                          },
                          onSubmitted: (value) {
                            if (value.isNotEmpty) {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => SearchBookList(
                                    bookSearchKeyword: value,
                                  ),
                                ),
                              ).then(
                                (value) => {
                                  setState(() {
                                    _bookSearchKeywordController.clear();
                                    _isClearButtonVisible = false;
                                  })
                                },
                              );
                            }
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
                            suffixIcon: _searchInputClearIcon(),
                          ),
                        ),
                      ),
                      const SizedBox(
                        height: 8.0,
                      ),
                      SizedBox(
                        width: MediaQuery.of(context).size.width,
                        child: SearchPopularKeyword(
                          bookSearchKeywordController:
                              _bookSearchKeywordController,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  IconButton? _searchInputClearIcon() {
    return _isClearButtonVisible
        ? IconButton(
            onPressed: () {
              setState(() {
                _bookSearchKeywordController.text = '';
                _isClearButtonVisible = false;
              });
            },
            icon: const Icon(
              Icons.cancel_rounded,
              color: GREY_05_COLOR,
              size: 18.0,
            ),
          )
        : null;
  }
}
