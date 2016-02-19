package com.mapps.mapp.utils;

import android.content.res.Resources;

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
 * Created by intern on 2/19/16.
 */
public class JsonUtils {
    public static final String JSON_PROP_NAME = "name";
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
                categories.add(new DrawItem(categoryName,categoryImagePath ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static String getCategoryImagePath(JSONArray jsonArray, int index, String categoryName) {
        String imageName = null;

        try {
            imageName = jsonArray.getJSONObject(index).getString(JSON_PROP_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String path = categoryName + "/" + imageName;
        return path;
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
