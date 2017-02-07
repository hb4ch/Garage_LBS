package ecnu.cs14.garagelocation.info;

import android.content.Context;
import ecnu.cs14.garagelocation.data.Ap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Select {@link MapSet} according to given aps.
 * Created by K on 2017/1/25.
 */
public class MapSetSelector {
    private FileSystem fileSystem;

    public MapSetSelector(Context context) {
        fileSystem = new FileSystem(context);
    }

    /**
     * Select.
     * @param aps A {@link List} of {@link Ap} ordered by signal levels.
     * @return The most probable {@link MapSet}.
     */
    public MapSet select(List<Ap> aps) {
        final int select_threshold = 3;
        int max = 0;
        String maxFilename = null;
        HashMap<String, Integer> count = new HashMap<>();
        Map<Ap, String> index;
        try {
            index = fileSystem.getIndex();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (Ap ap :
                aps) {
            String filename = index.get(ap);
            if (!count.containsKey(filename)) {
                count.put(filename, 1);
                if (max == 0) {
                    max = 1;
                    maxFilename = filename;
                }
            } else {
                int c = count.get(filename) + 1;
                if (c >= select_threshold) {
                    maxFilename = filename;
                    break;
                }
                count.put(filename, c);
                if (c > max) {
                    max = c;
                    maxFilename = filename;
                }
            }
        }
        try {
            return new MapSet(fileSystem.getMapSetJson(maxFilename));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
