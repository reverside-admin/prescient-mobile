package za.co.prescient.activity;


//import static za.co.prescient.activity.model.Constant.FIFTH_COLUMN;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import za.co.prescient.R;

import java.util.ArrayList;
import java.util.HashMap;

import static za.co.prescient.activity.model.Constant.*;


public class ViewCheckedInGuestListAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;
    Context context;
    LayoutInflater inflater1;

    public ViewCheckedInGuestListAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView colFirst;
        TextView colSecond;
        TextView colThird;
        TextView colFourth;
        TextView colFifth;
        TextView colSixth;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("call is done");
        final int pos = position;
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        inflater1 = inflater;

        context = inflater.getContext();
        final SessionManager session = new SessionManager(context);

        if (convertView == null) {

            //mrunmaya
            convertView = inflater.inflate(R.layout.checked_in_guest_list_view, null);
            holder = new ViewHolder();
            holder.colFirst = (TextView) convertView.findViewById(R.id.FirstCol);
            holder.colSecond = (TextView) convertView.findViewById(R.id.SecondCol);
            holder.colThird = (TextView) convertView.findViewById(R.id.ThirdCol);
            holder.colFourth = (TextView) convertView.findViewById(R.id.FourthCol);
            holder.colFifth = (TextView) convertView.findViewById(R.id.FifthCol);
            holder.colSixth = (TextView) convertView.findViewById(R.id.SixthCol);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.colFirst.setText((String) map.get(FIRST_COLUMN));
        holder.colSecond.setText((String) map.get(SECOND_COLUMN));
        holder.colThird.setText((String) map.get(THIRD_COLUMN));
        holder.colFourth.setText((String) map.get(FOURTH_COLUMN));
        holder.colFifth.setText((String)map.get(FIFTH_COLUMN));
        holder.colSixth.setText((String)map.get(SIXTH_COLUMN));

        return convertView;
    }


}