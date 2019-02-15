package com.d.lib.common.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.d.lib.common.R;

import java.util.List;

/**
 * AlertDialogFactory
 * Created by D on 2017/4/29.
 */
public class AlertDialogFactory {
    private Context mContext;

    private AlertDialogFactory(Context context) {
        this.mContext = context;
    }

    public static AlertDialogFactory createFactory(Context context) {
        return new AlertDialogFactory(context);
    }

    /**
     * LoadingDialog
     */
    public AlertDialog getLoadingDialog() {
        return getLoadingDialog(null);
    }

    /**
     * LoadingDialog
     */
    public AlertDialog getLoadingDialog(String text) {
        final AlertDialog dlg = new AlertDialog
                .Builder(new ContextThemeWrapper(mContext, R.style.lib_pub_dialog_style))
                .create();
        if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
            dlg.show();
        }
        dlg.setContentView(R.layout.lib_pub_dialog_loading);
        TextView tips = (TextView) dlg.findViewById(R.id.tv_tips);
        if (text != null) {
            tips.setText(text);
        }
        return dlg;
    }

    public AlertDialog getAlertDialog(String title, String content,
                                      String btnOkText, String btnCancelText,
                                      final OnClickListener btnOkListener,
                                      final OnClickListener btnCancelListener) {
        final AlertDialog dlg = new AlertDialog
                .Builder(new ContextThemeWrapper(mContext, R.style.lib_pub_dialog_style))
                .create();
        if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
            dlg.show();
        }
        dlg.setContentView(R.layout.lib_pub_dialog);

        TextView tv_title = (TextView) dlg.findViewById(R.id.tv_title);
        tv_title.setVisibility(!TextUtils.isEmpty(title) ? View.VISIBLE : View.GONE);
        tv_title.setText(!TextUtils.isEmpty(title) ? title : "");

        TextView tv_content = (TextView) dlg.findViewById(R.id.tv_content);
        tv_content.setVisibility(!TextUtils.isEmpty(content) ? View.VISIBLE : View.GONE);
        tv_content.setText(!TextUtils.isEmpty(content) ? content : "");

        Button btnOk = (Button) dlg.findViewById(R.id.btn_ok);
        btnOk.setText(!TextUtils.isEmpty(btnOkText) ? btnOkText : mContext.getResources().getString(R.string.lib_pub_ok));

        Button btnCancel = (Button) dlg.findViewById(R.id.btn_cancel);
        btnCancel.setText(!TextUtils.isEmpty(btnCancelText) ? btnCancelText : "");
        btnCancel.setVisibility(!TextUtils.isEmpty(btnCancelText) ? View.VISIBLE : View.GONE);

        View lineBottom = dlg.findViewById(R.id.line_bottom);
        lineBottom.setVisibility(!TextUtils.isEmpty(btnCancelText) ? View.VISIBLE : View.GONE);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (btnOkListener != null) {
                    btnOkListener.onClick(dlg, v);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (btnCancelListener != null) {
                    btnCancelListener.onClick(dlg, v);
                }
            }
        });
        return dlg;
    }

    public AlertSubDialog getAlertSubDialog(String title, String content, String subTips,
                                            boolean isChecked,
                                            AlertSubDialog.OnCheckListener listener) {
        AlertSubDialog dialog = new AlertSubDialog(mContext, title, content, subTips, isChecked);
        dialog.setOnCheckListener(listener);
        dialog.show();
        return dialog;
    }

    public EditDialog getEditDialog(String title, String content, EditDialog.OnEditListener listener) {
        EditDialog dialog = new EditDialog(mContext, title, content);
        dialog.setOnEditListener(listener);
        dialog.show();
        return dialog;
    }

    public InfoDialog getInfoDialog(String title, List<InfoDialog.Bean> datas) {
        InfoDialog dialog = new InfoDialog(mContext, title, datas);
        dialog.show();
        return dialog;
    }

    public OperationDialog getOperationDialog(String title, List<OperationDialog.Bean> datas,
                                              AbsSheetDialog.OnItemClickListener<OperationDialog.Bean> listener) {
        OperationDialog dialog = new OperationDialog(mContext, title, datas);
        dialog.setOnItemClickListener(listener);
        dialog.show();
        return dialog;
    }

    public BottomVerSheetDialog getBottomVerDialog(List<BottomVerSheetDialog.Bean> datas,
                                                   AbsSheetDialog.OnItemClickListener<BottomVerSheetDialog.Bean> listener) {
        return getBottomVerDialog(null, datas, listener);
    }

    public BottomVerSheetDialog getBottomVerDialog(String title, List<BottomVerSheetDialog.Bean> datas,
                                                   AbsSheetDialog.OnItemClickListener<BottomVerSheetDialog.Bean> listener) {
        BottomVerSheetDialog dialog = new BottomVerSheetDialog(mContext, title, datas);
        dialog.setOnItemClickListener(listener);
        dialog.show();
        return dialog;
    }

    public BottomHorSheetDialog getBottomHorDialog(String title, List<BottomHorSheetDialog.Bean> datas,
                                                   AbsSheetDialog.OnItemClickListener<BottomHorSheetDialog.Bean> listener) {
        BottomHorSheetDialog dialog = new BottomHorSheetDialog(mContext, title, datas);
        dialog.setOnItemClickListener(listener);
        dialog.show();
        return dialog;
    }

    public BottomShareSheetDialog getBottomShareDialog(String title, List<BottomShareSheetDialog.Bean> datas) {
        BottomShareSheetDialog dialog = new BottomShareSheetDialog(mContext, title, datas);
        dialog.show();
        return dialog;
    }

    public interface OnClickListener {
        void onClick(AlertDialog dlg, View v);
    }
}
