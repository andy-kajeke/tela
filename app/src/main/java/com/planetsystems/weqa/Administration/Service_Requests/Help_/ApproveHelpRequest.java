package com.planetsystems.weqa.Administration.Service_Requests.Help_;

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

import com.planetsystems.weqa.Administration.Service_Requests.Materials_.ApproveMaterial;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;

public class ApproveHelpRequest extends AppCompatActivity {

    EditText helpCategory, priority, requestedBy, reason;
    Button approve, cancel;

    String name_extra, school_extra, help_extra;
    String id_extra, priority_extra, reason_extra, requestedOn_extra;
    String dayString;

    private static final String approvalStatus = "approvalStatus";
    private static final String approvalDate = "approvalDate";
    private static final String confirmation = "confirmation";
    private static final String SyncHelpRequests = "SyncHelpRequests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_help_request);
        setTitle("Help Request");

        helpCategory = (EditText) findViewById(R.id.helpType);
        priority = (EditText) findViewById(R.id.priority);
        requestedBy = (EditText) findViewById(R.id.staff_name);
        reason = (EditText) findViewById(R.id.reason);
        approve = (Button) findViewById(R.id.approveRequest);
        cancel = (Button) findViewById(R.id.cancelRequest);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("db_id");
        name_extra = bundle.getString("staffCode");
        help_extra = bundle.getString("help");
        priority_extra = bundle.getString("priority");
        requestedOn_extra = bundle.getString("requestedOn");
        reason_extra = bundle.getString("reason");
        school_extra = bundle.getString("school");

        helpCategory.setText(help_extra);
        priority.setText(priority_extra);
        requestedBy.setText(name_extra);
        reason.setText(reason_extra);

        long date = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("dd /MM/ yyy");
        dayString = day.format(date);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ApproveHelpRequest.this)
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

                new AlertDialog.Builder(ApproveHelpRequest.this)
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
            db.update(SyncHelpRequests, approveCV,"id = " + "'"+id_extra+"'",null);

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
            db.update(SyncHelpRequests, approveCV,"id = " + "'"+id_extra+"'",null);

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
