package net.kigawa.testplugin;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Test
{
    public static void main(String[] args)
    {

        Map<String, Integer> map = new LinkedHashMap<>();

        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 2);

        int max = 0;
        for (String key : map.keySet()) {
            if (map.get(key) < max) continue;
            max = map.get(key);
        }
        for (String key : new ArrayList<>(map.keySet())) {
            if (map.get(key) < max) map.remove(key);
        }
        System.out.println(map);
    }
}
