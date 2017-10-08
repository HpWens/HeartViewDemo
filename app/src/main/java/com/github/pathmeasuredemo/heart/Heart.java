package com.github.pathmeasuredemo.heart;

/**
 * desc:心形实体
 * author: wens
 * date: 2017/9/30.
 */

public class Heart {

    private float x;
    private float y;

    private float progress;

    //当前心集合的索引
    private int index;

    public Heart() {

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
