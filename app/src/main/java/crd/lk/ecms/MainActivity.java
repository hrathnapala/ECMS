package crd.lk.ecms;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import crd.lk.ecms.config.CustomAdapter;
import crd.lk.ecms.db.DBHelper;
import crd.lk.ecms.pogo.Complain;

public class MainActivity extends AppCompatActivity {
    public MainActivity mainActivity = null;
    public ArrayList<Complain> CustomListViewValuesArr = new ArrayList<Complain>();
    public ArrayList<String> test = new ArrayList<String>();
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private ProgressDialog progressDialog;
    DBHelper myDBHelper;
    ListView list;
    CustomAdapter adapter;
    private String test1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getData();

        myDBHelper = new DBHelper(getApplicationContext());
        final String number = myDBHelper.getMobileNumber();

        mainActivity = this;
        list = (ListView) findViewById(R.id.list);

        CustomListViewValuesArr.clear();

        Cursor cursor = myDBHelper.ComplainData();
        if (cursor.getCount() == 0) {
            Complain complain = new Complain();
            complain.setCompRef("No Data");
            complain.setCompSubject("Please SYNC to receive new Data");
            CustomListViewValuesArr.add(complain);
        } else {
            while (cursor.moveToNext()) {
                Complain complain = new Complain();
                complain.setIsReplyed(cursor.getString(0));
                complain.setCompRef(cursor.getString(2));
                complain.setMasterId(cursor.getString(3));
                complain.setCompSubject(cursor.getString(27));
                complain.setCompCat(cursor.getString(4));
                complain.setCompMethod(cursor.getString(5));
                complain.setCompDate(cursor.getString(6));
                complain.setCompTime(cursor.getString(7));
                complain.setCompName(cursor.getString(8));
                complain.setCompAddr(cursor.getString(9));
                complain.setCompTpno(cursor.getString(10));
                complain.setPolParty(cursor.getString(11));
                complain.setLocalAuth(cursor.getString(12));
                complain.setWardName(cursor.getString(13));
                complain.setWardNo(cursor.getString(14));
                complain.setDistrictName(cursor.getString(15));
                complain.setPolDiv(cursor.getString(16));
                complain.setPolCent(cursor.getString(17));
                complain.setPoliceArea(cursor.getString(18));
                complain.setCompDiscription(cursor.getString(19));
                complain.setCompLat(cursor.getString(20));
                complain.setCompLng(cursor.getString(21));
                complain.setCompImg(cursor.getString(22));
                complain.setSeverityLvl(cursor.getString(23));

                CustomListViewValuesArr.add(complain);

            }
        }


        adapter = new CustomAdapter(mainActivity, CustomListViewValuesArr);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),number,Toast.LENGTH_SHORT).show();
                Intent longIntent = new Intent(MainActivity.this, ReviewActivity.class);
                longIntent.putExtra("cId", view.getTag().toString());
                startActivity(longIntent);

            }
        });

    }


    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {
        Log.i("Item Click", mPosition + "");
        Complain complain = (Complain) CustomListViewValuesArr.get(mPosition);
        // Toast.makeText(getApplicationContext() , " Long iD"+ complain.getMasterId(),Toast.LENGTH_SHORT).show();
        Intent longIntent = new Intent(MainActivity.this, ReviewActivity.class);
        longIntent.putExtra("recMasterID", complain.getMasterId());
        startActivity(longIntent);

        //Toast.makeText(getApplicationContext() , complain.getCompRef()+" Comp iD"+ complain.getMasterId(),Toast.LENGTH_SHORT).show();
    }
/*
    public void onItemLongClick(int mPosition) {
        Log.i("Item Click Long", mPosition+"");

        Complain complain = (Complain) CustomListViewValuesArr.get(mPosition);
            Toast.makeText(getApplicationContext() , " Long iD"+ complain.getMasterId(),Toast.LENGTH_SHORT).show();
        Intent longIntent = new Intent(MainActivity.this, ReviewActivity.class);
        longIntent.putExtra("recMasterID", complain.getMasterId());
        startActivity(longIntent);


    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle("Receiving Conversion Data"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

            //String ecms_url = "http://119.235.4.83/index.php/deviceCont/generate_json/" + myDBHelper.getMobileNumber() + "-" + myDBHelper.getLastMasterID();
            //String ecms_url = "http://www.json-generator.com/api/json/get/caHYUmdnoy?indent=2";
//            SyncData task = new SyncData();
//            task.execute(new String[]{ecms_url});
            FetchData fetchData = new FetchData();
            fetchData.execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class SyncData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            Log.e("URL ", urls[0]);
            Request request = new Request.Builder().url(urls[0]).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                Log.d("test", response.body().string());

                return response.body().string();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Server Syncronization Error Occured", Toast.LENGTH_SHORT).show();
            }
            return "Syncronization failed";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // myDBHelper.updateComplainData(result);
                Toast.makeText(getApplicationContext(), "Election Data Synchronization Successfull", Toast.LENGTH_LONG).show();
                //setListData();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Synchronizing Error Occured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setListData() {
        Cursor gateEquipCursor = myDBHelper.getComplainData();
        CustomListViewValuesArr.clear();
        while (gateEquipCursor.moveToNext()) {
            int idIndex = gateEquipCursor.getColumnIndex("id");
            int complainRefIndex = gateEquipCursor.getColumnIndex("compRef");
            int masterIdIndex = gateEquipCursor.getColumnIndex("masterId");
            int compSubjectIndex = gateEquipCursor.getColumnIndex("compSubject");

            final Complain compObj = new Complain();
            compObj.setId(gateEquipCursor.getString(idIndex));
            compObj.setCompRef(gateEquipCursor.getString(complainRefIndex));
            compObj.setMasterId(gateEquipCursor.getString(masterIdIndex));
            compObj.setCompSubject(gateEquipCursor.getString(compSubjectIndex));
            CustomListViewValuesArr.add(compObj);
        }
        list.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Complain List Updated", Toast.LENGTH_SHORT).show();
    }


    public class FetchData extends AsyncTask<Void, Void, Void> {
        String data = "";
        String singleParsed = "";
        String dataParsed = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://99c4a1f0e50d.ngrok.io/test/search.php?phone=0"+myDBHelper.getMobileNumber());
               // URL url = new URL("http://127.0.0.1/test/search.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";

                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                    System.out.println(data);

                }
                System.out.println(data);
                JSONArray JA = new JSONArray(data);
                myDBHelper = new DBHelper(getApplicationContext());
                try {
                    myDBHelper.updateComplainData(data);
                } catch (Exception e) {

                }
                CustomListViewValuesArr.clear();
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);
//                    singleParsed = "Name : " + JO.get("name") + "\n";
//                    dataParsed = dataParsed + singleParsed;
                    Complain complain1 = new Complain();
                    complain1.setMasterId(JO.get("masterId").toString());
                    complain1.setCompRef(JO.get("compRef").toString());
                    complain1.setCompSubject(JO.get("compSubject").toString());

                    CustomListViewValuesArr.add(complain1);
                    //System.out.println(complain1.getCompSubject());


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //MainActivity.data.setText(this.dataParsed);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // myDBHelper.updateComplainData(result);
                Toast.makeText(getApplicationContext(), "Election Data Synchronization Successfull", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
                //setListData();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Synchronizing Error Occured", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void getData(){

    }


}


