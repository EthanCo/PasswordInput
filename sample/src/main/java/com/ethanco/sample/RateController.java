package com.ethanco.sample;

/**
 * @Description 频率控制
 * Created by YOLANDA on 2015-12-07.
 */
public class RateController {
    private int rateTime = 500;//500毫秒内按钮无效，可自己调整频率
    private long lastClickTime;

    public RateController(int rateTime) {
        this.rateTime = rateTime;
    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        if (0 < timeD && timeD < rateTime) {
            return true;
        }
        return false;
    }
}
