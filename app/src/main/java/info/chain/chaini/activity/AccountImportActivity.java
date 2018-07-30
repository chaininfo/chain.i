package info.chain.chaini.activity;

import android.Manifest;
import android.accounts.Account;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.bean.EosAccountBean;
import info.chain.chaini.bean.QrCodeAccountPrivateKeyBean;
import info.chain.chaini.eosapi.crypto.ec.EosPrivateKey;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.gen.UserBean;
import info.chain.chaini.permission.PermissionItem;
import info.chain.chaini.utils.ActivityUtils;
import info.chain.chaini.utils.PasswordToKeyUtils;
import info.chain.chaini.utils.Utils;

public class AccountImportActivity extends AppCompatActivity {

    private AccountImportTask account_import_task = null;

    private ImageView iv_scan;
    private EditText et_account_name;
    private EditText et_owner_private_key;
    private EditText et_active_private_key;
    private Button bu_import_account;
    private View import_process_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_import);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_account_name = findViewById(R.id.account_name);
        et_owner_private_key = findViewById(R.id.owner_private_key);
        et_active_private_key = findViewById(R.id.active_private_key);
        bu_import_account = findViewById(R.id.import_account);
        import_process_view = findViewById(R.id.import_progress);

        bu_import_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptImport();
            }
        });

        iv_scan = findViewById(R.id.iv_scan);
        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
                permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, getString(R.string.camer), R.drawable.permission_ic_camera));
                if (Utils.getPermissions(permissonItems, getString(R.string.open_camer_scan))) {
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "import_account");
                    ActivityUtils.next(AccountImportActivity.this, ScanCodeActivity.class, bundle, 100);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                    List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
                    permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, getString(R.string.camer), R.drawable.permission_ic_camera));
                    if (Utils.getPermissions(permissonItems, getString(R.string.open_camer_scan))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("from", "import_account");
                        ActivityUtils.next(AccountImportActivity.this, ScanCodeActivity.class, bundle, 100);
                    }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            /*
            mAccountName.setText(data.getStringExtra("account_name"));
            mOwnerPrivateKey.setText(data.getStringExtra("owner_private_key"));
            mActivePrivateKey.setText(data.getStringExtra("active_private_key"));
            mAccountName.setSelection(mAccountName.getText().length());
            */
            String strResult = data.getStringExtra("scan_result");
            Gson mGson = new GsonBuilder().create();
            QrCodeAccountPrivateKeyBean acount = mGson.fromJson(strResult, QrCodeAccountPrivateKeyBean.class);
            et_account_name.setText(acount.getAccount_name());
            et_owner_private_key.setText(acount.getOwner_private_key());
            et_active_private_key.setText(acount.getActive_private_key());
            et_account_name.setSelection(et_account_name.getText().length());
        }
    }

    private void attemptImport() {
        if (account_import_task != null) {
            return;
        }
        et_account_name.setError(null);
        et_owner_private_key.setError(null);
        et_active_private_key.setError(null);

        String account_name = et_account_name.getText().toString();
        String owner_private_key = et_owner_private_key.getText().toString();
        String active_private_key = et_active_private_key.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one
        if (TextUtils.isEmpty(account_name)) {
            et_account_name.setError(getString(R.string.error_field_required));
            focusView = et_account_name;
            cancel = true;
        } else if (!isAccountNameValid(account_name)) {
            et_account_name.setError(getString(R.string.error_invalid_username));
            focusView = et_account_name;
            cancel = true;
        } else if (TextUtils.isEmpty(owner_private_key) || !isPrivateKeyValid(owner_private_key)) {
            et_owner_private_key.setError(getString(R.string.error_invalid_password));
            focusView = et_owner_private_key;
            cancel = true;
        } else if (TextUtils.isEmpty(active_private_key) || !isPrivateKeyValid(active_private_key)) {
            et_active_private_key.setError(getString(R.string.error_invalid_password));
            focusView = et_active_private_key;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }

        PasswordDialog mPasswordDialog = new PasswordDialog(AccountImportActivity.this, new PasswordCallback() {
            @Override
            public void sure(final String password) {
                if (MyApplication.getWallet().getWallet_shapwd().equals(PasswordToKeyUtils.shaCheck(password))) {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    showProgress(true);
                    account_import_task = new AccountImportTask(account_name, owner_private_key, active_private_key);
                    account_import_task.execute((Void) null);
                } else {
                    CharSequence text = getApplicationContext().getString(R.string.input_password);;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }

            @Override
            public void cancle() {
            }
        });
        mPasswordDialog.setCancelable(true);
        mPasswordDialog.show();
    }

    private boolean isAccountNameValid(String user) {
        //TODO: Replace this with your own logic
        return true;
        //return user.contains("@");
    }

    private boolean isPrivateKeyValid(String key) {
        //TODO: Replace this with your own logic
        return key.length() > 4;
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            import_process_view.setVisibility(show ? View.VISIBLE : View.GONE);
            import_process_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    import_process_view.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            import_process_view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AccountImportTask extends AsyncTask<Void, Void, Boolean> {

        private final String account_name;
        private final String owner_private_key;
        private final String active_private_key;

        AccountImportTask(String account_name, String owner_private_key, String active_private_key) {
            this.account_name = account_name;
            this.owner_private_key = owner_private_key;
            this.active_private_key = active_private_key;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //
            EosPrivateKey mActiveKey = new EosPrivateKey(active_private_key);
            EosPrivateKey mOwnerKey = new EosPrivateKey(owner_private_key);
            UserBean userBean = MyApplication.getUserBean();
            if(userBean != null) {
                //
                int master = 0;
                if(MyApplication.getAccounts().size() <= 0) {
                    master = 1;
                }

                AccountBean account = new AccountBean();
                account.setAccount_name(account_name);
                account.setChain_id(MyApplication.getChain().getId());
                account.setMaster(new Long((long) 0));
                account.setUser_id(userBean.getId());
                account.setAccount_icon("token/icon_eos.png");
                account.setAccount_icon_id(new Long(master));
                EosAccountBean eosaccount = new EosAccountBean();
                eosaccount.account_name = account_name;
                eosaccount.active_key = mActiveKey.getPublicKey().toString();
                eosaccount.owner_key = mOwnerKey.getPublicKey().toString();
                Gson mGson = new GsonBuilder().create();
                String streosaccount = mGson.toJson(eosaccount);
                account.setAccount_info(streosaccount);
                MyApplication.addAccount(MyApplication.getChain().getId(), account);

                //
                MyApplication.getEosWallet().importKey("default", owner_private_key);
                MyApplication.getEosWallet().importKey("default", active_private_key);
                MyApplication.getEosWallet().saveFile("default");
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
            account_import_task = null;
            showProgress(false);

            if (success) {
                //
                CharSequence text = "import successful!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
                finish();
            } else {
                CharSequence text = "import failed!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();

                et_account_name.setError(getString(R.string.error_invalid_username));
                et_account_name.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            account_import_task = null;
            showProgress(false);
        }
    }
}
