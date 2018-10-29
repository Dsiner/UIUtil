package com.d.uiutil.progress;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.ui.view.progress.SnapProgressBar;
import com.d.uiutil.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by D on 2017/11/1.
 */
public class SnapProgressActivity extends Activity {
    private SnapProgressBar[] spbarSnaps;
    private Random random = new Random();
    private Handler handler;
    private Task task;
    private boolean isRunning;
    private List<MediaInfo> datas = new ArrayList<>();
    private int index;
    private int count;

    static class Task implements Runnable {
        WeakReference<SnapProgressActivity> weakRef;

        Task(SnapProgressActivity activity) {
            weakRef = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            if (isFinished()) {
                return;
            }
            SnapProgressActivity theActivity = weakRef.get();
            theActivity.doTask();
            theActivity.handler.postDelayed(theActivity.task, 1000);
        }

        private boolean isFinished() {
            return weakRef == null || weakRef.get() == null
                    || !weakRef.get().isRunning
                    || weakRef.get().isFinishing();
        }
    }

    private void doTask() {
        if (datas.size() <= 0) {
            return;
        }
        float progress = 1f * random.nextInt(100) / 100;
        progress = Math.max(0, progress);
        progress = Math.min(1, progress);
        progress = 0.33f * count + 0.33f * progress;

        MediaInfo media = datas.get(index);
        spbarSnaps[4].setState(SnapProgressBar.STATE_PROGRESS).
                thumb(Presenter.getThumb(getApplicationContext(), media.id))
                .progress(progress);

        if (++count > 2) {
            count = 0;
            index++;
            if (index >= datas.size()) {
                index = 0;
            }
        }
    }

    private void restartTask() {
        stopTask();
        handler.postDelayed(task, 1000);
        isRunning = true;
    }

    private void stopTask() {
        isRunning = false;
        handler.removeCallbacks(task);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_snap);
        initView();
    }

    private void initView() {
        handler = new Handler();
        task = new Task(this);
        spbarSnaps = new SnapProgressBar[]{(SnapProgressBar) findViewById(R.id.spbar_snap_scanning),
                (SnapProgressBar) findViewById(R.id.spbar_snap_padding),
                (SnapProgressBar) findViewById(R.id.spbar_snap_done),
                (SnapProgressBar) findViewById(R.id.spbar_snap_error),
                (SnapProgressBar) findViewById(R.id.spbar_snap_progress)};
        spbarSnaps[0].setState(SnapProgressBar.STATE_SCANNING);
        spbarSnaps[1].setState(SnapProgressBar.STATE_PENDDING);
        spbarSnaps[2].setState(SnapProgressBar.STATE_DONE);
        spbarSnaps[3].setState(SnapProgressBar.STATE_ERROR);
        spbarSnaps[4].setState(SnapProgressBar.STATE_PROGRESS).progress(0);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<MediaInfo> list = Presenter.getPhotos(getApplicationContext());
                        if (list.size() > 0) {
                            index = 0;
                            datas.addAll(list);
                            restartTask();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopTask();
        super.onDestroy();
    }

    static class Presenter {
        static Bitmap getThumb(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight());
        }

        static Bitmap getThumb(Context context, String id) {
            return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                    Long.parseLong(id),
                    MediaStore.Images.Thumbnails.MICRO_KIND,
                    new BitmapFactory.Options());
        }

        static List<MediaInfo> getPhotos(Context context) {
            List<MediaInfo> datas = new ArrayList<>();
            String selection = MediaStore.Files.FileColumns.DATA + " LIKE '%" + "/DCIM/Camera/" + "%' ";
            Uri uri = MediaStore.Files.getContentUri("external");
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns._ID},
                    selection,
                    null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        datas.add(new MediaInfo(id, path));
                    } while (cursor.moveToNext());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return datas;
        }
    }

    static class MediaInfo {
        public String id;
        public String path;

        public MediaInfo(String id, String path) {
            this.id = id;
            this.path = path;
        }
    }
}
