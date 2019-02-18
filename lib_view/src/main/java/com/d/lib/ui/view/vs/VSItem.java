package com.d.lib.ui.view.vs;

/**
 * VSItem
 * Created by D on 2017/4/19.
 */
public class VSItem {
    public String mainText;
    public String percent;
    public boolean isChecked;

    public VSItem(String text, boolean checked) {
        this.mainText = text;
        this.isChecked = checked;
    }
}
