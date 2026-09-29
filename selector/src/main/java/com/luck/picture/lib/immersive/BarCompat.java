package com.luck.picture.lib.immersive;

import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author：luck
 * @describe：系统栏兼容处理
 * Android 15（API 35）起 Window.setStatusBarColor/setNavigationBarColor 已弃用，
 * Android 15/16 在 targetSdk 35+ 时强制边到边，上述调用不再生效。
 * 为保留 Android 5.0~14 的视觉行为，同时避免 DEX 中残留对弃用 API 的直接调用
 * （Play Console 无边框弃用告警为静态扫描结果），这里统一通过反射调用。
 */
public final class BarCompat {

    private BarCompat() {
    }

    /**
     * 仅在 Android 5.0~14 上有实际效果
     */
    public static void setStatusBarColor(Window window, int color) {
        try {
            Window.class.getMethod("setStatusBarColor", int.class).invoke(window, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * 仅在 Android 5.0~14 上有实际效果
     */
    public static void setNavigationBarColor(Window window, int color) {
        try {
            Window.class.getMethod("setNavigationBarColor", int.class).invoke(window, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * 常量读取与字段写入均走反射，Android 15+ 边到边模式下内容默认延伸至刘海区域，无需设置
     */
    public static void setCutoutModeShortEdges(Window window) {
        try {
            int shortEdges = WindowManager.LayoutParams.class
                    .getField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES").getInt(null);
            WindowManager.LayoutParams lp = window.getAttributes();
            WindowManager.LayoutParams.class.getField("layoutInDisplayCutoutMode").setInt(lp, shortEdges);
            window.setAttributes(lp);
        } catch (Exception ignored) {
        }
    }
}
