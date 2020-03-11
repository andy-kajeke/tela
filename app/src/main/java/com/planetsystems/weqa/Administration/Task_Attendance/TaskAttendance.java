package com.planetsystems.weqa.Administration.Task_Attendance;

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
import android.widget.TextView;

import com.planetsystems.weqa.Administration.Time_Attendance.AttendanceAdapter;
import com.planetsystems.weqa.Administration.Time_Attendance.attendanceModel;
import com.planetsystems.weqa.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskAttendance extends AppCompatActivity {

    ListView lstView1;
    TextView datetoday;
    ArrayList<attendanceModel> markList;
    AttendanceAdapter adapter;
    String HT_Id;
    String checkIn_date;
    String checkIn_schoolId;
    private static final String empFirstName = "empFirstName";
    private static final String empLastName = "empLastName";
    private static final String employeeNo = "employeeNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Task Attendance");

        lstView1 = (ListView) findViewById(R.id.card_listView);
        datetoday = (TextView) findViewById(R.id.datetoday);

        Date currentTime = Calendar.getInstance().getTime();

        Bundle bundle = getIntent().getExtras();
        HT_Id = bundle.getString("id");
        checkIn_date = bundle.getString("date");
        checkIn_schoolId = bundle.getString("school");

        markList = new ArrayList<>();
        //Toast.makeText(this, ""+markList.size(), Toast.LENGTH_SHORT).show();
        adapter = new AttendanceAdapter(getApplicationContext(), R.layout.time_attendance_item, markList);

        lstView1.setAdapter(adapter);
        lstView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),""+markList.get(position).getStatus(),Toast.LENGTH_SHORT).show();
              Intent i = new Intent(getApplicationContext(), AdminObservations.class);
              i.putExtra("admin_id", HT_Id);
              i.putExtra("date", checkIn_date);
              i.putExtra("id",markList.get(position).getId());
              i.putExtra("name",markList.get(position).getName());
              startActivity(i);

            }
        });
        DisplayClockedInEmployee();
    }

    private void DisplayClockedInEmployee() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncClockIns WHERE clockInDate = " + "'" + checkIn_date + "'" +
                    " AND schoolId = " + "'" + checkIn_schoolId + "'", null);
            //cursor = db.rawQuery("select * from SyncClockIns", null);

            while (cursor.moveToNext()) {
                attendanceModel mark_List = new attendanceModel();
                mark_List.setId(cursor.getString(cursor.getColumnIndex(employeeNo)));
                mark_List.setName(cursor.getString(cursor.getColumnIndex(empFirstName))
                        + " " + cursor.getString(cursor.getColumnIndex(empLastName)));
                markList.add(mark_List);

                //Toast.makeText(getApplicationContext(), dayOfTheWeek, Toast.LENGTH_LONG).show();

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
