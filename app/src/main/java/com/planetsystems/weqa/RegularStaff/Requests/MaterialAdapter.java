package com.planetsystems.weqa.RegularStaff.Requests;

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


public class MaterialAdapter extends ArrayAdapter<MaterialModel> {
    ArrayList<MaterialModel> proList;
    LayoutInflater vi;
    int Resource;

    ViewHolder holder;

    public MaterialAdapter(Context context, int resource, ArrayList<MaterialModel> objects) {
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
            holder.It_name = (TextView) v.findViewById(R.id.category);
            holder.It_code = (TextView) v.findViewById(R.id.name);
            holder.imageView = (ImageView) v.findViewById(R.id.personphoto);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.It_name.setText(proList.get(position).getItemName());
        holder.It_code.setText(proList.get(position).getItemCode());

        String L= ""+proList.get(position).getItemName();
        char k =proList.get(position).getItemName().charAt(0);
        String Cap= ""+k+"".toString().toUpperCase();
        String s = Character.toString(k);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use

        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRoundRect(""+s.toUpperCase(),color1,60); //radius in px

        holder.imageView.setImageDrawable(drawable);

        return v;

    }

    static class ViewHolder {
        public TextView It_name;
        public TextView It_code;
        public ImageView imageView;
        }

}


