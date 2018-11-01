package com.d.common.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.common.view.dialog.AbsSheetDialog;
import com.d.lib.common.view.dialog.AlertDialogFactory;
import com.d.lib.common.view.dialog.AlertSubDialog;
import com.d.lib.common.view.dialog.EditDialog;
import com.d.lib.common.view.dialog.InfoDialog;
import com.d.lib.common.view.dialog.OperationDialog;

import java.util.Arrays;

public class AlertActivity extends Activity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_title_left) {
            finish();
        } else if (resId == R.id.btn_loading) {
            AlertDialogFactory.createFactory(this).getLoadingDialog(getResources().getString(R.string.lib_pub_loading));
        } else if (resId == R.id.btn_style0) {
            AlertDialogFactory.createFactory(this).getAlertDialog(null,
                    getResources().getString(R.string.module_ui_dialog_content),
                    getResources().getString(R.string.module_ui_dialog_gotit), null,
                    new AlertDialogFactory.OnClickListener() {
                        @Override
                        public void onClick(AlertDialog dlg, View v) {

                        }
                    }, null);
        } else if (resId == R.id.btn_style1) {
            AlertDialogFactory.createFactory(this).getAlertDialog(null,
                    getResources().getString(R.string.module_ui_dialog_content),
                    getResources().getString(R.string.lib_pub_ok), getResources().getString(R.string.lib_pub_cancel),
                    new AlertDialogFactory.OnClickListener() {
                        @Override
                        public void onClick(AlertDialog dlg, View v) {

                        }
                    }, new AlertDialogFactory.OnClickListener() {
                        @Override
                        public void onClick(AlertDialog dlg, View v) {

                        }
                    });
        } else if (resId == R.id.btn_style2) {
            AlertDialogFactory.createFactory(this).getAlertDialog(getResources().getString(R.string.module_ui_dialog_title_single),
                    getResources().getString(R.string.module_ui_dialog_content),
                    getResources().getString(R.string.lib_pub_ok), getResources().getString(R.string.lib_pub_cancel),
                    new AlertDialogFactory.OnClickListener() {
                        @Override
                        public void onClick(AlertDialog dlg, View v) {

                        }
                    }, new AlertDialogFactory.OnClickListener() {
                        @Override
                        public void onClick(AlertDialog dlg, View v) {

                        }
                    });
        } else if (resId == R.id.btn_style3) {
            AlertDialogFactory.createFactory(this).getAlertSubDialog(null,
                    getResources().getString(R.string.module_ui_dialog_content),
                    getResources().getString(R.string.module_ui_dialog_sub_tips),
                    false, new AlertSubDialog.OnCheckListener() {
                        @Override
                        public void onSubmit(Dialog dlg, boolean isChecked) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        } else if (resId == R.id.btn_style4) {
            AlertDialogFactory.createFactory(this).getAlertSubDialog(getResources().getString(R.string.module_ui_dialog_title_single),
                    getResources().getString(R.string.module_ui_dialog_content),
                    getResources().getString(R.string.module_ui_dialog_sub_tips),
                    true, new AlertSubDialog.OnCheckListener() {
                        @Override
                        public void onSubmit(Dialog dlg, boolean isChecked) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        } else if (resId == R.id.btn_style5) {
            AlertDialogFactory.createFactory(this)
                    .getAlertDialog(getResources().getString(R.string.module_ui_dialog_title_single),
                            getResources().getString(R.string.module_ui_dialog_content),
                            getResources().getString(R.string.lib_pub_ok), null,
                            new AlertDialogFactory.OnClickListener() {
                                @Override
                                public void onClick(AlertDialog dlg, View v) {

                                }
                            }, null);
        } else if (resId == R.id.btn_style6) {
            AlertDialogFactory.createFactory(this).getEditDialog(getResources().getString(R.string.module_ui_dialog_title_single)
                    , "", new EditDialog.OnEditListener() {
                        @Override
                        public void onSubmit(Dialog dlg, String content) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        } else if (resId == R.id.btn_style7) {
            AlertDialogFactory.createFactory(this).getInfoDialog(getResources().getString(R.string.module_ui_dialog_title_single),
                    Arrays.asList(new InfoDialog.Bean(getResources().getString(R.string.module_ui_dialog_title_single) + "1", "" + 1111111),
                            new InfoDialog.Bean(getResources().getString(R.string.module_ui_dialog_title_single) + "2", "" + 2222222),
                            new InfoDialog.Bean(getResources().getString(R.string.module_ui_dialog_title_single) + "3", "" + 3333333),
                            new InfoDialog.Bean(getResources().getString(R.string.module_ui_dialog_title_single) + "4", "" + 4444444),
                            new InfoDialog.Bean(getResources().getString(R.string.module_ui_dialog_title_single) + "5", "" + 5555555)));
        } else if (resId == R.id.btn_style8) {
            AlertDialogFactory.createFactory(this).getOperationDialog(getResources().getString(R.string.module_ui_dialog_content),
                    Arrays.asList(new OperationDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation) + "1", R.color.lib_pub_color_main, false),
                            new OperationDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation) + "2", R.color.lib_pub_color_main, false)),
                    new AbsSheetDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dlg, int position, Object item) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_ui_activity_alert);
        initClick();
    }

    private void initClick() {
        ViewHelper.setOnClick(this, this, R.id.iv_title_left);
        ViewHelper.setOnClick(this, this, R.id.btn_loading);
        ViewHelper.setOnClick(this, this, R.id.btn_style0);
        ViewHelper.setOnClick(this, this, R.id.btn_style1);
        ViewHelper.setOnClick(this, this, R.id.btn_style2);
        ViewHelper.setOnClick(this, this, R.id.btn_style3);
        ViewHelper.setOnClick(this, this, R.id.btn_style4);
        ViewHelper.setOnClick(this, this, R.id.btn_style5);
        ViewHelper.setOnClick(this, this, R.id.btn_style6);
        ViewHelper.setOnClick(this, this, R.id.btn_style7);
        ViewHelper.setOnClick(this, this, R.id.btn_style8);
    }
}
