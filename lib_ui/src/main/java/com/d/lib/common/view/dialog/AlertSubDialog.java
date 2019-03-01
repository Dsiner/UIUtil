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
    private String mTitle;
    private String mContent;
    private String mSubTips;
    private boolean mIsChecked;
    private OnCheckListener mListener;

    public AlertSubDialog(Context context, String title, String content, String subTips, boolean isChecked) {
        super(context, R.style.lib_pub_dialog_style, true, Gravity.CENTER,
                (int) context.getResources().getDimension(R.dimen.lib_pub_dimen_dialog_width),
                WindowManager.LayoutParams.WRAP_CONTENT);
        this.mTitle = title;
        this.mContent = content;
        this.mSubTips = subTips;
        this.mIsChecked = isChecked;
        initView(mRootView);
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
        TextView tvCancel = (TextView) rootView.findViewById(R.id.btn_cancel);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        TextView tvSubTips = (TextView) rootView.findViewById(R.id.tv_sub_tips);
        final FrameLayout flytToggle = (FrameLayout) rootView.findViewById(R.id.flyt_toggle);
        final CheckBox cbToggle = (CheckBox) rootView.findViewById(R.id.cb_toggle);
        if (!TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mContent)) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(mContent);
        } else {
            tvContent.setVisibility(View.GONE);
        }
        tvSubTips.setText(!TextUtils.isEmpty(mSubTips) ? mSubTips : "");
        cbToggle.setChecked(mIsChecked);
        flytToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsChecked = !cbToggle.isChecked();
                cbToggle.setChecked(mIsChecked);
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onSubmit(AlertSubDialog.this, mIsChecked);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onCancel(AlertSubDialog.this);
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
        this.mListener = listener;
    }
}
