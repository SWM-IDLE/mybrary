import 'package:flutter/material.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';

class HomeBestSeller extends StatelessWidget {
  final List<Books> bookListByBestSeller;

  const HomeBestSeller({
    required this.bookListByBestSeller,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const Padding(
          padding: EdgeInsets.only(
            left: 16.0,
            bottom: 16.0,
          ),
          child: Row(
            children: [
              Text(
                '이번 주',
                style: commonMainRegularStyle,
              ),
              Text(
                ' 베스트셀러',
                style: commonSubBoldStyle,
              ),
              Text(
                '는?',
                style: commonMainRegularStyle,
              ),
            ],
          ),
        ),
        Expanded(
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            physics: const BouncingScrollPhysics(),
            itemCount: bookListByBestSeller.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(
                  right: 10.0,
                  bottom: 10.0,
                ),
                child: Row(
                  children: [
                    if (index == 0)
                      const SizedBox(
                        width: 16.0,
                      ),
                    InkWell(
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => SearchDetailScreen(
                              isbn13: bookListByBestSeller[index].isbn13!,
                            ),
                          ),
                        );
                      },
                      child: Container(
                        width: 116,
                        decoration: BoxDecoration(
                          image: DecorationImage(
                            image: NetworkImage(
                              bookListByBestSeller[index].thumbnailUrl!,
                            ),
                            fit: BoxFit.fill,
                          ),
                          borderRadius: BorderRadius.circular(8),
                          boxShadow: const [
                            BoxShadow(
                              color: Color(0x3F000000),
                              blurRadius: 2,
                              offset: Offset(1, 1),
                              spreadRadius: 1,
                            )
                          ],
                        ),
                      ),
                    ),
                    if (index == bookListByBestSeller.length - 1)
                      const SizedBox(
                        width: 8.0,
                      ),
                  ],
                ),
              );
            },
          ),
        ),
      ],
    );
  }
}
