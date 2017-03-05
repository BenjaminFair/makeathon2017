package com.benjaminfair.personalspacemonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SetupFragment extends Fragment {

    private MainActivity parent; // TODO: This is terrible

    private Button btnConnect;

    public SetupFragment() {
        // Required empty public constructor
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(PersonalSpaceService.ACTION_GATT_CONNECTED)) {
                btnConnect.setText(R.string.disconnect);
                btnConnect.setEnabled(true);
            }

            if (action.equals(PersonalSpaceService.ACTION_GATT_DISCONNECTED)) {
                btnConnect.setText(R.string.connect);
                btnConnect.setEnabled(true);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        parent = (MainActivity) getActivity();

        btnConnect = (Button) view.findViewById(R.id.connect_disconnect);
        if (parent.mService.getState() == PersonalSpaceService.State.CONNECTED) {
            btnConnect.setText(R.string.disconnect);
        } else if (parent.mService.getState() == PersonalSpaceService.State.CONNECTING) {
            btnConnect.setEnabled(false);
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent.mService.getState() == PersonalSpaceService.State.CONNECTED) {
                    parent.mService.disconnect();
                } else {
                    Intent newIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivity(newIntent);
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(PersonalSpaceService.ACTION_GATT_CONNECTED);
        filter.addAction(PersonalSpaceService.ACTION_GATT_DISCONNECTED);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }
}
