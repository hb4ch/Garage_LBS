package ecnu.cs14.garagelocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import ecnu.cs14.garagelocation.env.Environment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public final class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

    private MapView mapView;
    private Environment environment;
    private final MainActivityHandler mHandler = new MainActivityHandler(this);

    private static final class MainActivityHandler extends android.os.Handler {
        private final WeakReference<MainActivity> mActivity;

        final static int MSG_FINGERPRINT = 0;

        MainActivityHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FINGERPRINT:
                {
                    HashMap fingerprint = (HashMap) msg.obj;
                    mActivity.get().mapView.setText(fingerprint.toString());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map_view);

        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        boolean requestNeeded = false;

        for (String permission :
                permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    requestNeeded = true;
                    break;
                }
        }
        if (requestNeeded) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        environment = Environment.getInstance(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result :
                grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "未获得权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> aps = environment.getAps();
                Map<String, Integer> fingerprint = environment.generateFingerprint(new HashSet<>(aps));
                Message message = new Message();
                message.obj = fingerprint;
                message.what = MainActivityHandler.MSG_FINGERPRINT;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        environment.destroy();
    }
}
