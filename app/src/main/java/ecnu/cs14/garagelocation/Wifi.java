package ecnu.cs14.garagelocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;

/**
 * Handles wifi related affairs.
 * Provides scan results.
 * Created by K on 16/12/11.
 */

public final class Wifi {
    private Context mContext;
    private WifiManager mManager;
    private HashSet<WeakReference<ScanResultsReceiver>> mCallbacks;

    public interface ScanResultsReceiver {
        void receive(List<ScanResult> results);
    }

    public Wifi(Context context) {
        mContext = context;
        mManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mCallbacks = new HashSet<>();
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> list = mManager.getScanResults();
                for (WeakReference<ScanResultsReceiver> r :
                        mCallbacks) {
                    r.get().receive(list);
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void enable() {
        mManager.setWifiEnabled(true);
    }

    public void scan() {
        mManager.startScan();
    }

    public void registerScanResultsReceiver(ScanResultsReceiver receiver) {
        mCallbacks.add(new WeakReference<>(receiver));
    }
}
