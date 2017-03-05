package com.benjaminfair.personalspacemonitor;

import java.util.ArrayList;

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

    static class Measurement {
        private static final int vOffset = 30; // cm
        private static final int hOffset = 60; // cm
        private int front, right, back, left;

        int getArea() {
            int area = (front + back - vOffset) * (left + right - hOffset);
            if (area > 0)back
                return area;
            else
                return 0;
        }

        Measurement(Byte[] in) {
            front = in[0] + in[1] << 8;
            right = in[2] + in[3] << 8;
            back = in[4] + in[5] << 8;
            left = in[6] + in[7] << 8;
        }
    }

    private ArrayList<Measurement> mData;

    public void addMeasurement(Byte[] in) {
        mData.add(new Measurement(in));
    }

}
