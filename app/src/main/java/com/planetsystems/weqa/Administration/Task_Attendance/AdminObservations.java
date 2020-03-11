package com.planetsystems.weqa.Administration.Task_Attendance;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.Tasks;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdminObservations extends AppCompatActivity {

    ListView lstView1;
    String test="";
    String datetoday;
    TextView name, staffID;
    Button submit;
    ArrayList<Tasks> markList;
    ConfirmationAdapter adapter;
    String id_extra, name_extra, admin_extra, dateOfDay_extra;

    private static final String Present = "Present";

    private static final String id = "id";
    private static final String taskId = "taskId";
    private static final String taskName = "taskName";
    private static final String startTime = "startTime";
    private static final String endTime = "endTime";
    private static final String employeeId = "employeeId";
    private static final String employeeNumber = "employeeNumber";
    private static final String supervisionStatus = "supervisionStatus";
    private static final String supervisorId = "supervisorId";
    private static final String taskDescription = "taskDescription";
    private static final String comment = "comment";
    private static final String transactionDate = "transactionDate";
    private static final String SyncConfirmTimeOnTaskAttendances = "SyncConfirmTimeOnTaskAttendances";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_observations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Admin Observations");

        lstView1 =(ListView)findViewById(R.id.card_listView);
        name = (TextView) findViewById(R.id.namexx);
        staffID = (TextView) findViewById(R.id.staffId);
        submit=(Button)findViewById(R.id.submit);

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        datetoday = dateFormat.format(date);


        Bundle bundle = getIntent().getExtras();
        admin_extra = bundle.getString("admin_id");
        dateOfDay_extra = bundle.getString("date");
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");

        name.append(name_extra);
        staffID.append(id_extra);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(AdminObservations.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to submit these observations?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                PostObservationsToTable();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });

            markList = new ArrayList<>();
            //Toast.makeText(this, ""+markList.size(), Toast.LENGTH_SHORT).show();
            adapter= new ConfirmationAdapter(getApplicationContext(),R.layout.task_item_head,markList);

            lstView1.setAdapter(adapter);
            lstView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    markList.get(position).setStatus("absent");
                    Toast.makeText(getApplicationContext(),""+markList.get(position).getStatus(),Toast.LENGTH_SHORT).show();
                }
            });

        DisplayEmployeeTasks();
    }

    private void DisplayEmployeeTasks(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from SyncTimeOnTasks WHERE employeeNumber = " + "'"+id_extra+"'"
                    + " AND transactionDate = " + "'"+datetoday+"'" + " AND actionStatus = " + "'"+Present+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeOnTasks", null);

            while(cursor.moveToNext())
            {
                Tasks mark_List = new Tasks();
                mark_List.setId(cursor.getString(cursor.getColumnIndex(taskId)));
                mark_List.setTaskDescription(cursor.getString(cursor.getColumnIndex(taskName)));
                mark_List.setStartTime(cursor.getString(cursor.getColumnIndex(startTime)));
                mark_List.setEndTime(cursor.getString(cursor.getColumnIndex(endTime)));
                mark_List.setTaughtNotTaught("Taught");
                mark_List.setInTime("In Time");
                mark_List.setComment("NA");
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

    private void PostObservationsToTable(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Cursor _cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            _cursor = db.rawQuery("select *from SyncConfirmTimeOnTaskAttendances where employeeNumber = " + id_extra
                    + " " + " AND transactionDate = " + "'"+dateOfDay_extra+"'",null);

            if(_cursor.moveToNext())
            {
                Toast.makeText(getApplicationContext(), "Already Submitted", Toast.LENGTH_LONG).show();
            }
            else {

                for (Tasks List:markList)
                {
                    // Adding contentValues to respective columns in the SyncConfirmTimeOnTaskAttendances table
                    ContentValues confirmTaskAttendance = new ContentValues();
                    confirmTaskAttendance.put(id, GenerateRandomString.randomString(15));
                    confirmTaskAttendance.put(employeeId, id_extra);
                    confirmTaskAttendance.put(employeeNumber, id_extra);
                    confirmTaskAttendance.put(taskId, List.getId());
                    confirmTaskAttendance.put(taskDescription, List.getTaskDescription());
                    confirmTaskAttendance.put(startTime, List.getStartTime());
                    confirmTaskAttendance.put(endTime, List.getEndTime());
                    confirmTaskAttendance.put(transactionDate, dateOfDay_extra);
                    confirmTaskAttendance.put(supervisionStatus, List.getTaughtNotTaught());
                    confirmTaskAttendance.put(comment, List.getInTime());
                    confirmTaskAttendance.put(supervisorId, admin_extra);
                    db.insert(SyncConfirmTimeOnTaskAttendances, null, confirmTaskAttendance);

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
}

