package ecnu.cs14.garagelocation.env;

import android.content.Context;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides all APs and generates fingerprint according to a base.
 * Created by K on 2017/1/21.
 */

public abstract class Environment {
    final static int SIGNAL_LEVEL_NUM = 1000;
    Environment(Context context) {

    }
    public abstract List<String> getAps();
    public abstract Map<String, Integer> generateFingerprint(Set<String> base);

    public static Environment getInstance(Context context) {
        return new EnvironmentImpl(context);
    }
}
