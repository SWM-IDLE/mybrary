import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';

class SearchDetailScreen extends StatefulWidget {
  final BookSearchData searchBookData;
  const SearchDetailScreen({required this.searchBookData, super.key});

  @override
  State<SearchDetailScreen> createState() => _SearchDetailScreenState();
}

class _SearchDetailScreenState extends State<SearchDetailScreen> {
  int? _selectedMyBookStatus;

  @override
  Widget build(BuildContext context) {
    final displaySize = MediaQuery.of(context).size;
    final bookDetail = widget.searchBookData;
    final DateTime publicationDate =
        DateTime.parse(bookDetail.publicationDate!);

    return Scaffold(
      appBar: _buildAppBar(),
      body: SafeArea(
        child: SingleChildScrollView(
          child: Column(
            children: [
              Container(
                width: displaySize.width,
                height: displaySize.height * 0.35,
                decoration: BoxDecoration(
                  color: BOOK_IMAGE_BACKGROUND_COLOR,
                ),
                child: Image.network(
                  bookDetail.thumbnailUrl!,
                  fit: BoxFit.contain,
                ),
              ),
              SizedBox(
                height: 20.0,
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      bookDetail.title!,
                      style: TextStyle(
                        fontSize: 18.0,
                        fontWeight: FontWeight.w700,
                      ),
                    ),
                    SizedBox(
                      height: 10.0,
                    ),
                    Row(
                      children: [
                        bookDescription(
                            '${bookDetail.authors!.map((e) => e).join(', ')} 저'),
                        SizedBox(
                          width: 4.0,
                        ),
                        bookDescription(' | '),
                        SizedBox(
                          width: 4.0,
                        ),
                        bookDescription(bookDetail.publisher!),
                        SizedBox(
                          width: 10.0,
                        ),
                        bookDescription(
                            '${publicationDate.year}.${publicationDate.month}'),
                      ],
                    ),
                    SizedBox(
                      height: 10.0,
                    ),
                    Row(
                      children: [
                        Row(
                          children: List.generate(5, (index) => index)
                              .map((e) => Icon(
                                    Icons.star,
                                    color: e < bookDetail.starRating!.floor()
                                        ? BOOK_STAR_ENABLED_COLOR
                                        : BOOK_STAR_DISABLED_COLOR,
                                  ))
                              .toList(),
                        ),
                        SizedBox(
                          width: 8.0,
                        ),
                        Text(
                          bookDetail.starRating.toString(),
                          style: TextStyle(
                            fontSize: 18.0,
                            fontWeight: FontWeight.w700,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 20.0,
              ),
              Container(
                padding: const EdgeInsets.all(28.0),
                decoration: BoxDecoration(
                  border: Border.symmetric(
                    horizontal: BorderSide(
                      color: BOOK_DESCRIPTION_COLOR,
                      width: 0.5,
                    ),
                  ),
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _myBookStatusButton(0,
                        assetUrl: 'assets/images/interest_book_icon.png',
                        status: '읽고싶어요'),
                    _myBookStatusButton(1,
                        assetUrl: 'assets/images/reading_book_icon.png',
                        status: '읽는중'),
                    _myBookStatusButton(2,
                        assetUrl: 'assets/images/readed_book_icon.png',
                        status: '읽었어요'),
                    _myBookStatusButton(3,
                        assetUrl: 'assets/images/shelf_book_icon.png',
                        status: '내책장'),
                  ],
                ),
              ),
              SizedBox(
                height: 20.0,
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      '책 소개',
                      style: TextStyle(
                        fontSize: 16.0,
                        fontWeight: FontWeight.w700,
                      ),
                    ),
                    SizedBox(
                      height: 10.0,
                    ),
                    Text(
                      bookDetail.description!,
                      style: TextStyle(
                        fontSize: 14.0,
                        fontWeight: FontWeight.w400,
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 32.0,
              ),
            ],
          ),
        ),
      ),
    );
  }

  AppBar _buildAppBar() {
    return AppBar(
      elevation: 0,
      backgroundColor: BOOK_IMAGE_BACKGROUND_COLOR,
      iconTheme: IconThemeData(color: Colors.black),
      actions: [
        IconButton(
          onPressed: () {
            Navigator.of(context)
                .pushNamedAndRemoveUntil('/home', (route) => false);
          },
          icon: Image.asset(
            'assets/images/home_icon.png',
          ),
        ),
      ],
    );
  }

  Widget bookDescription(String description) {
    return Text(
      description,
      style: TextStyle(
        color: BOOK_DESCRIPTION_COLOR,
        fontSize: 14.0,
        fontWeight: FontWeight.w400,
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
