package com.luck.picture.lib.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 边缘适配工具类：为任意视图添加系统边衬（WindowInsets）
 * 支持 Android 11+（API 30~）
 */
public class EdgeToEdgeInsetFixer {

    public enum Edge {
        TOP, BOTTOM, LEFT, RIGHT
    }

    /**
     * 为目标视图动态添加系统边距（navigation bar、status bar 避让）
     *
     * @param targetView 要适配的视图（如底部按钮容器）
     * @param edges      需要避让的边，例如：Edge.BOTTOM, Edge.TOP
     */
    public static void applyInsets(View targetView, Edge... edges) {
        if (targetView == null) {
            Log.w("EdgeToEdgeInsetFixer", "targetView is null");
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(targetView, (v, insets) -> {
            Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            ViewGroup.LayoutParams lp = v.getLayoutParams();
            if (!(lp instanceof ViewGroup.MarginLayoutParams)) {
                Log.w("EdgeToEdgeInsetFixer", "LayoutParams is not MarginLayoutParams");
                return insets;
            }

            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) lp;

            for (Edge edge : edges) {
                switch (edge) {
                    case TOP:
                        marginParams.topMargin = navInsets.top;
                        break;
                    case BOTTOM:
                        marginParams.bottomMargin = navInsets.bottom;
                        break;
                    case LEFT:
                        marginParams.leftMargin = navInsets.left;
                        break;
                    case RIGHT:
                        marginParams.rightMargin = navInsets.right;
                        break;
                }
            }

            v.setLayoutParams(marginParams);
            Log.d("EdgeToEdgeInsetFixer", "Applied insets: " + navInsets.toString());
            return insets;
        });

        ViewCompat.requestApplyInsets(targetView);
    }
}
