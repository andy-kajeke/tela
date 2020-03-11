package com.planetsystems.weqa.RegularStaff.Requests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.DataStore.SQLiteSync;
import com.planetsystems.weqa.DataStore.ServerConnection;
import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.Requests.ViewReplies.ViewRequestReplies;
import com.planetsystems.weqa.RegularStaff.Requests.ViewReplies.viewHelpRequest;
import com.planetsystems.weqa.RegularStaff.Requests.ViewReplies.viewMeetingRequest;
import com.planetsystems.weqa.RegularStaff.Requests.ViewReplies.viewSchoolMaterial;
import com.planetsystems.weqa.RegularStaff.Requests.ViewReplies.viewTimeOffRequest;

public class MakeRequests extends AppCompatActivity {

    EditText staffName, staffId;
    Button schoolMaterials, timeOff, meetings, help, replies;

    TextView close;
    Button viewSchMaterials;
    Button viewTimeOff;
    Button viewMeeting;
    Button viewHelp;
    Dialog updateDialog;

    String name_extra;
    String id_extra;
    String school_extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Service request");

        staffName = (EditText) findViewById(R.id.employeeName);
        staffId = (EditText) findViewById(R.id.employeeID);
        schoolMaterials = (Button) findViewById(R.id.SchoolMaterials);
        timeOff = (Button) findViewById(R.id.TimeOff);
        meetings = (Button) findViewById(R.id.Meeting);
        help = (Button) findViewById(R.id.Help);
        replies = (Button) findViewById(R.id.replies);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");
        school_extra = bundle.getString("school_id");

        staffName.append(name_extra);
        staffId.append(id_extra);

        updateDialog = new Dialog(this);


        //On select specific request
        schoolMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sch = new Intent(MakeRequests.this, ListSchoolMaterial.class);
                sch.putExtra("id", id_extra);
                sch.putExtra("name", name_extra);
                sch.putExtra("school_id", school_extra);
                startActivity(sch);

            }
        });
        timeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sch = new Intent(MakeRequests.this, RequestTimeOff.class);
                sch.putExtra("id", id_extra);
                sch.putExtra("name", name_extra);
                sch.putExtra("school_id", school_extra);
                startActivity(sch);

            }
        });
        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sch = new Intent(MakeRequests.this, RequestMeeting.class);
                sch.putExtra("id", id_extra);
                sch.putExtra("name", name_extra);
                sch.putExtra("school_id", school_extra);
                startActivity(sch);

            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sch = new Intent(MakeRequests.this, RequestHelp.class);
                sch.putExtra("id", id_extra);
                sch.putExtra("school_id", school_extra);
                startActivity(sch);

            }
        });

        replies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdatePopup();

            }
        });

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


    public void showUpdatePopup() {

        updateDialog.setContentView(R.layout.view_replies_popup);

        close =(TextView) updateDialog.findViewById(R.id.txclose);
        viewSchMaterials = (Button) updateDialog.findViewById(R.id.viewSchMaterials);
        viewTimeOff = (Button) updateDialog.findViewById(R.id.viewTimeOff);
        viewMeeting =(Button) updateDialog.findViewById(R.id.viewMeeting);
        viewHelp =(Button) updateDialog.findViewById(R.id.viewHelp);

        viewSchMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), viewSchoolMaterial.class);
                i.putExtra("id", id_extra);
                i.putExtra("school", school_extra);
                startActivity(i);

            }
        });

        viewTimeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), viewTimeOffRequest.class);
                i.putExtra("id", id_extra);
                i.putExtra("school", school_extra);
                startActivity(i);
            }
        });

        viewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), viewMeetingRequest.class);
                i.putExtra("id", id_extra);
                i.putExtra("school", school_extra);
                startActivity(i);
            }
        });

        viewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), viewHelpRequest.class);
                i.putExtra("id", id_extra);
                i.putExtra("school", school_extra);
                startActivity(i);
            }
        });

        close.setText("X");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });

        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.show();

    }
}
