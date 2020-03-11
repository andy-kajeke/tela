package com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.TaskAdapter;
import com.planetsystems.weqa.RegularStaff.Tasks;

import java.util.ArrayList;

public class TimeTable extends AppCompatActivity {

    ListView lstView1;
    EditText searchView;
    ArrayList<TimetableModel> markList;
    TimeTableAdapter adapter;

    String dateOfDay_extra;
    String school_extra;
    String class_unit;

    private static final String id = "id";
    private static final String startTime = "startTime";
    private static final String endTime = "endTime";
    private static final String taskName = "taskName";
    private static final String taskId = "taskId";
    private static final String SyncTimeOnTasks = "SyncTimeOnTasks";
    private static final String employeeId = "employeeId";
    private static final String employeeNumber = "employeeNumber";
    private static final String employName = "employName";
    private static final String actionStatus = "actionStatus";
    private static final String tractionTime = "tractionTime";
    private static final String transactionDate = "transactionDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstView1 = (ListView) findViewById(R.id.card_listView);
        searchView = (EditText) findViewById(R.id.search);
        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        dateOfDay_extra = bundle.getString("day");
        school_extra = bundle.getString("schoolId");
        class_unit = bundle.getString("class");

        setTitle(dateOfDay_extra + "'s" + " timetable" + " for " + class_unit);

        adapter= new TimeTableAdapter(getApplicationContext(),R.layout.get_timetable, markList);

        lstView1.setAdapter(adapter);
        lstView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent editTimetable = new Intent(TimeTable.this, EditTimeTable.class);
                editTimetable.putExtra("id", markList.get(position).getId());
                editTimetable.putExtra("task_name", markList.get(position).getTaskDescription());
                editTimetable.putExtra("task_id", markList.get(position).getTask_id());
                editTimetable.putExtra("start", markList.get(position).getStartTime());
                editTimetable.putExtra("end", markList.get(position).getEndTime());
                editTimetable.putExtra("staff", markList.get(position).getEmpName());
                startActivity(editTimetable);

            }
        });

        DisplayEmployeeTasks();
    }

    private void DisplayEmployeeTasks(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncTimeTables WHERE SyncTimeTables.schoolId = " + "'"+school_extra+"'" + " AND SyncTimeTables.day = " + "'"+dateOfDay_extra+"'"
                    + " AND classUnit = " + "'"+class_unit+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while(cursor.moveToNext())
            {
                TimetableModel mark_List = new TimetableModel();
                mark_List.setId(cursor.getString(cursor.getColumnIndex(id)));
                mark_List.setTaskDescription(cursor.getString(cursor.getColumnIndex(taskName)));
                mark_List.setTask_id(cursor.getString(cursor.getColumnIndex(taskId)));
                mark_List.setStartTime(cursor.getString(cursor.getColumnIndex(startTime)));
                mark_List.setEndTime(cursor.getString(cursor.getColumnIndex(endTime)));
                mark_List.setEmpName(cursor.getString(cursor.getColumnIndex(employName)));
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
