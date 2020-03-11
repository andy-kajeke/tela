package com.planetsystems.weqa.Administration.School_Updates.Edit_StaffList;

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
import android.widget.ListView;
import android.widget.Toast;

import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class EditStaffList extends AppCompatActivity {

    ListView staffs;
    ArrayList<StaffListModel> markList;
    StaffListAdapter adapter;
    //String id_extra;
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
    private static final String employeeRole = "employeeRole";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__staff_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        staffs = (ListView) findViewById(R.id.list_staffs);

        Bundle bundle = getIntent().getExtras();
        school_extra = bundle.getString("school");


        markList = new ArrayList<>();

        adapter = new StaffListAdapter(getApplicationContext(),R.layout.get_staff_list,markList);

        staffs.setAdapter(adapter);
        staffs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),markList.get(position).getItemCode(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Edit_Individual_Info.class);
                i.putExtra("id", markList.get(position).getId());
                i.putExtra("emp_No", markList.get(position).getEmployeeNumber());
                i.putExtra("emp_firstName", markList.get(position).getFirstName());
                i.putExtra("emp_lastName", markList.get(position).getLastName());
                i.putExtra("emp_emailAddress", markList.get(position).getEmailAddress());
                i.putExtra("emp_initials", markList.get(position).getInitial());
                i.putExtra("emp_licensed", markList.get(position).getLicensed());
                i.putExtra("emp_nationalId", markList.get(position).getNationalId());
                i.putExtra("emp_phoneNumber", markList.get(position).getPhoneNumber());
                i.putExtra("emp_deploymentSiteId", markList.get(position).getDeploymentSite_id());
                i.putExtra("emp_employmentRoleId", markList.get(position).getEmployeeRole_id());
                //i.putExtra("emp_role", markList.get(position).getRole());
                i.putExtra("emp_specialityId", markList.get(position).getSpeciality_id());
                i.putExtra("emp_status", markList.get(position).getStatus());
                i.putExtra("emp_employeeSkillCategoryId", markList.get(position).getEmployeeSkillCategory_id());
                i.putExtra("emp_dob", markList.get(position).getDob());
                i.putExtra("emp_gender", markList.get(position).getGender());
                i.putExtra("emp_registrationType", markList.get(position).getRegistrationType());
                startActivity(i);

            }
        });

        ListStaffMembers();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addStaff = new Intent(EditStaffList.this, Add_New_Staff.class);
                addStaff.putExtra("school", school_extra);
                startActivity(addStaff);
            }
        });
    }

    private void ListStaffMembers(){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select * from Employees Where deploymentSite_id = " + "'"+school_extra+"'",null);
//            cursor = db.rawQuery("select *from Employees, EmployeeRoles WHERE Employees.employeeRole_id = EmployeeRoles.id AND " +
//                    "deploymentSite_id = " + "'"+school_extra+"'", null);
            //cursor = db.rawQuery("select * from SyncTimeTables", null);

            while(cursor.moveToNext())
            {
                StaffListModel mark_List = new StaffListModel();
                mark_List.setId(cursor.getString(cursor.getColumnIndex(id)));
                mark_List.setFirstName(cursor.getString(cursor.getColumnIndex(firstName)));
                mark_List.setLastName(cursor.getString(cursor.getColumnIndex(lastName)));
                mark_List.setEmailAddress(cursor.getString(cursor.getColumnIndex(emailAddress)));
                mark_List.setEmployeeNumber(cursor.getString(cursor.getColumnIndex(employeeNumber)));
                mark_List.setLicensed(cursor.getString(cursor.getColumnIndex(licensed)));
                mark_List.setInitial(cursor.getString(cursor.getColumnIndex(initials)));
                mark_List.setNationalId(cursor.getString(cursor.getColumnIndex(nationalId)));
                mark_List.setPhoneNumber(cursor.getString(cursor.getColumnIndex(phoneNumber)));
                //mark_List.setRole(cursor.getString(cursor.getColumnIndex(employeeRole)));
                mark_List.setDeploymentSite_id(cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                mark_List.setEmployeeRole_id(cursor.getString(cursor.getColumnIndex(employeeRole_id)));
                mark_List.setSpeciality_id(cursor.getString(cursor.getColumnIndex(speciality_id)));
                mark_List.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                mark_List.setEmployeeSkillCategory_id(cursor.getString(cursor.getColumnIndex(employeeSkillCategory_id)));
                mark_List.setDob(cursor.getString(cursor.getColumnIndex(dob)));
                mark_List.setGender(cursor.getString(cursor.getColumnIndex(gender)));
                mark_List.setRegistrationType(cursor.getString(cursor.getColumnIndex(registrationType)));
                markList.add(mark_List);

                //Toast.makeText(getApplicationContext(), cursor.getString(cursor.getColumnIndex(id)), Toast.LENGTH_LONG).show();
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
