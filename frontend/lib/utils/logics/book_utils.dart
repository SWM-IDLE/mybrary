import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

String bookAuthorsOrTranslators(List<dynamic> authorsOrTranslators) {
  String result = "";
  if (authorsOrTranslators.isEmpty) {
    return result;
  }

  return authorsOrTranslators.map((people) => people.name).join(', ');
}

DateTime getPublishDate(String publishDate) {
  return DateTime.parse(publishDate);
}

Future<dynamic> showMenuBottomSheet(
  BuildContext context,
  Widget child,
) {
  return showModalBottomSheet(
    shape: bottomSheetStyle,
    backgroundColor: Colors.white,
    isScrollControlled: true,
    context: context,
    builder: (_) {
      return child;
    },
  );
}

Row starRatingRow({
  required double starRating,
  double? width,
  double? height,
}) {
  return Row(
    children: List.generate(5, (index) => index)
        .map(
          (e) => Image.asset(
            'assets/img/icon/star.png',
            width: width,
            height: height,
            // 별점 표시. 예로, 3.3은 3점이며 4.8은 4점으로 표시
            color:
                e < starRating.floor() ? bookStarColor : bookStarDisabledColor,
          ),
        )
        .toList(),
  );
}

void showCupertinoPicker(BuildContext context, Widget child) {
  showCupertinoModalPopup<void>(
    context: context,
    builder: (BuildContext context) => Container(
      height: 216,
      padding: const EdgeInsets.only(top: 6.0),
      margin: EdgeInsets.only(
        bottom: MediaQuery.of(context).viewInsets.bottom,
      ),
      color: CupertinoColors.systemBackground.resolveFrom(context),
      child: SafeArea(
        top: false,
        child: child,
      ),
    ),
  );
}
