package com.example.replayemulator;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
//import com.android.monkeyrunner.MonkeyRunner;


public class ScrollerButton {

    private static final String TAG = "ScrollerButton";
    private Context context;
    private LayoutInflater layoutInflater;
    private WindowManager.LayoutParams layoutParams;
    private View layout;
    private WindowManager windowManager;
    private Runnable scrollCommand;
    private int startDelay;
    private int inBetweenDelay;
    private Handler handler;
    boolean[] isActivated = {false};
    boolean[] isRecording = {false};

    public ScrollerButton(Context c, int startDelay, int inBetweenDelay, Runnable scrollCommand)
    {
        this.handler = new Handler();
        this.context = c;
        this.startDelay = startDelay;
        this.inBetweenDelay = inBetweenDelay;
        this.scrollCommand = scrollCommand;
        layoutInflater = LayoutInflater.from(context);
        layout = layoutInflater.inflate(R.layout.outside_widget, null);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "total view " + motionEvent.toString());
                return false;
            }
        });
        ViewCompat.setImportantForAccessibility(layout, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        ImageButton button = ((LinearLayout) layout.findViewById(R.id.record_menu)).findViewById(R.id.play_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EEEEEE", "Triggered a click");
                if (!isActivated[0])
                {
//                    button.setBackgroundColor(getColor(R.color.stopColor));
                    button.setImageDrawable(new ColorDrawable(context.getColor(R.color.stopColor)));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Window, " + startDelay + " - " + inBetweenDelay);
                            scrollCommand.run();
                            if (isActivated[0])
                            {
                                handler.postDelayed(this, inBetweenDelay);
                            }
                        }
                    }, startDelay);
                }
                else
                {
//                    button.setBackgroundColor(getColor(R.color.playColor));
                    button.setImageDrawable(new ColorDrawable(context.getColor(R.color.playColor)));
                    handler.removeCallbacks(null);

//                    stopScroll();
                }
                isActivated[0] = !isActivated[0];
            }
        });
        TextView textView = layout.findViewById(R.id.recording_cover);
        ImageButton recordButton = ((ImageButton) layout.findViewById(R.id.record_button));
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording[0])
                {
                    recordButton.setBackgroundColor(c.getColor(R.color.stop_button_color));
                    layout.setBackgroundColor(c.getColor(R.color.recording_color));
                    layout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            Log.d(TAG, "eee" + motionEvent.toString());
                            return false;
                        }
                    });
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                }
                else
                {
                    recordButton.setBackgroundColor(c.getColor(R.color.record_button_color));
                    layout.setBackgroundColor(c.getColor(R.color.transparent));
                    layout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return false;
                        }
                    });
                    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                }
                isRecording[0] = !isRecording[0];
                updateView();
            }
        });

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP|Gravity.LEFT;
        layoutParams.alpha = 0.7f;


    }
    public void openWindow()
    {
        windowManager.addView(layout,
                layoutParams
        );
    }

    public void updateView()
    {
        windowManager.updateViewLayout(layout, layoutParams);
    }

    public void closeWindow()
    {
        try
        {
            isActivated[0] = false;
            handler.removeCallbacks(null);
            windowManager.removeView(layout);
            layout.invalidate();
            ((ViewGroup) layout.getParent()).removeAllViews();
        }
        catch (Exception e)
        {
            Log.d("EEEE", e.toString());
        }
    }

//    public void triggerScroll()
//    {
//        Log.d("Scrolling - ", "startDelay = " + startDelay + ", inBetweenDelay = " + inBetweenDelay);
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
////        DragEvent event =
////        view.dispatchDragEvent();
//
//        device.pressHome();
//        device.swipe(100, 200, 100, 600, 5);
//    }
}