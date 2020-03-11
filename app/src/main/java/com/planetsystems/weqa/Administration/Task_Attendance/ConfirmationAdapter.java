package com.planetsystems.weqa.Administration.Task_Attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.planetsystems.weqa.R;
import com.planetsystems.weqa.RegularStaff.Tasks;
import java.util.ArrayList;


public class ConfirmationAdapter extends ArrayAdapter<Tasks> {
    ArrayList<Tasks> proList;
    LayoutInflater vi;
    int Resource;

    ViewHolder holder;

    public ConfirmationAdapter(Context context, int resource, ArrayList<Tasks> objects) {
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
            holder.imageView = (ImageView)v.findViewById(R.id.personphoto);
            holder.aSwitch = (Switch) v.findViewById(R.id.switch1);
            holder.timeSwitch = (Switch) v.findViewById(R.id.switch2);
//            holder.extraTime = (RadioButton) v.findViewById(R.id.extraTym);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.description.setText(proList.get(position).getTaskDescription());
        holder.startdate.setText(proList.get(position).getStartTime());
        holder.enddate.setText(proList.get(position).getEndTime());
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {
                    proList.get(position).setTaughtNotTaught("Not taught");
                    Toast.makeText(getContext(),""+proList.get(position).getTaughtNotTaught(),Toast.LENGTH_SHORT).show();
                } else {
                    proList.get(position).setTaughtNotTaught("Taught");
                    Toast.makeText(getContext(),""+proList.get(position).getTaughtNotTaught(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (!bChecked) {
                    proList.get(position).setInTime("Past Time");
                    Toast.makeText(getContext(),""+proList.get(position).getInTime(),Toast.LENGTH_SHORT).show();
                } else {
                    proList.get(position).setInTime("In Time");
                    Toast.makeText(getContext(),""+proList.get(position).getInTime(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        String L = ""+proList.get(position).getTaskDescription();
        char k = proList.get(position).getTaskDescription().charAt(3);
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
        public ImageView imageView;
        public Switch aSwitch, timeSwitch;
        public RadioButton inTime;
        public RadioButton extraTime;

    }

}


