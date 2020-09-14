import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:senraise/senraise.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  int _lineHeight = 32;
  bool _bold = true;
  int _fontSize = 24;
  String _serviceVersion;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
    String version;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      Senraise.init();
      version = await Senraise.getServiceVersion();
    } on PlatformException {
      version = 'Failed to initialize plugin.';
    }
    setState(() {
      _serviceVersion = version;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Senraise ReceiptService Plugin'),
        ),
        body: Center(
            child: Column(
          children: [
            Text('\nService version: $_serviceVersion\n'),
            Text(
                'Line height: $_lineHeight   Bold: $_bold    Font size: $_fontSize\n'),
            MaterialButton(
              child: Text("Init printer"),
              onPressed: () async {
                await Senraise.init();
              },
            ),
            Row(children: [
              Expanded(
                child: MaterialButton(
                  child: Text("Print text"),
                  onPressed: () async {
                    await Senraise.printText(
                        "TEXT PRINTED WITH PRINT SERVICE\n\n\n");
                  },
                ),
              ),
              Expanded(
                child: MaterialButton(
                  child: Text("Print ESC-POS"),
                  onPressed: () async {
                    await Senraise.printEpson(Uint8List.fromList(
                        [...("TEXT PRINTED WITH ESC-POS.\n".codeUnits)]));
                    await Senraise.printEpson(Uint8List.fromList([
                      ...utf8.encode("TEST UTF-8 CHARS: ŠĐČĆŽšđčćž\n\n\n"),
                      13,
                      10
                    ]));
                  },
                ),
              ),
            ]),
            Row(
              children: [
                Expanded(
                  child: MaterialButton(
                    child: Text("Set UTF-8 codepage"),
                    onPressed: () async {
                      await Senraise.setUtf8CodePage();
                    },
                  ),
                ),
                Expanded(
                  child: MaterialButton(
                    child: Text("Set CP852 codepage"),
                    onPressed: () async {
                      await Senraise.setCodePage(18);
                    },
                  ),
                ),
              ],
            ),
            Row(
              children: [
                Expanded(
                  child: MaterialButton(
                    child: Text("Toggle bold"),
                    onPressed: () async {
                      setState(() {
                        _bold = !_bold;
                      });
                      await Senraise.setBold(_bold);
                    },
                  ),
                ),
                Expanded(
                  child: MaterialButton(
                    child: Text("Print Bitmap"),
                    onPressed: () async {
                      var asset = await DefaultAssetBundle.of(context)
                          .load('graphics/ic_launcher.png');
                      await Senraise.printBitmap(asset.buffer.asUint8List());
                      await Senraise.printText("\n\n\n");
                    },
                  ),
                ),
              ],
            ),
            Row(children: [
              Expanded(
                child: MaterialButton(
                  child: Text("Line height --"),
                  onPressed: () async {
                    setState(() {
                      if (_lineHeight > 16) _lineHeight -= 16;
                    });
                    await Senraise.setLineHeight(lineHeight: _lineHeight);
                  },
                ),
              ),
              Expanded(
                child: MaterialButton(
                  child: Text("Line height ++"),
                  onPressed: () async {
                    setState(() {
                      _lineHeight += 16;
                    });
                    await Senraise.setLineHeight(lineHeight: _lineHeight);
                  },
                ),
              ),
            ]),
            Row(children: [
              Expanded(
                child: MaterialButton(
                  child: Text("Font size --"),
                  onPressed: () async {
                    setState(() {
                      if (_fontSize > 1) _fontSize--;
                    });
                    await Senraise.setTextSize(textSize: _fontSize);
                  },
                ),
              ),
              Expanded(
                child: MaterialButton(
                  child: Text("Font size ++"),
                  onPressed: () async {
                    setState(() {
                      _fontSize++;
                    });
                    await Senraise.setTextSize(textSize: _fontSize);
                  },
                ),
              ),
            ]),
            Row(
              children: [
                Expanded(
                  child: MaterialButton(
                    child: Text("Print barcode"),
                    onPressed: () async {
                      await Senraise.printBarCode("3859898989013", 6, 162, 2);
                      await Senraise.printText("\n\n\n");
                    },
                  ),
                ),
                Expanded(
                  child: MaterialButton(
                    child: Text("Print QR code"),
                    onPressed: () async {
                      await Senraise.printQRCode("IT WORKS!", 4, 3);
                      await Senraise.printText("\n\n\n");
                    },
                  ),
                ),
              ],
            ),
            MaterialButton(
              child: Text("Print Table Text"),
              onPressed: () async {
                await Senraise.printTableText(["TABLE TEXT TEST"],
                    Int32List.fromList([1]), Int32List.fromList([1]));
                await Senraise.printText("\n\n");
              },
            ),
          ],
        )),
      ),
    );
  }
}
