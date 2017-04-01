package com.akitektuo.cheffy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.akitektuo.cheffy.util.Constant.LIST_SEPARATOR;

/**
 * Created by AoD Akitektuo on 01-Apr-17.
 */

public class Tool {
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }
        return false;
    }

    public static String convertListToString(ArrayList<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str).append(LIST_SEPARATOR);
        }
        builder.delete(builder.lastIndexOf(LIST_SEPARATOR), builder.lastIndexOf(LIST_SEPARATOR) + LIST_SEPARATOR.length() + 1);
        return builder.toString();
    }

    public static ArrayList<String> convertStringToList(String str) {
        return new ArrayList<>(Arrays.asList(str.split(LIST_SEPARATOR)));
    }

    public static Bitmap getBitmapForName(Context context, String image) {
        Bitmap resizedBitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(image, "drawable", context.getPackageName()));
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return resizedBitmap;
    }
}
