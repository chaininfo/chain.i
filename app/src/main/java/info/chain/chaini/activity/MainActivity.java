package info.chain.chaini.activity;

import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.gen.UserBeanDao;
import info.chain.chaini.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation_view;

    private HomeFragment home_fragment;
    private ApplicationFragment app_fragment;
    private AccountFragment account_fragment;
    private TokenFragment token_fragment;
    private MeFragment me_fragment;
    private Fragment current_fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(home_fragment == null) {
                        home_fragment = HomeFragment.newInstance("", "");
                    }
                    switchFragment(current_fragment, home_fragment);
                    return true;
                case R.id.navigation_application:
                    if(app_fragment == null) {
                        app_fragment = ApplicationFragment.newInstance("", "");
                    }
                    switchFragment(current_fragment, app_fragment);
                    return true;
                case R.id.navigation_account:
                    /*
                    if(account_fragment == null) {
                        account_fragment = AccountFragment.newInstance("", "");
                    }
                    switchFragment(current_fragment, account_fragment);
                    return true;
                    */
                    if(token_fragment == null) {
                        token_fragment = TokenFragment.newInstance("", "");
                    }
                    switchFragment(current_fragment, token_fragment);
                    return true;
                case R.id.navigation_me:
                    if(me_fragment == null) {
                        me_fragment = MeFragment.newInstance("", "");
                    }
                    switchFragment(current_fragment, me_fragment);
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //
        MyApplication.getInstance().setUserBean(MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.User_name.eq(Utils.getSpUtils().getString("firstUser"))).build().unique());
        MyApplication.getInstance().onLogin();

        bottom_navigation_view = (BottomNavigationView) findViewById(R.id.navigation);
        bottom_navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom_navigation_view.setSelectedItemId(R.id.navigation_account);

        //
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation_view);
    }

    private void switchFragment(Fragment from, Fragment to) {
        if(to == null) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!to.isAdded()) {
            if(from == null) {
                transaction.add(R.id.fl_fragment, to).show(to).commit();
            } else {
                transaction.hide(from).add(R.id.fl_fragment, to).commitAllowingStateLoss();
            }
        } else {
            transaction.hide(from).show(to).commit();
        }
        current_fragment = to;
    }
}
