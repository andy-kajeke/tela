package com.planetsystems.weqa.RegularStaff;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.Clockin_ClockOut;
import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.My_Status.MyStatus;
import com.planetsystems.weqa.RegularStaff.Requests.MakeRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Emp_Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    int count =0;
    ListView lstView1;
    TextView datetoday;
    TextView emp_Name;
    TextView emp_Id;
    TextClock submitTime;
    Button submit, selfmenu;
    ArrayList<Tasks> markList;
    TaskAdapter adapter;
    String emp_id_extra;
    String emp_name_extra;
    String dayOfTheWeek;
    String dateOfDay_extra;
    String school_extra;

    private static final String id = "id";
    private static final String startTime = "startTime";
    private static final String endTime = "endTime";
    private static final String taskName = "taskName";
    private static final String taskId = "taskId";
    private static final String SyncTimeOnTasks = "SyncTimeOnTasks";
    private static final String employeeId = "employeeId";
    private static final String employeeNumber = "employeeNumber";
    private static final String actionStatus = "actionStatus";
    private static final String tractionTime = "tractionTime";
    private static final String transactionDate = "transactionDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tela");

        lstView1 = (ListView) findViewById(R.id.card_listView);
        emp_Name = (TextView) findViewById(R.id.namexx);
        emp_Id = (TextView) findViewById(R.id.staffId);
        datetoday = (TextView) findViewById(R.id.datetoday);
        submit = (Button) findViewById(R.id.submit);
        selfmenu = (Button) findViewById(R.id.menuBtn);

        Date currentTime = Calendar.getInstance().getTime();
        datetoday.setText("" + currentTime.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        //submitTime = new TextClock();
        markList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        emp_id_extra = bundle.getString("id");
        emp_name_extra = bundle.getString("name");
        dateOfDay_extra = bundle.getString("dateOfDay");
        school_extra = bundle.getString("school_id");

        emp_Name.append(emp_name_extra);
        emp_Id.append(emp_id_extra);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Emp_Home.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to submit your attendance?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                PostToSyncTimeOnTasks();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        selfmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Emp_Home.this, v);
                popup.setOnMenuItemClickListener(Emp_Home.this);
                popup.inflate(R.menu.popup_menu);
                popup.show();
            }
        });

        adapter= new TaskAdapter(getApplicationContext(),R.layout.task_item, markList);

        lstView1.setAdapter(adapter);
        lstView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

           }
        });

        DisplayEmployeeTasks();
    }

    private void DisplayEmployeeTasks(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncTimeTables WHERE SyncTimeTables.employeeNo = " + emp_id_extra + " AND SyncTimeTables.day = " + "'"+dayOfTheWeek+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while(cursor.moveToNext())
            {
                Tasks mark_List = new Tasks();
                mark_List.setTaskDescription(cursor.getString(cursor.getColumnIndex(taskName)));
                mark_List.setId(cursor.getString(cursor.getColumnIndex(taskId)));
                mark_List.setStartTime(cursor.getString(cursor.getColumnIndex(startTime)));
                mark_List.setEndTime(cursor.getString(cursor.getColumnIndex(endTime)));
                mark_List.setStatus("Present");
                mark_List.setComment("NA");
                markList.add(mark_List);
                count++;

                //Toast.makeText(getApplicationContext(), "Received:"+count+"", Toast.LENGTH_LONG).show();
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

    // save employee confirmation to syncTimeOneTasks table
    private void PostToSyncTimeOnTasks(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Cursor _cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            _cursor = db.rawQuery("select *from SyncTimeOnTasks where employeeNumber = " + emp_id_extra + " " +
                    " AND transactionDate = " + "'"+dateOfDay_extra+"'",null);

            if(_cursor.moveToNext())
            {
                Toast.makeText(getApplicationContext(), "Already Submitted", Toast.LENGTH_LONG).show();
            }
            else {

                for (Tasks List:markList)
                {
                    // Adding contentValues to respective columns in the SyncTimeOnTasks table
                    ContentValues checkIn = new ContentValues();
                    checkIn.put(id, GenerateRandomString.randomString(15));
                    checkIn.put(employeeId, emp_id_extra);
                    checkIn.put(employeeNumber, emp_id_extra);
                    checkIn.put(taskId, List.getId());
                    checkIn.put(taskName, List.getTaskDescription());
                    checkIn.put(startTime, List.getStartTime());
                    checkIn.put(endTime, List.getEndTime());
                    checkIn.put(transactionDate, dateOfDay_extra);
                    checkIn.put(actionStatus, List.getStatus());
                    db.insert(SyncTimeOnTasks, null, checkIn);

                    Toast.makeText(getApplicationContext(), "Submitted successfully..", Toast.LENGTH_LONG).show();
                }

            }

        }
        finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            if(_cursor != null && !_cursor.isClosed()){
                _cursor.close();
            }
            if(db != null && db.isOpen()){
                db.close();
            }
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.request) {

            Intent intent = new Intent(Emp_Home.this,MakeRequests.class);
            intent.putExtra("name", emp_name_extra);
            intent.putExtra("school_id", school_extra);
            intent.putExtra("id", emp_id_extra);
            startActivity(intent);
            //return true;
        }
        else if (id == R.id.myStatus) {

            Intent intent = new Intent(Emp_Home.this, MyStatus.class);
            intent.putExtra("name", emp_name_extra);
            intent.putExtra("id", emp_id_extra);
            startActivity(intent);
            //return true;
        }
        else if (id == R.id.Logout) {
            Intent intent = new Intent(Emp_Home.this,Clockin_ClockOut.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Emp_Home.this.finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
        //return false;
    }
}
