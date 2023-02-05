package com.example.replayemulator;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.ArrayDeque;
import java.util.Deque;

public class ReplayService extends AccessibilityService {

    public static final String START_DELAY_KEY = "startDelay";
    public static final String IN_BETWEEN_DELAY_KEY = "endDelay";

    private static final String CHANNEL_NAME = "channel for swiping";
    private static final String CHANNEL_ID = "channel for id";
    private static final String TAG = "ReplayService";

    private int startDelay = 2000;
    private int inBetweenDelay = 2000;

    private ScrollerButton scrollerButton;
    public ReplayService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG, "access event = " + accessibilityEvent.toString());

    }

    @Override
    public void onInterrupt() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
//        startService();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("EEEE", "extras = " + intent.getExtras());
//                startService();
        startDelay = intent.getIntExtra(START_DELAY_KEY,100);
        inBetweenDelay = intent.getIntExtra(IN_BETWEEN_DELAY_KEY, 1000);

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
        startService();

        scrollerButton = new ScrollerButton(this, startDelay, inBetweenDelay, new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                int middleX = displayMetrics.widthPixels / 2;
                int topOfScroll = displayMetrics.heightPixels / 4;
                int bottomOfScroll = (int) (topOfScroll * 2.5);

                Path swipePath = new Path();
                swipePath.moveTo(middleX, bottomOfScroll);
                swipePath.lineTo(middleX, topOfScroll);
                GestureDescription.Builder builder = new GestureDescription.Builder();
                builder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 500));
                Log.d(TAG,"Test ef " + builder.build().toString());
//                Window w = getWindows();
                dispatchGesture(builder.build(), new GestureResultCallback() {
                    /**
                     * Called when the gesture has completed successfully
                     *
                     * @param gestureDescription The description of the gesture that completed.
                     */
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);
                        Log.d("EEEEEEE", "complted gesture");
                    }

                    /**
                     * Called when the gesture was cancelled
                     *
                     * @param gestureDescription The description of the gesture that was cancelled.
                     */
                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);
                        Log.d("EEEEEE", gestureDescription.toString() + " ");
                    }
                }, null);


            }
        });
        scrollerButton.openWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scrollerButton != null)
        {
            scrollerButton.closeWindow();

        }
    }

    private void startService()
    {

        NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Swiper service running...")
                .setContentText("Hit the button to start the swiping.")

                // this is important, otherwise the notification will show the way
                // you want i.e. it will show some default notification
                .setSmallIcon(R.drawable.ic_launcher_foreground)

                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

    }

    private AccessibilityNodeInfo getScrollableNode(AccessibilityNodeInfo root)
    {
        Deque<AccessibilityNodeInfo> deque = new ArrayDeque<>();
        deque.add(root);
        while (!deque.isEmpty()) {
            AccessibilityNodeInfo node = deque.removeFirst();
            if (node.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)) {
                return node;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                deque.addLast(node.getChild(i));
            }
        }
        return null;
    }

    @Override
    public boolean onGesture(@NonNull AccessibilityGestureEvent gestureEvent) {
        Log.d(TAG, "Gesture = " + gestureEvent.toString());
        return super.onGesture(gestureEvent);
    }


}