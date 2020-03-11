package com.planetsystems.weqa.Administration.Service_Requests.Help_;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.ApproveMeetings;
import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.MeetingModel;
import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.PendingMeeting;
import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.PendingMeetingAdapter;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class PendingHelpRequest extends AppCompatActivity {

    ListView list_pending;
    ArrayList<HelpModel> markList;
    PendingHelpAdapter adapter;
    String school_id_extra;

    public static final String id = "id";
    public static final String staffCode = "staffCode";
    public static final String confirmation = "confirmation";
    public static final String comment = "comment";
    public static final String deploymentSiteId = "deploymentSiteId";
    public static final String helpCategory = "helpCategory";
    public static final String priority ="priority";
    public static final String requestedDate = "requestedDate";
    public static final String Pending = "Pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_help_request);

        setTitle("Pending Help Requests");

        list_pending = (ListView) findViewById(R.id.list_pendings);

        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        school_id_extra = bundle.getString("school");

        adapter= new PendingHelpAdapter(getApplicationContext(),R.layout.get_pending_timeoff, markList);

        list_pending.setAdapter(adapter);
        list_pending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(PendingHelpRequest.this, ApproveHelpRequest.class);
                i.putExtra("staffCode", markList.get(position).getStaffCode());
                i.putExtra("db_id", markList.get(position).getId());
                i.putExtra("help", markList.get(position).getHelpCategory());
                i.putExtra("priority", markList.get(position).getPriority());
                i.putExtra("reason", markList.get(position).getComment());
                i.putExtra("requestedOn", markList.get(position).getRequestedDate());
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
            cursor = db.rawQuery("select * from SyncHelpRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND confirmation = " + "'"+Pending+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                HelpModel mark_List = new HelpModel();
                mark_List.setId(cursor.getString(cursor.getColumnIndex(id)));
                mark_List.setDeploymentSiteId(cursor.getString(cursor.getColumnIndex(deploymentSiteId)));
                mark_List.setStaffCode(cursor.getString(cursor.getColumnIndex(staffCode)));
                mark_List.setHelpCategory(cursor.getString(cursor.getColumnIndex(helpCategory)));
                mark_List.setPriority(cursor.getString(cursor.getColumnIndex(priority)));
                mark_List.setComment(cursor.getString(cursor.getColumnIndex(comment)));
                mark_List.setRequestedDate(cursor.getString(cursor.getColumnIndex(requestedDate)));
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
