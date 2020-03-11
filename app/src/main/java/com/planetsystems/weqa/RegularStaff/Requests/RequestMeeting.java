package com.planetsystems.weqa.RegularStaff.Requests;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestMeeting extends AppCompatActivity {

    EditText mdateFrom, mdateTo, mtimeFrom, mtimeTo, mstaff_comment;
    ProgressDialog dialog;
    Button mbtnFollow;
    String id_extra, name_extra;
    String school_extra;
    String datetoday;

    private DatePickerDialog.OnDateSetListener m_fromDatePicker;
    private DatePickerDialog.OnDateSetListener m_toDatePicker;
    private TimePickerDialog.OnTimeSetListener m_fromTimePicker;
    private TimePickerDialog.OnTimeSetListener m_toTimePicker;

    private static final String id = "id";
    private static final String deploymentSiteId = "deploymentSiteId";
    private static final String employee = "employee";
    private static final String employeeId = "employeeId";
    private static final String employeeRequestType = "employeeRequestType";
    private static final String comment = "comment";
    private static final String confirmation = "confirmation";
    private static final String generalComment = "generalComment";
    private static final String fromDate = "fromDate";
    private static final String toDate = "toDate";
    private static final String fromTime = "fromTime";
    private static final String toTime = "toTime";
    private static final String requestDate = "requestDate";
    private static final String typeOfLeave = "typeOfLeave";
    private static final String approvalStatus = "approvalStatus";
    private static final String SyncEmployeeTimeOffRequestDMs = "SyncEmployeeTimeOffRequestDMs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_meeting);
        setTitle("Meeting");

        mbtnFollow =(Button) findViewById(R.id.mTimeOut);
        mdateFrom =(EditText) findViewById(R.id.mDateFrom);
        mdateTo =(EditText) findViewById(R.id.mDateTo);
        mtimeFrom =(EditText) findViewById(R.id.mTimeFrom);
        mtimeTo =(EditText) findViewById(R.id.mTimeTo);
        mstaff_comment =(EditText) findViewById(R.id.mcomment);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");
        school_extra = bundle.getString("school_id");

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        datetoday = dateFormat.format(date);


        ///////////////////////////////////////Date meeting starts//////////////////////////////////////////////
        mdateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RequestMeeting.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,m_fromDatePicker,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setIcon(R.drawable.ic_date_range_black_24dp);
                dialog.setTitle("Select Start Date.");
                dialog.show();
            }
        });

        m_fromDatePicker = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year){
                month = month + 1;
                String date = month + "/" + year + "/" + day;
                mdateFrom.setText(date);
            }
        };

        ///////////////////////////////////////Date meeting ends//////////////////////////////////////////////////
        mdateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RequestMeeting.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,m_toDatePicker,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setIcon(R.drawable.ic_date_range_black_24dp);
                dialog.setTitle("Select End Date.");
                dialog.show();
            }
        });

        m_toDatePicker = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker,int day, int month, int year){
                month = month + 1;
                String date = month + "/" + year + "/" + day;
                mdateTo.setText(date);
            }
        };

        //////////////////////////////////////////////Time to start///////////////////////////////////////////////////
        mtimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(RequestMeeting.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,m_fromTimePicker,
                        hour, minute, is24Hour);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.setIcon(R.drawable.ic_access_time_black_24dp);
                timePickerDialog.setTitle("Select Start Time.");
                timePickerDialog.show();
            }
        });

        m_fromTimePicker = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                minute = minute + 1;
                String startTime = hour + " : " + minute;
                mtimeFrom.setText(startTime);
            }
        };
        //////////////////////////////////////////////Time to end///////////////////////////////////////////////////
        mtimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(RequestMeeting.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,m_toTimePicker,
                        hour, minute, is24Hour);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.setIcon(R.drawable.ic_access_time_black_24dp);
                timePickerDialog.setTitle(" Select End Time.");
                timePickerDialog.show();
            }
        });

        m_toTimePicker = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                minute = minute + 1;
                String startTime = hour + " : " + minute;
                mtimeTo.setText(startTime);
            }
        };


        mbtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new AlertDialog.Builder(RequestMeeting.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to submit?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    PostToSyncEmployeeTimeOffRequestDMs();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
            }

        });

    }

    private void PostToSyncEmployeeTimeOffRequestDMs(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            // Adding contentValues to respective columns in the SyncEmployeeTimeOffRequestDMs table
            ContentValues meeting = new ContentValues();
            meeting.put(id, GenerateRandomString.randomString(15));
            meeting.put(employeeId, id_extra);
            meeting.put(employee, name_extra);
            meeting.put(deploymentSiteId, school_extra);
            meeting.put(confirmation, "Pending");
            meeting.put(employeeRequestType, "Meeting");
            meeting.put(fromDate, mdateFrom.getText().toString());
            meeting.put(toDate, mdateTo.getText().toString());
            meeting.put(fromTime, mtimeFrom.getText().toString());
            meeting.put(toTime, mtimeTo.getText().toString());
            meeting.put(requestDate, datetoday);
            meeting.put(comment, "");
            meeting.put(generalComment, mstaff_comment.getText().toString());
            meeting.put(typeOfLeave, "Meeting");
            db.insert(SyncEmployeeTimeOffRequestDMs, null, meeting);

            Toast.makeText(getApplicationContext(), "Submitted successfully..", Toast.LENGTH_LONG).show();

        }
        finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            if(db != null && db.isOpen()){
                db.close();

            }
        }
    }
}
