package ecnu.cs14.garagelocation.info;

import android.content.Context;
import android.content.res.AssetManager;
import ecnu.cs14.garagelocation.data.Ap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The low level of the map storing system. Loads JSONs for {@link MapSet}.
 * Created by K on 2017/1/26.
 */

final class FileSystem {
    private AssetManager manager;
    private Map<Ap, String> index;
    public FileSystem(Context context) {
        manager = context.getAssets();
    }

    private static String streamToString(InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream);
        for (; ; ) {
            int readSize = in.read(buffer, 0, buffer.length);
            if (readSize < 0)
                break;
            out.append(buffer, 0, readSize);
        }
        return out.toString();
    }

    private static final String INDEX_FILENAME = "map-index.json";
    public Map<Ap, String> getIndex() throws IOException, JSONException {
        if (index != null) {
            return index;
        }
        InputStream stream = manager.open(INDEX_FILENAME);
        String fileString = streamToString(stream);
        JSONObject json = new JSONObject(fileString);
        Iterator<String> keys = json.keys();
        index = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            index.put(new Ap("", key), json.getString(key));
        }
        return index;
    }

    public JSONObject getMapSetJson(String filename) throws IOException, JSONException {
        InputStream stream = manager.open(filename);
        String fileString = streamToString(stream);
        return new JSONObject(fileString);
    }

    public JSONObject getMapSetJson(Ap ap) throws IOException, JSONException {
        if (null == index) {
            getIndex();
        }
        return getMapSetJson(index.get(ap));
    }
}
