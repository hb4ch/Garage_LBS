package ecnu.cs14.garagelocation.env;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.*;

/**
 * An implementation of {@link Environment}.
 * Created by K on 2017/1/21.
 */

final class EnvironmentImpl extends Environment implements Wifi.ScanResultsReceiver {
    private final static String TAG = EnvironmentImpl.class.getName();

    private Wifi mWifi = null;
    private final List<ScanResult> mResults = new ArrayList<>();
    private boolean mResultsUpdated = false;

    EnvironmentImpl(Context context) {
        super(context);
        mWifi = new Wifi(context);
        mWifi.registerScanResultsReceiver(this);
    }

    @Override
    public void receive(List<ScanResult> newResults) {
        synchronized (mResults) {
            mResults.clear();
            mResults.addAll(newResults);
            mResultsUpdated = true;
            mResults.notify();
        }
    }

    @Override
    public List<String> getAps() {
        List<String> list = new ArrayList<>();
        mWifi.scan();
        synchronized (mResults) {
            if (!mResultsUpdated) {
                try {
                    Log.i(TAG, "getAps: waiting for scan results");
                    mResults.wait(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!mResultsUpdated) {
                return new ArrayList<>();
            }
            Collections.sort(mResults, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {
                    return lhs.level - rhs.level;
                }
            });
            for (ScanResult sr :
                    mResults) {
                list.add(sr.BSSID);
            }
            mResultsUpdated = false;
        }
        Log.i(TAG, "getAps: aps got");
        return list;
    }

    @Override
    public Map<String, Integer> generateFingerprint(Set<String> base) {
        int sampleCnt = 5;
        List[] scans = new List[sampleCnt];
        for (int i = 0; i < sampleCnt; i++) {
            synchronized (mResults) {
                mWifi.scan();
                if (!mResultsUpdated) {
                    try {
                        Log.i(TAG, "generateFingerprint: waiting for scan results");
                        mResults.wait(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!mResultsUpdated) {
                    return new HashMap<>();
                }
                scans[i] = new ArrayList<>(mResults);
                mResultsUpdated = false;
            }
        }

        Map<String, Integer> fingerprint = new HashMap<>();
        for (String mac :
                base) {
            int signal = 0;
            for (int i = 0; i < sampleCnt; i++) {
                List scan = scans[i];
                for (int j = 0; j < scan.size(); j++) {
                    ScanResult scanResult = (ScanResult) scan.get(j);
                    if (scanResult.BSSID.equals(mac)) {
                        signal += WifiManager.calculateSignalLevel(scanResult.level, SIGNAL_LEVEL_NUM);
                    }
                }
            }
            fingerprint.put(mac, signal);
        }
        Log.i(TAG, "generateFingerprint: fingerprint made");
        return fingerprint;
    }
}
