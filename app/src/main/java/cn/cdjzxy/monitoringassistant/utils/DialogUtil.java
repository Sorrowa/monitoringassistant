package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.widget.dialogplus.DialogPlus;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlusBuilder;
import com.wonders.health.lib.base.widget.dialogplus.ViewHolder;

import cn.cdjzxy.monitoringassistant.R;

public class DialogUtil {
    public static void createDialog(Context context, String title, String content) {
        createDialog(context, title, content, null, null, null, null);
    }

    public static void createDialog(Context context, String title, String content, String okStr, String cancelStr, View.OnClickListener okListener, View.OnClickListener cancelListener) {
        View headerView = LayoutInflater.from(context).inflate(R.layout.view_dialog_header, null);
        View footerView = LayoutInflater.from(context).inflate(R.layout.view_dialog_footer, null);
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_dialog_content, null);
        TextView tvTitle = headerView.findViewById(R.id.tv_dialog_title);
        TextView tvContent = contentView.findViewById(R.id.tv_dialog_content);
        TextView btnCancel = footerView.findViewById(R.id.btn_cancel);
        TextView btnOk = footerView.findViewById(R.id.btn_ok);

        if (!CheckUtil.isEmpty(title)) {
            tvTitle.setText(title);
        }

        if (!CheckUtil.isEmpty(content)) {
            tvContent.setText(content);
        }
        if (!CheckUtil.isEmpty(okStr)) {
            btnOk.setText(okStr);
        }
        if (!CheckUtil.isEmpty(cancelStr)) {
            btnCancel.setText(cancelStr);
        }

        ViewHolder viewHolder = new ViewHolder(contentView);
        viewHolder.setBackgroundResource(R.drawable.shape_dialog);

        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(context);
        dialogPlusBuilder.setContentHolder(viewHolder);
        dialogPlusBuilder.setFooter(footerView, true);
        dialogPlusBuilder.setHeader(headerView, true);
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setCancelable(false);
        dialogPlusBuilder.setContentBackgroundResource(Color.TRANSPARENT);
        dialogPlusBuilder.setContentWidth(500);
        final DialogPlus dialogPlus = dialogPlusBuilder.create();

        dialogPlus.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                if (!CheckUtil.isNull(cancelListener)) {
                    cancelListener.onClick(v);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                if (!CheckUtil.isNull(okListener)) {
                    okListener.onClick(v);
                }

            }
        });

    }


}
