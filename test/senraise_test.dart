import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:senraise/senraise.dart';

void main() {
  const MethodChannel channel = MethodChannel('senraise');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Senraise.platformVersion, '42');
  });
}
