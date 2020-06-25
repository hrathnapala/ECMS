package crd.lk.ecms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import crd.lk.ecms.db.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegister;
    EditText editTextMobileNo;
    Context mContext;
    DBHelper dbHelper;
    public static final String MainPP_SP = "permission data";
    private static final int REQUEST = 112;
    Activity activity = RegisterActivity.this;
    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DBHelper(getApplicationContext());
        String number = dbHelper.getMobileNumber();
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextMobileNo = (EditText) findViewById(R.id.editTextMobileNo);


        if (!dbHelper.getMobileNumber().isEmpty()) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);

        } else {
            mContext = this;
            String TAG = "PhoneActivityTAG";

            SharedPreferences settings = getSharedPreferences(MainPP_SP, 0);
            HashMap<String, String> map = (HashMap<String, String>) settings.getAll();

            if (Build.VERSION.SDK_INT >= 23) {
                Log.d("TAG", "@@@ IN IF Build.VERSION.SDK_INT >= 23");
                String[] PERMISSIONS = {android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
                if (!hasPermissions(mContext, PERMISSIONS)) {
                    Log.d("TAG", "@@@ IN IF hasPermissions");
                    ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
                } else {
                    Log.d("TAG", "@@@ IN ELSE hasPermissions");
                    //callNextActivity();
                }
            } else {
                Log.d("TAG", "@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
                //callNextActivity();
            }


            if (!checkPermission(wantPermission)) {
                requestPermission(wantPermission);
            } else {

                editTextMobileNo.setText(getPhone());
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
//                for (int i = 0; i < subscription.size(); i++) {
//                    SubscriptionInfo info = subscription.get(i);
//                    Log.d(TAG, "number " + info.getNumber());
//                    Log.d(TAG, "network name : " + info.getCarrierName());
//                    Log.d(TAG, "country iso " + info.getCountryIso());
//                }
//            }
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mobileNo = editTextMobileNo.getText().toString();
                    try {


                        if (mobileNo.matches("")) {
                            Toast.makeText(mContext, "Empty field", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Integer.parseInt(mobileNo);
                        if (mobileNo.trim().length() < 10) {
                            Toast.makeText(mContext, "Invalid phone Number", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dbHelper.saveMobileNo(mobileNo)) {

                                Toast.makeText(getApplicationContext(), "Mobile Number Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent retIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(retIntent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Mobile Number Already registered", Toast.LENGTH_SHORT).show();
                                Intent retIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(retIntent);
                                finish();
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Invalid Input", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    editTextMobileNo.setText(getPhone());
                    Log.d("TAG", "@@@ PERMISSIONS grant");
                    //            callNextActivity();
                } else {
                    Log.d("TAG", "@@@ PERMISSIONS Denied");
                    Toast.makeText(mContext, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getPhone() {
        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, wantPermission) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return phoneMgr.getLine1Number();
    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Toast.makeText(activity, "Phone state permission allows us to get phone number. Please allow it for additional functionality.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }


    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}
