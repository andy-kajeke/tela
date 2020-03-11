package com.planetsystems.weqa.RegularStaff.Requests.ViewReplies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.planetsystems.weqa.Administration.Service_Requests.Help_.HelpModel;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.TimeOffModel;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class viewHelpRequest extends AppCompatActivity {

    ListView list_pending;
    ArrayList<HelpModel> markList;
    ViewHelpAdapter adapter;
    String school_id_extra;
    String id_extra;

    private static final String approvalStatus = "approvalStatus";
    private static final String helpCategory = "helpCategory";
    private static final String approvalDate = "approvalDate";
    private static final String requestedDate = "requestedDate";
    private static final String staffCode = "staffCode";
    private static final String Seen = "Seen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_help_request);
        setTitle("Request Reply");

        list_pending = (ListView) findViewById(R.id.list_pendings);

        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        school_id_extra = bundle.getString("school");

        adapter= new ViewHelpAdapter(getApplicationContext(),R.layout.view_replies, markList);

        list_pending.setAdapter(adapter);

        DisplayHelpRequest();
    }

    private void DisplayHelpRequest() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncHelpRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                    + " AND staffCode = " + "'"+id_extra+"'" + " AND confirmation = " + "'"+Seen+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                HelpModel mark_List = new HelpModel();

                mark_List.setHelpCategory(cursor.getString(cursor.getColumnIndex(helpCategory)));
                mark_List.setStaffCode(cursor.getString(cursor.getColumnIndex(staffCode)));
                mark_List.setApprovalStatus(cursor.getString(cursor.getColumnIndex(approvalStatus)));
                mark_List.setRequestedDate(cursor.getString(cursor.getColumnIndex(requestedDate)));
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
