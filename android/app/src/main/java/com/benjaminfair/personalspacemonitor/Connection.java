package com.benjaminfair.personalspacemonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

class Connection {
    private final static String TAG = Connection.class.getSimpleName();

    private static Connection ourInstance;
    private static final Object lock = new Object();

    static Connection getInstance() {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = new Connection();
            }
        }
        return ourInstance;
    }

    public interface connectionsChangeHandler {
        void onConnect();
        void onDisconnect();
    }

    private connectionsChangeHandler mConnectionsChangeHandler;

    private Connection() {
    }

    private enum State {CONNECTED, DISCONNECTED}
    private State mState = State.DISCONNECTED;

    public void setConnectionsChangeHandler(connectionsChangeHandler handler) {
        mConnectionsChangeHandler = handler;
    }

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(PersonalSpaceService.ACTION_GATT_CONNECTED)) {
            }

            //*********************//
            if (action.equals(PersonalSpaceService.ACTION_GATT_DISCONNECTED)) {

            }

            //*********************//
            if (action.equals(PersonalSpaceService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(PersonalSpaceService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            listAdapter.add("["+currentDateTimeString+"] RX: "+text);
                            messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }
            //*********************//
            if (action.equals(PersonalSpaceService.DEVICE_DOES_NOT_SUPPORT_UART)){
                showMessage("Device doesn't support UART. Disconnecting");
                mService.disconnect();
            }


        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(this, PersonalSpaceService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PersonalSpaceService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(PersonalSpaceService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(PersonalSpaceService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(PersonalSpaceService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(PersonalSpaceService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
}
