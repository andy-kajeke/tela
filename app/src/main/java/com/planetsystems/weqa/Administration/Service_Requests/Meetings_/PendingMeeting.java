package com.planetsystems.weqa.Administration.Service_Requests.Meetings_;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.ApproveTimeOff;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.PendingTimeOff;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.PendingTimeOffAdapter;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.TimeOffModel;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class PendingMeeting extends AppCompatActivity {

    ListView list_pending;
    ArrayList<MeetingModel> markList;
    PendingMeetingAdapter adapter;
    String school_id_extra;

    private static final String db_id = "id";
    private static final String deploymentSiteId = "deploymentSiteId";
    private static final String employee = "employee";
    private static final String employeeId = "employeeId";
    private static final String typeOfLeave = "typeOfLeave";
    private static final String requestDate = "requestDate";
    private static final String fromDate = "fromDate";
    private static final String toDate = "toDate";
    private static final String fromTime = "fromTime";
    private static final String toTime = "toTime";
    private static final String generalComment = "generalComment";
    private static final String Pending = "Pending";
    private static final String Meeting = "Meeting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_meeting);

        setTitle("Pending Meeting Requests");

        list_pending = (ListView) findViewById(R.id.list_pendings);

        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        school_id_extra = bundle.getString("school");

        adapter= new PendingMeetingAdapter(getApplicationContext(),R.layout.get_pending_timeoff, markList);

        list_pending.setAdapter(adapter);
        list_pending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(PendingMeeting.this, ApproveMeetings.class);
                i.putExtra("leave", markList.get(position).getTypeOfLeave());
                i.putExtra("db_id", markList.get(position).getDb_id());
                i.putExtra("emp_id", markList.get(position).getEmployee_Id());
                i.putExtra("emp_name", markList.get(position).getEmployee());
                i.putExtra("startDate", markList.get(position).getFromDate());
                i.putExtra("endDate", markList.get(position).getToDate());
                i.putExtra("startTime", markList.get(position).getFromTime());
                i.putExtra("endTime", markList.get(position).getToTime());
                i.putExtra("reason", markList.get(position).getGeneralComment());
                i.putExtra("requestedOn", markList.get(position).getRequestDate());
                i.putExtra("school", school_id_extra);
                startActivity(i);
            }
        });

        DisplayPendingMeetings();

    }

    private void DisplayPendingMeetings() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeTimeOffRequestDMs WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND confirmation = " + "'"+Pending+"'" + " AND employeeRequestType = " + "'"+Meeting+"'"
                    , null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                MeetingModel mark_List = new MeetingModel();
                mark_List.setDb_id(cursor.getString(cursor.getColumnIndex(db_id)));
                mark_List.setDeploymentSiteId(cursor.getString(cursor.getColumnIndex(deploymentSiteId)));
                mark_List.setEmployee(cursor.getString(cursor.getColumnIndex(employee)));
                mark_List.setEmployee_Id(cursor.getString(cursor.getColumnIndex(employeeId)));
                mark_List.setTypeOfLeave(cursor.getString(cursor.getColumnIndex(typeOfLeave)));
                mark_List.setRequestDate(cursor.getString(cursor.getColumnIndex(requestDate)));
                mark_List.setFromDate(cursor.getString(cursor.getColumnIndex(fromDate)));
                mark_List.setToDate(cursor.getString(cursor.getColumnIndex(toDate)));
                mark_List.setFromTime(cursor.getString(cursor.getColumnIndex(fromTime)));
                mark_List.setToTime(cursor.getString(cursor.getColumnIndex(toTime)));
                mark_List.setGeneralComment(cursor.getString(cursor.getColumnIndex(generalComment)));
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
