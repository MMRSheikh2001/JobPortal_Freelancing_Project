import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/themes/app_theme.dart';

void main() {
  runApp(const ProviderScope(child:MyApp()));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'WorkBridge',
      theme: AppTheme.light_,
      initialRoute: AppRouter.root,
      onGenerateRoute: AppRouter.onGenerateRoute,
    );
  }
}
