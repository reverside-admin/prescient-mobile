package za.co.prescient.activity;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import za.co.prescient.R;
import za.co.prescient.activity.model.TouchPoint;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<TouchPoint> {

    private Activity activity;
    private List<TouchPoint> itemList;


    public ListViewAdapter(Activity activity, int layout, List<TouchPoint> itemList) {
        super(activity, layout, itemList);
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        Log.i("getView() ::", "is called");

        if (convertView == null)
            listItemView = activity.getLayoutInflater().inflate(
                    R.layout.activity_listviewactivity, null);

        TouchPoint touchPoint = this.itemList.get(position);

       // Button itemName = (Button) listItemView.findViewById(R.id.item_name);
        TextView itemName = (TextView) listItemView.findViewById(R.id.item_name);


        itemName.setText(touchPoint.getName());

        return listItemView;
    }
}
