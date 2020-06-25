package crd.lk.ecms.config;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import crd.lk.ecms.MainActivity;
import crd.lk.ecms.R;
import crd.lk.ecms.db.DBHelper;
import crd.lk.ecms.pogo.Complain;

/**
 * Created by Chamila on 08/01/2018.
 */

//public class CustomAdapter extends BaseAdapter {
//    private Activity activity;
//    private ArrayList data;
//    private static LayoutInflater inflater=null;
//    public Resources res;
//    Complain complain=null;
//    int i=0;
//
//
//
//    public CustomAdapter(Activity a, ArrayList d,Resources resLocal){
//        activity = a;
//        data=d;
//        res = resLocal;
//        /***********  Layout inflator to call external xml layout () ***********/
//        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Log.i("Array ",d.size()+"");
//    }
//
//
//    @Override
//    public int getCount() {
//        if(data.size()<=0)
//            return 1;
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return i;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//
//
//    /********* Create a holder Class to contain inflated xml file elements *********/
//    public static class ViewHolder{
//        public TextView text;
//        public TextView textWide;
//    }
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View vi = convertView;
//        ViewHolder holder;
//        if(convertView==null){
//            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
//            vi = inflater.inflate(R.layout.tabitem, null);
//            /****** View Holder Object to contain tabitem.xml file elements ******/
//            holder = new ViewHolder();
//            holder.text = (TextView) vi.findViewById(R.id.text);
//            holder.textWide=(TextView)vi.findViewById(R.id.text1);
//            /************  Set holder with LayoutInflater ************/
//            vi.setTag( holder );
//        }
//        else
//            holder=(ViewHolder)vi.getTag();
//        if(data.size()<=0){
//            holder.text.setText("No Data - Please SYNC to receive new Data");
//        }else {
//            /***** Get each Model object from Arraylist ********/
//            complain =null;
//            complain = ( Complain ) data.get( position );
//
//            /************  Set Model values in Holder elements ***********/
//
//            holder.text.setText( complain.getCompRef() );
//            holder.textWide.setText( complain.getCompSubject() );
//            /******** Set Item Click Listner for LayoutInflater for each row *******/
//
//            vi.setOnClickListener(new OnItemClickListener( position ));
//  //          vi.setOnLongClickListener(new OnItemLongClickListener());
//        }
//        return vi;
//    }
//
//
//    /********* Called when Item click in ListView ************/
//    private class OnItemClickListener  implements View.OnClickListener {
//        private int mPosition;
//
//        OnItemClickListener(int position){
//            mPosition = position;
//        }
//
//        @Override
//        public void onClick(View arg0) {
//            MainActivity sct = (MainActivity) activity;
//            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
//            sct.onItemClick(mPosition);
//        }
//    }
//
//
//    /********* Called when Item click in ListView ************/
///*
//    private class OnItemLongClickListener  implements View.OnLongClickListener {
//        private int mPosition;
//
//        @Override
//        public boolean onLongClick(View view) {
//            MainActivity sct = (MainActivity) activity;
//
//            sct.onItemLongClick(mPosition);
//            return true;
//        }
//
//        public void setmPosition(int mPosition) {
//            this.mPosition = mPosition;
//        }
//    }
//*/
//
//
//}
public class CustomAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Complain> data;
    DBHelper myDb;

    public CustomAdapter(Context context, ArrayList<Complain> data) {
        this.context = context;
        this.data = data;
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        myDb = new DBHelper(this.context);
        Cursor cursor = myDb.ComplainData();
        View v = View.inflate(context,R.layout.tabitem,null);
        TextView text = (TextView) v.findViewById(R.id.text);
        TextView textWide = (TextView) v.findViewById(R.id.text1);

        if(data.isEmpty()){
            text.setText("No Data - Please SYNC to receive new Data");
        }else {

            text.setText(data.get(i).getCompRef());
            textWide.setText(data.get(i).getCompSubject());

            v.setTag(data.get(i).getMasterId());
        }
        return v;
    }
}
