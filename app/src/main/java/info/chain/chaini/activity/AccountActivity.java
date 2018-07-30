package info.chain.chaini.activity;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.utils.ActivityUtils;

public class AccountActivity extends AppCompatActivity {

    //
    private List<AccountBean> accounts;
    private AccountBean master_account;

    //
    private RecyclerView recycler_view;

    //
    private AccountRVAdapter account_rv_adapter;
    private RefreshTask refresh_task;

    //
    private AssetManager asset_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        if(accounts == null) {
            accounts = new ArrayList<>();
        }

        //
        CollapsingToolbarLayout collapsing_layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsing_layout.setTitle("None Master Account");

        //
        asset_manager = getAssets();

        //
        recycler_view = findViewById(R.id.recycler_view);

        //
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layout_manager);
        account_rv_adapter = new AccountRVAdapter();
        recycler_view.setAdapter(account_rv_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        doRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account_import) {
            final Bundle bundle = new Bundle();
            ActivityUtils.next(AccountActivity.this, AccountImportActivity.class, bundle);
            return true;
        }
        else if(id == R.id.action_account_add) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void doRefresh() {
        if (refresh_task != null)
            return;

        refresh_task = new RefreshTask();
        refresh_task.execute((Void) null);
    }

    private class AccountRVAdapter extends RecyclerView.Adapter<AccountRVAdapter.ViewHolder> implements AccountRVAdapterItemClickListener {
        //
        public AccountRVAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_account_content_item, parent, false);
            ViewHolder vh = new ViewHolder(view, this);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AccountBean account = accounts.get(position);
            InputStream account_icon = null;
            try {
                account_icon = asset_manager.open(account.getAccount_icon());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(account_icon != null) {
                holder.iv_account_image.setImageBitmap(BitmapFactory.decodeStream(account_icon));
            }

            if(account.getMaster().equals(1)) {
                holder.tv_account_master.setText("*");
            } else {
                holder.tv_account_master.setText(" ");
            }

            holder.tv_account_name.setText(account.getAccount_name());
            holder.tv_chain_name.setText(account.getChain().getChain_type());
        }

        @Override
        public int getItemCount() {
            return accounts.size();
        }

        @Override
        public void onItemSubClick(View view, int position) {
            AccountBean account = accounts.get(position);
            final Bundle bundle = new Bundle();
            bundle.putLong("chain_id", account.getChain_id());
            bundle.putLong("account_id", account.getId());
            ActivityUtils.next(AccountActivity.this, TokenActivity.class, bundle);
        }
        @Override
        public void onItemImageClick(View view, int position) {
            MyApplication.SelectAccount(accounts.get(position).getId());
            doRefresh();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView iv_account_image;
            public RelativeLayout item_sub_layout;
            public TextView tv_account_name;
            public TextView tv_chain_name;
            public TextView tv_account_master;
            public AccountRVAdapterItemClickListener callback;

            public ViewHolder(View view, AccountRVAdapterItemClickListener callback) {
                super(view);
                iv_account_image = view.findViewById(R.id.iv_account_image);
                tv_account_name = view.findViewById(R.id.tv_account_name);
                tv_chain_name = view.findViewById(R.id.tv_chain_name);
                tv_account_master = view.findViewById(R.id.tv_account_master);
                item_sub_layout = view.findViewById(R.id.item_sub_layout);
                iv_account_image.setOnClickListener(this);
                item_sub_layout.setOnClickListener(this);
                this.callback = callback;
            }

            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.iv_account_image) {
                    callback.onItemImageClick(v, getPosition());
                } else {
                    callback.onItemSubClick(v, getPosition());
                }
            }
        }
    }

    public interface AccountRVAdapterItemClickListener {
        void onItemImageClick(View view, int position);
        void onItemSubClick(View view, int position);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class RefreshTask extends AsyncTask<Void, Void, Boolean> {

        RefreshTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //
            accounts.clear();
            List<AccountBean> temp_accounts = MyApplication.getAccounts();
            for(AccountBean account : temp_accounts) {
                accounts.add(account);
            }

            master_account = null;
            for(AccountBean account : accounts) {
                if(account.getMaster().equals(1)) {
                    master_account = account;
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            refresh_task = null;

            //
            account_rv_adapter.notifyDataSetChanged();

            if(master_account != null) {
                CollapsingToolbarLayout collapsing_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                collapsing_layout.setTitle("Master:" + master_account.getAccount_name());
            } else  {
                CollapsingToolbarLayout collapsing_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                collapsing_layout.setTitle("No Master Account");
            }
        }

        @Override
        protected void onCancelled() {
            refresh_task = null;
        }
    }
}
