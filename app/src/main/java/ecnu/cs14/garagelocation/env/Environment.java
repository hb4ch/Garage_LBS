package ecnu.cs14.garagelocation.env;

import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Provides all APs and generates fingerprint according to a base.
 * Created by K on 2017/1/21.
 */

public abstract class Environment {
    final static int SIGNAL_LEVEL_NUM = 1000;
    Environment(Context context) {

    }
    public abstract List<String> getAps();
    public abstract HashMap<String, Integer> generateFingerprint(HashSet<String> base);

    public static Environment getInstance(Context context) {
        return new EnvironmentImpl(context);
    }
}
