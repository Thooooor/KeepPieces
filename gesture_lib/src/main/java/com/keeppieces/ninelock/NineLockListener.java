package com.keeppieces.ninelock;

/**
 * 作者： 吴昶 .
 * 时间: 2018/12/10 16:59
 * 功能简介：
 */
public interface NineLockListener {
    void onLockResult(int[] result);
    void onError();
}
