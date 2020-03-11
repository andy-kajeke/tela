package com.planetsystems.weqa.RegularStaff.Requests;

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
import android.widget.Toast;

import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class ListSchoolMaterial extends AppCompatActivity {

    ListView material;
    ArrayList<MaterialModel> markList;
    MaterialAdapter adapter;
    String id_extra;
    String school_extra;
    String name_extra;

    private static final String code = "code";
    private static final String id = "id";
    private static final String itemName = "itemName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_school_material);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Item");

        material = (ListView) findViewById(R.id.material_listView);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");
        school_extra = bundle.getString("school_id");


            markList = new ArrayList<>();

            Toast.makeText(this, ""+markList.size(), Toast.LENGTH_SHORT).show();
            adapter = new MaterialAdapter(getApplicationContext(),R.layout.get_materials,markList);

            material.setAdapter(adapter);
            material.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),markList.get(position).getItemCode(),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), RequestSchoolMaterials.class);
                    i.putExtra("emp_id", id_extra);
                    i.putExtra("emp_name", name_extra);
                    i.putExtra("item_id", markList.get(position).getItemId());
                    i.putExtra("item_name", markList.get(position).getItemName());
                    i.putExtra("school_id", school_extra);
                    startActivity(i);

                }
            });

            ListSchoolMaterials();
    }

    private void ListSchoolMaterials(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from Items ",null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while(cursor.moveToNext())
            {
                MaterialModel mark_List = new MaterialModel();
                mark_List.setItemCode(cursor.getString(cursor.getColumnIndex(code)));
                mark_List.setItemName(cursor.getString(cursor.getColumnIndex(itemName)));
                mark_List.setItemId(cursor.getString(cursor.getColumnIndex(id)));

                markList.add(mark_List);

                //Toast.makeText(getApplicationContext(), dayOfTheWeek, Toast.LENGTH_LONG).show();
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

