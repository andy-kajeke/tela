package com.planetsystems.weqa.Administration.Leaner_Attendance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.DataStore.SQLiteSync;
import com.planetsystems.weqa.DataStore.ServerConnection;
import com.planetsystems.weqa.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Learner extends AppCompatActivity {

    TextView className,day,totalPresent,totalAbsent;
    EditText numBoysPresent, numBoysAbsent;
    EditText numGalsPresent, numGalsAbsent;
    CheckBox cPresent, cAbsent;
    EditText comment;
    Button postInfo;

    String id_extra;
    String name_extra;
    String school_id_extra;
    String admin_extra;
    String dateString;
    String dayOfTheWeek;
    int boys_present, boys_absent;
    int girls_present, girls_absent;
    int sum_present;
    int sum_absent;

    private static final String id = "id";
    private static final String Comment = "comment";
    private static final String deploymentUnit = "deploymentUnit";
    private static final String deploymentUnitId = "deploymentUnitId";
    private static final String femaleAbsent = "femaleAbsent";
    private static final String femalePresent = "femalePresent";
    private static final String maleAbsent = "maleAbsent";
    private static final String malePresent = "malePresent";
    private static final String submissionDate = "submissionDate";
    private static final String supervisorId = "supervisorId";
    private static final String taskDay = "taskDay";
    private static final String SyncAttendanceRecords = "SyncAttendanceRecords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        day = (TextView) findViewById(R.id.date_of_day);
        className = (TextView) findViewById(R.id.class_name);
        totalPresent = (TextView) findViewById(R.id.totalPresent);
        totalAbsent = (TextView) findViewById(R.id.totalAbsent);
        numBoysPresent = (EditText) findViewById(R.id.inputBoysPresent);
        numBoysAbsent = (EditText) findViewById(R.id.inputBoysAbsent);
        numGalsPresent = (EditText) findViewById(R.id.inputGirlsPresent);
        numGalsAbsent = (EditText) findViewById(R.id.inputGirlsAbsent);
        cPresent = (CheckBox) findViewById(R.id.checkPresent);
        cAbsent = (CheckBox) findViewById(R.id.checkAbsent);
        comment = (EditText) findViewById(R.id.edit_text);
        postInfo = (Button) findViewById(R.id.submit);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");
        school_id_extra = bundle.getString("school");
        admin_extra = bundle.getString("admin");

        className.setText("Class:  " + "Primary " + name_extra);

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        dateString = dateFormat.format(date);
        day.setText(dateString);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        cPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    boys_present = Integer.parseInt(numBoysPresent.getText().toString());
                    girls_present = Integer.parseInt(numGalsPresent.getText().toString());
                    sum_present = boys_present + girls_present;
                    totalPresent.append(String.valueOf(sum_present));
                }

            }
        });

        cAbsent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    boys_absent = Integer.parseInt(numBoysAbsent.getText().toString());
                    girls_absent = Integer.parseInt(numGalsAbsent.getText().toString());
                    sum_absent = boys_absent + girls_absent;
                    totalAbsent.append(String.valueOf(sum_absent));
                }

            }
        });

        postInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Learner.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to submit?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                PostLearnerAttendance();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
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

    private void PostLearnerAttendance(){

        SQLiteDatabase db = null;
        Cursor cursor = null;
        Cursor _cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            _cursor = db.rawQuery("select *from SyncAttendanceRecords where deploymentUnit = " + "'"+name_extra+"'" + " " +
                    " AND submissionDate = " + "'"+dateString+"'",null);

            if(_cursor.moveToNext())
            {
                Toast.makeText(getApplicationContext(), "Already Submitted \n" + name_extra + " " + "Recodes for today", Toast.LENGTH_LONG).show();
            }
            else {

                // Adding contentValues to respective columns in the SyncAttendanceRecords table
                ContentValues learnerAttendance = new ContentValues();
                learnerAttendance.put(id, GenerateRandomString.randomString(15));
                learnerAttendance.put(deploymentUnit, name_extra);
                learnerAttendance.put(deploymentUnitId, school_id_extra);
                learnerAttendance.put(femaleAbsent, numGalsAbsent.getText().toString());
                learnerAttendance.put(femalePresent, numGalsPresent.getText().toString());
                learnerAttendance.put(maleAbsent, numBoysAbsent.getText().toString());
                learnerAttendance.put(malePresent, numBoysPresent.getText().toString());
                learnerAttendance.put(submissionDate, dateString);
                learnerAttendance.put(supervisorId, admin_extra);
                learnerAttendance.put(taskDay, dayOfTheWeek);
                learnerAttendance.put(Comment, comment.getText().toString());
                db.insert(SyncAttendanceRecords, null, learnerAttendance);

                Toast.makeText(getApplicationContext(), "Submitted successfully..", Toast.LENGTH_LONG).show();

            }

        }
        finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            if(_cursor != null && !_cursor.isClosed()){
                _cursor.close();
            }
            if(db != null && db.isOpen()){
                db.close();
            }
        }

    }

}
