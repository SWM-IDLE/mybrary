import 'package:flutter/material.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';

class SearchBarcodeScanScreen extends StatefulWidget {
  const SearchBarcodeScanScreen({super.key});

  @override
  State<SearchBarcodeScanScreen> createState() =>
      _SearchBarcodeScanScreenState();
}

class _SearchBarcodeScanScreenState extends State<SearchBarcodeScanScreen> {
  late BookSearchData _searchBookIsbnInfo = BookSearchData.fromJson({});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0.0,
      ),
      extendBodyBehindAppBar: true,
      body: MobileScanner(
        // fit: BoxFit.contain,
        controller: MobileScannerController(
          detectionSpeed: DetectionSpeed.normal,
          facing: CameraFacing.back,
          torchEnabled: false,
        ),
        onDetect: (capture) async {
          final List<Barcode> barcodes = capture.barcodes;
          for (final barcode in barcodes) {
            if (barcode.rawValue != null) {
              setState(() {
                _fetchBookSearchIsbnResponse(barcode.rawValue!).then((value) {
                  _searchBookIsbnInfo = value;
                });
              });
              debugPrint('$_searchBookIsbnInfo');
              Navigator.pushReplacement(
                context,
                MaterialPageRoute(
                  builder: (_) => SearchDetailScreen(
                    searchBookData: _searchBookIsbnInfo,
                  ),
                ),
              );
            }
          }
        },
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
