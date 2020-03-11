package com.planetsystems.weqa.Administration.Service_Requests.Materials_;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class PendingMaterials extends AppCompatActivity {

    ListView list_pending;
    ArrayList<SchoolMaterialModel> markList;
    PendingSMAdapter adapter;
    String school_id_extra;

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
    private static final String Approved = "Approved";
    private static final String confirmation = "confirmation";
    private static final String dateRequired = "dateRequired";
    private static final String SchoolMaterials = "School Materials";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_materials);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_pending = (ListView) findViewById(R.id.list_pendings);

        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        school_id_extra = bundle.getString("school");

        adapter= new PendingSMAdapter(getApplicationContext(),R.layout.get_pending_sm, markList);

        list_pending.setAdapter(adapter);
        list_pending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(PendingMaterials.this, ApproveMaterial.class);
                i.putExtra("item_id", markList.get(position).getItemId());
                i.putExtra("item_name", markList.get(position).getItemName());
                i.putExtra("db_id", markList.get(position).getDb_id());
                i.putExtra("emp_id", markList.get(position).getEmployeeId());
                i.putExtra("emp_name", markList.get(position).getEmployee());
                i.putExtra("item_qnty", markList.get(position).getQuantity());
                i.putExtra("reason", markList.get(position).getSm_comment());
                i.putExtra("requestedOn", markList.get(position).getRequestDate());
                i.putExtra("neededOn", markList.get(position).getDateRequired());
                i.putExtra("school", school_id_extra);
                startActivity(i);
            }
        });

        DisplayPendingSchoolMaterials();

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
                SchoolMaterialModel mark_List = new SchoolMaterialModel();
                mark_List.setDb_id(cursor.getString(cursor.getColumnIndex(db_id)));
                mark_List.setDeploymentSiteId(cursor.getString(cursor.getColumnIndex(deploymentSiteId)));
                mark_List.setEmployee(cursor.getString(cursor.getColumnIndex(employee)));
                mark_List.setEmployeeId(cursor.getString(cursor.getColumnIndex(employeeId)));
                mark_List.setItemId(cursor.getString(cursor.getColumnIndex(itemId)));
                mark_List.setItemName(cursor.getString(cursor.getColumnIndex(itemName)));
                mark_List.setRequestDate(cursor.getString(cursor.getColumnIndex(requestDate)));
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
