package com.akitektuo.evozon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akitektuo.evozon.R;

import java.util.List;

/**
 * Created by AoD Akitektuo on 29-Mar-17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;

    private List<RecipeItem> items;

    public RecipeAdapter(Context context, List<RecipeItem> objects) {
        this.context = context;
        items = objects;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder viewHolder, int position) {
        RecipeItem item = items.get(position);
        viewHolder.image.setImageBitmap(item.getBitmap());
        viewHolder.text.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image_item_recipe);
            text = (TextView) view.findViewById(R.id.text_item_name);
        }
    }
}
