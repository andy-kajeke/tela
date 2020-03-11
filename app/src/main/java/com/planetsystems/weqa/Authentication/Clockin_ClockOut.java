package com.planetsystems.weqa.Authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planetsystems.weqa.DataStore.MainActivity;
import com.planetsystems.weqa.DataStore.SQLiteSync;
import com.planetsystems.weqa.DataStore.ServerConnection;
import com.planetsystems.weqa.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Clockin_ClockOut extends AppCompatActivity implements LocationListener {

    Dialog myDialog, outDialog;
    CardView checkin, checkout, datacenter;
    TextView dateDisplay;
    TextClock clock_in_time;
    String dayOfTheWeek;
    String dayString;

    ////checkout//
    TextView close;
    Button btnFollow;
    EditText staff_Id, staff_comment;
    CheckBox norm,oth;
    ProgressDialog dialog;
    LocationManager locationManager;
    double lng;
    double lat;
    private static final String id = "id";
    private static final String SyncClockOuts = "SyncClockOuts";
    private static final String employeeId = "employeeId";
    private static final String employeeNo = "employeeNo";
    private static final String day = "day";
    private static final String clockOutDate = "clockOutDate";
    private static final String clockOutTime = "clockOutTime";
    private static final String comment = "comment";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String synStatus = "synStatus";
    private static final String Sync_data_to_master = "http://35.227.70.118:8080/SqliteSync-3.2.3/API3";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_hook);
        setTitle("Tela");

        clock_in_time = (TextClock) findViewById(R.id.textClock3);
        dateDisplay = (TextView) findViewById(R.id.calendarView4);
        datacenter = (CardView) findViewById(R.id.cardview2);
        checkin = (CardView) findViewById(R.id.cardview3);
        checkout = (CardView) findViewById(R.id.cardview4);

        myDialog = new Dialog(this);
        outDialog = new Dialog(this);

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd /MM/ yyy");
        String dateString = dateFormat.format(date);
        dateDisplay.setText(dateString);

        SimpleDateFormat day = new SimpleDateFormat("dd /MM/ yyy");
        dayString = day.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        datacenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowPopupFinger();
                //Toast.makeText(getApplicationContext(),clock_in_time.getText() + " :: " + dayString,Toast.LENGTH_LONG).show();
                Intent i = new Intent(Clockin_ClockOut.this, MainActivity.class);
                startActivity(i);
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowPopupFinger();
                //Toast.makeText(getApplicationContext(),clock_in_time.getText() + " :: " + dayString,Toast.LENGTH_LONG).show();
                Intent i = new Intent(Clockin_ClockOut.this, StaffClockIn.class);
                i.putExtra("time",clock_in_time.getText());
                i.putExtra("date",dayString);
                startActivity(i);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCheckOutPopup();
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        getLocation();

        //synchronize
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        if (!isConnected()) {
            Toast.makeText(this, "No network connection",Toast.LENGTH_LONG).show();

        } else {

            String sqlite_sync_url = ServerConnection.serviceTyp.SYNC_TO_MASTER_DB;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("sqlite_sync_url", sqlite_sync_url);

            SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/sqlitesync.db",
                    sqlite_sync_url);

            sqLite_sync.synchronizeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
                @Override
                public void onSuccess() {
                    //Toast.makeText(Clockin_ClockOut.this, "Data synchronization finished successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Exception error) {

                    error.printStackTrace();
                    //showMessage("Data synchronization finished with error: \n" + error.getMessage());
                    //Toast.makeText(Clockin_ClockOut.this, "Data synchronization finished successfully", Toast.LENGTH_LONG).show();
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

        String Device_serialNo = String.valueOf(Build.SERIAL.hashCode());
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        //UUID deviceUuid = new UUID(Device_serialNo.hashCode());
        return Device_serialNo.toString();
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

    public void ShowCheckOutPopup() {

        outDialog.setContentView(R.layout.customcheckoutpopup);

        close =(TextView) outDialog.findViewById(R.id.txclose);
        btnFollow =(Button) outDialog.findViewById(R.id.out);
        staff_Id =(EditText) outDialog.findViewById(R.id.staff_id);
        staff_comment =(EditText) outDialog.findViewById(R.id.comment);
        norm =(CheckBox)outDialog.findViewById(R.id.normal) ;
        oth =(CheckBox)outDialog.findViewById(R.id.other) ;

        norm.setChecked(true);
        staff_comment.setText("Normal end of day");
        staff_comment.setFocusableInTouchMode(false);

        oth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    staff_comment.setText("Specify reason");
                    staff_comment.setFocusableInTouchMode(true);
                    norm.setChecked(false);
                }

            }
        });
        norm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    staff_comment.setText("Normal end of day");
                    staff_comment.setFocusableInTouchMode(false);
                    oth.setChecked(false);
                }

            }
        });
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staff_Id.getText().toString().equalsIgnoreCase("")){
                    staff_Id.setError("Id Missing!");
                }else{
                    new AlertDialog.Builder(Clockin_ClockOut.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to clock out?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    StaffCheckOut();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        close.setText("X");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outDialog.dismiss();
            }
        });

        outDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        outDialog.show();

    }

    private void StaffCheckOut(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select *from SyncClockOuts where employeeNo = " + staff_Id.getText().toString() + " " +
                    " AND clockOutDate = " + "'"+dayString+"'",null);

            if(cursor.moveToNext())
            {
                Toast.makeText(getApplicationContext(), "Already Clocked Out", Toast.LENGTH_LONG).show();
            }
            else {
                ContentValues checkOut = new ContentValues();
                checkOut.put(id, GenerateRandomString.randomString(15));
                checkOut.put(clockOutDate, dayString);
                checkOut.put(clockOutTime, clock_in_time.getText().toString());
                checkOut.put(day, dayOfTheWeek);
                checkOut.put(employeeId, staff_Id.getText().toString());
                checkOut.put(employeeNo, staff_Id.getText().toString());
                checkOut.put(comment, staff_comment.getText().toString());
                checkOut.put(latitude, lat);
                checkOut.put(longitude, lng);
                checkOut.put(synStatus, "New");
                db.insert(SyncClockOuts, null, checkOut);
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

        new AlertDialog.Builder(Clockin_ClockOut.this)
                .setTitle("Success")
                .setMessage("Submitted Ok!")
                .setIcon(R.drawable.success)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        outDialog.cancel();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    //////////////////////GPS Functionality//////////////////////////////////////////////////////
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lng = location.getLatitude();
        lat = location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            //LocName.setText("Town Name: " + addresses.get(0).getAddressLine(0));
            if (null != addresses && addresses.size() > 0) {
                String _addre = addresses.get(0).getAddressLine(0);
                //LocName.setText(_addre);
            }
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(Clockin_ClockOut.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
