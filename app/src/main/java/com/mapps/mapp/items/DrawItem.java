package com.mapps.mapp.items;

import java.util.ArrayList;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawItem {
    public String imagePath;
    public String itemName;
    public ArrayList<BgItem> bgItems;


    public DrawItem( String itemName, String imagePath, ArrayList<BgItem> bgItems) {
        this.imagePath = imagePath;
        this.itemName = itemName;
        this.bgItems = bgItems;
    }
}
