package com.planetsystems.weqa.DataStore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.R;

import java.util.UUID;

public class ManualSync extends Activity {

    public static final String Sync_data_to_master = "http://35.227.70.118:8080/SqliteSync-3.2.3/API3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_manual_sync);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        //((TextView)findViewById(R.id.tbSqlite_sync_url)).setText(preferences.getString("sqlite_sync_url", Sync_data_to_master));

        if (!isConnected()) {
            //Toast.makeText(this, "No network connection",Toast.LENGTH_LONG).show();
            ((TextView)findViewById(R.id.tbSqlite_sync_url)).setText("No network connection");

        } else {

            showProgressBar();
            //String sqlite_sync_url = ((TextView)findViewById(R.id.tbSqlite_sync_url)).getText().toString();
            String sqlite_sync_url = ServerConnection.serviceTyp.SYNC_TO_MASTER_DB;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("sqlite_sync_url", sqlite_sync_url);

            SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/sqlitesync.db",
                    sqlite_sync_url);

            sqLite_sync.synchronizeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
                @Override
                public void onSuccess() {
                    hideProgressBar();
                    //showMessage("Data synchronization finished successfully");
                    //Toast.makeText(ManualSync.this, "Data synchronization finished successfully", Toast.LENGTH_LONG).show();
                    ((TextView)findViewById(R.id.tbSqlite_sync_url)).setText("finished successfully...");
                }

                @Override
                public void onError(Exception error) {
                    hideProgressBar();
                    error.printStackTrace();
                    //showMessage("Data synchronization finished with error: \n" + error.getMessage());
                    ((TextView)findViewById(R.id.tbSqlite_sync_url)).setText("finished with error: \n" + error.getMessage());
                }
            });

        }

    }

    @SuppressLint("MissingPermission")
    private String getSubscriberId(){
        //        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        //UUID deviceUuid = new UUID(Device_serialNo.hashCode());
        String Device_serialNo = String.valueOf(Build.SERIAL.hashCode());
        return Device_serialNo.toString();
    }

    private void showMessage(String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Sync Data to Tela System");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dlgAlert.create().show();
    }

    private void showProgressBar(){
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    ///Check Internet connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
