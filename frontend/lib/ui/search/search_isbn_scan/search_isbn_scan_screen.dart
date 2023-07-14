import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';
import 'package:mybrary/ui/search/search_isbn_scan/components/isbn_scan_appbar.dart';
import 'package:mybrary/ui/search/search_isbn_scan/components/isbn_scan_box.dart';
import 'package:mybrary/ui/search/search_isbn_scan/components/isbn_scan_description.dart';
import 'package:mybrary/ui/search/search_isbn_scan/components/isbn_scan_header.dart';

class SearchIsbnScanScreen extends StatefulWidget {
  const SearchIsbnScanScreen({super.key});

  @override
  State<SearchIsbnScanScreen> createState() => _SearchIsbnScanScreenState();
}

class _SearchIsbnScanScreenState extends State<SearchIsbnScanScreen> {
  BookSearchData _bookSearchIsbnData = BookSearchData.fromJson({});
  MobileScannerController isbnCameraController = MobileScannerController(
    detectionSpeed: DetectionSpeed.normal,
    facing: CameraFacing.back,
    torchEnabled: false,
  );

  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(
      SystemUiOverlayStyle(
        statusBarColor: MYBOOK_SCAN_BACKGROUND_COLOR,
        statusBarBrightness: Brightness.dark,
        statusBarIconBrightness: Brightness.light,
        systemNavigationBarColor: MYBOOK_SCAN_BACKGROUND_COLOR,
        systemNavigationBarIconBrightness: Brightness.light,
      ),
    );
    super.initState();
  }

  @override
  void dispose() {
    SystemChrome.setSystemUIOverlayStyle(
      SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarBrightness: Brightness.light,
        statusBarIconBrightness: Brightness.dark,
        systemNavigationBarColor: LESS_GREY_COLOR.withOpacity(0.2),
        systemNavigationBarIconBrightness: Brightness.dark,
      ),
    );
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final width = MediaQuery.of(context).size.width;
    final height = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: IsbnScanAppBar(
        appBar: AppBar(),
      ),
      resizeToAvoidBottomInset: false,
      body: SizedBox(
        height: height,
        child: Stack(
          children: [
            SizedBox(
              width: width,
              child: MobileScanner(
                controller: isbnCameraController,
                scanWindow: Rect.fromLTWH(0, 150, width * 0.8, height * 0.35),
                onDetect: (capture) {
                  final List<Barcode> barcodes = capture.barcodes;
                  for (final barcode in barcodes) {
                    if (barcode.rawValue != null) {
                      _fetchBookSearchIsbnResponse(barcode.rawValue!)
                          .then((value) {
                        _bookSearchIsbnData = value;
                      }).catchError((error) {
                        return;
                      });
                    }

                    if (_bookSearchIsbnData.title != null) {
                      onNavigateToSearchDetailScreen();
                      isbnCameraController.dispose();
                    }
                  }
                },
              ),
            ),
            IsbnScanHeader(
              width: width,
              height: height,
            ),
            IsbnScanDescription(
              width: width,
              height: height,
            ),
            IsbnScanBox(
              width: width,
              height: height,
            ),
          ],
        ),
      ),
    );
  }

  void onNavigateToSearchDetailScreen() {
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (_) => SearchDetailScreen(
          bookSearchData: _bookSearchIsbnData,
        ),
      ),
    );
  }

  Future<BookSearchData> _fetchBookSearchIsbnResponse(String isbn) async {
    BookSearchResponse bookSearchResponse =
        await RemoteDataSource.getBookSearchResponse(
            '${getApi(API.getBookSearchIsbn)}?isbn=$isbn');

    final bookSearchData = bookSearchResponse.data!.bookSearchResult![0];

    return bookSearchData;
  }
}
