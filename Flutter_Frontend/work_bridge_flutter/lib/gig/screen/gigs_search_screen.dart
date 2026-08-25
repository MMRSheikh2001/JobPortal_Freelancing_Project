import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'package:work_bridge_flutter/gig/entity/request/gig_search_request.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_response.dart';
import 'package:work_bridge_flutter/gig/provider/gig_provider.dart';
import 'package:work_bridge_flutter/masterdata/models/response/category_response.dart';


import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';


/// Loads all categories for the gig filter.
final gigCategoriesProvider =
FutureProvider<List<CategoryResponseDTO>>((ref) {
  return ref.watch(masterDataRepositoryProvider).getAllCategories();
});


class GigsSearchScreen extends ConsumerStatefulWidget {
  const GigsSearchScreen({super.key});

  @override
  ConsumerState<GigsSearchScreen> createState() =>
      _GigsSearchScreenState();
}


class _GigsSearchScreenState extends ConsumerState<GigsSearchScreen> {

  final _keywordCtrl = TextEditingController();

  Timer? _debounce;


  @override
  void dispose() {
    _keywordCtrl.dispose();
    _debounce?.cancel();
    super.dispose();
  }


  // =====================================================
  // Keyword Search
  // =====================================================

  void _onKeywordChanged(String value) {

    _debounce?.cancel();

    _debounce = Timer(
      const Duration(milliseconds: 400),
          () {

        final notifier =
        ref.read(gigSearchFilterProvider.notifier);

        notifier.state = notifier.state.copyWith(
          keyword: value.trim().isEmpty
              ? null
              : value.trim(),
        );
      },
    );
  }


  // =====================================================
  // Reset
  // =====================================================

  void _reset() {

    _debounce?.cancel();

    _keywordCtrl.clear();

    ref.read(gigSearchFilterProvider.notifier).state =
    const GigSearchRequestDTO(
      active: true,
    );
  }


  // =====================================================
  // Filter Sheet
  // =====================================================

  void _openFilterSheet() {

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (_) => const _GigFilterSheet(),
    );
  }


  // =====================================================
  // Active Filter Count
  // =====================================================

  int _countActiveFilters(
      GigSearchRequestDTO filter) {

    int count = 0;

    if (filter.categoryId != null) count++;

    if (filter.minPrice != null) count++;

    if (filter.maxPrice != null) count++;

    if (filter.maxDeliveryDays != null) count++;

    if (filter.minimumRating != null) count++;

    if (filter.minimumOrders != null) count++;

    return count;
  }


  @override
  Widget build(BuildContext context) {

    final resultsAsync =
    ref.watch(gigSearchResultsProvider);

    final filter =
    ref.watch(gigSearchFilterProvider);

    final activeFilterCount =
    _countActiveFilters(filter);


    return Scaffold(

      appBar: AppBar(

        title: const Text('Find Gigs'),

        actions: [

          TextButton.icon(

            onPressed: _reset,

            style: TextButton.styleFrom(
              foregroundColor: Colors.white,
            ),

            icon: const Icon(
              Icons.refresh,
              size: 18,
              color: Colors.white,
            ),

            label: const Text(
              'Reset',
              style: TextStyle(
                color: Colors.white,
              ),
            ),
          ),
        ],
      ),


      body: Column(
        children: [

          // =====================================================
          // Search Bar
          // =====================================================

          Padding(
            padding: const EdgeInsets.fromLTRB(
              16,
              12,
              16,
              8,
            ),

            child: Row(
              children: [

                Expanded(

                  child: TextField(

                    controller: _keywordCtrl,

                    onChanged: _onKeywordChanged,

                    decoration: InputDecoration(

                      hintText:
                      'Search gigs...',

                      prefixIcon:
                      const Icon(Icons.search),

                      filled: true,

                      border:
                      OutlineInputBorder(
                        borderRadius:
                        BorderRadius.circular(12),
                        borderSide:
                        BorderSide.none,
                      ),
                    ),
                  ),
                ),


                const SizedBox(width: 8),


                // =====================================================
                // Filter Button
                // =====================================================

                Stack(

                  clipBehavior:
                  Clip.none,

                  children: [

                    IconButton.filledTonal(

                      onPressed:
                      _openFilterSheet,

                      icon:
                      const Icon(Icons.tune),
                    ),


                    if (activeFilterCount > 0)

                      Positioned(

                        right: -2,
                        top: -2,

                        child: Container(

                          padding:
                          const EdgeInsets.all(4),

                          decoration:
                          const BoxDecoration(
                            color: Colors.red,
                            shape:
                            BoxShape.circle,
                          ),

                          child: Text(

                            '$activeFilterCount',

                            style:
                            const TextStyle(
                              color: Colors.white,
                              fontSize: 10,
                              fontWeight:
                              FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                  ],
                ),
              ],
            ),
          ),


          // =====================================================
          // Results
          // =====================================================

          Expanded(

            child: resultsAsync.when(

              loading: () =>
              const Center(
                child:
                CircularProgressIndicator(),
              ),


              error: (error, stack) =>
                  Center(

                    child: Padding(
                      padding:
                      const EdgeInsets.all(24),

                      child: Column(

                        mainAxisSize:
                        MainAxisSize.min,

                        children: [

                          const Icon(
                            Icons.error_outline,
                            size: 40,
                            color: Colors.red,
                          ),

                          const SizedBox(height: 12),

                          Text(
                            'Failed to load gigs:\n$error',
                            textAlign:
                            TextAlign.center,
                          ),

                          const SizedBox(height: 12),

                          OutlinedButton(

                            onPressed: () {
                              ref.invalidate(
                                gigSearchResultsProvider,
                              );
                            },

                            child:
                            const Text('Retry'),
                          ),
                        ],
                      ),
                    ),
                  ),


              data: (gigs) {

                if (gigs.isEmpty) {

                  return const Center(

                    child: Padding(
                      padding:
                      EdgeInsets.all(24),

                      child: Text(
                        'No gigs match your search. '
                            'Try adjusting your filters.',
                        textAlign:
                        TextAlign.center,

                        style: TextStyle(
                          color: Colors.black54,
                        ),
                      ),
                    ),
                  );
                }


                return RefreshIndicator(

                  onRefresh: () async {

                    ref.invalidate(
                      gigSearchResultsProvider,
                    );

                    await ref.read(
                      gigSearchResultsProvider
                          .future,
                    );
                  },


                  child: ListView.separated(

                    padding:
                    const EdgeInsets.fromLTRB(
                      16,
                      4,
                      16,
                      16,
                    ),

                    itemCount:
                    gigs.length,

                    separatorBuilder:
                        (_, __) =>
                    const SizedBox(
                      height: 10,
                    ),

                    itemBuilder: (_, index) {

                      return _GigCard(
                        gig: gigs[index],
                      );
                    },
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}


// =============================================================
// Gig Card
// =============================================================

class _GigCard extends StatelessWidget {

  const _GigCard({
    required this.gig,
  });

  final GigResponseDTO gig;


  String get _priceLabel {

    if (gig.startingPrice == null) {
      return 'Price not specified';
    }

    return 'Starting from ৳${gig.startingPrice!.toStringAsFixed(0)}';
  }


  String get _ratingLabel {

    if (gig.averageRating == null) {
      return 'No rating';
    }

    return gig.averageRating!
        .toStringAsFixed(1);
  }


  @override
  Widget build(BuildContext context) {

    return Card(

      clipBehavior:
      Clip.antiAlias,

      child: InkWell(

        onTap: () {

          if (gig.id == null) {
            return;
          }

          Navigator.of(context).pushNamed(
            AppRouter.gigDetails,
            arguments: gig.id,
          );
        },


        child: Padding(

          padding:
          const EdgeInsets.all(14),

          child: Column(

            crossAxisAlignment:
            CrossAxisAlignment.start,

            children: [

              // =====================================================
              // Image + Title
              // =====================================================

              Row(

                crossAxisAlignment:
                CrossAxisAlignment.start,

                children: [

                  // Gig image

                  ClipRRect(

                    borderRadius:
                    BorderRadius.circular(10),

                    child: SizedBox(

                      width: 80,
                      height: 80,

                      child: gig.gigImage != null &&
                          gig.gigImage!
                              .isNotEmpty

                          ? Image.network(

                        '${ApiConstants.gigImageUrl}'
                            '${gig.gigImage}',

                        fit:
                        BoxFit.cover,

                        errorBuilder:
                            (_, __, ___) {

                          return Container(

                            color:
                            Colors.grey.shade100,

                            child:
                            const Icon(
                              Icons
                                  .image_not_supported_outlined,
                              color:
                              Colors.grey,
                            ),
                          );
                        },

                        loadingBuilder:
                            (
                            context,
                            child,
                            loadingProgress,
                            ) {

                          if (loadingProgress ==
                              null) {
                            return child;
                          }

                          return Container(

                            color:
                            Colors.grey.shade100,

                            child:
                            const Center(
                              child:
                              SizedBox(
                                width: 20,
                                height: 20,
                                child:
                                CircularProgressIndicator(
                                  strokeWidth:
                                  2,
                                ),
                              ),
                            ),
                          );
                        },
                      )

                          : Container(

                        color:
                        Colors.grey.shade100,

                        child:
                        const Icon(
                          Icons
                              .design_services_outlined,
                          color:
                          Colors.grey,
                          size: 32,
                        ),
                      ),
                    ),
                  ),


                  const SizedBox(width: 12),


                  // Title + seller

                  Expanded(

                    child: Column(

                      crossAxisAlignment:
                      CrossAxisAlignment.start,

                      children: [

                        Text(

                          gig.title ??
                              'Untitled gig',

                          style:
                          const TextStyle(
                            fontWeight:
                            FontWeight.bold,
                            fontSize: 15,
                          ),

                          maxLines: 2,

                          overflow:
                          TextOverflow.ellipsis,
                        ),


                        const SizedBox(height: 4),


                        Text(

                          gig.userName ??
                              'Unknown seller',

                          style:
                          const TextStyle(
                            color:
                            Colors.black54,
                          ),

                          maxLines: 1,

                          overflow:
                          TextOverflow.ellipsis,
                        ),


                        const SizedBox(height: 6),


                        if (gig.categoryName != null)

                          _Tag(
                            icon:
                            Icons.category_outlined,
                            label:
                            gig.categoryName!,
                          ),
                      ],
                    ),
                  ),
                ],
              ),


              const SizedBox(height: 10),


              // =====================================================
              // Short Description
              // =====================================================

              if (gig.shortDescription != null &&
                  gig.shortDescription!.isNotEmpty)

                Text(

                  gig.shortDescription!,

                  maxLines: 2,

                  overflow:
                  TextOverflow.ellipsis,

                  style:
                  const TextStyle(
                    color:
                    Colors.black87,
                  ),
                ),


              const SizedBox(height: 10),


              // =====================================================
              // Gig Information
              // =====================================================

              Wrap(

                spacing: 6,
                runSpacing: 6,

                children: [

                  if (gig.deliveryDays != null)

                    _Tag(
                      icon:
                      Icons.schedule_outlined,
                      label:
                      '${gig.deliveryDays} days',
                    ),


                  if (gig.revisions != null)

                    _Tag(
                      icon:
                      Icons.refresh,
                      label:
                      '${gig.revisions} revisions',
                    ),


                  if (gig.averageRating != null)

                    _Tag(
                      icon:
                      Icons.star_outline,
                      label:
                      '$_ratingLabel '
                          '(${gig.totalReviews ?? 0})',
                    ),


                  if (gig.completedOrders != null)

                    _Tag(
                      icon:
                      Icons.shopping_bag_outlined,
                      label:
                      '${gig.completedOrders} completed',
                    ),
                ],
              ),


              const SizedBox(height: 10),


              // =====================================================
              // Price
              // =====================================================

              Row(

                children: [

                  Icon(
                    Icons.payments_outlined,
                    size: 17,
                    color:
                    Colors.green.shade700,
                  ),

                  const SizedBox(width: 4),

                  Text(

                    _priceLabel,

                    style:
                    TextStyle(
                      color:
                      Colors.green.shade700,
                      fontWeight:
                      FontWeight.w600,
                    ),
                  ),


                  const Spacer(),


                  const Icon(
                    Icons.arrow_forward_ios,
                    size: 14,
                    color:
                    Colors.black38,
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}


// =============================================================
// Tag
// =============================================================

class _Tag extends StatelessWidget {

  const _Tag({
    required this.icon,
    required this.label,
  });

  final IconData icon;
  final String label;


  @override
  Widget build(BuildContext context) {

    return Container(

      padding:
      const EdgeInsets.symmetric(
        horizontal: 8,
        vertical: 4,
      ),

      decoration:
      BoxDecoration(
        color:
        Colors.grey.shade100,
        borderRadius:
        BorderRadius.circular(8),
      ),

      child: Row(

        mainAxisSize:
        MainAxisSize.min,

        children: [

          Icon(
            icon,
            size: 13,
            color:
            Colors.black54,
          ),

          const SizedBox(width: 4),

          Text(
            label,
            style:
            const TextStyle(
              fontSize: 12,
              color:
              Colors.black87,
            ),
          ),
        ],
      ),
    );
  }
}


// =============================================================
// Filter Sheet
// =============================================================

class _GigFilterSheet
    extends ConsumerStatefulWidget {

  const _GigFilterSheet();


  @override
  ConsumerState<_GigFilterSheet>
  createState() =>
      _GigFilterSheetState();
}


class _GigFilterSheetState
    extends ConsumerState<_GigFilterSheet> {

  late GigSearchRequestDTO _draft;


  final _minPriceCtrl =
  TextEditingController();

  final _maxPriceCtrl =
  TextEditingController();

  final _deliveryDaysCtrl =
  TextEditingController();

  final _minimumRatingCtrl =
  TextEditingController();

  final _minimumOrdersCtrl =
  TextEditingController();


  @override
  void initState() {

    super.initState();

    _draft =
        ref.read(gigSearchFilterProvider);

    _minPriceCtrl.text =
        _draft.minPrice
            ?.toStringAsFixed(0) ??
            '';

    _maxPriceCtrl.text =
        _draft.maxPrice
            ?.toStringAsFixed(0) ??
            '';

    _deliveryDaysCtrl.text =
        _draft.maxDeliveryDays
            ?.toString() ??
            '';

    _minimumRatingCtrl.text =
        _draft.minimumRating
            ?.toString() ??
            '';

    _minimumOrdersCtrl.text =
        _draft.minimumOrders
            ?.toString() ??
            '';
  }


  @override
  void dispose() {

    _minPriceCtrl.dispose();
    _maxPriceCtrl.dispose();
    _deliveryDaysCtrl.dispose();
    _minimumRatingCtrl.dispose();
    _minimumOrdersCtrl.dispose();

    super.dispose();
  }


  // =====================================================
  // Apply
  // =====================================================

  void _apply() {

    final minPrice =
    double.tryParse(
      _minPriceCtrl.text.trim(),
    );

    final maxPrice =
    double.tryParse(
      _maxPriceCtrl.text.trim(),
    );

    final maxDeliveryDays =
    int.tryParse(
      _deliveryDaysCtrl.text.trim(),
    );

    final minimumRating =
    int.tryParse(
      _minimumRatingCtrl.text.trim(),
    );

    final minimumOrders =
    int.tryParse(
      _minimumOrdersCtrl.text.trim(),
    );


    ref.read(
      gigSearchFilterProvider.notifier,
    ).state = _draft.copyWith(

      minPrice:
      minPrice,

      maxPrice:
      maxPrice,

      maxDeliveryDays:
      maxDeliveryDays,

      minimumRating:
      minimumRating,

      minimumOrders:
      minimumOrders,
    );


    Navigator.of(context).pop();
  }


  // =====================================================
  // Clear
  // =====================================================

  void _clear() {

    setState(() {

      _draft =
          GigSearchRequestDTO(
            active: true,
          );

      _minPriceCtrl.clear();
      _maxPriceCtrl.clear();
      _deliveryDaysCtrl.clear();
      _minimumRatingCtrl.clear();
      _minimumOrdersCtrl.clear();
    });
  }


  @override
  Widget build(BuildContext context) {

    final categoriesAsync =
    ref.watch(gigCategoriesProvider);


    return DraggableScrollableSheet(

      initialChildSize: 0.85,

      minChildSize: 0.5,

      maxChildSize: 0.95,

      expand: false,


      builder:
          (context, scrollController) {

        return Padding(

          padding:
          EdgeInsets.only(
            left: 16,
            right: 16,
            top: 16,
            bottom:
            MediaQuery.of(context)
                .viewInsets
                .bottom +
                16,
          ),


          child: ListView(

            controller:
            scrollController,

            children: [

              // =====================================================
              // Header
              // =====================================================

              Row(

                mainAxisAlignment:
                MainAxisAlignment.spaceBetween,

                children: [

                  const Text(
                    'Filters',
                    style:
                    TextStyle(
                      fontSize: 18,
                      fontWeight:
                      FontWeight.bold,
                    ),
                  ),


                  TextButton(
                    onPressed: _clear,
                    child:
                    const Text(
                      'Clear all',
                    ),
                  ),
                ],
              ),


              const SizedBox(height: 16),


              // =====================================================
              // Category
              // =====================================================

              const Text(
                'Category',
                style:
                TextStyle(
                  fontWeight:
                  FontWeight.w600,
                ),
              ),


              const SizedBox(height: 8),


              categoriesAsync.when(

                loading: () =>
                const Center(
                  child:
                  CircularProgressIndicator(),
                ),


                error: (error, _) =>
                    Text(
                      'Failed to load categories: $error',
                      style:
                      const TextStyle(
                        color: Colors.red,
                      ),
                    ),


                data: (categories) {

                  return DropdownButtonFormField<int?>(
                    initialValue:
                    _draft.categoryId,

                    decoration:
                    const InputDecoration(
                      labelText:
                      'Category',
                      border:
                      OutlineInputBorder(),
                    ),

                    items: [

                      const DropdownMenuItem<int?>(
                        value: null,
                        child:
                        Text('All categories'),
                      ),

                      ...categories.map(
                            (category) =>
                            DropdownMenuItem<int?>(
                              value:
                              category.id,
                              child:
                              Text(
                                category.name ??
                                    'Unnamed',
                              ),
                            ),
                      ),
                    ],

                    onChanged: (value) {

                      setState(() {

                        _draft =
                            _draft.copyWith(
                              categoryId:
                              value,
                            );
                      });
                    },
                  );
                },
              ),


              const SizedBox(height: 18),


              // =====================================================
              // Price
              // =====================================================

              const Text(
                'Price Range',
                style:
                TextStyle(
                  fontWeight:
                  FontWeight.w600,
                ),
              ),


              const SizedBox(height: 8),


              Row(

                children: [

                  Expanded(

                    child: TextField(

                      controller:
                      _minPriceCtrl,

                      keyboardType:
                      const TextInputType.numberWithOptions(
                        decimal: true,
                      ),

                      decoration:
                      const InputDecoration(
                        labelText:
                        'Minimum',
                        prefixText:
                        '৳ ',
                        border:
                        OutlineInputBorder(),
                      ),
                    ),
                  ),


                  const SizedBox(width: 12),


                  Expanded(

                    child: TextField(

                      controller:
                      _maxPriceCtrl,

                      keyboardType:
                      const TextInputType.numberWithOptions(
                        decimal: true,
                      ),

                      decoration:
                      const InputDecoration(
                        labelText:
                        'Maximum',
                        prefixText:
                        '৳ ',
                        border:
                        OutlineInputBorder(),
                      ),
                    ),
                  ),
                ],
              ),


              const SizedBox(height: 18),


              // =====================================================
              // Delivery Days
              // =====================================================

              TextField(

                controller:
                _deliveryDaysCtrl,

                keyboardType:
                TextInputType.number,

                decoration:
                const InputDecoration(
                  labelText:
                  'Maximum Delivery Days',
                  hintText:
                  'e.g. 7',
                  prefixIcon:
                  Icon(
                    Icons.schedule_outlined,
                  ),
                  border:
                  OutlineInputBorder(),
                ),
              ),


              const SizedBox(height: 18),


              // =====================================================
              // Minimum Rating
              // =====================================================

              TextField(

                controller:
                _minimumRatingCtrl,

                keyboardType:
                TextInputType.number,

                decoration:
                const InputDecoration(
                  labelText:
                  'Minimum Rating',
                  hintText:
                  'e.g. 4',
                  prefixIcon:
                  Icon(
                    Icons.star_outline,
                  ),
                  border:
                  OutlineInputBorder(),
                ),
              ),


              const SizedBox(height: 18),


              // =====================================================
              // Minimum Orders
              // =====================================================

              TextField(

                controller:
                _minimumOrdersCtrl,

                keyboardType:
                TextInputType.number,

                decoration:
                const InputDecoration(
                  labelText:
                  'Minimum Completed Orders',
                  hintText:
                  'e.g. 10',
                  prefixIcon:
                  Icon(
                    Icons.shopping_bag_outlined,
                  ),
                  border:
                  OutlineInputBorder(),
                ),
              ),


              const SizedBox(height: 24),


              // =====================================================
              // Apply
              // =====================================================

              FilledButton(

                onPressed:
                _apply,

                style:
                FilledButton.styleFrom(
                  minimumSize:
                  const Size.fromHeight(
                    48,
                  ),
                ),

                child:
                const Text(
                  'Apply Filters',
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}