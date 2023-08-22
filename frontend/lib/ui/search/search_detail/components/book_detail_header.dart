import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:html/parser.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/mybook/interest_book_list/interest_book_list_screen.dart';
import 'package:mybrary/utils/logics/book_utils.dart';
import 'package:mybrary/utils/logics/common_utils.dart';

class BookDetailHeader extends StatefulWidget {
  final String thumbnail;
  final String title;
  final List<Authors> authors;
  final int interestCount;
  final int readCount;
  final int holderCount;
  final bool interested;
  final bool completed;
  final bool registered;
  final bool newRegistered;

  final String isbn13;

  const BookDetailHeader({
    required this.thumbnail,
    required this.title,
    required this.authors,
    required this.interestCount,
    required this.readCount,
    required this.holderCount,
    required this.interested,
    required this.completed,
    required this.registered,
    required this.newRegistered,
    required this.isbn13,
    super.key,
  });

  @override
  State<BookDetailHeader> createState() => _BookDetailHeaderState();
}

class _BookDetailHeaderState extends State<BookDetailHeader> {
  final _bookRepository = BookRepository();

  bool onTapInterestBook = false;
  late bool _newInterested;
  late int _newInterestCount;

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _newInterested = widget.interested;
    _newInterestCount = widget.interestCount;
  }

  @override
  Widget build(BuildContext context) {
    return Column(
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
                color: commonBlackColor.withOpacity(0.3),
                blurRadius: 6,
                offset: const Offset(0, 4),
                spreadRadius: 0,
              )
            ],
            image: DecorationImage(
              image: NetworkImage(widget.thumbnail),
              fit: BoxFit.fill,
            ),
          ),
        ),
        const SizedBox(height: 20.0),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 18.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              const SizedBox(height: 2.0),
              Text(
                parse(widget.title).documentElement!.text,
                style: bookDetailTitleStyle,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 6.0),
              Text(
                bookAuthorsOrTranslators(widget.authors),
                style: commonSubRegularStyle.copyWith(
                  color: bookDescriptionColor,
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 24.0),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            bookStatusColumn(
              padding: 32.0,
              children: [
                InkWell(
                  onTap: () async {
                    final result =
                        await _bookRepository.createOrDeleteInterestBook(
                      context: context,
                      userId: _userId,
                      isbn13: widget.isbn13,
                    );

                    setState(() {
                      onTapInterestBook = result.interested!;

                      _isInterestBook(
                        _newInterested,
                        _newInterestCount,
                        context,
                      );
                    });
                  },
                  child: Column(
                    children: [
                      SvgPicture.asset(
                        'assets/svg/icon/small/${_newInterested ? 'heart_green.svg' : 'heart.svg'}',
                      ),
                      const SizedBox(height: 4.0),
                      const Text('읽고싶어요', style: bookStatusStyle),
                    ],
                  ),
                ),
                const SizedBox(height: 8.0),
                Text(
                  '$_newInterestCount 명',
                  style: bookStatusCountStyle,
                ),
              ],
            ),
            bookStatusColumn(
              padding: 36.0,
              children: [
                InkWell(
                  onTap: () async {},
                  child: Column(
                    children: [
                      SvgPicture.asset(
                        'assets/svg/icon/small/${widget.completed ? 'read_green.svg' : 'read.svg'}',
                      ),
                      const SizedBox(height: 4.0),
                      const Text('완독했어요', style: bookStatusStyle),
                    ],
                  ),
                ),
                const SizedBox(height: 8.0),
                Text(
                  '${widget.readCount} 명',
                  style: bookStatusCountStyle,
                ),
              ],
            ),
            bookStatusColumn(
              padding: 24.0,
              lastBox: true,
              children: [
                InkWell(
                  onTap: () async {},
                  child: Column(
                    children: [
                      SvgPicture.asset(
                        'assets/svg/icon/small/${widget.newRegistered || widget.registered ? 'holder_green.svg' : 'holder.svg'}',
                      ),
                      const SizedBox(height: 4.0),
                      const Text('소장하고있어요', style: bookStatusStyle),
                    ],
                  ),
                ),
                const SizedBox(height: 8.0),
                Text(
                  '${widget.newRegistered == true ? widget.holderCount + 1 : widget.holderCount} 명',
                  style: bookStatusCountStyle,
                ),
              ],
            ),
          ],
        ),
        const SizedBox(height: 20.0),
      ],
    );
  }

  Widget bookStatusColumn({
    required double padding,
    bool lastBox = false,
    required List<Widget> children,
  }) {
    return Container(
      decoration: lastBox
          ? null
          : const BoxDecoration(
              border: Border(
                right: BorderSide(
                  color: greyDDDDDD,
                  width: 1,
                ),
              ),
            ),
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: padding),
        child: Column(
          children: children,
        ),
      ),
    );
  }

  void _isInterestBook(
    bool interested,
    int interestCount,
    BuildContext context,
  ) {
    if (!interested && onTapInterestBook) {
      _newInterested = true;
      _newInterestCount = interestCount + 1;
      showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서에 담겼습니다.',
        snackBarAction: _moveNextToInterestBookListScreen(),
      );
    } else if (interested && onTapInterestBook == false) {
      _newInterested = false;
      _newInterestCount = interestCount - 1;
      showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서가 삭제되었습니다.',
      );
    } else if (interested && onTapInterestBook) {
      _newInterested = true;
      _newInterestCount = interestCount;
      showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서에 담겼습니다.',
        snackBarAction: _moveNextToInterestBookListScreen(),
      );
    } else {
      _newInterested = false;
      _newInterestCount = interestCount;
      showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서가 삭제되었습니다.',
      );
    }
  }

  Widget _moveNextToInterestBookListScreen() {
    return InkWell(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => const InterestBookListScreen(),
          ),
        );
        ScaffoldMessenger.of(context).hideCurrentSnackBar();
      },
      child: const Text(
        '관심북으로 이동',
        style: commonSnackBarButtonStyle,
      ),
    );
  }
}
