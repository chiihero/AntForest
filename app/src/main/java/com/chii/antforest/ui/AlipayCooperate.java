package com.chii.antforest.ui;

import com.chii.antforest.util.CooperationIdMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlipayCooperate extends AlipayId {
    private static List<AlipayCooperate> list;

    public AlipayCooperate(String i, String n) {
        id = i;
        name = n;
    }

    public static List<AlipayCooperate> getList() {
        if (list == null || CooperationIdMap.shouldReload) {
            list = new ArrayList<AlipayCooperate>();
            Set idSet = CooperationIdMap.getIdMap().entrySet();
            for (Object o : idSet) {
                Map.Entry entry = (Map.Entry) o;
                list.add(new AlipayCooperate(entry.getKey().toString(),
                        entry.getValue().toString()));
            }
        }
        return list;
    }

    public static void remove(String id) {
        getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) {
                list.remove(i);
                break;
            }
        }
    }

}
