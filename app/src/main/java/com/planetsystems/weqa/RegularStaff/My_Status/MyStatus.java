package com.planetsystems.weqa.RegularStaff.My_Status;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.weqa.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MyStatus extends AppCompatActivity {

    TextView staffName, staffId;
    TextView monday, tuesday, wednesday, thursday, friday, totalhrs;
    TextView textMon, textTue, textWed, textThru, textFri, textSat;
    TextView monONSITE, monONTASK, tueONSITE, tueONTASK, wedONSITE, wedONTASK;
    TextView thruONSITE, thruONTASK, friONSITE, friONTASK;
    CardView day1, day2, day3, day4, day5;

    String name_extra;
    String id_extra;
    String mon,tue,wed,thu,fri,total;
    String monSiteHrs, monTaskHrs, tueStieHrs, tueTaskHrs, wedSiteHrs, wedTaskHrs;
    String thruSiteHrs, thruTaskHrs, friSiteHrs, friTaskHrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Time Input");

        staffName = (TextView) findViewById(R.id.employeeName);
        staffId = (TextView) findViewById(R.id.employeeID);

        day1 = (CardView) findViewById(R.id.cardMon);
        day2 = (CardView) findViewById(R.id.cardTue);
        day3 = (CardView) findViewById(R.id.cardWed);
        day4 = (CardView) findViewById(R.id.cardThru);
        day5 = (CardView) findViewById(R.id.cardFri);

        textMon = (TextView) findViewById(R.id.day_1);
        textTue = (TextView) findViewById(R.id.day_2);
        textWed = (TextView) findViewById(R.id.day_3);
        textThru = (TextView) findViewById(R.id.day_4);
        textFri = (TextView) findViewById(R.id.day_5);

        monday = (TextView) findViewById(R.id.monhrs);
        monONSITE = (TextView) findViewById(R.id.monhrsOnsite);
        monONTASK = (TextView) findViewById(R.id.monhrsOntask);

        tuesday = (TextView) findViewById(R.id.tuehrs);
        tueONSITE = (TextView) findViewById(R.id.tuehrsOnsite);
        tueONTASK = (TextView) findViewById(R.id.tuehrsOntask);

        wednesday = (TextView) findViewById(R.id.wedhrs);
        wedONSITE = (TextView) findViewById(R.id.wedhrsOnsite);
        wedONTASK = (TextView) findViewById(R.id.wedhrsOntask);

        thursday = (TextView) findViewById(R.id.thurhrs);
        thruONSITE = (TextView) findViewById(R.id.thruhrsOnsite);
        thruONTASK = (TextView) findViewById(R.id.thruhrsOntask);

        friday = (TextView) findViewById(R.id.frihrs);
        friONSITE = (TextView) findViewById(R.id.frihrsOnsite);
        friONTASK = (TextView) findViewById(R.id.frihrsOntask);

        totalhrs = (TextView) findViewById(R.id.totalhrs);

        Bundle bundle = getIntent().getExtras();
        id_extra = bundle.getString("id");
        name_extra = bundle.getString("name");

        staffName.append(name_extra);
        staffId.append(id_extra);

        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent tot = new Intent(MyStatus.this, MyInputOnTask.class);
//                tot.putExtra("id", id_extra);
//                tot.putExtra("name", name_extra);
//                tot.putExtra("day", textMon.getText().toString());
//                startActivity(tot);

            }
        });

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent tot = new Intent(MyStatus.this, MyInputOnTask.class);
//                tot.putExtra("id", id_extra);
//                tot.putExtra("name", name_extra);
//                tot.putExtra("day", textTue.getText().toString());
//                startActivity(tot);

            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent tot = new Intent(MyStatus.this, MyInputOnTask.class);
//                tot.putExtra("id", id_extra);
//                tot.putExtra("name", name_extra);
//                tot.putExtra("day", textWed.getText().toString());
//                startActivity(tot);

            }
        });

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent tot = new Intent(MyStatus.this, MyInputOnTask.class);
//                tot.putExtra("id", id_extra);
//                tot.putExtra("name", name_extra);
//                tot.putExtra("day", textThru.getText().toString());
//                startActivity(tot);

            }
        });

        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent tot = new Intent(MyStatus.this, MyInputOnTask.class);
//                tot.putExtra("id", id_extra);
//                tot.putExtra("name", name_extra);
//                tot.putExtra("day", textFri.getText().toString());
//                startActivity(tot);

            }
        });

        //new GET_EMPLOYEE_INFO().execute(Constants.ServiceType.TIME_SHEET + "/" + id_extra);

    }

    public class GET_EMPLOYEE_INFO extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("vidType","v"));
                URI uri = new URI(urls[0] + "?" + URLEncodedUtils.format(params, "utf-8"));
                HttpGet httppost = new HttpGet(uri);
                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    mon = jsono.getString("mon");
                    monSiteHrs = jsono.getString("monTimeOnSite");
                    monTaskHrs = jsono.getString("monTimeOntask");

                    tue = jsono.getString("TUE");
                    tueStieHrs = jsono.getString("TUETimeOnSite");
                    tueTaskHrs = jsono.getString("TUETimeOntask");

                    wed = jsono.getString("wed");
                    wedSiteHrs = jsono.getString("wedTimeOnSite");
                    wedTaskHrs = jsono.getString("wedTimeOntask");

                    thu = jsono.getString("thur");
                    thruSiteHrs = jsono.getString("thurTimeOnSite");
                    thruTaskHrs = jsono.getString("thurTimeOntask");

                    fri = jsono.getString("fri");
                    friSiteHrs = jsono.getString("friTimeOnSite");
                    friTaskHrs = jsono.getString("friTimeOntask");

                    total = jsono.getString("totalWorkHours");


                    return true;
                }else {

                }

                //------------------>>

            } catch (android.net.ParseException e1) {
                e1.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (URISyntaxException es) {
                es.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            if(result == true){

                monday.append(mon);
                monONSITE.append(monSiteHrs + "hr(s)");
                monONTASK.append(monTaskHrs + "hr(s)");

                tuesday.append(tue);
                tueONSITE.append(tueStieHrs + "hr(s)");
                tueONTASK.append(tueTaskHrs + "hr(s)");

                wednesday.append(wed);
                wedONSITE.append(wedSiteHrs + "hr(s)");
                wedONTASK.append(wedTaskHrs + "hr(s)");

                thursday.append(thu);
                thruONSITE.append(thruSiteHrs + "hr(s)");
                thruONTASK.append(thruTaskHrs + "hr(s)");

                friday.append(fri);
                friONSITE.append(friSiteHrs + "hr(s)");
                friONTASK.append(friTaskHrs + "hr(s)");

                totalhrs.append(total + "hr(s)");

            }else{
                Toast.makeText(MyStatus.this, "Invalid ID!!  Employee does not exist ", Toast.LENGTH_LONG).show();

            }
        }
    }

}
