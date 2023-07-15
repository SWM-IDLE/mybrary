String bookAuthorsOrTranslators(List<String> authorsOrTranslators) {
  String result = "";
  if (authorsOrTranslators.isEmpty) {
    return result;
  }

  result = authorsOrTranslators.map((people) => people).join(', ');
  return '$result 저';
}

DateTime getPublishDate(String publishDate) {
  return DateTime.parse(publishDate);
}
