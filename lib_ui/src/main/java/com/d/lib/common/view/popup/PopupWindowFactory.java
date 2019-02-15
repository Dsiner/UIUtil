package com.d.lib.common.view.popup;

import android.content.Context;

import java.util.List;

public class PopupWindowFactory {
    private Context mContext;

    private PopupWindowFactory(Context context) {
        this.mContext = context;
    }

    public static PopupWindowFactory createFactory(Context context) {
        return new PopupWindowFactory(context);
    }

    public MenuPopup getMenuPopup(List<MenuPopup.Bean> datas, MenuPopup.OnMenuListener l) {
        MenuPopup popup = new MenuPopup(mContext, datas);
        popup.setOnMenuListener(l);
        return popup;
    }
}
