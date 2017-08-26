package com.d.uiutil.lrc;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.d.lib.ui.lrc.DefaultLrcParser;
import com.d.lib.ui.lrc.LrcRow;
import com.d.lib.ui.lrc.LrcView;
import com.d.uiutil.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LrcActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private LrcView lrcvLrc;
    private SeekBar sbProgress;
    private SeekBar sbScale;
    private Button btnPlayPause;
    private MediaPlayer player;
    private boolean isPressed;

    private boolean isRunning;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) {
                return;
            }
            setProgress();
            handler.postDelayed(runnable, 500);
        }
    };

    private void setProgress() {
        if (!isPressed) {
            sbProgress.setMax(player.getDuration());
            sbProgress.setProgress(player.getCurrentPosition());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        lrcvLrc = (LrcView) findViewById(R.id.lrcv_lrc);
        sbProgress = (SeekBar) findViewById(R.id.sb_progress);
        sbScale = (SeekBar) findViewById(R.id.sb_scale);
        btnPlayPause = (Button) findViewById(R.id.btn_play_pause);
        btnPlayPause.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
        sbScale.setOnSeekBarChangeListener(this);
        sbScale.setMax(100);
        sbScale.setProgress(50);
        initPlayer();
        initLrc();
    }

    private void initLrc() {
        lrcvLrc.setOnSeekChangeListener(new LrcView.OnSeekChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                player.seekTo(progress);
                setProgress();
            }
        });
        lrcvLrc.setOnClickListener(new LrcView.OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(LrcActivity.this, "click lrc!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPlayer() {
        player = MediaPlayer.create(this, R.raw.huasha);
        player.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopTimer();
        player.seekTo(0);
        sbProgress.setProgress(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbProgress) {
            lrcvLrc.seekTo(progress, fromUser);
        } else if (seekBar == sbScale && fromUser) {
            lrcvLrc.setLrcScale(progress / 100f);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == sbProgress) {
            isPressed = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == sbProgress) {
            isPressed = false;
            player.seekTo(seekBar.getProgress());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_pause:
                if ("play".equals(btnPlayPause.getText())) {
                    player.start();
                    lrcvLrc.setLrcRows(getLrcRows());
                    btnPlayPause.setText("暂停");
                    reStartTimer();
                } else {
                    if (player.isPlaying()) {
                        player.pause();
                        btnPlayPause.setText("播放");
                        stopTimer();
                    } else {
                        player.start();
                        btnPlayPause.setText("暂停");
                        reStartTimer();
                    }
                }
                break;
        }
    }

    private List<LrcRow> getLrcRows() {
        List<LrcRow> rows = null;
        InputStream is = getResources().openRawResource(R.raw.hs);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = DefaultLrcParser.getInstance().getLrcRows(sb.toString());
            Log.d("lrc", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private void reStartTimer() {
        stopTimer();
        isRunning = true;
        handler.post(runnable);
    }

    private void stopTimer() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (lrcvLrc != null) {
            lrcvLrc.reset();
        }
        super.onDestroy();
    }
}
