package com.planetsystems.weqa.Administration.Service_Requests;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.planetsystems.weqa.Administration.Service_Requests.Help_.PendingHelpRequest;
import com.planetsystems.weqa.Administration.Service_Requests.Materials_.PendingMaterials;
import com.planetsystems.weqa.Administration.Service_Requests.Materials_.SchoolMaterialModel;
import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.PendingMeeting;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.PendingTimeOff;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class RequestMade extends AppCompatActivity {

    int count_smPending =0;
    int count_smApproved =0;
    int count_leavePending =0;
    int count_leaveApproved =0;
    int count_meetingPending =0;
    int count_meetingApproved =0;
    int count_helpPending =0;
    int count_helpApproved =0;
    TextView smApproved, smPending;
    TextView leaveApproved, leavePending;
    TextView meetingApproved, meetingPending;
    TextView helpApproved, helpPending;

    CardView schoolMaterials;
    CardView timeOff;
    CardView meetings;
    CardView help;

    ArrayList<SchoolMaterialModel> markList, approveList;
    String school_id_extra;

    private static final String Pending = "Pending";
    private static final String Approved = "Accepted";
    private static final String SchoolMaterials = "School Materials";
    public static final String TimeOff = "TimeOff/Leave";
    public static final String Meeting = "Meeting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_made);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smApproved = (TextView) findViewById(R.id.sm_approved);
        smPending = (TextView) findViewById(R.id.sm_pending);
        leaveApproved = (TextView) findViewById(R.id.leave_approved);
        leavePending = (TextView) findViewById(R.id.leave_pending);
        meetingApproved = (TextView) findViewById(R.id.meeting_approved);
        meetingPending = (TextView) findViewById(R.id.meeting_pending);
        helpApproved = (TextView) findViewById(R.id.help_approved);
        helpPending = (TextView) findViewById(R.id.help_pending);
        schoolMaterials =(CardView) findViewById(R.id.cardSM);
        timeOff = (CardView) findViewById(R.id.cardTimeOff);
        meetings = (CardView) findViewById(R.id.cardMeetings);
        help = (CardView) findViewById(R.id.cardHelp);

        markList = new ArrayList<>();
        approveList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        school_id_extra = bundle.getString("school");

        schoolMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RequestMade.this, PendingMaterials.class);
                i.putExtra("school", school_id_extra);
                startActivity(i);

            }
        });

        timeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RequestMade.this, PendingTimeOff.class);
                i.putExtra("school", school_id_extra);
                startActivity(i);

            }
        });

        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RequestMade.this, PendingMeeting.class);
                i.putExtra("school", school_id_extra);
                startActivity(i);

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestMade.this, PendingHelpRequest.class);
                i.putExtra("school", school_id_extra);
                startActivity(i);
            }
        });

        DisplayPendingSchoolMaterials();
        DisplayApprovedSchoolMaterials();
        DisplayPendingTimeOffRequests();
        DisplayApprovedTimeOffRequests();
        DisplayPendingMeetingRequest();
        DisplayApprovedMeetingRequest();
        DisplayPendingHelpRequest();
        DisplayApprovedHelpRequest();

        //Toast.makeText(getApplicationContext(), school_id, Toast.LENGTH_LONG).show();
    }

    private void DisplayPendingSchoolMaterials() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeMaterialRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                    + " AND confirmation = " + "'"+Pending+"'" + " AND employeeRequestType = " + "'"+SchoolMaterials+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_smPending++;
                smPending.setText("("+""+count_smPending+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    private void DisplayApprovedSchoolMaterials() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeMaterialRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND approvalStatus = " + "'"+Approved+"'" + " AND employeeRequestType = " + "'"+SchoolMaterials+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_smApproved++;
                smApproved.setText("("+""+count_smApproved+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    private void DisplayPendingTimeOffRequests() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeTimeOffRequestDMs WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND confirmation = " + "'"+Pending+"'" + " AND employeeRequestType = " + "'"+TimeOff+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_leavePending++;
                leavePending.setText("("+""+count_leavePending+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    private void DisplayApprovedTimeOffRequests() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeTimeOffRequestDMs WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND approvalStatus = " + "'"+Approved+"'" + " AND employeeRequestType = " + "'"+TimeOff+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_leaveApproved++;
                leavePending.setText("("+""+count_leaveApproved+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    private void DisplayPendingMeetingRequest() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeTimeOffRequestDMs WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND confirmation = " + "'"+Pending+"'" + " AND employeeRequestType = " + "'"+Meeting+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_meetingPending++;
                meetingPending.setText("("+""+count_meetingPending+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    private void DisplayApprovedMeetingRequest() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeTimeOffRequestDMs WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND approvalStatus = " + "'"+Approved+"'" + " AND employeeRequestType = " + "'"+Meeting+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_meetingApproved++;
                meetingApproved.setText("("+""+count_meetingApproved+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void DisplayPendingHelpRequest() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncHelpRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND confirmation = " + "'"+Pending+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_helpPending++;
                helpPending.setText("("+""+count_helpPending+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void DisplayApprovedHelpRequest() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncHelpRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND approvalStatus = " + "'"+Approved+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                count_helpApproved++;
                helpApproved.setText("("+""+count_helpApproved+""+")");
                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
            }

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
