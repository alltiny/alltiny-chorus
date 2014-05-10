package org.alltiny.chorus.util;

import java.util.List;

/**
 * I some cases we are forced to search for our items and usage of a lookup map is not possible.
 * Due the fastest approach is the binary search, I implemented it herein as reusable class.
 *
 * @param <ListType> types in the list in which shall be searched.
 * @param <Qualifier> item to compare against.
 */
public class BinarySearch<ListType, Qualifier> {

    private final List<ListType> list;
    private final Comparator<ListType,Qualifier> comparator;

    public BinarySearch(List<ListType> list, Comparator<ListType,Qualifier> comparator) {
        this.list = list;
        this.comparator = comparator;
    }

    public int getIndexOf(Qualifier qual) {
        return getIndexOf(0, list.size() - 1, qual);
    }

    private int getIndexOf(int min, int max, Qualifier qual) {
        if (min >= max) {
            return min;
        } else {
            int i = (min + max) / 2;
            int c = comparator.compare(list.get(i), qual);
            if (c == 0) { // exact hit.
                return i;
            } else if (c < 0) { // list type was too small, go to right.
                return getIndexOf(i + 1, max, qual); // we already know that i isn't our item.
            } else { // list type was too big, so we go to left.
                return getIndexOf(min, i - 1, qual); // we already know that i isn't our item.
            }
        }
    }

    /**
     * The binary search needs an implementation of this interface.
     */
    public static interface Comparator<ListType, Qualifier> {

        /**
         * This method return 0 if the qualifier hits the list type.
         * It returns -1 if the list type is small than the qualifier.
         * It returns +1 if the list type is bigger than the qualifier.
         */
        public int compare(ListType listType, Qualifier qualifier);
    }
}
