package com.koli.openquiz.sync.domain;

public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    public Orientation turnCw() {
        int index = this.ordinal() + 1;
        if(index > 3) {
            index = 0;
        }
        return values()[index];
    }

    public Orientation turnCcw() {
        int index = this.ordinal() - 1;
        if(index < 0) {
            index = 3;
        }
        return values()[index];
    }
}
