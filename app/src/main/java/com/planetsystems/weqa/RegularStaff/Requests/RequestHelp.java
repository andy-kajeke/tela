package com.planetsystems.weqa.RegularStaff.Requests;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;

public class RequestHelp extends AppCompatActivity {

    Spinner category;
    EditText hcomment;
    RadioButton high, normal;
    TextView hclose,priorityRate;
    Button hbtnFollow;
    String id_extra, school_extra;
    String datetoday;

    public static final String id = "id";
    public static final String staffCode = "staffCode";
    public static final String confirmation = "confirmation";
    public static final String comment = "comment";
    public static final String deploymentSiteId = "deploymentSiteId";
    public static final String helpCategory = "helpCategory";
    public static final String priority ="priority";
    public static final String requestedDate = "requestedDate";
    public static final String SyncHelpRequests = "SyncHelpRequests";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);
        setTitle("Help");

        priorityRate = (TextView) findViewById(R.id.priorityRate);
        high = (RadioButton) findViewById(R.id.High);
        normal = (RadioButton) findViewById(R.id.Normal);
        hbtnFollow =(Button) findViewById(R.id.help);
        category = (Spinner) findViewById(R.id.helpCategory);
        hcomment =(EditText) findViewById(R.id.helpcomment);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        school_extra = bundle.getString("school_id");

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        datetoday = dateFormat.format(date);

        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this, R.array.help_category,
                android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(Adapter);

        //On select specific request
        high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    normal.setChecked(false);

                    priorityRate.setText("High priority");

                }

            }
        });
        normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    high.setChecked(false);

                    priorityRate.setText("Normal priority");
                }

            }
        });


        hbtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new AlertDialog.Builder(RequestHelp.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to submit?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    HelpRequest();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    private void HelpRequest(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            // Adding contentValues to respective columns in the SyncEmployeeTimeOffRequestDMs table
            ContentValues help = new ContentValues();
            help.put(id, GenerateRandomString.randomString(15));
            help.put(staffCode, id_extra);;
            help.put(deploymentSiteId, school_extra);
            help.put(confirmation, "Pending");
            help.put(helpCategory, category.getSelectedItem().toString());
            help.put(comment, hcomment.getText().toString());
            help.put(priority, priorityRate.getText().toString());
            help.put(requestedDate, datetoday);
            db.insert(SyncHelpRequests, null, help);

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
