package com.d.ui.view.lrc;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.view.lrc.DefaultLrcParser;
import com.d.lib.ui.view.lrc.LrcRow;
import com.d.lib.ui.view.lrc.LrcView;
import com.d.ui.view.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LrcActivity extends Activity
        implements MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    private LrcView lrcv_lrc;
    private SeekBar sb_progress;
    private SeekBar sb_scale;
    private TextView tv_play_pause;
    private MediaPlayer mPlayer;
    private boolean mIsPressed;

    private boolean mIsRunning;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsRunning) {
                return;
            }
            setProgress();
            mHandler.postDelayed(mRunnable, 500);
        }
    };

    private void setProgress() {
        if (!mIsPressed) {
            sb_progress.setMax(mPlayer.getDuration());
            sb_progress.setProgress(mPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_title_left == resId) {
            finish();

        } else if (R.id.tv_play_pause == resId) {
            if ("Play".equals(tv_play_pause.getText())) {
                mPlayer.start();
                lrcv_lrc.setLrcRows(getLrcRows());
                tv_play_pause.setText("暂停");
                reStartTimer();
            } else {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    tv_play_pause.setText("播放");
                    stopTimer();
                } else {
                    mPlayer.start();
                    tv_play_pause.setText("暂停");
                    reStartTimer();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        bindView();
        init();
    }

    private void bindView() {
        lrcv_lrc = ViewHelper.findViewById(this, R.id.lrcv_lrc);
        sb_progress = ViewHelper.findViewById(this, R.id.sb_progress);
        sb_scale = ViewHelper.findViewById(this, R.id.sb_scale);
        tv_play_pause = ViewHelper.findViewById(this, R.id.tv_play_pause);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left,
                R.id.tv_play_pause);
    }

    private void init() {
        sb_progress.setOnSeekBarChangeListener(this);
        sb_scale.setOnSeekBarChangeListener(this);
        sb_scale.setMax(100);
        sb_scale.setProgress(50);
        initPlayer();
        initLrc();
    }

    private void initLrc() {
        lrcv_lrc.setOnSeekChangeListener(new LrcView.OnSeekChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                mPlayer.seekTo(progress);
                setProgress();
            }
        });
        lrcv_lrc.setOnClickListener(new LrcView.OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(LrcActivity.this,
                        "Click lrc!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPlayer() {
        mPlayer = MediaPlayer.create(this, R.raw.huasha);
        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopTimer();
        mPlayer.seekTo(0);
        sb_progress.setProgress(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sb_progress) {
            lrcv_lrc.seekTo(progress, fromUser);
        } else if (seekBar == sb_scale && fromUser) {
            lrcv_lrc.setLrcScale(progress / 100f);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == sb_progress) {
            mIsPressed = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == sb_progress) {
            mIsPressed = false;
            mPlayer.seekTo(seekBar.getProgress());
        }
    }

    private List<LrcRow> getLrcRows() {
        List<LrcRow> rows = null;
        InputStream is = getResources().openRawResource(R.raw.huashalrc);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = DefaultLrcParser.getLrcRows(sb.toString());
            Log.d("Lrc", sb.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return rows;
    }

    private void reStartTimer() {
        stopTimer();
        mIsRunning = true;
        mHandler.post(mRunnable);
    }

    private void stopTimer() {
        mIsRunning = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (lrcv_lrc != null) {
            lrcv_lrc.reset();
        }
        super.onDestroy();
    }
}
