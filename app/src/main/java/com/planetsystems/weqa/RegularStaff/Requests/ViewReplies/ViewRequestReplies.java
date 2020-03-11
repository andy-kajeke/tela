package com.planetsystems.weqa.RegularStaff.Requests.ViewReplies;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.planetsystems.weqa.Administration.School_Updates.Edit_StaffList.EditStaffList;
import com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable.SelectClass;
import com.planetsystems.weqa.R;

public class ViewRequestReplies extends AppCompatActivity {

    Button approved, rejected;
    String id_extra, schoolID_extra;
    //String id_extra_r, schoolID_extra_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_replies);

        approved = (Button) findViewById(R.id.approved);
        rejected = (Button) findViewById(R.id.rejected);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        schoolID_extra = bundle.getString("school_id");

        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApprovedRequestsOnly();
            }
        });

        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RejectedRequestsOnly();
            }
        });

    }

    private void ApprovedRequestsOnly() {

    }

    private void RejectedRequestsOnly() {

    }
}
