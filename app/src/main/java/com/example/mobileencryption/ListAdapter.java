package com.example.mobileencryption;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> ID;
    ArrayList<String> Text;



    public ListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> text
    )
    {
        this.context = context2;
        this.ID = id;
        this.Text = text;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.row, null);

            holder = new Holder();

            holder.ID_TextView = child.findViewById(R.id.row_id);
            holder.Name_TextView = child.findViewById(R.id.back);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.ID_TextView.setText(ID.get(position));
        holder.Name_TextView.setText(Text.get(position));
        return child;
    }

    public class Holder {

        TextView ID_TextView;
        TextView Name_TextView;
        Button edit, delete;
    }

}
