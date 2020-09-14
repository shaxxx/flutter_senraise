import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class Senraise {
  static const MethodChannel _channel = const MethodChannel('senraise');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future printText(String text) async {
    await _channel.invokeMethod('printText', {"text": text});
  }

  static Future printEpson(Uint8List data) async {
    await _channel.invokeMethod('printEpson', {"data": data});
  }

  static Future setTextSize({int textSize = 24}) async {
    await _channel.invokeMethod('setTextSize', {"textSize": textSize});
  }

  static Future<String> getServiceVersion() async {
    return await _channel.invokeMethod('getServiceVersion');
  }

  static Future init() async {
    await _channel.invokeMethod('printEpson', {
      "data": Uint8List.fromList([27, 64])
    });
  }

  static Future setUtf8CodePage() async {
    await setCodePage(-1);
  }

  static Future setCodePage(int codePage) async {
    if (codePage < 0) {
      await printEpson(Uint8List.fromList([28, 67, codePage]));
    } else {
      await printEpson(Uint8List.fromList([27, 116, codePage]));
    }
  }

  static Future setMagnification(int codePage) async {
    if (codePage < 0) {
      await printEpson(Uint8List.fromList([28, 67, codePage]));
    } else {
      await printEpson(Uint8List.fromList([27, 116, codePage]));
    }
  }

  static Future setLineHeight({int lineHeight = 32}) async {
    await printEpson(Uint8List.fromList([27, 51, lineHeight]));
  }

  static Future setBold(bool bold) async {
    await printEpson(Uint8List.fromList([27, 71, bold ? 1 : 0]));
  }

  static Future setLetterSpacing({int spacing = 0}) async {
    await printEpson(Uint8List.fromList([27, 32, spacing]));
  }

  static Future printBitmap(Uint8List data) async {
    await _channel.invokeMethod('printBitmap', {"data": data});
  }

  static Future printBarCode(
      String data, int symbology, int height, int width) async {
    await _channel.invokeMethod('printBarCode', {
      "data": data,
      "symbology": symbology,
      "height": height,
      "width": width
    });
  }

  static Future printQRCode(String data, int modulesize, int errorlevel) async {
    await _channel.invokeMethod('printQRCode',
        {"data": data, "modulesize": modulesize, "errorlevel": errorlevel});
  }

  static Future printTableText(
      List<String> text, Int32List weight, Int32List alignment) async {
    await _channel.invokeMethod('printTableText',
        {"text": text, "weight": weight, "alignment": alignment});
  }
}
