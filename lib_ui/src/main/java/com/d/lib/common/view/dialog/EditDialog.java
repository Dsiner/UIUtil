package com.d.lib.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.d.lib.common.R;
import com.d.lib.common.view.ClearEditText;

/**
 * EditDialog
 * Created by D on 2018/6/15.
 */
public class EditDialog extends AbstractDialog {
    private String title;
    private String content;
    private OnEditListener listener;

    public EditDialog(Context context, String title, String content) {
        super(context, R.style.lib_pub_dialog_style, false, 0, 0, 0);
        this.title = title;
        this.content = content;
        initView(rootView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.lib_pub_dialog_edit;
    }

    @Override
    protected void init(View rootView) {

    }

    protected void initView(View rootView) {
        TextView tvOk = (TextView) rootView.findViewById(R.id.btn_ok);
        TextView tvCancle = (TextView) rootView.findViewById(R.id.btn_cancel);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        final ClearEditText cetEdit = (ClearEditText) rootView.findViewById(R.id.cet_edit);
        cetEdit.setText(!TextUtils.isEmpty(content) ? content : "");
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSubmit(EditDialog.this, cetEdit.getText().toString());
                }
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCancel(EditDialog.this);
                }
            }
        });
    }

    public interface OnEditListener {

        /**
         * Click ok
         *
         * @param content content
         */
        void onSubmit(Dialog dlg, String content);

        /**
         * Click cancel
         */
        void onCancel(Dialog dlg);
    }

    public void setOnEditListener(OnEditListener listener) {
        this.listener = listener;
    }
}
