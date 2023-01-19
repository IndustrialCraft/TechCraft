package com.github.industrialcraft.techcraft.knapping;

import java.util.Arrays;

public class KnappingPattern {
    private final int[] pattern;
    private int bitsPresent;
    private int hash;
    public KnappingPattern() {
        this.pattern = new int[25];
        this.hash = 0;
    }
    public void fillPattern(int value){
        Arrays.fill(pattern, value);
        this.bitsPresent = value*25;
        onPatternModified();
    }
    private int getIndex(int x, int y){
        if(x < 0 || x > 4)
            throw new ArrayIndexOutOfBoundsException("x must be within 0..4");
        if(y < 0 || y > 4)
            throw new ArrayIndexOutOfBoundsException("y must be within 0..4");
        return x + (y*5);
    }
    public void chip(int x, int y){
        int index = getIndex(x, y);
        if(pattern[index] > 0) {
            this.bitsPresent--;
            pattern[index]--;
            onPatternModified();
        }
    }
    public void set(int x, int y, int value){
        pattern[getIndex(x, y)] = value;
    }
    public int get(int x, int y){
        return pattern[getIndex(x, y)];
    }
    public int get(int index){
        if(index < 0 || index > 24)
            throw new ArrayIndexOutOfBoundsException("index must be within 0..24");
        onPatternModified();
        return pattern[index];
    }
    public void set(int index, int value){
        if(index < 0 || index > 24)
            throw new ArrayIndexOutOfBoundsException("index must be within 0..24");
        onPatternModified();
        pattern[index] = value;
    }
    public void recomputeHash(){
        this.hash = 1;
        for(int i = 0;i < 25;i++) {
            hash <<= 2;
            hash += pattern[i];
        }
    }
    @Override
    public int hashCode() {
        if(hash == 0)
            recomputeHash();
        return hash;
    }
    private void onPatternModified(){
        this.hash = 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnappingPattern that = (KnappingPattern) o;
        return Arrays.equals(pattern, that.pattern);
    }

    public int getBitsPresent() {
        return bitsPresent;
    }
}
