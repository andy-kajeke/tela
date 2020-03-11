package com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planetsystems.weqa.Administration.School_Updates.Edit_StaffList.Edit_Individual_Info;
import com.planetsystems.weqa.R;

public class EditTimeTable extends AppCompatActivity {

    EditText task_Name,staff_name;
    EditText startTym,endTym;
    CardView submitChanges;

    String id_extra;
    String task_Name_extra;
    String taskId_extra;
    String startTime_extra;
    String endTime_extra;
    String staffName_extra;

    private static final String id = "id";
    private static final String taskName = "taskName";
    private static final String startTime = "startTime";
    private static final String endTime = "endTime";
    private static final String employName = "employName";
    private static final String SyncTimeTables = "SyncTimeTables";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        task_Name = (EditText) findViewById(R.id.taskName);
        startTym = (EditText) findViewById(R.id.startTime);
        endTym = (EditText) findViewById(R.id.endTime);
        staff_name = (EditText) findViewById(R.id.staffName);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        taskId_extra = bundle.getString("task_id");
        task_Name_extra = bundle.getString("task_name");
        startTime_extra = bundle.getString("start");
        endTime_extra = bundle.getString("end");
        staffName_extra = bundle.getString("staff");
        submitChanges = (CardView) findViewById(R.id.submit_changes);

        task_Name.append(task_Name_extra);
        startTym.append(startTime_extra);
        endTym.append(endTime_extra);
        staff_name.append(staffName_extra);

        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditTimeTable.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to submit these changes?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                PostChangeMade();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    private void PostChangeMade(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            // Updating contentValues to respective columns in the SyncEmployeeTimeOffRequestDMs table
            ContentValues editTimeTable = new ContentValues();
            editTimeTable.put(taskName, task_Name.getText().toString());
            editTimeTable.put(startTime, startTym.getText().toString());
            editTimeTable.put(endTime, endTym.getText().toString());
            editTimeTable.put(employName, staff_name.getText().toString());

            //db.update(SyncTimeTables, editTimeTable,"id = ?",new String[] { id_extra });
            db.update(SyncTimeTables, editTimeTable,"id = " + "'"+id_extra+"'",null);

            Toast.makeText(getApplicationContext(), "Submitted successfully..", Toast.LENGTH_LONG).show();

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
