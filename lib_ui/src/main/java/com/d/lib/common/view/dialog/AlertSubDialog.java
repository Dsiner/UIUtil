package com.d.lib.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.d.lib.common.R;

/**
 * AlertSubDialog
 * Created by D on 2018/6/15.
 */
public class AlertSubDialog extends AbstractDialog {
    private String title;
    private String content;
    private String subTips;
    private boolean isChecked;
    private OnCheckListener listener;

    public AlertSubDialog(Context context, String title, String content, String subTips, boolean isChecked) {
        super(context, R.style.lib_pub_dialog_style, true, Gravity.CENTER,
                (int) context.getResources().getDimension(R.dimen.lib_pub_dimen_dialog_width),
                WindowManager.LayoutParams.WRAP_CONTENT);
        this.title = title;
        this.content = content;
        this.subTips = subTips;
        this.isChecked = isChecked;
        initView(rootView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.lib_pub_dialog_check;
    }

    @Override
    protected void init(View rootView) {

    }

    private void initView(View rootView) {
        TextView tvOk = (TextView) rootView.findViewById(R.id.btn_ok);
        TextView tvCancle = (TextView) rootView.findViewById(R.id.btn_cancel);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        TextView tvSubTips = (TextView) rootView.findViewById(R.id.tv_sub_tips);
        final FrameLayout flytToggle = (FrameLayout) rootView.findViewById(R.id.flyt_toggle);
        final CheckBox cbToggle = (CheckBox) rootView.findViewById(R.id.cb_toggle);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        } else {
            tvContent.setVisibility(View.GONE);
        }
        tvSubTips.setText(!TextUtils.isEmpty(subTips) ? subTips : "");
        cbToggle.setChecked(isChecked);
        flytToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !cbToggle.isChecked();
                cbToggle.setChecked(isChecked);
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSubmit(AlertSubDialog.this, isChecked);
                }
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCancel(AlertSubDialog.this);
                }
            }
        });
    }

    public interface OnCheckListener {

        /**
         * Click ok
         *
         * @param isChecked isChecked
         */
        void onSubmit(Dialog dlg, boolean isChecked);

        /**
         * Click cancel
         */
        void onCancel(Dialog dlg);
    }

    public void setOnCheckListener(OnCheckListener listener) {
        this.listener = listener;
    }
}
