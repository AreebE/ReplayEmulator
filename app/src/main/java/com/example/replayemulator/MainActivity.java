package com.example.replayemulator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ScrollerButton scrollerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("EEEEE", Settings.canDrawOverlays(this) + "");
//            requestPermissions(new String[]{Settings.ACTION_ACCESSIBILITY_SETTINGS}, 0);

            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
//            Settings.Secure.putString(getContentResolver(),
//                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "auto/scroll/autoscrollingaplication/SwipingService.java");
//            Settings.Secure.putString(getContentResolver(),
//                    Settings.Secure.ACCESSIBILITY_ENABLED, "1");
        }


        setContentView(R.layout.main);
        ((Button) findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UiDevice uiDevice = UiDevice.getInstance();
//                uiDevice.drag(200, 100, 200, 400,2);
//                Intent intent = new Intent(MainActivity.this, ReplayService.class);
//                stopService(intent);
//                intent.putExtra(ReplayService.START_DELAY_KEY, 100);
//                intent.putExtra(ReplayService.IN_BETWEEN_DELAY_KEY, 50);
//                startForegroundService(intent);
//                triggerScroll();
            }
        });
        ((TextView) findViewById(R.id.test_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Replay, ", "e on click");
            }
        });
        ((TextView) findViewById(R.id.test_view)).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Log.d("Replay, ", "f on drag + " + dragEvent.toString() + ", " + dragEvent.getAction());
                return false;
            }
        });
        ((TextView) findViewById(R.id.test_view)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("Replay, ", "e");
                Log.d("Replay ", motionEvent.toString() + ", " + MotionEvent.actionToString(motionEvent.getAction()));
                return true;
            }



        });



//
//        UiDevice =
//        Instrumentation ins = new Instrumentation();
//        ins.sendPointerSync(
//                MotionEvent.obtain(
//                        SystemClock.uptimeMillis(),
//                SystemClock.uptimeMillis(),
//                        MotionEvent.ACTION_DOWN,
//                        200,
//                        100,
//                        0));
//        ins.sendPointerSync(
//                MotionEvent.obtain(
//                        SystemClock.uptimeMillis(),
//                        SystemClock.uptimeMillis(),
//                        MotionEvent.ACTION_UP,
//                        200,
//                        300,
//                        0));
    }

    /**
     * Called when a touch screen event was not handled by any of the views
     * under it.  This is most useful to process touch events that happen
     * outside of your window bounds, where there is no view to receive it.
     *
     * @param event The touch screen event being processed.
     * @return Return true if you have consumed the event, false if you haven't.
     * The default implementation always returns false.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("EEEEE", MotionEvent.actionToString(event.getAction()));
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(MainActivity.this, ReplayService.class);

        stopService(intent);
    }

    public void triggerScroll()
    {
//        Log.d("Scrolling - ", "startDelay = " + startDelay + ", inBetweenDelay = " + inBetweenDelay);
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.pressHome();
//        device.swipe(100, 200, 100, 600, 5);
//        AccessibilityNodeInfo nodeInfo =
    }
}