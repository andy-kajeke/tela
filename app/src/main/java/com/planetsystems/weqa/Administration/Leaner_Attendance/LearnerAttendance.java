package com.planetsystems.weqa.Administration.Leaner_Attendance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.planetsystems.weqa.R;
import java.util.ArrayList;

public class LearnerAttendance extends AppCompatActivity {

    ListView lstView1;
    ArrayList<Get_Classes> markList;
    Get_Classes_Adapter adapter;
    String  school_id_extra;
    String HT_Id;

    private static final String code = "code";
    private static final String id = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Classes");

        lstView1 = (ListView) findViewById(R.id.list_classes);

        Bundle bundle = getIntent().getExtras();
        HT_Id = bundle.getString("id");
        school_id_extra = bundle.getString("school");


            markList = new ArrayList<>();

//            Toast.makeText(this, ""+markList.size(), Toast.LENGTH_SHORT).show();
            adapter= new Get_Classes_Adapter(getApplicationContext(),R.layout.get_classes,markList);

            lstView1.setAdapter(adapter);
            lstView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),""+markList.get(position).getId(),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Learner.class);
                    i.putExtra("id",markList.get(position).getId());
                    i.putExtra("name",markList.get(position).getCode());
                    i.putExtra("school", school_id_extra);
                    i.putExtra("admin", HT_Id);
                    startActivity(i);

                }
            });

        ListDeploymentUnits();
    }

    private void ListDeploymentUnits(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from DeploymentUnits WHERE deploymentSite_id = " + "'"+school_id_extra+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while(cursor.moveToNext())
            {
                Get_Classes mark_List = new Get_Classes();

                mark_List.setCode(cursor.getString(cursor.getColumnIndex(code)));
                mark_List.setId(cursor.getString(cursor.getColumnIndex(id)));
                markList.add(mark_List);

            }

        }
        finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            if(db != null && db.isOpen()){
                db.close();
            }
        }

    }

}

