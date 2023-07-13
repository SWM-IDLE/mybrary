import 'package:flutter/material.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';

class SearchBarcodeScanScreen extends StatefulWidget {
  const SearchBarcodeScanScreen({super.key});

  @override
  State<SearchBarcodeScanScreen> createState() =>
      _SearchBarcodeScanScreenState();
}

class _SearchBarcodeScanScreenState extends State<SearchBarcodeScanScreen> {
  BookSearchData _searchBookIsbnInfo = BookSearchData.fromJson({});
  MobileScannerController cameraController = MobileScannerController(
    detectionSpeed: DetectionSpeed.normal,
    facing: CameraFacing.back,
    torchEnabled: false,
  );

  @override
  Widget build(BuildContext context) {
    final barcodeDescriptionTextStyle = TextStyle(
      color: WHITE_COLOR,
      fontSize: 15.0,
    );

    return Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        backgroundColor: MYBOOK_SCAN_BACKGROUND_COLOR,
        automaticallyImplyLeading: false,
        centerTitle: true,
        title: Text(
          '마이북 스캔',
          style: TextStyle(
            color: WHITE_COLOR,
            fontSize: 16.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: SizedBox(
        height: MediaQuery.of(context).size.height,
        child: Stack(
          children: [
            Container(
              width: MediaQuery.of(context).size.width,
              child: MobileScanner(
                // fit: BoxFit.contain,
                controller: cameraController,
                scanWindow: Rect.fromLTWH(
                    0,
                    150,
                    MediaQuery.of(context).size.width * 0.8,
                    MediaQuery.of(context).size.height * 0.3),
                onDetect: (capture) {
                  final List<Barcode> barcodes = capture.barcodes;
                  for (final barcode in barcodes) {
                    if (barcode.rawValue != null) {
                      setState(() {
                        _fetchBookSearchIsbnResponse(barcode.rawValue!)
                            .then((value) {
                          _searchBookIsbnInfo = value;
                        });
                      });
                    }

                    if (_searchBookIsbnInfo.title != null) {
                      onNavigateToSearchDetailScreen();
                      cameraController.dispose();
                    }
                  }
                },
              ),
            ),
            Positioned(
              top: 0,
              child: Container(
                width: MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height * 0.08,
                decoration: BoxDecoration(
                  color: MYBOOK_SCAN_BACKGROUND_COLOR.withOpacity(0.7),
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    Text(
                      '바코드 스캔',
                      style: TextStyle(
                        color: PRIMARY_COLOR,
                        fontSize: 15.0,
                      ),
                    ),
                    Text(
                      '책장 스캔',
                      style: TextStyle(
                        color: WHITE_COLOR.withOpacity(0.3),
                        fontSize: 15.0,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Positioned(
              bottom: 0,
              child: Container(
                width: MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height * 0.35,
                decoration: BoxDecoration(
                  color: MYBOOK_SCAN_BACKGROUND_COLOR.withOpacity(0.7),
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Column(
                      children: [
                        Text(
                          '도서 뒷면의 바코드를 인식시켜',
                          style: barcodeDescriptionTextStyle,
                        ),
                        SizedBox(
                          height: 10.0,
                        ),
                        Text(
                          '책을 검색할 수 있어요',
                          style: barcodeDescriptionTextStyle,
                        ),
                      ],
                    ),
                    SizedBox(
                      height: 40.0,
                    ),
                    IconButton(
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      icon: Icon(
                        Icons.cancel,
                        color: PRIMARY_COLOR,
                        size: 70.0,
                      ),
                    ),
                    SizedBox(
                      height: 30.0,
                    ),
                  ],
                ),
              ),
            ),
            Positioned(
              top: MediaQuery.of(context).size.height * 0.08,
              child: Column(
                children: [
                  Container(
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.height * 0.11,
                    decoration: BoxDecoration(
                      color: MYBOOK_SCAN_BACKGROUND_COLOR.withOpacity(0.3),
                    ),
                  ),
                  Row(
                    children: [
                      Container(
                        width: MediaQuery.of(context).size.width * 0.1,
                        height: MediaQuery.of(context).size.height * 0.25,
                        decoration: BoxDecoration(
                          color: MYBOOK_SCAN_BACKGROUND_COLOR.withOpacity(0.3),
                        ),
                      ),
                      Stack(
                        children: [
                          Container(
                            width: MediaQuery.of(context).size.width * 0.8,
                            height: MediaQuery.of(context).size.height * 0.25,
                            decoration: BoxDecoration(
                              border: Border.all(
                                color: Colors.transparent,
                              ),
                            ),
                          ),
                          Positioned(
                            top: 0,
                            left: 0,
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Container(
                                  width: 30.0,
                                  height: 3.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                                Container(
                                  width: 3.0,
                                  height: 30.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Positioned(
                            top: 0,
                            right: 0,
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.end,
                              children: [
                                Container(
                                  width: 30.0,
                                  height: 3.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                                Container(
                                  width: 3.0,
                                  height: 30.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Positioned(
                            bottom: 0,
                            left: 0,
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Container(
                                  width: 3.0,
                                  height: 30.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                                Container(
                                  width: 30.0,
                                  height: 3.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Positioned(
                            bottom: 0,
                            right: 0,
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.end,
                              children: [
                                Container(
                                  width: 3.0,
                                  height: 30.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                                Container(
                                  width: 30.0,
                                  height: 3.0,
                                  decoration: BoxDecoration(
                                    color: PRIMARY_COLOR,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      Container(
                        width: MediaQuery.of(context).size.width * 0.1,
                        height: MediaQuery.of(context).size.height * 0.25,
                        decoration: BoxDecoration(
                          color: MYBOOK_SCAN_BACKGROUND_COLOR.withOpacity(0.3),
                        ),
                      ),
                    ],
                  ),
                  Container(
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.height * 0.11,
                    decoration: BoxDecoration(
                      color: MYBOOK_SCAN_BACKGROUND_COLOR.withOpacity(0.3),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  onNavigateToSearchDetailScreen() {
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (_) => SearchDetailScreen(
          searchBookData: _searchBookIsbnInfo,
        ),
      ),
    );
  }

  Future<BookSearchData> _fetchBookSearchIsbnResponse(String isbn) async {
    BookSearchResponse bookSearchResponse =
        await RemoteDataSource.getBookSearchKeywordResponse(
            BookSearchRequestType.searchIsbnScan, isbn);

    final bookSearchData = bookSearchResponse.data!.bookSearchResult![0];

    return bookSearchData;
  }
}
