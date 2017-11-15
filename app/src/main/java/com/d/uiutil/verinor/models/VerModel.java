package com.d.uiutil.verinor.models;

import com.d.uiutil.verinor.adapter.VerAdapter;

import java.util.List;

/**
 * Created by D on 2017/1/4.
 */
public class VerModel extends VerAdapter.OffsetBean {
    public int type;
    public String content = "";
    public List<VerInorModel> datas;

    public VerModel(int type) {
        this.type = type;
    }
}
