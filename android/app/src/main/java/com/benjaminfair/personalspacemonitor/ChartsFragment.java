package com.benjaminfair.personalspacemonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class ChartsFragment extends Fragment implements View.OnClickListener {

    private static final int UPDATE_PERIOD = 1000; // ms

    private MainActivity parent; // TODO: EVIL!!
    private Data mData = Data.getInstance();
    private LineChart mAreaChart;

    public ChartsFragment() {
        // Required empty public constructor
    }

    private Runnable mUpdateData = new Runnable() {
        @Override
        public void run() {
            List<Entry> entries = mData.getAreaData();
            if (entries.size() > 0) {
                LineDataSet lineDataSet = new LineDataSet(entries, "Personal Space (m^2)");
                LineData lineData = new LineData(lineDataSet);

                mAreaChart.setData(lineData);
                mAreaChart.invalidate();
            }
            parent.mHandler.postDelayed(this, UPDATE_PERIOD);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charts, container, false);

        parent = (MainActivity) getActivity();

        mAreaChart = (LineChart) view.findViewById(R.id.area_chart);
        parent.mHandler.post(mUpdateData);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        parent.mHandler.removeCallbacks(mUpdateData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_data:
                mData.clear();
                mAreaChart.clear();
                mAreaChart.invalidate();
                break;
        }
    }
}
