package info.chain.chaini.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.permission.HiPermission;
import info.chain.chaini.permission.PermissionCallback;
import info.chain.chaini.permission.PermissionItem;

/**
 * Created by pocketEos on 2017/11/23.
 * Utils初始化相关
 */
public final class Utils {

    private static Context context;
    private static SPUtils spUtils;


    private Utils() {
        throw new UnsupportedOperationException("...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
        spUtils = new SPUtils("USER");
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext context
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("应该首先初始化");
    }

    /**
     * Gets sp utils.
     *
     * @return the sp utils
     */
    public static SPUtils getSpUtils() {
        return spUtils;
    }

    public static boolean getPermissions(List<PermissionItem> permissonItems, String msg) {
        final Boolean[] isGetPermissions = {false};
        HiPermission.create(context)
                .permissions(permissonItems)
                .title(context.getResources().getString(R.string.dear_user))
                .msg(msg)
                .animStyle(R.style.PermissionAnimScale)
                .style(R.style.PermissionThemeStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        isGetPermissions[0] = false;

                        CharSequence text = context.getResources().getString(R.string.close_permisson_toast);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                    @Override
                    public void onFinish() {
                        isGetPermissions[0] = true;
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                    }
                });
        return isGetPermissions[0];
    }
}
