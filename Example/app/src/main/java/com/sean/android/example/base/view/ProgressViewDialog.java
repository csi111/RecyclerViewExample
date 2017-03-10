package com.sean.android.example.base.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sean.android.example.R;

/**
 * Created by sean on 2017. 3. 11..
 */

public class ProgressViewDialog extends Dialog {

    public ProgressViewDialog(Context context) {
        super(context, R.style.AppTheme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inf = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inf.inflate(R.layout.dialog_view_progress, null);
        setContentView(layout);
        setCancelable(false);
    }
}
