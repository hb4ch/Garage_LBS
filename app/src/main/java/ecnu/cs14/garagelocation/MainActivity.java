package ecnu.cs14.garagelocation;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import ecnu.cs14.garagelocation.env.Environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

    private MapView mapView;
    private Environment environment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map_view);
        environment = Environment.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: started");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                HashMap fingerprint = (HashMap) msg.obj;
                mapView.setText(fingerprint.toString());
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> aps = environment.getAps();
                HashMap<String, Integer> fingerprint = environment.generateFingerprint(new HashSet<>(aps));
                Message message = new Message();
                message.obj = fingerprint;
                handler.sendMessage(message);
            }
        }).start();
    }

}
