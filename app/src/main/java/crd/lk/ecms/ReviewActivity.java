package crd.lk.ecms;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import crd.lk.ecms.db.DBHelper;
import crd.lk.ecms.pogo.Complain;


public class ReviewActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected float meterDistance = 0;
    protected long minTime = 5000;
    String recMasterID;
    String cId;
    DBHelper myDBHelper;
    EditText editTextCompRef, editTextComplainSubj, editTextRemarks, editTextLocation, editTextCategory, editTextCompMethod, editTextCompDate, editTextCompTime;
    EditText editTextCompName, editTextCompAddr, editTextCompTel, editTextCompParty, editTextCompLocalAuth, editTextWardName, editTextWardNo, editTextDistrict;
    EditText editTextPolDiv, editTextPolCentre, editTextPolArea, editTextPolDescrip;
    Spinner spinnerStatus;
    Button buttonSend;
    double lattitude = 0.0;
    double longitude = 0.0;
    private boolean locAcquired = false;
    String server_url = "http://57a804a8d4de.ngrok.io/test//test.php";
    AlertDialog.Builder builder;


    @Override
    protected void onStart() {
        super.onStart();
        CheckGpsStatus();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        myDBHelper = new DBHelper(getApplicationContext());


        Intent i = getIntent();
        //recMasterID = i.getStringExtra("recMasterID");
        cId = i.getStringExtra("cId");
//        FetchData fetchData = new FetchData();
//        fetchData.execute();

        builder = new AlertDialog.Builder(ReviewActivity.this);
        //Button
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setEnabled(true);

        //EditText
        editTextComplainSubj = (EditText) findViewById(R.id.editTextSubject);
        editTextCompRef = (EditText) findViewById(R.id.editTextCompRef);
        editTextRemarks = (EditText) findViewById(R.id.editTextRemarks);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);

        editTextCategory = (EditText) findViewById(R.id.editTextCompCategory);
        editTextCompMethod = (EditText) findViewById(R.id.editTextCompMethod);
        editTextCompDate = (EditText) findViewById(R.id.editTextCompDate);
        editTextCompTime = (EditText) findViewById(R.id.editTextCompTime);
        editTextCompAddr = (EditText) findViewById(R.id.editTextCompAddress);
        editTextCompTel = (EditText) findViewById(R.id.editTextCompTelNo);
        editTextCompParty = (EditText) findViewById(R.id.editTextCompParty);
        editTextCompLocalAuth = (EditText) findViewById(R.id.editTextLocalAuth);
        editTextWardName = (EditText) findViewById(R.id.editTextCompWardName);
        editTextWardNo = (EditText) findViewById(R.id.editTextCompWardNo);
        editTextDistrict = (EditText) findViewById(R.id.editTextCompDist);
        editTextPolDiv = (EditText) findViewById(R.id.editTextCompPolDiv);
        editTextPolCentre = (EditText) findViewById(R.id.editTextCompPolCen);
        editTextPolArea = (EditText) findViewById(R.id.editTextCompPoliceArea);
        editTextPolDescrip = (EditText) findViewById(R.id.editTextCompDescrip);
        editTextCompName = (EditText) findViewById(R.id.editTextCompName);


        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 50, this);
        if (locAcquired) {
            buttonSend.setEnabled(true);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = myDBHelper.getComplainTotalData(cId);
        while (cursor.moveToNext()) {
            editTextCompRef.setText(cursor.getString(2));
            editTextComplainSubj.setText(cursor.getString(27));
            editTextCategory.setText(cursor.getString(4));
            editTextCompMethod.setText(cursor.getString(5));
            editTextCompDate.setText(cursor.getString(6));
            editTextCompTime.setText(cursor.getString(7));
            editTextCompName.setText(cursor.getString(8));
            editTextCompAddr.setText(cursor.getString(9));
            editTextCompTel.setText(cursor.getString(10));
            editTextCompParty.setText(cursor.getString(11));
            editTextCompLocalAuth.setText(cursor.getString(12));
            editTextWardName.setText(cursor.getString(13));
            editTextWardNo.setText(cursor.getString(14));
            editTextDistrict.setText(cursor.getString(15));
            editTextPolDiv.setText(cursor.getString(16));
            editTextPolCentre.setText(cursor.getString(17));
            editTextPolArea.setText(cursor.getString(18));
            editTextPolDescrip.setText(cursor.getString(19));
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Id, phone_number, lat, lng, response_status, response
//                String remarks = (editTextRemarks.getText().toString()).replaceAll("[^a-zA-Z0-9]", " ");
//                String urlData = recMasterID + "-" + myDBHelper.getMobileNumber() + "-" + lattitude + "-" + longitude + "-" + spinnerStatus.getSelectedItem().toString() + "-" + remarks.replaceAll(" ", "%20");
//                updateData(urlData);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                builder.setTitle("Server Response");
                                builder.setMessage("Response : " + response);
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(ReviewActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewActivity.this, "Cannot connect to the database", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("name","Heshan");
                        params.put("age","20");
                        return params;
                    }
                };

                MySingleton.getInstance(ReviewActivity.this).addToRequest(stringRequest);

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        lattitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(getApplicationContext(), "Location Accquired", Toast.LENGTH_SHORT).show();
        editTextLocation.setText(lattitude + " , " + longitude);
        locAcquired = true;

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public void CheckGpsStatus() {
        LocationManager locationManager;
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (GpsStatus == true) {
            // textview.setText("Location Services Is Enabled");
        } else {
            Toast.makeText(getApplicationContext(), "Required to Enable GPS to get precise location", Toast.LENGTH_SHORT).show();
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
    }


    public void updateData(final String urlData) {
        class HttpAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params1) {
                String response = "";
                try {
                    // URL url = new URL("http://119.235.4.83/index.php/deviceCont/eo_verification/"+urlData);
                    URL url = new URL("http://cenrdlk.com/ECMS_Dev/index.php/deviceCont/eo_verification/" + urlData);
                    Map<String, Object> params = new LinkedHashMap<>();
                    params.put("lastServerID", "MobileNo");
                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String, Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);

                    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = in.read()) >= 0; )
                        sb.append((char) c);
                    response = sb.toString();
                    //   System.out.println("Result " + response);

                } catch (MalformedURLException | ProtocolException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }


            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result != null) {
                    myDBHelper.closedVerifiedComplainData(recMasterID);
                    Toast.makeText(getApplicationContext(), "Election Data Update Successfull", Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent(ReviewActivity.this, MainActivity.class);
                    startActivity(returnIntent);
                    finish();
                }
            }
        }
        HttpAsync httpAsync = new HttpAsync();
        httpAsync.execute();
    }

//    public class FetchData extends AsyncTask<Void,Void,Void> {
//        String data = "";
//        String singleParsed = "";
//        String dataParsed = "";
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                URL url = new URL("http://www.json-generator.com/api/json/get/bUGNRsqXPC?indent=2");
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String line = "";
//
//                while (line != null) {
//                    line = bufferedReader.readLine();
//                    data = data + line;
//                }
//                JSONArray JA = new JSONArray(data);
//
//                for (int i = 0; i < JA.length(); i++) {
//                    JSONObject JO = (JSONObject) JA.get(i);
////                    singleParsed = "Name : " + JO.get("name") + "\n";
////                    dataParsed = dataParsed + singleParsed;
////                    Complain complain1 = new Complain();
////                    complain1.setId(JO.get("id").toString());
////                    complain1.setCompRef(JO.get("compRef").toString());
////                    complain1.setCompSubject(JO.get("compSubject").toString());
////                    Log.d("test", complain1.getCompRef());
//
//                    if (JO.get("masterId").toString().equals(cId)){
//                        editTextCompRef.setText(JO.get("compRef").toString());
//                        editTextComplainSubj.setText(JO.get("compSubject").toString());
//                        editTextCategory.setText(JO.get("compCat").toString());
//                        editTextCompMethod.setText(JO.get("compMethod").toString());
//                        editTextCompDate.setText(JO.get("compDate").toString());
//                        editTextCompTime.setText(JO.get("compTime").toString());
//                        editTextCompName.setText(JO.get("compName").toString());
//                        editTextCompAddr.setText(JO.get("compAddr").toString());
//                        editTextCompTel.setText(JO.get("compTpno").toString());
//                        editTextCompParty.setText(JO.get("polParty").toString());
//                        editTextCompLocalAuth.setText(JO.get("localAuth").toString());
//                        editTextWardName.setText(JO.get("wardName").toString());
//                        editTextWardNo.setText(JO.get("wardNo").toString());
//                        editTextDistrict.setText(JO.get("districtName").toString());
//                        editTextPolDiv.setText(JO.get("polDiv").toString());
//                        editTextPolCentre.setText(JO.get("polCent").toString());
//                        editTextPolArea.setText(JO.get("policeArea").toString());
//                        editTextPolDescrip.setText(JO.get("compDiscription").toString());
//                        editTextLocation.setText(JO.get("compLat").toString() +" "+ JO.get("compLng").toString());
//                    }
////                    }else {
////                        Toast.makeText(ReviewActivity.this, "Errooo", Toast.LENGTH_SHORT).show();
////                    }
//
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//    }


}
