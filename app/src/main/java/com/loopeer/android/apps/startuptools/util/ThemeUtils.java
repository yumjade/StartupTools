package com.loopeer.android.apps.startuptools.util;

import com.loopeer.android.apps.startuptools.R;

/**
 * Created by KorHsien on 2015/3/27.
 */
public class ThemeUtils {
    public static int getThemeByParentItemPosition(int parentId) {
        switch (parentId) {
            case 0:
                return R.style.Theme_StartUpTools_Tint_0;
            case 1:
                return R.style.Theme_StartUpTools_Tint_1;
            case 2:
                return R.style.Theme_StartUpTools_Tint_2;
            case 3:
                return R.style.Theme_StartUpTools_Tint_3;
            case 4:
                return R.style.Theme_StartUpTools_Tint_4;
            case 5:
                return R.style.Theme_StartUpTools_Tint_5;
            case 6:
                return R.style.Theme_StartUpTools_Tint_6;
            case 7:
                return R.style.Theme_StartUpTools_Tint_7;
        }
        return R.style.Theme_StartUpTools_NoTitleBar;
    }
}
