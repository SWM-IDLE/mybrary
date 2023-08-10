import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:mybrary/res/constants/color.dart';
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
  MobileScannerController isbnCameraController = MobileScannerController(
    detectionSpeed: DetectionSpeed.normal,
    facing: CameraFacing.back,
    torchEnabled: false,
  );

  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(
      const SystemUiOverlayStyle(
        statusBarColor: myBookScanBackgroundColor,
        statusBarBrightness: Brightness.dark,
        statusBarIconBrightness: Brightness.light,
        systemNavigationBarColor: myBookScanBackgroundColor,
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
        systemNavigationBarColor: commonLessGreyColor.withOpacity(0.2),
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
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                          builder: (_) => SearchDetailScreen(
                            isbn13: barcode.rawValue!,
                          ),
                        ),
                      );
                    }
                    isbnCameraController.dispose();
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
}
