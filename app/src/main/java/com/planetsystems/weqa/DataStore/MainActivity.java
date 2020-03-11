package com.planetsystems.weqa.DataStore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_READ_PHONE_STATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Data Management");

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        ((TextView)findViewById(R.id.tbSqlite_sync_url)).setText(preferences.getString("sqlite_sync_url", ServerConnection.serviceTyp.SYNC_TO_MASTER_DB));

        findViewById(R.id.btReinitialize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                final Button button = (Button)view;
                button.setEnabled(false);

                String sqlite_sync_url = ((TextView)findViewById(R.id.tbSqlite_sync_url)).getText().toString();
                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sqlite_sync_url", sqlite_sync_url);

                SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/sqlitesync.db",
                        sqlite_sync_url);

                sqLite_sync.initializeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                        button.setEnabled(true);
                        showMessage("Initialization finished successfully");
                    }

                    @Override
                    public void onError(Exception error) {
                        hideProgressBar();
                        button.setEnabled(true);
                        error.printStackTrace();
                        showMessage("Initialization finished with error: \n" + error.getMessage());
                    }
                });
                Toast.makeText(getApplicationContext(),getSubscriberId(),Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btSendAndReceive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                final Button button = (Button)view;
                button.setEnabled(false);

                String sqlite_sync_url = ((TextView)findViewById(R.id.tbSqlite_sync_url)).getText().toString();
                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sqlite_sync_url", sqlite_sync_url);

                SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/sqlitesync.db",
                        sqlite_sync_url);

                sqLite_sync.synchronizeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                        button.setEnabled(true);
                        showMessage("Data synchronization finished successfully");
                    }

                    @Override
                    public void onError(Exception error) {
                        hideProgressBar();
                        button.setEnabled(true);
                        error.printStackTrace();
                        showMessage("Data synchronization finished with error: \n" + error.getMessage());
                    }
                });
            }
        });

        //Auto-sync the database on network connection
        if (!isConnected()) {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
        } else {

            showProgressBar();
            String sqlite_sync_url = ((TextView)findViewById(R.id.tbSqlite_sync_url)).getText().toString();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("sqlite_sync_url", sqlite_sync_url);

            SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/sqlitesync.db",
                    sqlite_sync_url);

            sqLite_sync.synchronizeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
                @Override
                public void onSuccess() {
                    hideProgressBar();
                    showMessage("Data synchronization finished successfully");
                }

                @Override
                public void onError(Exception error) {
                    hideProgressBar();
                    error.printStackTrace();
                    showMessage("Data synchronization finished with error: \n" + error.getMessage());
                }
            });

        }

        findViewById(R.id.btSelectFrom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> tables = new ArrayList<String>();

                SQLiteDatabase db = null;
                Cursor cursor = null;

                try{
                    db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
                    cursor = db.rawQuery("select tbl_Name from sqlite_master where type='table'", null);
                    while (cursor.moveToNext()){
                        tables.add(cursor.getString(0));
                    }
                }
                finally {
                    if(cursor != null && !cursor.isClosed()){
                        cursor.close();
                    }
                    if(db != null && db.isOpen()){
                        db.close();
                    }
                }

                new AlertDialog.Builder(MainActivity.this)
                        .setAdapter(new ArrayAdapter<String>(
                                MainActivity.this,
                                R.layout.spinner_item,
                                tables
                        ), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TableViewActivity.SelectFrom(MainActivity.this, (String)((AlertDialog)dialog).getListView().getAdapter().getItem(which));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setTitle("SELECT * FROM...")
                        .setCancelable(false)
                        .show();
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
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
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        return deviceUuid.toString();

        String Device_serialNo = String.valueOf(Build.SERIAL.hashCode());
        return Device_serialNo.toString();
    }

    private void showMessage(String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("SQLite-sync Demo");
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

    ///Internet connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
