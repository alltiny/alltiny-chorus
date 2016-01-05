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

    public int getIndexOf(Qualifier qualifier) {
        int count = list.size();
        return getIndexOf(0, count - 1, count / 2, qualifier);
    }

    /**
     * @param i index to check first
     */
    public int getIndexOf(final int i, Qualifier qualifier) {
        int count = list.size();
        return getIndexOf(0, count - 1, count / 2, qualifier);
    }

    /**
     * @param min lowest possible index
     * @param max highest possible index
     * @param i current index to be checked
     * @param qualifier to search for
     */
    private int getIndexOf(final int min, final int max, final int i, Qualifier qualifier) {
        if (min >= max) {
            return min;
        } else {
            int c = comparator.compare(list.get(i), qualifier);
            if (c == 0) { // exact hit.
                return i;
            } else if (c < 0) { // list type was too small, go to right.
                return getIndexOf(i + 1, max, (i + 1 + max) / 2, qualifier); // we already know that i isn't our item.
            } else { // list type was too big, so we go to left.
                return getIndexOf(min, i - 1, (min + i - 1) / 2, qualifier); // we already know that i isn't our item.
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
