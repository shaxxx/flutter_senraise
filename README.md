# senraise

Senraise device with build in printer plugin. Tested with MUNBYN IPDA064 device.
This is cheap chinese Android POS device that comes with built in PrinterService.
Printer itself is available like any external printer and you can send ESC/POS commands
to it like you would for any normal POS Printer.

However, under the hood it's all beeing handled by Senraise Printer service (closed source).
This service will simply try to emulate ESC/POS functions to create bitmap and then send that bitmap to the printer.
Printer never gets simple text, it's all converted to bitmap.

Problem is their ESC/POS emulation is missing basic features.
It's imposible to select text (font) size via ESC/POS, without executing command via PrinterService direclty.
Since we need to fit 40 chars in one line this was deal breaker for us so we decided to wrap their service as flutter plugin.
And don't try to use any codepage other than UTF-8, it's not supported by PrinterService.

## Getting Started

For more details see example app.
