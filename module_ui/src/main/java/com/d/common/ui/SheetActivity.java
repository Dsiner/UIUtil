package com.d.common.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.common.view.dialog.AbsSheetDialog;
import com.d.lib.common.view.dialog.AlertDialogFactory;
import com.d.lib.common.view.dialog.BottomHorSheetDialog;
import com.d.lib.common.view.dialog.BottomShareSheetDialog;
import com.d.lib.common.view.dialog.BottomVerSheetDialog;

import java.util.Arrays;

public class SheetActivity extends Activity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_title_left) {
            finish();
        } else if (resId == R.id.btn_style0) {
            AlertDialogFactory.createFactory(this).getBottomVerDialog(null,
                    Arrays.asList(new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_normal) + "1", R.color.lib_pub_color_text_main, true),
                            new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_normal) + "2", R.color.lib_pub_color_text_main, false)),
                    new AbsSheetDialog.OnItemClickListener<BottomVerSheetDialog.Bean>() {
                        @Override
                        public void onClick(Dialog dlg, int position, BottomVerSheetDialog.Bean item) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        } else if (resId == R.id.btn_style1) {
            AlertDialogFactory.createFactory(this).getBottomVerDialog(getResources().getString(R.string.module_ui_dialog_bottom_title),
                    Arrays.asList(new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_normal) + "1", R.color.lib_pub_color_text_main, false),
                            new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_normal) + "2", R.color.lib_pub_color_text_main, false),
                            new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_normal) + "3", R.color.lib_pub_color_text_main, false),
                            new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_dangerous), R.color.lib_pub_color_red, false),
                            new BottomVerSheetDialog.Bean(getResources().getString(R.string.module_ui_dialog_bottom_operation_none), R.color.lib_pub_color_text_hint, false)),
                    new AbsSheetDialog.OnItemClickListener<BottomVerSheetDialog.Bean>() {
                        @Override
                        public void onClick(Dialog dlg, int position, BottomVerSheetDialog.Bean item) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        } else if (resId == R.id.btn_style2) {
            AlertDialogFactory.createFactory(this).getBottomHorDialog(getResources().getString(R.string.module_ui_share),
                    Arrays.asList(new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_qq), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_weixin), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_moments), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_weibo), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_sms), R.drawable.lib_pub_ic_btb_icon)),
                    new AbsSheetDialog.OnItemClickListener<BottomHorSheetDialog.Bean>() {
                        @Override
                        public void onClick(Dialog dlg, int position, BottomHorSheetDialog.Bean item) {

                        }

                        @Override
                        public void onCancel(Dialog dlg) {

                        }
                    });
        } else if (resId == R.id.btn_style3) {
            AlertDialogFactory.createFactory(this)
                    .getBottomShareDialog(getResources().getString(R.string.module_ui_share_file_name),
                            Arrays.asList(new BottomShareSheetDialog.Bean(getResources().getString(R.string.module_ui_share_link_address), "https://www.baidu.com/link?url=eZH1yw2u1h-CNrpm7Q3jD_EfVnUxwUuBHlTGhLlA_KfhwtG0TGSl4a7kPsJNMqE8&wd=&eqid=f50ccdfc00009400000000065b232f14"),
                                    new BottomShareSheetDialog.Bean(getResources().getString(R.string.module_ui_share_retrieve_password), "3854", false)));
        } else if (resId == R.id.btn_style4) {
            AlertDialogFactory.createFactory(this)
                    .getBottomShareDialog(getResources().getString(R.string.module_ui_share_file_name),
                            Arrays.asList(new BottomShareSheetDialog.Bean(getResources().getString(R.string.module_ui_share_link_address), "https://www.baidu.com/link?url=eZH1yw2u1h-CNrpm7Q3jD_EfVnUxwUuBHlTGhLlA_KfhwtG0TGSl4a7kPsJNMqE8&wd=&eqid=f50ccdfc00009400000000065b232f14"),
                                    new BottomShareSheetDialog.Bean(getResources().getString(R.string.module_ui_share_retrieve_password), "3854", true)));
        } else if (resId == R.id.btn_style5) {
            AlertDialogFactory.createFactory(this).getBottomHorDialog(getResources().getString(R.string.module_ui_share),
                    Arrays.asList(new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_qq), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_weixin), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_moments), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_weibo), R.drawable.lib_pub_ic_btb_icon),
                            new BottomHorSheetDialog.Bean(getResources().getString(R.string.module_ui_share_sms), R.drawable.lib_pub_ic_btb_icon)),
                    new AbsSheetDialog.OnItemClickListener<BottomHorSheetDialog.Bean>() {
                        @Override
                        public void onClick(Dialog dlg, int position, BottomHorSheetDialog.Bean item) {

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
        setContentView(R.layout.module_ui_activity_sheet);
        initClick();
    }

    private void initClick() {
        ViewHelper.setOnClick(this, this, R.id.iv_title_left);

        ViewHelper.setOnClick(this, this, R.id.btn_style0);
        ViewHelper.setOnClick(this, this, R.id.btn_style1);
        ViewHelper.setOnClick(this, this, R.id.btn_style2);
        ViewHelper.setOnClick(this, this, R.id.btn_style3);
        ViewHelper.setOnClick(this, this, R.id.btn_style4);
        ViewHelper.setOnClick(this, this, R.id.btn_style5);
    }
}
