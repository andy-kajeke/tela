package com.planetsystems.weqa.RegularStaff.Requests;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.planetsystems.weqa.Authentication.GenerateRandomString;
import com.planetsystems.weqa.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestSchoolMaterials extends AppCompatActivity {

    ProgressDialog dialog;
    EditText smdate, qnty, sm_staff_comment;
    EditText itemName;
    TextView smclose;
    Button smbtnFollow;
    String id_extra, itemName_extra, itemID_extra;
    String name_extra, school_extra;
    String datetoday;
    private DatePickerDialog.OnDateSetListener sm_DatePicker;

    private static final String id = "id";
    private static final String deploymentSiteId = "deploymentSiteId";
    private static final String employee = "employee";
    private static final String employeeId = "employeeId";
    private static final String employeeRequestType = "employeeRequestType";
    private static final String itemId = "itemId";
    private static final String ItemName = "itemName";
    private static final String quantity = "quantity";
    private static final String requestDate = "requestDate";
    private static final String comment = "comment";
    private static final String dateRequired = "dateRequired";
    private  static final String approvalStatus = "approvalStatus";
    private  static final String confirmation = "confirmation";
    private static final String SyncEmployeeMaterialRequests = "SyncEmployeeMaterialRequests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_school_materials);
        setTitle("School Materials");

        smbtnFollow =(Button) findViewById(R.id.sm);
        itemName = (EditText) findViewById(R.id.smItemName);
        smdate =(EditText) findViewById(R.id.showDate);
        qnty =(EditText) findViewById(R.id.Qnty);
        sm_staff_comment =(EditText) findViewById(R.id.smcomment);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("emp_id");
        name_extra = bundle.getString("emp_name");
        itemID_extra = bundle.getString("item_id");
        itemName_extra = bundle.getString("item_name");
        school_extra = bundle.getString("school_id");

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        datetoday = dateFormat.format(date);

        itemName.setText(itemName_extra);

        Toast.makeText(getApplicationContext(),id_extra + " "+ itemID_extra,Toast.LENGTH_SHORT).show();

        ///////////////////////////////////////Date when needed//////////////////////////////////////////////
        smdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RequestSchoolMaterials.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,sm_DatePicker,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        sm_DatePicker = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year){
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                smdate.setText(date);
            }
        };

        smbtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qnty.getText().toString().equalsIgnoreCase("")){
                    qnty.setError("Quantity missing");
                }else{
                    new AlertDialog.Builder(RequestSchoolMaterials.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to submit?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                   PostToSyncEmployeeMaterialRequests();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

    }

    private void PostToSyncEmployeeMaterialRequests(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Cursor _cursor = null;

        try{
            db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);

            ContentValues schoolMaterial = new ContentValues();
            schoolMaterial.put(id, GenerateRandomString.randomString(15));
            schoolMaterial.put(employeeId, id_extra);
            schoolMaterial.put(employee, name_extra);
            schoolMaterial.put(deploymentSiteId, school_extra);
            schoolMaterial.put(employeeRequestType, "School Materials");
            schoolMaterial.put(itemId, itemID_extra);
            schoolMaterial.put(ItemName, itemName_extra);
            schoolMaterial.put(quantity, qnty.getText().toString());
            schoolMaterial.put(requestDate, datetoday);
            schoolMaterial.put(confirmation, "Pending");
            schoolMaterial.put(comment, sm_staff_comment.getText().toString());
            schoolMaterial.put(dateRequired, smdate.getText().toString());
            db.insert(SyncEmployeeMaterialRequests, null, schoolMaterial);

            Toast.makeText(getApplicationContext(), "Submitted successfully..", Toast.LENGTH_LONG).show();

        }
        finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            if(_cursor != null && !_cursor.isClosed()){
                _cursor.close();
            }
            if(db != null && db.isOpen()){
                db.close();

            }
        }

    }
}
