package com.planetsystems.weqa.RegularStaff.Requests.ViewReplies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.MeetingModel;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.TimeOffModel;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class viewMeetingRequest extends AppCompatActivity {

    ListView list_pending;
    ArrayList<MeetingModel> markList;
    ViewMeetingAdapter adapter;
    String school_id_extra;
    String id_extra;

    private static final String approvalStatus = "approvalStatus";
    private static final String typeOfLeave = "typeOfLeave";
    private static final String approvalDate = "approvalDate";
    private static final String requestDate = "requestDate";
    private static final String employee = "employee";
    private static final String Seen = "Seen";
    private static final String Meeting = "Meeting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meeting_request);
        setTitle("Request Reply");

        list_pending = (ListView) findViewById(R.id.list_pendings);

        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        school_id_extra = bundle.getString("school");

        adapter= new ViewMeetingAdapter(getApplicationContext(),R.layout.view_replies, markList);

        list_pending.setAdapter(adapter);

        DisplayLeaveRequest();
    }

    private void DisplayLeaveRequest() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeTimeOffRequestDMs WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                    + " AND employeeId = " + "'"+id_extra+"'" + " AND employeeRequestType = " + "'"+Meeting+"'"
                    + " AND confirmation = " + "'"+Seen+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                MeetingModel mark_List = new MeetingModel();

                mark_List.setTypeOfLeave(cursor.getString(cursor.getColumnIndex(typeOfLeave)));
                mark_List.setEmployee(cursor.getString(cursor.getColumnIndex(employee)));
                mark_List.setApprovalStatus(cursor.getString(cursor.getColumnIndex(approvalStatus)));
                mark_List.setRequestDate(cursor.getString(cursor.getColumnIndex(requestDate)));
                mark_List.setApprovalDate(cursor.getString(cursor.getColumnIndex(approvalDate)));

                markList.add(mark_List);

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
