import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/search/search_detail/components/book_contents.dart';
import 'package:mybrary/ui/search/search_detail/components/book_description.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_provider.dart';
import 'package:mybrary/ui/search/search_detail/components/book_details.dart';
import 'package:mybrary/ui/search/search_detail/components/book_summary.dart';

class SearchDetailScreen extends StatefulWidget {
  final String isbn13;
  const SearchDetailScreen({
    required this.isbn13,
    super.key,
  });

  @override
  State<SearchDetailScreen> createState() => _SearchDetailScreenState();
}

class _SearchDetailScreenState extends State<SearchDetailScreen> {
  final _searchRepository = SearchRepository();

  late Future<BookSearchDetailResponseData> _bookSearchDetailResponse;

  @override
  void initState() {
    super.initState();

    _bookSearchDetailResponse = _searchRepository.getBookSearchDetailResponse(
      isbn13: widget.isbn13,
    );
  }

  @override
  Widget build(BuildContext context) {
    final displaySize = MediaQuery.of(context).size;

    return SubPageLayout(
      appBarTitle: '',
      appBarActions: [
        IconButton(
          onPressed: () {
            Navigator.pushAndRemoveUntil(context, MaterialPageRoute(
              builder: (context) {
                return const RootTab();
              },
            ), (route) => false);
          },
          icon: SvgPicture.asset(
            'assets/svg/icon/home.svg',
          ),
        ),
      ],
      child: SafeArea(
        bottom: false,
        child: FutureBuilder<BookSearchDetailResponseData>(
          future: _bookSearchDetailResponse,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              final bookSearchDetail = snapshot.data!;

              return SingleChildScrollView(
                physics: const BouncingScrollPhysics(),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(
                      width: displaySize.width,
                      height: displaySize.height * 0.47,
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Container(
                            width: 176,
                            height: 254,
                            decoration: ShapeDecoration(
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(10),
                              ),
                              shadows: [
                                BoxShadow(
                                  color: BLACK_COLOR.withOpacity(0.3),
                                  blurRadius: 6,
                                  offset: const Offset(0, 4),
                                  spreadRadius: 0,
                                )
                              ],
                              image: DecorationImage(
                                image:
                                    NetworkImage(bookSearchDetail.thumbnail!),
                                fit: BoxFit.fill,
                              ),
                            ),
                          ),
                          const SizedBox(height: 20.0),
                          BookSummary(
                            title: bookSearchDetail.title!,
                            authors: bookSearchDetail.authors!,
                          ),
                        ],
                      ),
                    ),
                    bookDetailDivider(),
                    BookDescription(
                      subTitle: bookSearchDetail.subTitle!,
                      description: bookSearchDetail.description!,
                    ),
                    bookDetailDivider(),
                    BookContents(toc: bookSearchDetail.toc!),
                    bookDetailDivider(),
                    BookDetails(
                      isbn10: bookSearchDetail.isbn10!,
                      isbn13: bookSearchDetail.isbn13!,
                      weight: bookSearchDetail.weight!,
                      sizeDepth: bookSearchDetail.sizeDepth!,
                      sizeHeight: bookSearchDetail.sizeHeight!,
                      sizeWidth: bookSearchDetail.sizeWidth!,
                    ),
                    bookDetailDivider(),
                    BookDetailProvider(
                      link: bookSearchDetail.link!,
                    ),
                    const SizedBox(height: 50.0),
                  ],
                ),
              );
            }
            return const CircularLoading();
          },
        ),
      ),
    );
  }

  Widget bookDetailDivider() {
    return const Padding(
      padding: EdgeInsets.symmetric(vertical: 24.0),
      child: Divider(
        height: 1,
        thickness: 6,
        color: GREY_01_COLOR,
      ),
    );
  }
}
