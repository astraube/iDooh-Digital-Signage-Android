package br.com.i9algo.taxiadv.v2.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.R;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class CustomErrorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_error);

        TextView errorDetailsText = (TextView) findViewById(R.id.error_details);
        errorDetailsText.setText(CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));
        Button restartButton = (Button) findViewById(R.id.restart_button);


        // Version 1.5.0
        final Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
        final CustomActivityOnCrash.EventListener eventListener = CustomActivityOnCrash.getEventListenerFromIntent(getIntent());
        if (restartActivityClass != null) {
            restartButton.setText(R.string.restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomErrorActivity.this, restartActivityClass);
                    CustomActivityOnCrash.restartApplicationWithIntent(CustomErrorActivity.this, intent, eventListener);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(CustomErrorActivity.this, eventListener);
                }
            });
        }
        // Auto Restart App
        if (!BuildConfig.DEBUG) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);

                        Intent intent = new Intent(CustomErrorActivity.this, restartActivityClass);
                        CustomActivityOnCrash.restartApplicationWithIntent(CustomErrorActivity.this, intent, eventListener);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        /*
        // Version 2.2.0
        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());

        // Auto Restart App
        if (!BuildConfig.DEBUG) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);

                        Intent intent = new Intent(CustomErrorActivity.this, LauncherActivity.class);
                        startActivity(intent);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return;
        }

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }

        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(CustomErrorActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(CustomErrorActivity.this, config);
                }
            });
        }
        */
    }
}