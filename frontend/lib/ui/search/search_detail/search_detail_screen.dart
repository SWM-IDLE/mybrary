import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/search/search_detail/components/book_description.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_appbar.dart';
import 'package:mybrary/ui/search/search_detail/components/book_status_button.dart';
import 'package:mybrary/ui/search/search_detail/components/book_summary.dart';

class SearchDetailScreen extends StatefulWidget {
  final BookSearchData bookSearchData;
  const SearchDetailScreen({required this.bookSearchData, super.key});

  @override
  State<SearchDetailScreen> createState() => _SearchDetailScreenState();
}

class _SearchDetailScreenState extends State<SearchDetailScreen> {
  int? _selectedMyBookStatus;

  @override
  Widget build(BuildContext context) {
    final displaySize = MediaQuery.of(context).size;
    final bookDetail = widget.bookSearchData;

    return Scaffold(
      appBar: BookDetailAppBar(
        appBar: AppBar(),
      ),
      body: SafeArea(
        bottom: false,
        child: SingleChildScrollView(
          child: Column(
            children: [
              Container(
                width: displaySize.width,
                height: displaySize.height * 0.35,
                decoration: BoxDecoration(
                  color: BOOK_BACKGROUND_COLOR,
                ),
                child: Image.network(
                  bookDetail.thumbnailUrl!,
                  fit: BoxFit.contain,
                ),
              ),
              SizedBox(
                height: 20.0,
              ),
              BookSummary(
                bookSearchData: bookDetail,
              ),
              SizedBox(
                height: 25.0,
              ),
              BookStatusButton(
                children: [
                  _myBookStatusButton(0,
                      assetUrl: 'assets/img/icon/interest_book.png',
                      status: '읽고싶어요'),
                  _myBookStatusButton(1,
                      assetUrl: 'assets/img/icon/reading_book.png',
                      status: '읽는중'),
                  _myBookStatusButton(2,
                      assetUrl: 'assets/img/icon/readed_book.png',
                      status: '읽었어요'),
                  _myBookStatusButton(3,
                      assetUrl: 'assets/img/icon/shelf_book.png',
                      status: '내책장'),
                ],
              ),
              SizedBox(
                height: 20.0,
              ),
              BookDescription(
                bookSearchData: bookDetail,
              ),
              SizedBox(
                height: 30.0,
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _myBookStatusButton(int index,
      {required String assetUrl, required String status}) {
    return GestureDetector(
      onTap: () {
        setState(() {
          _selectedMyBookStatus = index;
        });
      },
      child: Column(
        children: [
          Image.asset(
            assetUrl,
            width: 32.0,
            height: 32.0,
            color: _selectedMyBookStatus == index
                ? PRIMARY_COLOR
                : BOOK_DISABLED_COLOR,
          ),
          SizedBox(
            height: 8.0,
          ),
          Text(
            status,
            style: TextStyle(
              color: _selectedMyBookStatus == index
                  ? PRIMARY_COLOR
                  : BOOK_DISABLED_COLOR,
              fontSize: 14.0,
              fontWeight: FontWeight.w700,
            ),
          ),
        ],
      ),
    );
  }
}
