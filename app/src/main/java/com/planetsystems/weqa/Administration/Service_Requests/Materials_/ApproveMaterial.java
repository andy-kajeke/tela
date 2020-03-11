package com.planetsystems.weqa.Administration.Service_Requests.Materials_;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;

public class ApproveMaterial extends AppCompatActivity {

    EditText item_name, qntyNeeded, requestedOn, neededOn;
    EditText requestedBy, reason;
    Button approve, cancel;

    String dayString;
    String id_extra, itemName_extra, itemID_extra;
    String name_extra, school_extra, qnty_extra;
    String requiredOn_extra, requestedOn_extra, reason_extra;

    private static final String db_id = "id";
    private static final String approvalStatus = "approvalStatus";
    private static final String approvalDate = "approvalDate";
    private static final String confirmation = "confirmation";
    private static final String SyncEmployeeMaterialRequests = "SyncEmployeeMaterialRequests";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_material);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item_name = (EditText) findViewById(R.id.itemName);
        qntyNeeded = (EditText) findViewById(R.id.qntyNeeded);
        requestedOn = (EditText) findViewById(R.id.requestedOn);
        neededOn = (EditText) findViewById(R.id.neededOn);
        requestedBy = (EditText) findViewById(R.id.staff_name);
        reason = (EditText) findViewById(R.id.reason);
        approve = (Button) findViewById(R.id.approveRequest);
        cancel = (Button) findViewById(R.id.cancelRequest);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("db_id");
        name_extra = bundle.getString("emp_name");
        itemID_extra = bundle.getString("item_id");
        itemName_extra = bundle.getString("item_name");
        qnty_extra = bundle.getString("item_qnty");
        requiredOn_extra = bundle.getString("neededOn");
        requestedOn_extra = bundle.getString("requestedOn");
        reason_extra = bundle.getString("reason");
        school_extra = bundle.getString("school");

        item_name.append(itemName_extra);
        qntyNeeded.append(qnty_extra);
        neededOn.append(requiredOn_extra);
        requestedOn.append(requestedOn_extra);
        requestedBy.append(name_extra);
        reason.append(reason_extra);

        long date = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("dd /MM/ yyy");
        dayString = day.format(date);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ApproveMaterial.this)
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

                new AlertDialog.Builder(ApproveMaterial.this)
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
            db.update(SyncEmployeeMaterialRequests, approveCV,"id = " + "'"+id_extra+"'",null);

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
            db.update(SyncEmployeeMaterialRequests, approveCV,"id = " + "'"+id_extra+"'",null);

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
