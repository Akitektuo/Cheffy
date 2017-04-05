package com.akitektuo.cheffy.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.akitektuo.cheffy.util.Constant.LIST_SEPARATOR;

/**
 * Created by Akitektuo on 01-Apr-17 at 21:01.
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

    public static void saveImage(Context context, Bitmap image, String name) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("image_food", Context.MODE_PRIVATE);
        File myPath = new File(directory, name + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getImage(Context context, String name) {
        Bitmap bitmap = null;
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("image_food", Context.MODE_PRIVATE);
        try {
            File f = new File(directory, name + ".jpg");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
