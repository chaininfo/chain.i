package info.chain.chaini.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.gen.UserBean;
import info.chain.chaini.gen.UserBeanDao;
import info.chain.chaini.gen.WalletBean;
import info.chain.chaini.gen.WalletBeanDao;
import info.chain.chaini.utils.ActivityUtils;
import info.chain.chaini.utils.EncryptUtil;
import info.chain.chaini.utils.PasswordToKeyUtils;
import info.chain.chaini.utils.Utils;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView user_name;
    private EditText user_password;
    private EditText user_confirm_password;
    private Button user_login;
    private View login_process_view;
    private View login_from_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        user_name = (AutoCompleteTextView) findViewById(R.id.login_user);
        user_password = (EditText) findViewById(R.id.login_password);
        user_confirm_password = (EditText)findViewById(R.id.login_confirm_password);
        user_login = (Button) findViewById(R.id.login_create_wallet);
        login_process_view = (View)findViewById(R.id.login_progress);
        login_from_view = (View)findViewById(R.id.login_form);


        //
        user_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //
        user_confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //
        user_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        user_name.setError(null);
        user_password.setError(null);
        user_confirm_password.setError(null);

        // Store values at the time of the login attempt.
        String user = user_name.getText().toString();
        String password = user_password.getText().toString();
        String confirm_password = user_confirm_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one
        if (TextUtils.isEmpty(user)) {
            user_name.setError(getString(R.string.error_field_required));
            focusView = user_name;
            cancel = true;
        } else if (!isUserNameValid(user)) {
            user_name.setError(getString(R.string.error_invalid_username));
            focusView = user_name;
            cancel = true;
        } else if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            user_password.setError(getString(R.string.error_invalid_password));
            focusView = user_password;
            cancel = true;
        } else if (TextUtils.isEmpty(confirm_password) || !isPasswordValid(confirm_password)) {
            user_confirm_password.setError(getString(R.string.error_invalid_password));
            focusView = user_confirm_password;
            cancel = true;
        } else if (!password.equals(confirm_password)) {
            user_confirm_password.setError(getString(R.string.error_unmatch_password));
            focusView = user_confirm_password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(user, password, confirm_password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUserNameValid(String user) {
        //TODO: Replace this with your own logic
        return true;
        //return user.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*
            login_from_view.setVisibility(show ? View.GONE : View.VISIBLE);
            login_from_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_from_view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            */

            login_process_view.setVisibility(show ? View.VISIBLE : View.GONE);
            login_process_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_process_view.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_process_view.setVisibility(show ? View.VISIBLE : View.GONE);
            //login_from_view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String user;
        private final String password;
        private final String confirm_password;

        UserLoginTask(String user, String password, String confirm_password) {
            this.user = user;
            this.password = password;
            this.confirm_password = confirm_password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            //
            UserBean userBean = MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.User_name.eq(user.trim())).build().unique();
            if (userBean != null) {
                userBean.setUser_name(user.trim());
                MyApplication.getDaoInstant().getUserBeanDao().update(userBean);
                Utils.getSpUtils().put("firstUser", user.trim());
            } else {
                userBean = new UserBean();
                userBean.setUser_name(user.trim());
                userBean.setUser_img("");
                userBean.setUser_phone(user.trim());
                userBean.setUser_uid("");
                userBean.setChain_init(new Long(0));
                userBean.setAccount_init(new Long(0));
                userBean.setAccount_token_init(new Long(0));
                userBean.setToken_init(new Long(0));
                userBean.setApp_init(new Long(0));
                userBean.setWallet_init(new Long(0));
                MyApplication.getDaoInstant().getUserBeanDao().insert(userBean);
                Utils.getSpUtils().put("firstUser", user.trim());
            }

            WalletBean walletBean = MyApplication.getDaoInstant().getWalletBeanDao().queryBuilder().where(WalletBeanDao.Properties.Wallet_phone.eq(user.trim())).build().unique();
            if (walletBean != null) {
                String randomString = EncryptUtil.getRandomString(32);
                walletBean.setWallet_shapwd(PasswordToKeyUtils.shaEncrypt(randomString + password.trim()));
                walletBean.setWallet_phone(user.trim());
                walletBean.setWallet_name(user.trim().substring(7, 11).toString());
                MyApplication.getDaoInstant().getWalletBeanDao().update(walletBean);
            } else {
                walletBean = new WalletBean();
                String randomString = EncryptUtil.getRandomString(32);
                walletBean.setWallet_shapwd(PasswordToKeyUtils.shaEncrypt(randomString + password.trim()));
                walletBean.setWallet_phone(user.trim());
                walletBean.setWallet_name(user.trim().substring(7, 11).toString());
                MyApplication.getDaoInstant().getWalletBeanDao().insert(walletBean);
            }

            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
                ActivityUtils.next(LoginActivity.this, MainActivity.class, true);
            } else {
                user_name.setError(getString(R.string.error_incorrect_password));
                user_name.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

