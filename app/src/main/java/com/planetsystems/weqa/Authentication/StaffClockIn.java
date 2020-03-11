package com.planetsystems.weqa.Authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.planetsystems.weqa.Administration.AdminSide;
import com.planetsystems.weqa.Administration.Smc_Role.SmcRole;
import com.planetsystems.weqa.DataStore.MainActivity;
import com.planetsystems.weqa.DataStore.SQLiteSync;
import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.Emp_Home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffClockIn extends Activity implements LocationListener {

    MainActivity mainActivity;
    CardView btnCheckIn;
    EditText staffid;

    LocationManager locationManager;
    double lng;
    double lat;

    String checkIn_time;
    String checkIn_date;
    String dayOfTheWeek;
    String status = "New";
    private static final String id = "id";
    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String employeeRole = "employeeRole";
    private static final String SyncClockIns = "SyncClockIns";
    private static final String clockInDate = "clockInDate";
    private static final String clockInTime = "clockInTime";
    private static final String day = "day";
    private static final String employeeId = "employeeId";
    private static final String employeeNo = "employeeNo";
    private static final String empFirstName = "empFirstName";
    private static final String empLastName = "empLastName";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String synStatus = "synStatus";
    private static final String schoolId = "schoolId";
    private static final String deploymentSite_id = "deploymentSite_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_clock_in);

        btnCheckIn = (CardView) findViewById(R.id.google);
        staffid = (EditText) findViewById(R.id.staffid);

        Bundle bundle = getIntent().getExtras();
        checkIn_time = bundle.getString("time");
        checkIn_date = bundle.getString("date");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emp_id = staffid.getText().toString();
                SQLiteDatabase db = null;
                //SQLiteDatabase db = db_helper.getWritableDatabase();
                Cursor cursor = null;
                Cursor cursor_ = null;
                Cursor cursor2 = null;
                Cursor cursor3 = null;
                //String num = cursor.getString(cursor.getColumnIndex(deploymentSite_id));

                try{
                    db = openOrCreateDatabase("/data/data/" + getPackageName() + "/sqlitesync.db", Context.MODE_PRIVATE, null);
                    //cursor = db.rawQuery("select tbl_Name from sqlite_master where type='table'", null);
                    cursor = db.rawQuery("select *from Employees, EmployeeRoles WHERE Employees.employeeRole_id = EmployeeRoles.id AND " +
                            "employeeNumber = " + emp_id, null);
                    if(cursor.moveToNext())
                    {


                        if (cursor.getString(cursor.getColumnIndex(employeeRole)).equals("Teacher"))
                        {
                            Intent emp_home = new Intent(StaffClockIn.this, Emp_Home.class);
                            emp_home.putExtra("id", emp_id);
                            emp_home.putExtra("name", cursor.getString(cursor.getColumnIndex(firstName)) +
                                    " " + cursor.getString(cursor.getColumnIndex(lastName)));
                            emp_home.putExtra("dateOfDay", checkIn_date);
                            emp_home.putExtra("school_id", cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                            startActivity(emp_home);

                            // check if teacher already clocked_in on same date
                            cursor_ = db.rawQuery("select *from SyncClockIns where employeeNo = " + emp_id + " " +
                                    " AND clockInDate = " + "'"+checkIn_date+"'",null);
                            //if
                            if(cursor_.moveToNext())
                            {
                                Toast.makeText(getApplicationContext(), "Already Clocked_in", Toast.LENGTH_LONG).show();
                            }
                            //else
                            else {

                                ContentValues checkIn_Teacher = new ContentValues();
                                checkIn_Teacher.put(id, GenerateRandomString.randomString(15));
                                checkIn_Teacher.put(clockInDate, checkIn_date);
                                checkIn_Teacher.put(clockInTime, checkIn_time);
                                checkIn_Teacher.put(day, dayOfTheWeek);
                                checkIn_Teacher.put(employeeId, emp_id);
                                checkIn_Teacher.put(employeeNo, emp_id);
                                checkIn_Teacher.put(empFirstName, cursor.getString(cursor.getColumnIndex(firstName)));
                                checkIn_Teacher.put(empLastName, cursor.getString(cursor.getColumnIndex(lastName)));
                                checkIn_Teacher.put(latitude, lat);
                                checkIn_Teacher.put(longitude, lng);
                                checkIn_Teacher.put(synStatus, "New");
                                checkIn_Teacher.put(schoolId, cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                                db.insert(SyncClockIns, null, checkIn_Teacher);

                                Toast.makeText(getApplicationContext(), "Clocked in successfully", Toast.LENGTH_LONG).show();

                            }


                        }else if(cursor.getString(cursor.getColumnIndex(employeeRole)).equals("Head teacher"))
                        {
                            Intent adminSide = new Intent(StaffClockIn.this,AdminSide.class);
                            adminSide.putExtra("admin_id", emp_id);
                            adminSide.putExtra("admin_name", cursor.getString(cursor.getColumnIndex(firstName)) +
                                    " " + cursor.getString(cursor.getColumnIndex(lastName)));
                            adminSide.putExtra("role", cursor.getString(cursor.getColumnIndex(employeeRole)));
                            adminSide.putExtra("date", checkIn_date);
                            adminSide.putExtra("school", cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                            startActivity(adminSide);

                            // check if Head_Teacher already clocked_in on same date
                            cursor2 = db.rawQuery("select *from SyncClockIns where employeeNo = " + emp_id + " " +
                                    " AND clockInDate = " + "'"+checkIn_date+"'",null);
                            //if
                            if(cursor2.moveToNext())
                            {
                                Toast.makeText(getApplicationContext(), "Already Clocked_in", Toast.LENGTH_LONG).show();
                            }
                            //else
                            else {

                                ContentValues checkIn_HeadTeacher = new ContentValues();
                                checkIn_HeadTeacher.put(id, GenerateRandomString.randomString(15));
                                checkIn_HeadTeacher.put(clockInDate, checkIn_date);
                                checkIn_HeadTeacher.put(clockInTime, checkIn_time);
                                checkIn_HeadTeacher.put(day, dayOfTheWeek);
                                checkIn_HeadTeacher.put(employeeId, emp_id);
                                checkIn_HeadTeacher.put(employeeNo, emp_id);
                                checkIn_HeadTeacher.put(empFirstName, cursor.getString(cursor.getColumnIndex(firstName)));
                                checkIn_HeadTeacher.put(empLastName, cursor.getString(cursor.getColumnIndex(lastName)));
                                checkIn_HeadTeacher.put(latitude, lat);
                                checkIn_HeadTeacher.put(longitude, lng);
                                checkIn_HeadTeacher.put(synStatus, "New");
                                checkIn_HeadTeacher.put(schoolId, cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                                db.insert(SyncClockIns, null, checkIn_HeadTeacher);

                                Toast.makeText(getApplicationContext(), "Clocked in successfully", Toast.LENGTH_LONG).show();
                            }

                        }else if(cursor.getString(cursor.getColumnIndex(employeeRole)).equals("SMC"))
                        {
                            Intent smc = new Intent(StaffClockIn.this, SmcRole.class);
                            smc.putExtra("smc_id", emp_id);
                            smc.putExtra("school", cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                            startActivity(smc);

                            // check if SMC already clocked_in on same date
                            cursor3 = db.rawQuery("select *from SyncClockIns where employeeNo = " + emp_id + " " +
                                    " AND clockInDate = " + "'"+checkIn_date+"'",null);
                            //if
                            if(cursor3.moveToNext())
                            {
                                Toast.makeText(getApplicationContext(), "Already Clocked_in", Toast.LENGTH_LONG).show();
                            }
                            //else
                            else {

                                ContentValues checkIn_Smc = new ContentValues();
                                checkIn_Smc.put(id, GenerateRandomString.randomString(15));
                                checkIn_Smc.put(clockInDate, checkIn_date);
                                checkIn_Smc.put(clockInTime, checkIn_time);
                                checkIn_Smc.put(day, dayOfTheWeek);
                                checkIn_Smc.put(employeeId, emp_id);
                                checkIn_Smc.put(employeeNo, emp_id);
                                checkIn_Smc.put(empFirstName, cursor.getString(cursor.getColumnIndex(firstName)));
                                checkIn_Smc.put(empLastName, cursor.getString(cursor.getColumnIndex(lastName)));
                                checkIn_Smc.put(latitude, lat);
                                checkIn_Smc.put(longitude, lng);
                                checkIn_Smc.put(synStatus, "New");
                                checkIn_Smc.put(schoolId, cursor.getString(cursor.getColumnIndex(deploymentSite_id)));
                                db.insert(SyncClockIns, null, checkIn_Smc);

                                Toast.makeText(getApplicationContext(), "Clocked in successfully", Toast.LENGTH_LONG).show();
                            }

                        }

                    }else {
                        Toast.makeText(getApplicationContext(), "Staff ID not found", Toast.LENGTH_LONG).show();
                    }

                }
                finally {
                    if(cursor != null && !cursor.isClosed()){
                        cursor.close();
                    }
                    if(cursor_ != null && !cursor_.isClosed()){
                        cursor_.close();
                    }
                    if(cursor2 != null && !cursor2.isClosed()){
                        cursor2.close();
                    }
                    if(cursor3 != null && !cursor3.isClosed()){
                        cursor3.close();
                    }
                    if(db != null && db.isOpen()){
                        db.close();
                    }
                }

            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        getLocation();

    }

    //////////////////////GPS Functionality//////////////////////////////////////////////////////
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lng = location.getLatitude();
        lat = location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            //LocName.setText("Town Name: " + addresses.get(0).getAddressLine(0));
            if (null != addresses && addresses.size() > 0) {
                String _addre = addresses.get(0).getAddressLine(0);
                //LocName.setText(_addre);
            }
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(StaffClockIn.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
