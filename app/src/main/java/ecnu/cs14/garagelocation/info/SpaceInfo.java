package ecnu.cs14.garagelocation.info;

import android.content.Context;
import ecnu.cs14.garagelocation.data.Ap;
import ecnu.cs14.garagelocation.data.Map;

import java.util.List;

/**
 * A map data provider. Also supports map updating.
 * Created by K on 2017/2/1.
 */

public abstract class SpaceInfo {
    public SpaceInfo(Context context) {

    }

    public abstract List<Map> getAllMaps(List<Ap> aps);
    public abstract Map autoSelectMap(List<Ap> aps);
    public abstract Map selectMap(int index);
    public abstract void updateMap(int index, Map map);
    public abstract void saveAllMaps();
}
