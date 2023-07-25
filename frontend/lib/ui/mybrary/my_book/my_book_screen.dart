import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class MyBookScreen extends StatelessWidget {
  const MyBookScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Row(
              children: [
                Text(
                  '전체 17권',
                  style: TextStyle(
                    color: GREY_06_COLOR,
                    fontSize: 16.0,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                SizedBox(width: 4.0),
                GestureDetector(
                  onTap: () {},
                  child: Icon(
                    Icons.arrow_drop_down,
                    color: GREY_06_COLOR,
                  ),
                ),
              ],
            ),
            GestureDetector(
              onTap: () {},
              child: Icon(
                Icons.bookmark_remove,
                color: PRIMARY_COLOR,
              ),
            ),
          ],
        ),
        SizedBox(height: 20.0),
        Expanded(
          child: GridView.builder(
            itemCount: 9,
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 3,
              crossAxisSpacing: 8.0,
              mainAxisSpacing: 12.0,
              childAspectRatio: 4 / 7,
            ),
            itemBuilder: (context, index) {
              final bookImgPath = [
                'assets/img/test/book_01.jpeg',
                'assets/img/test/book_02.jpeg',
                'assets/img/test/book_03.jpeg',
                'assets/img/test/book_04.jpeg',
                'assets/img/test/book_05.jpeg',
                'assets/img/test/book_06.jpeg',
                'assets/img/test/book_07.jpeg',
                'assets/img/test/book_08.jpeg',
                'assets/img/test/book_09.jpeg',
              ];

              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    width: MediaQuery.of(context).size.width * 0.3,
                    height: 160,
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: BOOK_BORDER_COLOR,
                      ),
                      borderRadius: BorderRadius.circular(8.0),
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(8.0),
                      child: Image.asset(
                        bookImgPath[index],
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                  SizedBox(height: 4.0),
                  Text(
                    '책 제목',
                    style: TextStyle(
                      color: GREY_06_COLOR,
                      fontSize: 12.0,
                      fontWeight: FontWeight.w700,
                    ),
                  ),
                  Text(
                    '지은이',
                    style: TextStyle(
                      color: GREY_05_COLOR,
                      fontSize: 11.0,
                      fontWeight: FontWeight.w400,
                    ),
                  ),
                ],
              );
            },
          ),
        ),
      ],
    );
  }
}
