package com.mapps.mapp.items;

import java.util.ArrayList;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawItem {
    public String imagePath;
    public String itemName;
    public ArrayList<String> imageDetailsPaths;


    public DrawItem( String itemName, String imagePath, ArrayList<String> imageDetailsPaths) {
        this.imagePath = imagePath;
        this.itemName = itemName;
        this.imageDetailsPaths = imageDetailsPaths;
    }
}
