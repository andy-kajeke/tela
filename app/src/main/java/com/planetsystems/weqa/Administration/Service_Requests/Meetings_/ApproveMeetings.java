package com.planetsystems.weqa.Administration.Service_Requests.Meetings_;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.ApproveTimeOff;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;

public class ApproveMeetings extends AppCompatActivity {

    EditText startTime, endTime, requestedOn;
    EditText requestedBy, reason, startDate, endDate;
    Button approve, cancel;

    String dayString;
    String id_extra, leave_extra, itemID_extra;
    String name_extra, school_extra, startDate_extra, endDate_extra;
    String startTime_extra, endTime_extra, requestedOn_extra, reason_extra;

    private static final String approvalStatus = "approvalStatus";
    private static final String approvalDate = "approvalDate";
    private static final String confirmation = "confirmation";
    private static final String SyncEmployeeTimeOffRequestDMs = "SyncEmployeeTimeOffRequestDMs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_meetings);

        startTime = (EditText) findViewById(R.id.startTime);
        endTime = (EditText) findViewById(R.id.endTime);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        requestedOn = (EditText) findViewById(R.id.requestedOn);
        requestedBy = (EditText) findViewById(R.id.staff_name);
        reason = (EditText) findViewById(R.id.reason);
        approve = (Button) findViewById(R.id.approveRequest);
        cancel = (Button) findViewById(R.id.cancelRequest);

        setTitle("Meeting Request");

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("db_id");
        name_extra = bundle.getString("emp_name");
        leave_extra = bundle.getString("leave");
        startDate_extra = bundle.getString("startDate");
        endDate_extra = bundle.getString("endDate");
        startTime_extra = bundle.getString("startTime");
        endTime_extra = bundle.getString("endTime");
        requestedOn_extra = bundle.getString("requestedOn");
        reason_extra = bundle.getString("reason");
        school_extra = bundle.getString("school");

        startTime.setText(startTime_extra);
        endTime.setText(endTime_extra);
        startDate.setText(startDate_extra);
        endDate.setText(endDate_extra);
        requestedOn.setText(requestedOn_extra);
        requestedBy.setText(name_extra);
        reason.setText(reason_extra);

        long date = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("dd /MM/ yyy");
        dayString = day.format(date);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ApproveMeetings.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to approve this request?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ApproveRequest();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ApproveMeetings.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to reject this request?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                RejectRequest();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    private void ApproveRequest(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            ContentValues approveCV = new ContentValues();
            approveCV.put(approvalStatus, "Accepted");
            approveCV.put(confirmation, "Seen");
            approveCV.put(approvalDate, dayString);
            //db.update(SyncTimeTables, editTimeTable,"id = ?",new String[] { id_extra });
            db.update(SyncEmployeeTimeOffRequestDMs, approveCV,"id = " + "'"+id_extra+"'",null);

            Toast.makeText(getApplicationContext(), "Approved successfully..", Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void RejectRequest(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            ContentValues approveCV = new ContentValues();
            approveCV.put(approvalStatus, "Rejected");
            approveCV.put(confirmation, "Seen");
            approveCV.put(approvalDate, dayString);
            //db.update(SyncTimeTables, editTimeTable,"id = ?",new String[] { id_extra });
            db.update(SyncEmployeeTimeOffRequestDMs, approveCV,"id = " + "'"+id_extra+"'",null);

            Toast.makeText(getApplicationContext(), "Rejected successfully..", Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}
