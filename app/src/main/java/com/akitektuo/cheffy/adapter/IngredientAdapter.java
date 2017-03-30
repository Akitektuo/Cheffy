package com.akitektuo.cheffy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.akitektuo.cheffy.R;

import java.util.List;

/**
 * Created by AoD Akitektuo on 30-Mar-17.
 */

public class IngredientAdapter extends ArrayAdapter<IngredientItem> {
    private Context context;

    private List<IngredientItem> items;

    public IngredientAdapter(Context context, List<IngredientItem> objects) {
        super(context, R.layout.item_ingredient, objects);
        this.context = context;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_ingredient, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        IngredientItem item = items.get(position);
        holder.textIngredient.setText(item.getIngredient());
        holder.textQuantity.setText(item.getQuantity());
        return view;
    }

    private class ViewHolder {
        TextView textIngredient;
        TextView textQuantity;

        ViewHolder(View view) {
            textIngredient = (TextView) view.findViewById(R.id.text_item_ingredient);
            textQuantity = (TextView) view.findViewById(R.id.text_item_quantity);
        }
    }
}
