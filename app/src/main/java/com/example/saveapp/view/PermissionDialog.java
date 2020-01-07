package com.example.saveapp.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.saveapp.R;

public class PermissionDialog extends Dialog {
    private Listener mListener;

    public PermissionDialog(@NonNull Context context, Listener listener) {
        super(context);
        mListener = listener;
        setContentView(R.layout.dialog_permission);
        init();
    }

    private void init() {
        TextView confirm = findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onConfirm();
            }
        });
        TextView cancel = findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度持平
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度持平
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setCanceledOnTouchOutside(false);
    }

    public interface Listener {
        void onConfirm();
    }
}
