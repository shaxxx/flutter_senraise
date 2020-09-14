package hr.integrator.senraise.printservice.senraise;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import recieptservice.com.recieptservice.PrinterInterface;

public class RecieptServiceConnection {

    private static RecieptServiceConnection mRecieptServiceConnection = new RecieptServiceConnection();
    private Context context;
    private static final String SERVICE＿PACKAGE = "recieptservice.com.recieptservice";
    private static final String SERVICE＿ACTION = "recieptservice.com.recieptservice.service.PrinterService";

    String TAG = "RecieptServiceConnection";
    private RecieptServiceConnection() {

    }

    public static RecieptServiceConnection getInstance() {
        return mRecieptServiceConnection;
    }

    private PrinterInterface recieptService;

    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setClassName(SERVICE＿PACKAGE, SERVICE＿ACTION);
        //context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void disconnectPrinterService(Context context) {
        if (recieptService != null) {
            context.getApplicationContext().unbindService(connService);
            recieptService = null;
        }
    }

    public boolean isConnect() {
        return recieptService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            recieptService = null;
            Log.e(TAG, "Service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recieptService = PrinterInterface.Stub.asInterface(service);
            if (recieptService != null) {
                Log.i(TAG,"Initialized recieptService");
            }
            else {
                Log.e(TAG, "Failed to initialize recieptService!");
            }
        }
    };

    public void printText(String text){
        if (recieptService == null) {
                return;
        }
        try {
            recieptService.printText(text);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printEpson(byte[] data){
        if (recieptService == null) {
            return;
        }
        try {
            recieptService.printEpson(data);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getServiceVersion(){
        if (recieptService == null) {
            return null;
        }
        try {
            return recieptService.getServiceVersion();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTextSize(float textSize){
        if (recieptService == null) {
            return;
        }
        try {
            recieptService.setTextSize(textSize);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printBitmap(Bitmap pic){
        if (recieptService == null) {
            return;
        }
        try {
            recieptService.printBitmap(pic);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printBarCode(String data, int symbology, int height, int width){
        if (recieptService == null) {
            return;
        }
        try {
            recieptService.printBarCode(data, symbology, height, width);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printQRCode(String data, int modulesize, int errorlevel){
        if (recieptService == null) {
            return;
        }
        try {
            recieptService.printQRCode(data, modulesize, errorlevel);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printTableText(String[] text, int []weight, int []alignment){
        if (recieptService == null) {
            return;
        }
        try {
            recieptService.printTableText(text, weight, alignment);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
