import 'package:mybrary/data/model/search/book_search_detail_response.dart';

String bookAuthorsOrTranslators(List<Authors> authorsOrTranslators) {
  String result = "";
  if (authorsOrTranslators.isEmpty) {
    return result;
  }

  return authorsOrTranslators.map((people) => people.name).join(', ');
}

DateTime getPublishDate(String publishDate) {
  return DateTime.parse(publishDate);
}
