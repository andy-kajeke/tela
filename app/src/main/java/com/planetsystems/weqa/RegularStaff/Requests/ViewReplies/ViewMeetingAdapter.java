package com.planetsystems.weqa.RegularStaff.Requests.ViewReplies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.planetsystems.weqa.Administration.Service_Requests.Meetings_.MeetingModel;
import com.planetsystems.weqa.Administration.Service_Requests.Time_Off.TimeOffModel;
import com.planetsystems.weqa.R;

import java.util.ArrayList;

public class ViewMeetingAdapter extends ArrayAdapter<MeetingModel> {
    ArrayList<MeetingModel> proList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public ViewMeetingAdapter(Context context, int resource, ArrayList<MeetingModel> objects) {
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
            holder.description = (TextView) v.findViewById(R.id.taskDescription);
            holder.startdate = (TextView) v.findViewById(R.id.startdate);
            holder.enddate = (TextView) v.findViewById(R.id.endtdate);
            holder.teacher = (TextView) v.findViewById(R.id.teacherName);
            holder.confirmReply = (TextView) v.findViewById(R.id.confirmation);
            holder.approvalDate = (TextView) v.findViewById(R.id.repliedDate);
            holder.imageView=(ImageView)v.findViewById(R.id.personphoto);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.description.setText(proList.get(position).getTypeOfLeave());
        holder.confirmReply.setText(proList.get(position).getApprovalStatus());
        holder.enddate.setText(proList.get(position).getRequestDate());
        holder.teacher.setText(proList.get(position).getEmployee());
        holder.approvalDate.setText(proList.get(position).getApprovalDate());

        String L = ""+proList.get(position).getTypeOfLeave();
        char k = proList.get(position).getTypeOfLeave().charAt(0);
        String Cap = ""+k+"".toString().toUpperCase();
        String s = Character.toString(k);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use

        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRoundRect(""+s.toUpperCase(),color1,60); //radius in px

        holder.imageView.setImageDrawable(drawable);

        return v;

    }

    static class ViewHolder {
        public TextView description;
        public TextView startdate;
        public TextView enddate;
        public TextView teacher;
        public TextView confirmReply;
        public TextView approvalDate;
        public ImageView imageView;

        }

}


