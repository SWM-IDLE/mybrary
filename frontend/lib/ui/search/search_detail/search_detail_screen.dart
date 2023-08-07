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
import 'package:mybrary/ui/search/search_detail/components/book_detail_header.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_provider.dart';
import 'package:mybrary/ui/search/search_detail/components/book_details.dart';

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

  late String _bookTitle = '';
  late Future<BookSearchDetailResponseData> _bookSearchDetailResponse;

  final ScrollController _bookDetailScrollController = ScrollController();

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
      appBarTitle: _bookTitle,
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
              _bookTitle = bookSearchDetail.title!;

              return SingleChildScrollView(
                controller: _bookDetailScrollController,
                physics: const BouncingScrollPhysics(),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(
                      width: displaySize.width,
                      height: displaySize.height * 0.47,
                      child: BookDetailHeader(
                        thumbnail: bookSearchDetail.thumbnail!,
                        title: bookSearchDetail.title!,
                        authors: bookSearchDetail.authors!,
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
