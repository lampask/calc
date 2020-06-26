package com.myld.fornetti;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Show extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigth = dm.heightPixels;

        getWindow().setLayout((int) (width * .9f),(int) (heigth * .2f));

        String text = getIntent().getStringExtra("content");
        int[] count = new int[16];
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == 32) continue;
            count[text.charAt(i)-'A']++;
        }

        TextView a = findViewById(R.id.textView2);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        int g = 0;
       for(int i = 0; i < 15; i++) {
            if(count[i] == 0) continue;
            if (sb.length() != 0) sb.append(" | ");
            String part = count[i] + " x " + (char)(i + 'A') + " ";
            sb.append(part);
            sb.setSpan(new ForegroundColorSpan(getCurCol(i)),g, g+part.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            g+=part.length()+3;
        }

        a.setText(sb,TextView.BufferType.SPANNABLE);
        Button ok = findViewById(R.id.button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int getCurCol(int i) {
        switch(i/2) {
            case 0:
                return Color.RED;
            case 1:
                return Color.rgb(255, 153, 0);
            case 2:
                return Color.rgb(255,204,0);
            case 3:
                return Color.rgb(0,204,0);
            case 4:
                return Color.rgb(51,153,255);
            case 5:
                return Color.BLUE;
            case 6:
                return Color.rgb(102, 0, 153);
            case 7:
                return Color.MAGENTA;
        }
        return Color.BLACK;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
    }


}
