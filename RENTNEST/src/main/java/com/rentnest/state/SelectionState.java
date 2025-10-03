package com.rentnest.state;

public class SelectionState {
    private static Integer listingId;
    public static void set(Integer id) { listingId = id; }
    public static Integer get() { return listingId; }
    public static void clear() { listingId = null; }
}
