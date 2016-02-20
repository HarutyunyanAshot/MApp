package com.mapps.mapp.utils;

import android.content.res.Resources;

import com.mapps.mapp.items.BgItem;
import com.mapps.mapp.items.DrawItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Mariam on 2/19/16.
 */
public class JsonUtils {
    public static final String JSON_PROP_NAME = "name";
    public static final String JSON_PROP_SIZE = "size";
    public static final String JSON_PROP_COLOR = "color";
    public static final String JSON_PROP_IMAGE = "image";
    public static final String JSON_PROP_DETAIL_IMAGES = "detail_images";


    public static ArrayList<DrawItem> getCategories(Resources res, String jsonName) {
        ArrayList<DrawItem> categories = new ArrayList<>();
        InputStream input = null;
        JSONArray jsonArray = null;
        try {
            input = res.getAssets().open(jsonName);
            jsonArray = new JSONArray(convertStreamToString(input));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject drawCategory = jsonArray.getJSONObject(i);
                String categoryName = drawCategory.getString(JSON_PROP_NAME);
                String categoryImageName = drawCategory.getString(JSON_PROP_IMAGE);
                String categoryImagePath = "images/" + categoryImageName;
                JSONArray categoryImages = drawCategory.getJSONArray(JSON_PROP_DETAIL_IMAGES);
                ArrayList<BgItem> bgItems = new ArrayList<>();
                BgItem item;
                for (int j = 0; j < categoryImages.length(); j++) {
                    item = new BgItem();
                    item.setSize(categoryImages.getJSONObject(j).getInt(JSON_PROP_SIZE));
                    item.setColor(categoryImages.getJSONObject(j).getInt(JSON_PROP_COLOR));
                    item.setPath("images/" + categoryName + "/" + categoryImages.getJSONObject(j).getString(JSON_PROP_NAME));
                    bgItems.add(item);
                }
                categories.add(new DrawItem(categoryName, categoryImagePath, bgItems));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }


    private static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
