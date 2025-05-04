package entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HoneyIndex {
    private int index;
    private int UID;

    public int getIndex() {return index;}
    public int getUID() {return UID;}

    public HoneyIndex(int UID, int index) {this.UID = UID;this.index = index;}
    public HoneyIndex() {};

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass()!=HoneyIndex.class) return false;
        HoneyIndex other = (HoneyIndex) obj;
        return this.UID == other.UID && this.index == other.index;
    }
}
