import 'package:flutter/material.dart';
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
