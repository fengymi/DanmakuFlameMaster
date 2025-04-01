package master.flame.danmaku.danmaku.model;

import android.annotation.SuppressLint;

/**
 * 重写DanmakuTimer类，基于播放器的绝对时间戳进行同步
 */
public class DanmakuTimer {
    /**
     * 视频播放器的当前时间戳（毫秒）
     * 由VideoManager直接更新
     */
    public static long videoTime;
    
    /**
     * 是否使用播放器的原始时间
     * true: 使用videoTime作为时间源
     * false: 使用内部计时
     */
    public static boolean useOrigin = true;
    
    /**
     * 调试模式
     */
    public static boolean debug;

    /**
     * 当前弹幕系统的内部时间（毫秒）
     */
    public long currMillisecond;

    /**
     * 上次更新的时间间隔
     */
    private long lastInterval;

    public DanmakuTimer() {
    }

    public DanmakuTimer(long curr) {
        update(curr);
    }

    /**
     * 更新弹幕计时器的当前时间
     * @param curr 传入的时间值（毫秒）
     * @return 距离上次更新的时间间隔
     */
    public long update(long curr) {
        // 如果启用了播放器时间同步，则始终使用videoTime
        if (useOrigin) {
            long realTime = videoTime;
            lastInterval = realTime - currMillisecond;
            currMillisecond = realTime;
        } else {
            lastInterval = curr - currMillisecond;
            currMillisecond = curr;
        }
        return lastInterval;
    }

    /**
     * 向当前时间添加指定的毫秒数
     * @param mills 要添加的毫秒数
     * @return 距离上次更新的时间间隔
     */
    public long add(long mills) {
        // 如果启用了播放器时间同步，直接返回videoTime
        if (useOrigin) {
            return update(videoTime);
        }
        return update(currMillisecond + mills);
    }

    /**
     * 获取最后一次时间更新的间隔
     * @return 上次更新的时间间隔
     */
    public long lastInterval() {
        return lastInterval;
    }

    /**
     * 格式化时间为分:秒格式
     * @param time 时间（毫秒）
     * @return 格式化后的时间字符串
     */
    @SuppressLint("DefaultLocale")
    public static String formatTime(long time) {
        long allSecond = time / 1000;

        long second = allSecond % 60;
        long minute = allSecond / 60;
        return String.format("%02d:%02d", minute, second);
    }
}
