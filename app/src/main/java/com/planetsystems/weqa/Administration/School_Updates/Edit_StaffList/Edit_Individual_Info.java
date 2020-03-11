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
import com.planetsystems.weqa.RegularStaff.Emp_Home;

public class Edit_Individual_Info extends AppCompatActivity {

    EditText edit_fName, edit_lName;
    EditText edit_initials, edit_phone_No;
    EditText edit_email, edit_nationalID, edit_gender;
    CardView submitChanges;

    String id_extra, firstName_extra, lastName_extra;
    String employeeNumber_extra, emailAddress_extra;
    String initials_extra, licensed_extra, nationalId_extra;
    String phoneNumber_extra, deploymentSiteId_extra, employmentRoleId_extra, role_extra;
    String specialityId_extra, status_extra, employeeSkillCategoryId_extra;
    String dob_extra, gender_extra, registrationType_extra;

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
        setContentView(R.layout.activity_edit__individual__info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_fName = (EditText) findViewById(R.id.fName);
        edit_lName = (EditText) findViewById(R.id.lName);
        edit_initials = (EditText) findViewById(R.id.initials);
        edit_email = (EditText) findViewById(R.id.email);
        edit_phone_No = (EditText) findViewById(R.id.phoneNo);
        edit_nationalID = (EditText) findViewById(R.id.nationalId);
        edit_gender = (EditText) findViewById(R.id.gender);
        submitChanges = (CardView) findViewById(R.id.submit_changes);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        employeeNumber_extra = bundle.getString("emp_No");
        firstName_extra = bundle.getString("emp_firstName");
        lastName_extra = bundle.getString("emp_lastName");
        emailAddress_extra = bundle.getString("emp_emailAddress");
        initials_extra = bundle.getString("emp_initials");
        licensed_extra = bundle.getString("emp_licensed");
        nationalId_extra = bundle.getString("emp_nationalId");
        phoneNumber_extra = bundle.getString("emp_phoneNumber");
        deploymentSiteId_extra = bundle.getString("emp_deploymentSiteId");
        employmentRoleId_extra = bundle.getString("emp_employmentRoleId");
        role_extra = bundle.getString("emp_role");
        specialityId_extra = bundle.getString("emp_specialityId");
        status_extra = bundle.getString("emp_status");
        employeeSkillCategoryId_extra = bundle.getString("emp_employeeSkillCategoryId");
        dob_extra = bundle.getString("emp_dob");
        gender_extra = bundle.getString("emp_gender");
        registrationType_extra = bundle.getString("emp_registrationType");

        edit_fName.append(firstName_extra);
        edit_lName.append(lastName_extra);
        edit_initials.append(initials_extra);
        edit_phone_No.append(phoneNumber_extra);
        edit_email.append(emailAddress_extra);
        edit_nationalID.append(nationalId_extra);
        edit_gender.append(gender_extra);

        //Toast.makeText(getApplicationContext(), id_extra, Toast.LENGTH_LONG).show();

        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Edit_Individual_Info.this)
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

            // Adding contentValues to respective columns in the SyncEmployeeTimeOffRequestDMs table
            ContentValues editStaffInfo = new ContentValues();
            //editStaffInfo.put(id, id_extra);
            editStaffInfo.put(firstName, edit_fName.getText().toString());
            editStaffInfo.put(lastName, edit_lName.getText().toString());
            editStaffInfo.put(initials, edit_initials.getText().toString());
            editStaffInfo.put(phoneNumber, edit_phone_No.getText().toString());
            editStaffInfo.put(emailAddress, edit_email.getText().toString());
            editStaffInfo.put(gender, edit_gender.getText().toString());
            editStaffInfo.put(nationalId, edit_nationalID.getText().toString());
            db.update(Employees, editStaffInfo,"id = ?",new String[] { id_extra });
            //db.update(Employees, editStaffInfo,"id = " + "'"+id_extra+"'",null);

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
