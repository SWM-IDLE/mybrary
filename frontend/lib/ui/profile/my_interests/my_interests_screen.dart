import 'package:flutter/material.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';
import 'package:mybrary/data/repository/interests_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/profile/my_interests/components/recommend_phrase.dart';

class MyInterestsScreen extends StatefulWidget {
  const MyInterestsScreen({super.key});

  @override
  State<MyInterestsScreen> createState() => _MyInterestsScreenState();
}

class _MyInterestsScreenState extends State<MyInterestsScreen> {
  List<CategoriesResponses> selectedInterests = [];

  void _onSelectedInterest(int index, String name) {
    setState(() {
      if (selectedInterests.any((category) => category.id == index)) {
        selectedInterests.removeWhere((category) => category.id == index);
      } else {
        if (selectedInterests.length < 3) {
          selectedInterests.add(
            CategoriesResponses(
              id: index,
              name: name,
            ),
          );
        } else {
          selectedInterests.remove(selectedInterests.first);
          selectedInterests.add(
            CategoriesResponses(
              id: index,
              name: name,
            ),
          );
        }
      }
    });
  }

  final InterestsRepository _interestsRepository = InterestsRepository();
  late Future<InterestCategoriesResponseData> _interestCategoriesResponseData;

  @override
  void initState() {
    super.initState();

    _interestCategoriesResponseData =
        _interestsRepository.getInterestCategories();
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '마이 관심사',
      actions: [
        InkWell(
          onTap: () {},
          child: const Text(
            '저장',
            style: saveTextButtonStyle,
          ),
        ),
      ],
      child: LayoutBuilder(
        builder: (context, constraint) {
          return SingleChildScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
            child: ConstrainedBox(
              constraints: BoxConstraints(
                minHeight: constraint.maxHeight,
              ),
              child: Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 16.0,
                  vertical: 8.0,
                ),
                child: Column(
                  children: [
                    const RecommendPhrase(),
                    const SizedBox(height: 16.0),
                    FutureBuilder<InterestCategoriesResponseData>(
                      future: _interestCategoriesResponseData,
                      builder: (context, snapshot) {
                        if (snapshot.hasData) {
                          final interestCategories =
                              snapshot.data!.interestCategories!;

                          return Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: interestCategories.map(
                              (e) {
                                final String description = e.description!;

                                final int startIndex =
                                    description.indexOf(e.name!);
                                final int endIndex =
                                    startIndex + e.name!.length;

                                final Row result;

                                String descriptionSubstring(
                                  int startIndex,
                                  int endIndex,
                                ) =>
                                    description.substring(
                                      startIndex,
                                      endIndex,
                                    );

                                if (startIndex > 0) {
                                  result = Row(
                                    children: [
                                      Text(
                                        descriptionSubstring(
                                          0,
                                          startIndex,
                                        ),
                                        style: interestDescriptionStyle,
                                      ),
                                      Text(
                                        descriptionSubstring(
                                          startIndex,
                                          endIndex,
                                        ),
                                        style:
                                            interestDescriptionStyle.copyWith(
                                          fontWeight: FontWeight.w700,
                                        ),
                                      ),
                                      Text(
                                        descriptionSubstring(
                                          endIndex,
                                          description.length,
                                        ),
                                        style: interestDescriptionStyle,
                                      ),
                                    ],
                                  );
                                } else {
                                  result = Row(
                                    children: [
                                      Text(
                                        descriptionSubstring(
                                          0,
                                          endIndex,
                                        ),
                                        style:
                                            interestDescriptionStyle.copyWith(
                                          fontWeight: FontWeight.w700,
                                        ),
                                      ),
                                      Text(
                                        descriptionSubstring(
                                          endIndex,
                                          description.length,
                                        ),
                                        style: interestDescriptionStyle,
                                      ),
                                    ],
                                  );
                                }

                                return Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    result,
                                    const SizedBox(height: 12.0),
                                    Wrap(
                                      spacing: 10.0,
                                      runSpacing: 10.0,
                                      children: e.interestResponses!.map(
                                        (e) {
                                          final bool isSelected =
                                              selectedInterests.any(
                                            (category) => category.id == e.id,
                                          );

                                          return InkWell(
                                            onTap: () => _onSelectedInterest(
                                              e.id!,
                                              e.name!,
                                            ),
                                            child: Container(
                                              padding:
                                                  const EdgeInsets.symmetric(
                                                horizontal: 16.0,
                                                vertical: 8.0,
                                              ),
                                              decoration: BoxDecoration(
                                                border: Border.all(
                                                  color: isSelected
                                                      ? primaryColor
                                                      : circularBorderColor,
                                                ),
                                                borderRadius:
                                                    BorderRadius.circular(50.0),
                                              ),
                                              child: Text(
                                                e.name!,
                                                style: isSelected
                                                    ? commonSubRegularStyle
                                                        .copyWith(
                                                        fontWeight:
                                                            FontWeight.w700,
                                                      )
                                                    : commonSubRegularStyle,
                                              ),
                                            ),
                                          );
                                        },
                                      ).toList(),
                                    ),
                                    const SizedBox(height: 32.0),
                                  ],
                                );
                              },
                            ).toList(),
                          );
                        }
                        return const CircularLoading();
                      },
                    ),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
