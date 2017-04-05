package com.akitektuo.cheffy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.activity.RecipeActivity;

import java.util.List;

import static com.akitektuo.cheffy.util.Constant.KEY_NAME;
import static com.akitektuo.cheffy.util.Tool.getImage;

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
        final RecipeItem item = items.get(position);
        viewHolder.image.setImageBitmap(getImage(context, item.getImage()));
        viewHolder.text.setText(item.getName());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra(KEY_NAME, item.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        ImageView image;
        TextView text;

        ViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout_item_parent);
            image = (ImageView) view.findViewById(R.id.image_item_recipe);
            text = (TextView) view.findViewById(R.id.text_item_name);
        }
    }
}
