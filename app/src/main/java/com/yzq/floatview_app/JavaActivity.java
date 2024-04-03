package com.yzq.floatview_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yzq.floating_view.FloatingViewConfig;
import com.yzq.floating_view.FloatingViewManager;

public class JavaActivity extends AppCompatActivity {

    public static final String TAG = "JavaActivity";

    public static void start(Context context) {
        Intent starter = new Intent(context, JavaActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        new FloatingViewManager
                .Builder(this)
                .floatingViewConfig(new FloatingViewConfig().contentView(R.layout.layout_float_view)
                        .draggable(true)
                        .autoMoveToEdge(false)
                        .gravity(Gravity.END | Gravity.BOTTOM))
                .globalShow(false)
                .build()
                .show();

    }
}