package com.planetsystems.weqa.RegularStaff.Requests.ViewReplies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.planetsystems.weqa.Administration.Service_Requests.Materials_.PendingSMAdapter;
import com.planetsystems.weqa.Administration.Service_Requests.Materials_.SchoolMaterialModel;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class viewSchoolMaterial extends AppCompatActivity {

    ListView list_pending;
    ArrayList<SchoolMaterialModel> markList;
    ViewReplyAdapter adapter;
    String school_id_extra;
    String id_extra;

    private static final String db_id = "id";
    private static final String comment = "comment";
    private static final String deploymentSiteId = "deploymentSiteId";
    private static final String employee = "employee";
    private static final String employeeId = "employeeId";
    private static final String itemId = "itemId";
    private static final String itemName = "itemName";
    private static final String quantity = "quantity";
    private static final String requestDate = "requestDate";
    private static final String Pending = "Pending";
    private static final String Seen = "Seen";
    private static final String approvalStatus = "approvalStatus";
    private static final String approvalDate = "approvalDate";
    private static final String dateRequired = "dateRequired";
    private static final String SchoolMaterials = "School Materials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_school_material);
        setTitle("Request Reply");

        list_pending = (ListView) findViewById(R.id.list_pendings);

        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        school_id_extra = bundle.getString("school");

        adapter= new ViewReplyAdapter(getApplicationContext(),R.layout.view_replies, markList);

        list_pending.setAdapter(adapter);

        DisplayPendingSchoolMaterials();
    }

    private void DisplayPendingSchoolMaterials() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncEmployeeMaterialRequests WHERE deploymentSiteId = " + "'"+school_id_extra+"'"
                            + " AND employeeId = " + "'"+id_extra+"'" + " AND employeeRequestType = " + "'"+SchoolMaterials+"'"
                    + " AND confirmation = " + "'"+Seen+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while (cursor.moveToNext()) {
                SchoolMaterialModel mark_List = new SchoolMaterialModel();
                mark_List.setDb_id(cursor.getString(cursor.getColumnIndex(db_id)));
                mark_List.setDeploymentSiteId(cursor.getString(cursor.getColumnIndex(deploymentSiteId)));
                mark_List.setEmployee(cursor.getString(cursor.getColumnIndex(employee)));
                mark_List.setEmployeeId(cursor.getString(cursor.getColumnIndex(employeeId)));
                mark_List.setItemId(cursor.getString(cursor.getColumnIndex(itemId)));
                mark_List.setItemName(cursor.getString(cursor.getColumnIndex(itemName)));
                mark_List.setApprovalStatus(cursor.getString(cursor.getColumnIndex(approvalStatus)));
                mark_List.setRequestDate(cursor.getString(cursor.getColumnIndex(requestDate)));
                mark_List.setApprovalDate(cursor.getString(cursor.getColumnIndex(approvalDate)));
                mark_List.setQuantity(cursor.getString(cursor.getColumnIndex(quantity)));
                mark_List.setSm_comment(cursor.getString(cursor.getColumnIndex(comment)));
                mark_List.setDateRequired(cursor.getString(cursor.getColumnIndex(dateRequired)));
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
