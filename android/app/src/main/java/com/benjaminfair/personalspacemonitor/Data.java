package com.benjaminfair.personalspacemonitor;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

class Data {
    private final static String TAG = Data.class.getSimpleName();

    private static Data ourInstance;
    private static final Object lock = new Object();

    static Data getInstance() {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = new Data();
            }
        }
        return ourInstance;
    }

    private Data() {}

    static class Measurement {
        private static final int vOffset = 30; // cm
        private static final int hOffset = 60; // cm
        private int front, right, back, left;
        private float area;

        Measurement(byte[] in) {
            front = in[0] + in[1] << 8;
            right = in[2] + in[3] << 8;
            back = in[4] + in[5] << 8;
            left = in[6] + in[7] << 8;
            area = ((front + back - vOffset) * (left + right - hOffset)) / 1000000; // convert from cm^2 to m^2
            if (area < 0)
                area = 0;
        }

        float getArea() {
            return area;
        }
    }

    private ArrayList<Measurement> mData = new ArrayList<>();
    private List<Entry> mArrayEntries = new ArrayList<>();

    public void addMeasurement(byte[] in) {
        Measurement m = new Measurement(in);
        mData.add(m);
        mArrayEntries.add(new Entry(mArrayEntries.size(), m.getArea()));
    }

    public List<Entry> getAreaData() {
        return mArrayEntries;
    }

}
