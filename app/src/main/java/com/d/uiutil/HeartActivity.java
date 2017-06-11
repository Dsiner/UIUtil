package com.d.uiutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.d.uiutil.heartlayout.TCChatEntity;
import com.d.uiutil.heartlayout.TCChatMsgListAdapter;
import com.d.uiutil.heartlayout.TCHeartLayout;

import java.util.ArrayList;
import java.util.Random;

public class HeartActivity extends AppCompatActivity {
    private String[] name = new String[]{"Tom:", "Lily:", "Hornoo:", "Bill:"};
    private String[] content = new String[]{"frequent", "audience", "essential", "glass", "inch", "animal",
            "discussion", "attend", "encourage", "shoulder", "repeat", "straight", "importance", "mountain"};
    private Random random = new Random();
    private TCHeartLayout tcHeartLayout;
    private TCChatMsgListAdapter adapter;
    private ArrayList<TCChatEntity> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        tcHeartLayout = (TCHeartLayout) findViewById(R.id.tch_pr);

        ListView listview = (ListView) findViewById(R.id.lv_msg);
        adapter = new TCChatMsgListAdapter(this, listview, datas);
        listview.setAdapter(adapter);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcHeartLayout.addFavor();
                handleTextMsg(name[random.nextInt(name.length)], content[random.nextInt(content.length)]);
            }
        });
    }

    public void handleTextMsg(String nickname, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(nickname);
        entity.setContext(text);
        entity.setType(0);
        notifyMsg(entity);
    }

    private void notifyMsg(final TCChatEntity entity) {
        datas.add(entity);
        adapter.notifyDataSetChanged();
    }
}
