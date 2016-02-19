package com.mapps.mapp.items;

import java.util.ArrayList;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawItem {
    public String imagePath;
    public String itemName;
    private ArrayList<String> imageDetailsPaths;


    public DrawItem( String itemName, String imagePath) {
        this.imagePath = imagePath;
        this.itemName = itemName;
    }
    public ArrayList<String> getImageDetailsPaths(){
        imageDetailsPaths = new ArrayList<>();
        //TODO staff
        return imageDetailsPaths;
    }
}
