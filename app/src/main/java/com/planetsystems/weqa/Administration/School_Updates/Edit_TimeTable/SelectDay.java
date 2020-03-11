package com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.planetsystems.weqa.R;

public class SelectDay extends Activity {

    Button monday, tuesday;
    Button wednesday, thursday;
    Button friday, saturday;

    String school_id_extra;
    String class_extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_select_day);

        monday = (Button) findViewById(R.id.mon_day);
        tuesday = (Button) findViewById(R.id.tue_day);
        wednesday = (Button) findViewById(R.id.wed_day);
        thursday = (Button) findViewById(R.id.thru_day);
        friday = (Button) findViewById(R.id.fri_day);
        saturday = (Button) findViewById(R.id.sat_day);

        Bundle bundle = getIntent().getExtras();
        school_id_extra = bundle.getString("school");
        class_extra = bundle.getString("class");

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timetable = new Intent(SelectDay.this, TimeTable.class);
                timetable.putExtra("schoolId", school_id_extra);
                timetable.putExtra("day", "Monday");
                timetable.putExtra("class", class_extra);
                startActivity(timetable);

            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timetable = new Intent(SelectDay.this, TimeTable.class);
                timetable.putExtra("schoolId", school_id_extra);
                timetable.putExtra("day", "Tuesday");
                timetable.putExtra("class", class_extra);
                startActivity(timetable);

            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timetable = new Intent(SelectDay.this, TimeTable.class);
                timetable.putExtra("schoolId", school_id_extra);
                timetable.putExtra("day", "Wednesday");
                timetable.putExtra("class", class_extra);
                startActivity(timetable);

            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timetable = new Intent(SelectDay.this, TimeTable.class);
                timetable.putExtra("schoolId", school_id_extra);
                timetable.putExtra("day", "Thursday");
                timetable.putExtra("class", class_extra);
                startActivity(timetable);

            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timetable = new Intent(SelectDay.this, TimeTable.class);
                timetable.putExtra("schoolId", school_id_extra);
                timetable.putExtra("day", "Friday");
                timetable.putExtra("class", class_extra);
                startActivity(timetable);

            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timetable = new Intent(SelectDay.this, TimeTable.class);
                timetable.putExtra("schoolId", school_id_extra);
                timetable.putExtra("day", "Saturday");
                timetable.putExtra("class", class_extra);
                startActivity(timetable);

            }
        });

    }

}
