package xyz.shy.utils;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.Long2IntAVLTreeMap;
import it.unimi.dsi.fastutil.longs.Long2IntSortedMap;
import it.unimi.dsi.fastutil.longs.Long2IntSortedMaps;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Created by Shy on 2018/6/14
 * fastutil demo
 * http://fastutil.di.unimi.it/docs/overview-summary.html
 */

public class FastUtilDemo {

    public static void main(String[] args) {
        /*
         * Suppose you want to store a sorted map from longs to integers.
         * The first step is to define a variable of the right interface,
         * and assign it a new tree map (say, of the AVL type):
         */
        Long2IntSortedMap m = new Long2IntAVLTreeMap();
        // Now we can easily modify and access its content:
        m.put(1, 5);
        m.put(2, 6);
        m.put(3, 7);
        m.put(1000000000L, 10);
        System.out.println(m.get(1)); // This method call will return 5
        System.out.println(m.get(4)); // This method call will return 0
        // We can also try to change the default return value:
        m.defaultReturnValue(-1);
        System.out.println(m.get(4)); // This method call will return -1
        // We can obtain a type-specific iterator on the key set:
        LongBidirectionalIterator i = m.keySet().iterator();
        // Now we sum all keys
        long s = 0;
        while (i.hasNext())
            s += i.nextLong();
        System.out.println(s);
        // We now generate a head map, and iterate bidirectionally over it starting from a given point:
        // This map contains only keys smaller than 4
        Long2IntSortedMap m1 = m.headMap(4);
        // This iterator is positioned between 2 and 3
        LongBidirectionalIterator t = m1.keySet().iterator(2);
        System.out.println(t.previous()); // This method call will return 2 (t.next() would return 3)

        //Should we need to access the map concurrently, we can wrap it:
        // This map can be safely accessed by many threads
        Long2IntSortedMap m2 = Long2IntSortedMaps.synchronize(m1);

        //Linked maps are very flexible data structures which can be used to implement, for instance, queues whose content can be probed efficiently:

        // This map remembers insertion order (note that we are using the array-based constructor)
        IntSortedSet ss = new IntLinkedOpenHashSet(new int[]{4, 3, 2, 1});
        ss.firstInt(); // This method call will return 4
        ss.lastInt(); // This method call will return 1
        ss.contains(5); // This method will return false
        IntBidirectionalIterator ii = ss.iterator(ss.lastInt()); // We could even cast it to a list iterator
        ii.previous(); // This method call will return 1
        ii.previous(); // This method call will return 2
        ss.remove(ss.lastInt()); // This will remove the last element in constant time

        //Now, we play with iterators. It is easy to create iterators over intervals or over arrays, and combine them:

        IntIterator iii = IntIterators.fromTo(0, 10); // This iterator will return 0, 1, ..., 9
        int[] a = new int[]{5, 1, 9};
        IntIterator j = IntIterators.wrap(a); // This iterator will return 5, 1, 9.
        IntIterator k = IntIterators.concat(new IntIterator[]{iii, j}); // This iterator will return 0, 1, ..., 9, 5, 1, 9

        // String key
        Object2IntMap<String> s2iMap = new Object2IntOpenHashMap<>();
        s2iMap.put("Shy", 1001);
        s2iMap.put("ReAct", 1002);
        s2iMap.put("Emma", 1003);
        s2iMap.put("AnonYmous", 1004);
        Integer shy = s2iMap.apply("Shy");
        System.out.println(shy);
    }
}
