import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  TextEditingController _bookSearchController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final searchInputBorderStyle = OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.circular(10.0),
    );
    return Scaffold(
      body: SafeArea(
        child: Column(
          children: [
            Row(
              children: [
                IconButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  icon: Icon(Icons.arrow_back),
                ),
                Expanded(
                  child: Padding(
                    padding: EdgeInsets.only(
                        left: 6.0, right: 14.0, top: 14.0, bottom: 12.0),
                    child: TextField(
                      controller: _bookSearchController,
                      cursorColor: Colors.green,
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.symmetric(vertical: 12.0),
                        hintText: '검색어를 입력하세요.',
                        filled: true,
                        fillColor: Colors.grey.withOpacity(0.2),
                        focusedBorder: searchInputBorderStyle,
                        enabledBorder: searchInputBorderStyle,
                        focusColor: Colors.grey,
                        prefixIcon: Icon(Icons.search, color: GREY_COLOR),
                        suffixIcon: IconButton(
                          onPressed: () {
                            _bookSearchController.clear();
                          },
                          icon: Icon(
                            Icons.clear,
                            color: GREY_COLOR,
                            size: 24.0,
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
