package crd.lk.ecms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public DBHelper(Context context) {
        super(context, "db_eleComplain", null, 1);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String complainSQL = "CREATE TABLE IF NOT EXISTS tbl_complain ("
                + "isReplyed INTEGER, "
                + "replyedTime Text, "
                + "compRef Text, "
                + "masterId INTEGER PRIMARY KEY, "
                + "compCat Text, "
                + "compMethod Text, "
                + "compDate Text, "
                + "compTime Text, "
                + "compName Text, "
                + "compAddr Text, "
                + "compTpno Text, "
                + "polParty Text, "
                + "localAuth Text, "
                + "wardName Text, "
                + "wardNo Text, "
                + "districtName Text, "
                + "polDiv Text, "
                + "polCent Text, "
                + "policeArea Text, "
                + "compDiscription Text, "
                + "compLat Text, "
                + "compLng Text, "
                + "compImg Text, "
                + "severityLvl Text, "
                + "status Text, "
                + "isVerified Text, "
                + "fwdTo Text, "
                + "compSubject Text)";
        sqLiteDatabase.execSQL(complainSQL);


        String settingsSQL = "CREATE TABLE IF NOT EXISTS tbl_settings ("
                + "isSynced INTEGER, "
                + "mobileNumber INTEGER PRIMARY KEY )";
        sqLiteDatabase.execSQL(settingsSQL);


    }


    public boolean saveMobileNo(String mobileNo) {
        ContentValues c = new ContentValues();
        c.put("isSynced", 0);
        c.put("mobileNumber", mobileNo);
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("tbl_settings", "", c);
        Log.i("DB Function", "Mobile Number Added");

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public String getMobileNumber() {
        String countQuery = "SELECT * FROM tbl_settings";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        String mobileNo = "";
        if (cursor.moveToFirst()) {
            int mobileNumIndex = cursor.getColumnIndex("mobileNumber");
            mobileNo = cursor.getString(mobileNumIndex);
        }
        cursor.close();
        return mobileNo;

    }

     public int countMobileNumber() {
        String countQuery = "SELECT * FROM tbl_settings";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;

    }


    public void updateComplainData(String json) {

        try {
            JSONArray JA = new JSONArray(json);

            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                String compRef = JO.get("compRef").toString();
                String masterId = JO.get("masterId").toString();
                String compSubject = JO.get("compSubject").toString();
                String compCat = JO.get("compCat").toString();
                String compMethod = JO.get("compMethod").toString();
                String compDate = JO.get("compDate").toString();
                String compTime = JO.get("compTime").toString();
                String compName = JO.get("compName").toString();
                String compAddr = JO.get("compAddr").toString();
                String compTpno = JO.get("compTpno").toString();
                String polParty = JO.get("polParty").toString();
                String localAuth = JO.get("localAuth").toString();
                String wardName = JO.get("wardName").toString();
                String wardNo = JO.get("wardNo").toString();
                String districtName = JO.get("districtName").toString();
                String polDiv = JO.get("polDiv").toString();
                String polCent = JO.get("polCent").toString();
                String policeArea = JO.get("policeArea").toString();
                String compDiscription = JO.get("compDiscription").toString();
                String compLat = JO.get("compLat").toString();
                String compLng = JO.get("compLng").toString();
                String compImg = JO.get("compImg").toString();
                String severityLvl = JO.get("severityLvl").toString();

                ContentValues c = new ContentValues();
                c.put("isReplyed", 0);
                c.put("compRef", compRef);
                c.put("masterId", masterId);
                c.put("compSubject", compSubject);
                c.put("compCat", compCat);
                c.put("compMethod", compMethod);
                c.put("compDate", compDate);
                c.put("compTime", compTime);
                c.put("compName", compName);
                c.put("compAddr", compAddr);
                c.put("compTpno", compTpno);
                c.put("polParty", polParty);
                c.put("localAuth", localAuth);
                c.put("wardName", wardName);
                c.put("wardNo", wardNo);
                c.put("districtName", districtName);
                c.put("polDiv", polDiv);
                c.put("polCent", polCent);
                c.put("policeArea", policeArea);
                c.put("compDiscription", compDiscription);
                c.put("compLat", compLat);
                c.put("compLng", compLng);
                c.put("compImg", compImg);
                c.put("severityLvl", severityLvl);
                SQLiteDatabase db = getWritableDatabase();
                db.insert("tbl_complain", null, c);
                db.update("tbl_complain", c, "masterId = ?", new String[]{masterId});
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Election Data Receiving Error", Toast.LENGTH_SHORT).show();

        }
    }

    public String getLastMasterID() {
        String countQuery = "SELECT * FROM tbl_complain";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        String collectorID = "0";
        if (cursor.moveToLast()) {
            int collectorIDIndex = cursor.getColumnIndex("masterId");
            collectorID = cursor.getString(collectorIDIndex);
        }
        cursor.close();
        return collectorID;
    }


    public int getComplainCount() {
        String countQuery = "SELECT * FROM tbl_complain";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public Cursor getComplainData() {
        String sql = "SELECT * FROM tbl_complain WHERE isReplyed = 0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor ComplainData() {
        String sql = "SELECT * FROM tbl_complain";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public void deleteComplainData() {
        SQLiteDatabase db = this.getReadableDatabase();
        // db.execSQL("DELETE FROM tbl_complain ");
        Log.v("Complain", "Cleared all");
        db.close();
    }

    public void closedVerifiedComplainData(String masterID) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE tbl_complain SET isReplyed =1 WHERE masterId='" + masterID + "'");
        Log.v("Complain", " Verified Data");
        db.close();
    }


    public Cursor getComplainTotalData(String recMasterID) {
        String sql = "SELECT * FROM tbl_complain WHERE masterId ='" + recMasterID + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_complain");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_settings");
        onCreate(sqLiteDatabase);
    }
}
