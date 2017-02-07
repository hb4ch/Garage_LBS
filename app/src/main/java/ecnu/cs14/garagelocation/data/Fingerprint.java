package ecnu.cs14.garagelocation.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * Fingerprint data structure.
 * Created by K on 2017/1/23.
 */

public final class Fingerprint extends HashMap<Ap, Integer> {
    public Fingerprint() {
        super();
    }

    public Fingerprint(List<Ap> base, JSONArray json) throws JSONException {
        super();
        for (int i = 0; i < base.size(); i++) {
            put(base.get(i), json.getInt(i));
        }
    }

    public JSONArray toJson(List<Ap> base) throws JSONException {
        JSONArray json = new JSONArray();
        for (int i = 0; i < base.size(); i++) {
            json.put((int) get(base.get(i)));
        }
        return json;
    }
}
