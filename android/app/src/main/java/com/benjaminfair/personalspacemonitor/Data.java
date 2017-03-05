package com.benjaminfair.personalspacemonitor;

import android.util.Log;

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
        private static final int vOffset = 0; // cm
        private static final int hOffset = 0; // cm
        public int front, right, back, left;
        private float area;

        Measurement(byte[] in) {
            front = (in[0] & 0xff) | ((in[1] & 0xff) << 8);
            right = (in[2] & 0xff) | ((in[3] & 0xff) << 8);
            back = (in[4] & 0xff) | ((in[5] & 0xff) << 8);
            left = (in[6] & 0xff) | ((in[7] & 0xff) << 8);
            area = (((float) front + (float) back - vOffset) * ((float) left + (float) right - hOffset)) / 1000000.0f; // convert from cm^2 to m^2
            Log.d(TAG, "Front " + front + " Right " + right + " Back " + back + " Left " + left + " Area " + area);
            if (area < 0)
                area = 0;
        }

        float getArea() {
            return area;
        }
    }

    private ArrayList<Measurement> mData = new ArrayList<>();
    private List<Entry> mArrayEntries = new ArrayList<>();

    void addMeasurement(byte[] in) {
        Measurement m = new Measurement(in);
        mData.add(m);
        mArrayEntries.add(new Entry(mArrayEntries.size(), m.getArea()));
    }

    List<Entry> getAreaData() {
        return mArrayEntries;
    }

    Measurement getLatestMeasurement() {
        if (mData.size() > 0) {
            return mData.get(mData.size() - 1);
        } else {
            return null;
        }
    }

    void clear() {
        mData.clear();
        mArrayEntries.clear();
    }

}
