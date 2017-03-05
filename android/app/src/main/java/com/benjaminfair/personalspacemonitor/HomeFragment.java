package com.benjaminfair.personalspacemonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benjaminfair.personalspacemonitor.R;

public class HomeFragment extends Fragment {
    private static final int UPDATE_PERIOD = 1000; // ms

    MainActivity parent; // TODO: EVIL!
    BubbleView bubbleView;

    public HomeFragment() {
        // Required empty public constructor
    }

    private Runnable updateData = new Runnable() {
        @Override
        public void run() {
            bubbleView.setDims(parent.mData.getLatestMeasurement());
            parent.mHandler.postDelayed(this, UPDATE_PERIOD);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        parent = (MainActivity) getActivity();
        bubbleView = (BubbleView) view.findViewById(R.id.bubble);

        parent.mHandler.post(updateData);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        parent.mHandler.removeCallbacks(updateData);
    }
}
