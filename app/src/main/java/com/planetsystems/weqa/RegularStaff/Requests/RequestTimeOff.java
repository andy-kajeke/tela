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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestTimeOff extends AppCompatActivity {

    ProgressDialog dialog;
    EditText dateFrom, dateTo, timeFrom, timeTo, tstaff_comment;
    Spinner leave;
    TextView closeTimeOff;
    Button tbtnFollow;
    String id_extra, name_extra;
    String school_extra;
    String datetoday;

    private DatePickerDialog.OnDateSetListener t_fromDatePicker;
    private DatePickerDialog.OnDateSetListener t_toDatePicker;
    private TimePickerDialog.OnTimeSetListener t_fromTimePicker;
    private TimePickerDialog.OnTimeSetListener t_toTimePicker;

    private static final String id = "id";
    private static final String deploymentSiteId = "deploymentSiteId";
    private static final String employee = "employee";
    private static final String employeeId = "employeeId";
    private static final String employeeRequestType = "employeeRequestType";
    private static final String comment = "comment";
    private static final String generalComment = "generalComment";
    private static final String fromDate = "fromDate";
    private static final String toDate = "toDate";
    private static final String fromTime = "fromTime";
    private static final String toTime = "toTime";
    private static final String requestDate = "requestDate";
    private static final String typeOfLeave = "typeOfLeave";
    private static final String approvalStatus = "approvalStatus";
    private static final String confirmation = "confirmation";
    private static final String SyncEmployeeTimeOffRequestDMs = "SyncEmployeeTimeOffRequestDMs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_time_off);
        setTitle("Time Off/ Leave");

        tbtnFollow =(Button) findViewById(R.id.tTimeOut);
        leave = (Spinner) findViewById(R.id.leaveType);
        dateFrom =(EditText) findViewById(R.id.tDateFrom);
        dateTo =(EditText) findViewById(R.id.tDateTo);
        timeFrom =(EditText) findViewById(R.id.tTimeFrom);
        timeTo =(EditText) findViewById(R.id.tTimeTo);
        tstaff_comment =(EditText) findViewById(R.id.tcomment);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");
        school_extra = bundle.getString("school_id");

        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this, R.array.leave_type,
                android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leave.setAdapter(Adapter);

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        datetoday = dateFormat.format(date);

        //Toast.makeText(getApplicationContext(),id_extra ,Toast.LENGTH_SHORT).show();

        ///////////////////////////////////////Date time off starts//////////////////////////////////////////////
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RequestTimeOff.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,t_fromDatePicker,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setIcon(R.drawable.ic_date_range_black_24dp);
                dialog.setTitle("Select Start Date.");
                dialog.show();
            }
        });

        t_fromDatePicker = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year){
                month = month + 1;
                //String date = year + "/" + day + "/" + month;
                int _day = day;
                int _month = month;
                int _year = year;
                dateFrom.setText( _month + "/" + _year + "/" + _day);
            }
        };

        ///////////////////////////////////////Date for time off ends//////////////////////////////////////////////////
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RequestTimeOff.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,t_toDatePicker,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setIcon(R.drawable.ic_date_range_black_24dp);
                dialog.setTitle("Select End Date.");
                dialog.show();
            }
        });

        t_toDatePicker = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker,int day, int month, int year){
                month = month + 1;
                String date = month + "/" + year + "/" + day;
                dateTo.setText(date);
            }
        };
        //////////////////////////////////////////////Time to start///////////////////////////////////////////////////
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(RequestTimeOff.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,t_fromTimePicker,
                        hour, minute, is24Hour);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.setIcon(R.drawable.ic_access_time_black_24dp);
                timePickerDialog.setTitle("Select Start Time.");
                timePickerDialog.show();
            }
        });

        t_fromTimePicker = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                minute = minute + 1;
                String startTime = hour + " : " + minute;
                timeFrom.setText(startTime);
            }
        };
        //////////////////////////////////////////////Time to end///////////////////////////////////////////////////
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(RequestTimeOff.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,t_toTimePicker,
                        hour, minute, is24Hour);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.setIcon(R.drawable.ic_access_time_black_24dp);
                timePickerDialog.setTitle("Select End Time.");
                timePickerDialog.show();
            }
        });

        t_toTimePicker = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                minute = minute + 1;
                String startTime = hour + " : " + minute;
                timeTo.setText(startTime);
            }
        };

        tbtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new AlertDialog.Builder(RequestTimeOff.this)
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
            ContentValues timeOff = new ContentValues();
            timeOff.put(id, GenerateRandomString.randomString(15));
            timeOff.put(employeeId, id_extra);
            timeOff.put(employee, name_extra);
            timeOff.put(deploymentSiteId, school_extra);
            timeOff.put(employeeRequestType, "TimeOff/Leave");
            timeOff.put(fromDate, dateFrom.getText().toString());
            timeOff.put(toDate, dateTo.getText().toString());
            timeOff.put(fromTime, timeFrom.getText().toString());
            timeOff.put(toTime, timeTo.getText().toString());
            timeOff.put(requestDate, datetoday);
            timeOff.put(confirmation, "Pending");
            timeOff.put(comment, leave.getSelectedItem().toString());
            timeOff.put(generalComment, tstaff_comment.getText().toString());
            timeOff.put(typeOfLeave, leave.getSelectedItem().toString());
            db.insert(SyncEmployeeTimeOffRequestDMs, null, timeOff);

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
