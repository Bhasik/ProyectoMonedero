package com.proyecto.alberto.monedero;

/**
 * Created by user on 15/04/2015.
 */
public class ItemMenuLateral {


    private String name;
    private int iconId;

    public ItemMenuLateral(String name, int iconId) {

        this.name = name;
        this.iconId = iconId;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

}
