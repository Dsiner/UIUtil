package com.d.ui.layout.heartlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.layout.heartlayout.TCChatEntity;
import com.d.lib.ui.layout.heartlayout.TCHeartLayout;
import com.d.ui.layout.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeartActivity extends Activity implements View.OnClickListener {
    private String[] mName = new String[]{"Tom:", "Lily:", "Hornoo:", "Bill:"};
    private String[] mContent = new String[]{"frequent", "audience", "essential", "glass", "inch", "animal",
            "discussion", "attend", "encourage", "shoulder", "repeat", "straight", "importance", "mountain"};
    private Random mRandom = new Random();
    private TCHeartLayout tch_pr;
    private ListView lv_msg;
    private TCChatMsgListAdapter mAdapter;
    private List<TCChatEntity> mDatas = new ArrayList<>();

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_title_left == resId) {
            finish();
        }
        if (R.id.btn_click == resId) {
            tch_pr.addFavor();
            handleTextMsg(mName[mRandom.nextInt(mName.length)],
                    mContent[mRandom.nextInt(mContent.length)]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        bindView();
        init();
    }

    private void bindView() {
        tch_pr = ViewHelper.findViewById(this, R.id.tch_pr);
        lv_msg = (ListView) findViewById(R.id.lv_msg);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left,
                R.id.btn_click);
    }

    private void init() {
        mAdapter = new TCChatMsgListAdapter(this, lv_msg, mDatas);
        lv_msg.setAdapter(mAdapter);
    }

    public void handleTextMsg(String nickname, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(nickname);
        entity.setContext(text);
        entity.setType(0);
        notifyMsg(entity);
    }

    private void notifyMsg(final TCChatEntity entity) {
        mDatas.add(entity);
        mAdapter.notifyDataSetChanged();
    }
}
