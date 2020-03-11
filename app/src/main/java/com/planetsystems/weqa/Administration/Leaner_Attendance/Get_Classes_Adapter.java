package com.planetsystems.weqa.Administration.Leaner_Attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.planetsystems.weqa.R;

import java.util.ArrayList;


public class Get_Classes_Adapter extends ArrayAdapter<Get_Classes> {
    ArrayList<Get_Classes> proList;
    LayoutInflater vi;
    int Resource;

    ViewHolder holder;

    public Get_Classes_Adapter(Context context, int resource, ArrayList<Get_Classes> objects) {
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
            holder.name = (TextView) v.findViewById(R.id.name);
            holder.id = (TextView) v.findViewById(R.id.category);
            holder.imageView = (ImageView) v.findViewById(R.id.personphoto);

            //holder.aSwitch=(Switch) v.findViewById(R.id.switch1);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.name.setText(proList.get(position).getCode());

        String L= ""+proList.get(position).getCode();
        char k =proList.get(position).getCode().charAt(1);
        String Cap= ""+k+"".toString().toUpperCase();
        String s = Character.toString(k);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use

        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRoundRect(""+s.toUpperCase(),color1,60); //radius in px

        holder.imageView.setImageDrawable(drawable);

        return v;

    }

    static class ViewHolder {
        public TextView name;
        public TextView id;
        public ImageView imageView;
        //public Switch aSwitch;

        }

}


