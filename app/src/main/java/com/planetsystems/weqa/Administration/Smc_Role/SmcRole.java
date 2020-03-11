package com.planetsystems.weqa.Administration.Smc_Role;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

public class SmcRole extends AppCompatActivity {

    TextView status;
    Button btnSubmit;
    EditText staffonsite, staffTeaching_,staffNotTeaching_, staff_comment;
    Switch primo1,primo2,primo3,primo4,primo5,primo6,primo7;
    RadioButton h_present, h_absent;
    ProgressDialog dialog;
    Dialog outDialog;
    String P1;
    String P2;
    String P3;
    String P4;
    String P5;
    String P6;
    String P7;
    String id_extra, school_extra;

    public static final String id = "id";
    public static final String deploymentUnitId = "deploymentUnitId";
    public static final String headTeacherPresent = "headTeacherPresent";
    public static final String p1 = "p1";
    public static final String p2 = "p2";
    public static final String p3 = "p3";
    public static final String p4 = "p4";
    public static final String p5 = "p5";
    public static final String p6 = "p6";
    public static final String p7 = "p7";
    public static final String smcCode = "smcCode";
    public static final String staffNotTeaching = "staffNotTeaching";
    public static final String staffPresent = "staffPresent";
    public static final String staffTeaching = "staffTeaching";
    public static final String SyncSMCs = "SyncSMCs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        status = (TextView) findViewById(R.id.status);
        btnSubmit = (Button) findViewById(R.id.out);
        staffonsite = (EditText) findViewById(R.id.staffPresent);
        staffTeaching_ = (EditText) findViewById(R.id.teaching);
        staffNotTeaching_ = (EditText) findViewById(R.id.notTeaching);
        h_present = (RadioButton) findViewById(R.id.present);
        h_absent = (RadioButton) findViewById(R.id.absent);
        //staff_comment = (EditText) findViewById(R.id.comment);
        primo1 = (Switch) findViewById(R.id.primo_1);
        primo2 = (Switch) findViewById(R.id.primo_2);
        primo3 = (Switch) findViewById(R.id.primo_3);
        primo4 = (Switch) findViewById(R.id.primo_4);
        primo5 = (Switch) findViewById(R.id.primo_5);
        primo6 = (Switch) findViewById(R.id.primo_6);
        primo7 = (Switch) findViewById(R.id.primo_7);

        outDialog = new Dialog(this);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("smc_id");
        school_extra = bundle.getString("school");

        h_present.setChecked(true);
        status.setText("PRESENT");

        primo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P1 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P1 = "TEACHER_PRESENT";
                }
            }
        });
        primo2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P2 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P2 = "TEACHER_PRESENT";
                }
            }
        });
        primo3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P3 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P3 = "TEACHER_PRESENT";
                }
            }
        });
        primo4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P4 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P4 = "TEACHER_PRESENT";
                }
            }
        });
        primo5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P5 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P5 = "TEACHER_PRESENT";
                }
            }
        });
        primo6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P6 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P6 = "TEACHER_PRESENT";
                }
            }
        });
        primo7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                    Toast.makeText(SmcRole.this,"Teacher Absent",Toast.LENGTH_SHORT).show();

                    P7 = "TEACHER_ABSENT";

                } else {

                    Toast.makeText(SmcRole.this,"Teacher Present",Toast.LENGTH_SHORT).show();

                    P7 = "TEACHER_PRESENT";
                }
            }
        });

        h_present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                } else {
                    h_absent.setChecked(false);
                    status.setText("PRESENT");
                }
            }
        });

        h_absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {

                } else {

                    h_present.setChecked(false);
                    status.setText("ABSENT");

                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staffTeaching_.getText().toString().equalsIgnoreCase("") &&
                        staffNotTeaching_.getText().toString().equalsIgnoreCase("")&&
                        staffonsite.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Parameter Missing",Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(SmcRole.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to submit?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SMCs();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

    }

    private void SMCs(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            // Adding contentValues to respective columns in the SyncEmployeeTimeOffRequestDMs table
            ContentValues smc = new ContentValues();
            smc.put(id, GenerateRandomString.randomString(15));
            smc.put(smcCode, id_extra);;
            smc.put(deploymentUnitId, school_extra);
            smc.put(staffNotTeaching, staffNotTeaching_.getText().toString());
            smc.put(staffTeaching, staffTeaching_.getText().toString());
            smc.put(staffPresent, staffonsite.getText().toString());
            smc.put(headTeacherPresent, status.getText().toString());
            smc.put(p1, P1);
            smc.put(p2, P2);
            smc.put(p3, P3);
            smc.put(p4, P4);
            smc.put(p5, P5);
            smc.put(p6, P6);
            smc.put(p7, P7);
            db.insert(SyncSMCs, null, smc);

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
