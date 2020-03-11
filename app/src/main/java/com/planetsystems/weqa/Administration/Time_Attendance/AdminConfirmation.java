package com.planetsystems.weqa.Administration.Time_Attendance;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminConfirmation extends AppCompatActivity {

    ProgressDialog dialog;
    int count=0;
    int count2 =0;
    //confirmation
    Dialog outDialog;
    TextView close,Status,Name;
    Button btnFollow;
    EditText staffId, staff_comment;
    CheckBox norm,oth;

    String id_extra;
    String name_extra;
    String HT_id_extra;
    String dayOfTheWeek;
    String dateOfTheWeek;
    private static final String id = "id";
    private static final String SyncConfirmTimeOnSiteAttendances = "SyncConfirmTimeOnSiteAttendances";
    private static final String employeeId = "employeeId";
    private static final String employeeNo = "employeeNo";
    private static final String supervisionDate = "supervisionDate";
    private static final String supervisionDay = "supervisionDay";
    private static final String supervisionStatus = "supervisionStatus";
    private static final String supervisorComment = "supervisorComment";
    private static final String supervisorId = "supervisorId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Attendance Confirmation");

        close =(TextView) findViewById(R.id.txclose);
        Name =(TextView) findViewById(R.id.staffName);
        Status =(TextView) findViewById(R.id.status);
        btnFollow =(Button) findViewById(R.id.out);
        staffId =(EditText) findViewById(R.id.staff_id);
        staff_comment =(EditText) findViewById(R.id.comment);
        norm =(CheckBox) findViewById(R.id.normal) ;
        oth =(CheckBox) findViewById(R.id.other) ;

        Bundle bundle = getIntent().getExtras();
        HT_id_extra = bundle.getString("admin");
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");

        Name.setText(name_extra);
        staffId.setText(id_extra);

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        dateOfTheWeek = dateFormat.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        norm.setChecked(true);
        Status.setText("PRESENT");
        //staff_comment.setFocusableInTouchMode(false);

        oth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    Status.setText("Absent");
                    norm.setChecked(false);
                }

            }
        });
        norm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    Status.setText("Present");
                    oth.setChecked(false);
                }

            }
        });
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staffId.getText().toString().equalsIgnoreCase("")){
                    staffId.setError("Id Missing!");
                }else{
                    new AlertDialog.Builder(AdminConfirmation.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to submit this?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    PostToSyncConfirmTimeOnSiteAttendances();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

    }

    private void PostToSyncConfirmTimeOnSiteAttendances(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", 1, null);
            cursor = db.rawQuery("select *from SyncConfirmTimeOnSiteAttendances where employeeNo = " + id_extra
                    + " " + " AND supervisionDate = " + "'"+dateOfTheWeek+"'",null);

            if (cursor.moveToNext()){

                Toast.makeText(getApplicationContext(), "Already Submitted", Toast.LENGTH_LONG).show();

            }else {

                ContentValues confirm = new ContentValues();
                confirm.put(id, GenerateRandomString.randomString(15));
                confirm.put(employeeId, id_extra);
                confirm.put(employeeNo, id_extra);
                confirm.put(supervisionDate, dateOfTheWeek);
                confirm.put(supervisionDay, dayOfTheWeek);
                confirm.put(supervisionStatus, Status.getText().toString());
                confirm.put(supervisorComment, staff_comment.getText().toString());
                confirm.put(supervisorId, HT_id_extra);
                db.insert(SyncConfirmTimeOnSiteAttendances, null, confirm);

                Toast.makeText(getApplicationContext(), "Submitted successfully..", Toast.LENGTH_LONG).show();

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
