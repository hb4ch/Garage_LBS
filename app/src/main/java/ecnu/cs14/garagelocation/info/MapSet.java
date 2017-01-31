package ecnu.cs14.garagelocation.info;

import ecnu.cs14.garagelocation.data.Ap;
import ecnu.cs14.garagelocation.data.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A set of maps representing an area.
 * Created by K on 2017/1/25.
 */

public final class MapSet {
    private List<Map> maps;
    private List<Ap> aps;
    private int selected = 0;

    public MapSet(JSONObject json) throws JSONException {
        maps = new ArrayList<>();
        aps = new ArrayList<>();
        JSONArray mapsJson = json.getJSONArray("maps");
        for (int i = 0; i < mapsJson.length(); i++) {
            maps.add(new Map(mapsJson.getJSONObject(i)));
        }
        JSONArray apsJson = json.getJSONArray("aps");
        for (int i = 0; i < apsJson.length(); i++) {
            aps.add(new Ap(apsJson.getJSONObject(i)));
        }
    }

    public MapSet(List<Map> maps) {
        this.maps = new ArrayList<>(maps);
        for (Map map:
             this.maps) {
            aps.addAll(map.aps);
        }
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        HashSet<Ap> aps = new HashSet<>();
        for (Map map :
                maps) {
            json.accumulate("maps", map.toJson());
            for (Ap ap :
                    map.aps) {
                aps.add(ap);
            }
        }
        for (Ap ap :
                aps) {
            json.accumulate("aps", ap.toJson());
        }
        return json;
    }

    /**
     * Get a suitable {@link Map} according to present APs.
     * @param aps The {@link List} of {@link Ap}.
     * @return The {@link Map}.
     */
    public Map getMap(List<Ap> aps) {
        int[] fitCount = new int[maps.size()];
        int max = 0;
        int indexMax = 0;
        for (Ap ap :
                aps) {
            for (int i = 0; i < maps.size(); i++) {
                if (maps.get(i).aps.contains(ap)) {
                    if (++fitCount[i] > max) {
                        indexMax = i;
                        max = fitCount[i];
                    }
                }
            }
        }
        selected = indexMax;
        return maps.get(selected);
    }

    public Map getSelectedMap(int index) {
        selected = index;
        return maps.get(selected);
    }

    public List<Map> getMaps() {
        return maps;
    }

    public List<Ap> getAps() {
        return aps;
    }
}
