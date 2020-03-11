package com.planetsystems.weqa.Administration.School_Updates.Edit_StaffList;

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
import android.widget.EditText;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

public class Add_New_Staff extends AppCompatActivity {

    EditText edit_fName, edit_lName;
    EditText edit_initials, edit_phone_No;
    EditText edit_email, edit_nationalID, edit_gender;
    CardView addUser;

    String school_extra;

    private static final String id = "id";
    private static final String emailAddress = "emailAddress";
    private static final String employeeNumber = "employeeNumber";
    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String initials = "initials";
    private static final String licensed = "licensed";
    private static final String nationalId = "nationalId";
    private static final String phoneNumber = "phoneNumber";
    private static final String deploymentSite_id = "deploymentSite_id";
    private static final String employeeRole_id = "employeeRole_id";
    private static final String speciality_id = "speciality_id";
    private static final String status = "status";
    private static final String employeeSkillCategory_id = "employeeSkillCategory_id";
    private static final String dob = "dob";
    private static final String gender = "gender";
    private static final String registrationType = "registrationType";
    private static final String Employees = "Employees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_fName = (EditText) findViewById(R.id.fName);
        edit_lName = (EditText) findViewById(R.id.lName);
        edit_initials = (EditText) findViewById(R.id.initials);
        edit_email = (EditText) findViewById(R.id.email);
        edit_phone_No = (EditText) findViewById(R.id.phoneNo);
        edit_nationalID = (EditText) findViewById(R.id.nationalId);
        edit_gender = (EditText) findViewById(R.id.gender);
        addUser = (CardView) findViewById(R.id.adduser);

        Bundle bundle = getIntent().getExtras();
        school_extra = bundle.getString("school");

        Toast.makeText(getApplicationContext(), school_extra, Toast.LENGTH_LONG).show();
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Add_New_Staff.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to add this new staff member to your school?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                PostNewUserAdded();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    private void PostNewUserAdded(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            // Adding contentValues to respective columns in the SyncEmployeeTimeOffRequestDMs table
            ContentValues addNewStaff = new ContentValues();
            addNewStaff.put(id, GenerateRandomString.randomString(15));
            addNewStaff.put(firstName, edit_fName.getText().toString());
            addNewStaff.put(lastName, edit_lName.getText().toString());
            addNewStaff.put(initials, edit_initials.getText().toString());
            addNewStaff.put(phoneNumber, edit_phone_No.getText().toString());
            addNewStaff.put(emailAddress, edit_email.getText().toString());
            addNewStaff.put(gender, edit_gender.getText().toString());
            addNewStaff.put(nationalId, edit_nationalID.getText().toString());
            addNewStaff.put(deploymentSite_id, school_extra);
            db.insert(Employees, null, addNewStaff);
            //db.update(Employees, editStaffInfo,"id = ?",new String[] { id_extra });

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
