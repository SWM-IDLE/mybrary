import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/utils/logics/permission_utils.dart';

class HomeBarcodeButton extends StatelessWidget {
  const HomeBarcodeButton({super.key});

  @override
  Widget build(BuildContext context) {
    return SliverToBoxAdapter(
      child: Container(
        margin: const EdgeInsets.symmetric(
          horizontal: 16.0,
          vertical: 4.0,
        ),
        padding: const EdgeInsets.symmetric(
          vertical: 4.0,
        ),
        decoration: BoxDecoration(
          color: homeBarcodeButtonColor,
          borderRadius: BorderRadius.circular(4.0),
        ),
        child: InkWell(
          onTap: () => onIsbnScan(context),
          child: Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 28.0,
                  vertical: 12.0,
                ),
                child: SvgPicture.asset(
                  'assets/svg/icon/barcode_scan.svg',
                  width: 22.0,
                  height: 22.0,
                ),
              ),
              const Text(
                '바코드로 마이북 등록하기',
                style: mainIntroTextStyle,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
