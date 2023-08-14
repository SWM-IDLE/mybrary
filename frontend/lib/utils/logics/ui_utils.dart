import 'package:flutter/material.dart';

double mediaQueryHeight(BuildContext context) {
  return MediaQuery.of(context).size.height;
}

double mediaQueryWidth(BuildContext context) {
  return MediaQuery.of(context).size.width;
}
