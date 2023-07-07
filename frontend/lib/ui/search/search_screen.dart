import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';
import 'package:mybrary/res/colors/color.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  late final List<dynamic> _bookSearchItems = [];

  final List<String> popularSearchKeyword = [
    '돈의 속성',
    '코스모스',
    '죽은 자의 집회',
    '데미안',
    '죽음의 수용소에서',
    '피프티피플',
    '함께 자라기',
  ];

  TextEditingController _bookSearchController = TextEditingController();

  @override
  void setState(VoidCallback fn) {
    print(_bookSearchItems);
    super.setState(fn);
  }

  @override
  void dispose() {
    _bookSearchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final searchInputBorderStyle = OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.circular(10.0),
    );

    final titleTextStlye = TextStyle(
      fontSize: 16.0,
      fontWeight: FontWeight.w700,
    );

    return Scaffold(
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                IconButton(
                  onPressed: () {
                    Navigator.of(context)
                        .pushNamedAndRemoveUntil('/home', (route) => false);
                  },
                  icon: Icon(
                    Icons.arrow_back,
                    color: GREY_COLOR,
                    size: 22.0,
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: EdgeInsets.symmetric(vertical: 14.0),
                    child: TextField(
                      autofocus: true,
                      textInputAction: TextInputAction.search,
                      controller: _bookSearchController,
                      cursorColor: Colors.green,
                      onSubmitted: (value) {
                        setState(() {
                          RemoteDataSource().getBookSearchKeywordData(value);
                        });
                      },
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.symmetric(vertical: 12.0),
                        hintText: '책, 저자, 회원을 검색해보세요.',
                        hintStyle: TextStyle(
                          fontSize: 14.0,
                        ),
                        filled: true,
                        fillColor: Colors.grey.withOpacity(0.2),
                        focusedBorder: searchInputBorderStyle,
                        enabledBorder: searchInputBorderStyle,
                        focusColor: Colors.grey,
                        prefixIcon: Icon(Icons.search, color: GREY_COLOR),
                        suffixIcon: IconButton(
                          onPressed: () {
                            setState(() {
                              _bookSearchController.clear();
                              _bookSearchItems.clear();
                            });
                          },
                          icon: Icon(
                            Icons.clear,
                            color: GREY_COLOR,
                            size: 22.0,
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                IconButton(
                  onPressed: () {},
                  icon: Icon(Icons.camera_alt_outlined),
                ),
              ],
            ),
            SizedBox(
              height: 8.0,
            ),
            FutureBuilder(
              future: RemoteDataSource()
                  .getBookSearchKeywordData(_bookSearchController.text),
              builder: (context, snapshot) {
                if (snapshot.hasData &&
                    snapshot.connectionState == ConnectionState.done) {
                  final searchBookKeywordResponseData = snapshot.data!['data'];
                  final searchBookKeywordDataList =
                      snapshot.data!['data']['bookSearchResult'];
                  _bookSearchItems.add(searchBookKeywordDataList);

                  return Expanded(
                    child: Column(
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 14.0),
                          width: double.infinity,
                          height: 40.0,
                          child: Text(
                            '검색 도서 ${searchBookKeywordDataList.length}',
                            style: titleTextStlye,
                          ),
                        ),
                        Expanded(
                          child: ListView.builder(
                            itemCount: _bookSearchItems.length,
                            itemBuilder: (context, index) {
                              final searchBookData = _bookSearchItems[index];
                              print('$index : ${_bookSearchItems[index]}');
                              final DateTime publicationDate = DateTime.parse(
                                  searchBookData['publicationDate']!);

                              // if(index == snapshot.data!.length) {
                              //   if(searchBookKeywordResponseData['nextRequestUrl']) {
                              //     loadMore();
                              //     return Loading(...);
                              //   } else {
                              //     return Container();
                              //   }
                              // }

                              return Column(
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 16.0,
                                      vertical: 22.0,
                                    ),
                                    child: Row(
                                      children: [
                                        Container(
                                          width: 80,
                                          height: 120,
                                          decoration: BoxDecoration(
                                            border: Border.all(
                                              color:
                                                  Colors.grey.withOpacity(0.2),
                                            ),
                                            borderRadius:
                                                BorderRadius.circular(10.0),
                                          ),
                                          child: Image.network(
                                            searchBookData['thumbnailUrl']!,
                                            fit: BoxFit.cover,
                                          ),
                                        ),
                                        SizedBox(
                                          width: 12.0,
                                        ),
                                        Expanded(
                                          child: Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Text(searchBookData['title']!,
                                                  style: TextStyle(
                                                    fontSize: 16.0,
                                                    fontWeight: FontWeight.bold,
                                                  ),
                                                  textWidthBasis:
                                                      TextWidthBasis.parent),
                                              SizedBox(
                                                height: 4.0,
                                              ),
                                              Text(
                                                '${searchBookData['publisher']!} 저',
                                                style: TextStyle(
                                                  fontSize: 14.0,
                                                  color:
                                                      BOOK_DESCRIPTION_GREY_COLOR,
                                                ),
                                              ),
                                              SizedBox(
                                                height: 4.0,
                                              ),
                                              Text(
                                                '${publicationDate.year}.${publicationDate.month}',
                                                style: TextStyle(
                                                  fontSize: 14.0,
                                                  color: GREY_COLOR,
                                                ),
                                              ),
                                              SizedBox(
                                                height: 6.0,
                                              ),
                                              Row(
                                                children: [
                                                  Icon(
                                                    Icons.star,
                                                    color: BOOK_STAR_COLOR,
                                                    size: 20.0,
                                                  ),
                                                  SizedBox(
                                                    width: 4.0,
                                                  ),
                                                  Text(
                                                    '${searchBookData['starRating']!}',
                                                    style: TextStyle(
                                                      fontSize: 15.0,
                                                      color: GREY_COLOR,
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ],
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                  Divider(
                                    thickness: 1,
                                    height: 1,
                                    color: DIVIDER_COLOR,
                                  ),
                                ],
                              );
                            },
                          ),
                        ),
                      ],
                    ),
                  );
                } else if (snapshot.connectionState ==
                    ConnectionState.waiting) {
                  return Center(
                      child: CircularProgressIndicator(
                    backgroundColor: PRIMARY_COLOR.withOpacity(0.2),
                    valueColor: AlwaysStoppedAnimation<Color>(PRIMARY_COLOR),
                  ));
                } else if (snapshot.error.toString().contains('404')) {
                  return Center(
                    child: Text('검색 결과가 없습니다.'),
                  );
                } else {
                  return Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 14.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          '인기 검색어',
                          style: titleTextStlye,
                        ),
                        SizedBox(
                          height: 16.0,
                        ),
                        Wrap(
                          spacing: 8.0,
                          runSpacing: 8.0,
                          children: List.generate(
                            popularSearchKeyword.length,
                            (index) => InkWell(
                              onTap: () {
                                _bookSearchController.text =
                                    popularSearchKeyword[index];
                                setState(() {
                                  RemoteDataSource().getBookSearchKeywordData(
                                      _bookSearchController.text);
                                });
                                FocusManager.instance.primaryFocus?.unfocus();
                              },
                              child: Container(
                                padding: EdgeInsets.symmetric(
                                    horizontal: 12.0, vertical: 8.0),
                                decoration: BoxDecoration(
                                  border: Border.all(
                                    color: GREY_COLOR.withOpacity(0.5),
                                  ),
                                  borderRadius: BorderRadius.circular(30.0),
                                ),
                                child: Text(
                                  popularSearchKeyword[index],
                                  style: TextStyle(
                                    fontSize: 14.0,
                                    color: GREY_COLOR,
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  );
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}
