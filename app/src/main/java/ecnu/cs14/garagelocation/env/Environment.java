package ecnu.cs14.garagelocation.env;

import android.content.Context;
import ecnu.cs14.garagelocation.data.Ap;
import ecnu.cs14.garagelocation.data.Fingerprint;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Provides all APs and generates fingerprint according to a base.
 * Created by K on 2017/1/21.
 */

public abstract class Environment {
    final static int SIGNAL_LEVEL_NUM = 1000;
    Environment(Context context) {

    }
    public abstract List<Ap> getAps();
    public abstract Fingerprint generateFingerprint(Collection<Ap> base);
    public abstract void destroy();

    public static Environment getInstance(Context context) {
        return new EnvironmentImpl(context);
    }
}
