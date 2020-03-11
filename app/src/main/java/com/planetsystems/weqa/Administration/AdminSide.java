package com.planetsystems.weqa.Administration;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planetsystems.weqa.Administration.Leaner_Attendance.LearnerAttendance;
import com.planetsystems.weqa.Administration.School_Updates.Edit_StaffList.EditStaffList;
import com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable.SelectClass;
import com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable.SelectDay;
import com.planetsystems.weqa.Administration.Service_Requests.RequestMade;
import com.planetsystems.weqa.Administration.Task_Attendance.TaskAttendance;
import com.planetsystems.weqa.Administration.Time_Attendance.TimeAttendance;
import com.planetsystems.weqa.DataStore.ManualSync;
import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.Emp_Home;

public class AdminSide extends AppCompatActivity {

    CardView myLessons;
    CardView attendClass;
    CardView learner;
    CardView staff;
    CardView requests;
    CardView update;
    CardView sync;
    Dialog updateDialog;
    Dialog selectDayDialog;
    EditText _date, staff_comment;
    TextView close;
    Button update_staff;
    Button edit_time_table;
    Button edit_staff_list;
    TextView headName,headRole;
    String HT_Id;
    String role_extra;
    String name_extra;
    String checkIn_date;
    String checkIn_schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_side);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        headName = (TextView) findViewById(R.id.headTeacher);
        headRole = (TextView) findViewById(R.id.adminPosition);
        myLessons = (CardView) findViewById(R.id.cardview1);
        attendClass = (CardView) findViewById(R.id.attendClass);
        learner = (CardView) findViewById(R.id.cardview4);
        staff = (CardView) findViewById(R.id.attendWork);
        requests = (CardView) findViewById(R.id.cardview5);
        update = (CardView) findViewById(R.id.cardview6);
        sync = (CardView) findViewById(R.id.cardview7);

        updateDialog = new Dialog(this);

        Bundle bundle = getIntent().getExtras();
        HT_Id = bundle.getString("admin_id");
        role_extra = bundle.getString("role");
        name_extra = bundle.getString("admin_name");
        checkIn_date = bundle.getString("date");
        checkIn_schoolId = bundle.getString("school");

        headRole.setText("[ " + role_extra + " ]");
        headName.setText(name_extra);

        myLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Emp_Home.class);
                i.putExtra("id",HT_Id);
                i.putExtra("name",name_extra);
                i.putExtra("school_id", checkIn_schoolId);
                startActivity(i);
            }
        });

        attendClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TaskAttendance.class);
                i.putExtra("id",HT_Id);
                i.putExtra("date", checkIn_date);
                i.putExtra("school", checkIn_schoolId);
                startActivity(i);
            }
        });
        learner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LearnerAttendance.class);
                i.putExtra("id",HT_Id);
                i.putExtra("school", checkIn_schoolId);
                startActivity(i);
            }
        });
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RequestMade.class);
                i.putExtra("school", checkIn_schoolId);
                startActivity(i);
            }
        });
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TimeAttendance.class);
                i.putExtra("id", HT_Id);
                i.putExtra("date", checkIn_date);
                i.putExtra("school", checkIn_schoolId);
                startActivity(i);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdatePopup();
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminSide.this,ManualSync.class);
                startActivity(i);
            }
        });

    }

    public void showUpdatePopup() {

        updateDialog.setContentView(R.layout.schoolupdatepopup);

        close =(TextView) updateDialog.findViewById(R.id.txclose);
        edit_staff_list = (Button) updateDialog.findViewById(R.id.staff_list);
        edit_time_table = (Button) updateDialog.findViewById(R.id.time_table);
        //update_staff =(Button) updateDialog.findViewById(R.id.out);

        edit_staff_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),EditStaffList.class);
                i.putExtra("school", checkIn_schoolId);
                startActivity(i);

            }
        });

        edit_time_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SelectClass.class);
                i.putExtra("school", checkIn_schoolId);
                startActivity(i);
            }
        });

//        update_staff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        close.setText("X");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });

        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.show();

    }
}
