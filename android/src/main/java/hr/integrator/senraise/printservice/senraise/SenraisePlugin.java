package hr.integrator.senraise.printservice.senraise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import hr.integrator.senraise.printservice.senraise.RecieptServiceConnection;

/** SenraisePlugin */
public class SenraisePlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private static RecieptServiceConnection recieptServiceConnection;
  private static String TAG = "SenraisePlugin";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "senraise");
    channel.setMethodCallHandler(this);
    recieptServiceConnection = RecieptServiceConnection.getInstance();
    recieptServiceConnection.connectPrinterService(flutterPluginBinding.getApplicationContext());
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "senraise");
    channel.setMethodCallHandler(new SenraisePlugin());
    recieptServiceConnection = RecieptServiceConnection.getInstance();
    recieptServiceConnection.connectPrinterService(registrar.context());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }
    else if (call.method.equals("printText")) {
        Log.d(TAG, "printText started");
        String text = call.argument("text");
        recieptServiceConnection.printText(text);
        result.success(null);
        Log.d(TAG, "printText ended");
      }
     else if (call.method.equals("printEpson")) {
      Log.d(TAG, "printEpson started");
      byte[] data = (byte[]) call.argument("data");
      recieptServiceConnection.printEpson(data);
      result.success(null);
      Log.d(TAG, "printEpson ended");
    }
    else if (call.method.equals("setTextSize")) {
        Log.d(TAG, "setTextSize started");
        float textSize = (int)call.argument("textSize");
        recieptServiceConnection.setTextSize(textSize);
        result.success(null);
        Log.d(TAG, "setTextSize ended");
    }
    else if (call.method.equals("getServiceVersion")) {
        Log.d(TAG, "getServiceVersion started");
        result.success(recieptServiceConnection.getServiceVersion());
        Log.d(TAG, "getServiceVersion ended");
    }
    else if (call.method.equals("printBitmap")) {
        Log.d(TAG, "printBitmap started");
        byte[] data = (byte[]) call.argument("data");
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        recieptServiceConnection.printBitmap(bmp);
        result.success(null);
        Log.d(TAG, "printBitmap ended");
    }
    else if (call.method.equals("printBarCode")) {
        Log.d(TAG, "printBarCode started");
        String data = (String) call.argument("data");
        int symbology = (int) call.argument("symbology");
        int height = (int) call.argument("height");
        int width = (int) call.argument("width");
        recieptServiceConnection.printBarCode(data, symbology, height, width);
        result.success(null);
        Log.d(TAG, "printBarCode ended");
    }
    else if (call.method.equals("printQRCode")) {
        Log.d(TAG, "printQRCode started");
        String data = (String) call.argument("data");
        int modulesize = (int) call.argument("modulesize");
        int errorlevel = (int) call.argument("errorlevel");
        recieptServiceConnection.printQRCode(data, modulesize, errorlevel);
        result.success(null);
        Log.d(TAG, "printQRCode ended");
    }
    else if (call.method.equals("printTableText")) {
        Log.d(TAG, "printTableText started");
        ArrayList<String> text = (ArrayList<String>) call.argument("text");
        int[] weight = (int[]) call.argument("weight");
        int[] alignment = (int[]) call.argument("alignment");
        String[] arr = new String[text.size()];
        arr = text.toArray(arr);
        recieptServiceConnection.printTableText( arr, weight, alignment);
        result.success(null);
        Log.d(TAG, "printTableText ended");
    }
     else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
