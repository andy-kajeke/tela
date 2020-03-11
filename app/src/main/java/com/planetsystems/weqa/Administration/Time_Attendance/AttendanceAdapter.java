package com.planetsystems.weqa.Administration.Time_Attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.planetsystems.weqa.R;

import java.util.ArrayList;


public class AttendanceAdapter extends ArrayAdapter<attendanceModel> {
    ArrayList<attendanceModel> proList;
    LayoutInflater vi;
    int Resource;

    ViewHolder holder;

    public AttendanceAdapter(Context context, int resource, ArrayList<attendanceModel> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        proList = objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();

            v = vi.inflate(Resource, null);
            holder.name = (TextView) v.findViewById(R.id.emp_name);
            holder.id = (TextView) v.findViewById(R.id.emp_id);
            holder.clock_in = (TextView) v.findViewById(R.id.emp_timeIn);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.name.setText(proList.get(position).getName());
        holder.id.setText(proList.get(position).getId());
        holder.clock_in.setText(proList.get(position).getClockIn());

        return v;

    }

    static class ViewHolder {
        public TextView name;
        public TextView id;
        public TextView clock_in;


    }

}


