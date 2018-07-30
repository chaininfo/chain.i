package info.chain.chaini.activity;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.gen.AccountTokenBean;
import info.chain.chaini.gen.ChainBean;
import info.chain.chaini.gen.TokenBean;
import info.chain.chaini.utils.ActivityUtils;
import io.reactivex.observers.DisposableObserver;

public class TokenActivity extends AppCompatActivity {

    private Long chainId;
    private Long accountId;

    //
    private ChainBean cur_chain;
    private AccountBean cur_account;
    private List<AccountTokenBean> account_tokens;

    private SwipeRefreshLayout refresh;
    private RecyclerView recycler_view;
    private AppBarLayout appbar_layout;

    //
    private TokenRVAdapter token_rv_adapter;

    //
    private boolean auto_refresh = true;
    private RefreshTask refresh_task = null;

    //
    private AssetManager asset_manager;

    //
    private double total_asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        if(account_tokens == null) {
            account_tokens = new ArrayList<>();
        }

        //
        asset_manager = getAssets();

        chainId = new Long(0);
        chainId = getIntent().getLongExtra("chain_id", chainId);
        accountId = new Long(0);
        accountId = getIntent().getLongExtra("account_id", accountId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle = new Bundle();
                bundle.putLong("chain_id", cur_chain.getId());
                bundle.putLong("account_id", cur_account.getId());
                ActivityUtils.next(TokenActivity.this, TokenAddActivity.class, bundle);
            }
        });

        //
        CollapsingToolbarLayout collapsing_layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsing_layout.setTitle("Total Asset:$0.00");

        //
        recycler_view = findViewById(R.id.recycler_view);
        refresh = findViewById(R.id.application_swipe_layout);
        appbar_layout = findViewById(R.id.app_bar);

        //
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layout_manager);
        token_rv_adapter = new TokenRVAdapter();
        recycler_view.setAdapter(token_rv_adapter);

        //
        //
        appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    refresh.setEnabled(true);
                    ;
                }else {
                    refresh.setEnabled(false);
                }
            }
        });

        //
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                auto_refresh = false;
                doRefresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        doRefresh();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_token, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_token_add) {
            final Bundle bundle = new Bundle();
            bundle.putLong("chain_id", cur_chain.getId());
            bundle.putLong("account_id", cur_account.getId());
            ActivityUtils.next(TokenActivity.this, TokenAddActivity.class, bundle);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doRefresh() {
        if(refresh_task != null)
            return;

        refresh_task = new RefreshTask();
        refresh_task.execute((Void) null);
    }

    private class TokenRVAdapter extends RecyclerView.Adapter<TokenRVAdapter.ViewHolder> implements TokenRVAdapterItemClickListener {

        //
        public TokenRVAdapter() {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_token_content_item, parent, false);
            ViewHolder vh = new ViewHolder(view, this);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AccountTokenBean account_token = account_tokens.get(position);
            TokenBean token = account_token.getToken();
            InputStream token_icon = null;
            try {
                token_icon = asset_manager.open(token.getToken_icon());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(token_icon != null) {
                holder.iv_token_img.setImageBitmap(BitmapFactory.decodeStream(token_icon));
            }
            holder.tv_token_name.setText(token.getToken_name());
            holder.tv_token_rate.setText(String.valueOf(token.getCny_rate()));
            holder.tv_token_balance.setText(String.format("$%.2f", account_token.getToken_balance()));
            holder.tv_token_asset.setText(String.format("$%.2f", account_token.getToken_balance() * token.getCny_rate()));
        }

        @Override
        public int getItemCount() {
            return account_tokens.size();
        }

        @Override
        public void onItemClick(View view, int position) {

        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView iv_token_img;
            public TextView tv_token_name;
            public TextView tv_token_rate;
            public TextView tv_token_balance;
            public TextView tv_token_asset;
            public TokenRVAdapterItemClickListener callback;

            public ViewHolder(View view, TokenRVAdapterItemClickListener callback) {
                super(view);
                view.setOnClickListener(this);
                this.callback = callback;
                iv_token_img = view.findViewById(R.id.iv_token_img);
                tv_token_name = view.findViewById(R.id.tv_token_name);
                tv_token_rate = view.findViewById(R.id.tv_token_rate);
                tv_token_balance = view.findViewById(R.id.tv_token_balance);
                tv_token_asset = view.findViewById(R.id.tv_token_asset);
            }

            @Override
            public void onClick(View v) {
                callback.onItemClick(v, getPosition());
            }
        }
    }

    public interface TokenRVAdapterItemClickListener {
        void onItemClick(View view, int position);
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
            if(cur_chain == null) {
                cur_chain = MyApplication.getChain(chainId);
            }
            if(cur_chain == null) {
                return true;
            }

            if(cur_account == null) {
                cur_account = MyApplication.getAccount(chainId, accountId);
            }
            if(cur_account == null) {
                return true;
            }

            account_tokens.clear();
            List<AccountTokenBean> temp_account_tokens = MyApplication.getAccountTokens(cur_chain.getId(), cur_account.getId());
            for(AccountTokenBean accounttoken : temp_account_tokens) {
                account_tokens.add(accounttoken);
            }

            /*
            get token balance
             */
            for(AccountTokenBean account_token : account_tokens) {
                MyApplication.getEosApi()
                    .getCurrencyBalance("eosio.token", cur_account.getAccount_name(), account_token.getToken().getToken_name().toUpperCase())
                    .subscribeWith(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String response) {
                            //
                            if (null != response) {
                                // successfule
                                Gson mGson = new GsonBuilder().create();
                                List<String> token_balances = mGson.fromJson(response, new TypeToken<List<String>>(){}.getType());
                                if(token_balances.size() <= 0)
                                    return;

                                String token_balance = token_balances.get(0);
                                int start = token_balance.indexOf(' ');
                                String str_balance = token_balance.substring(0, start);
                                Double dou_balance = new Double(str_balance);
                                account_token.setToken_balance(dou_balance);
                                MyApplication.updateAccountToken(cur_chain.getId(), cur_account.getId(), account_token);
                            } else {
                                // failed
                            }
                        }

                        public void onError(Throwable t) {
                            //toast(t.toString());
                        }

                        public void onComplete() {

                        }
                    });
            }

            /*
            get token total asset
             */
            total_asset = 0;
            for(AccountTokenBean account_token : account_tokens) {
                TokenBean token = account_token.getToken();
                total_asset += (token.getCny_rate() * account_token.getToken_balance());
            }

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            refresh_task = null;

            //
            token_rv_adapter.notifyDataSetChanged();

            //
            CollapsingToolbarLayout collapsing_layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
            collapsing_layout.setTitle(String.format("Total Asset:$%.2f",total_asset));

            //
            if(auto_refresh == false) {
                refresh.setRefreshing(false);
                auto_refresh = true;
                if (success) {
                    CharSequence text = "refresh successful!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                } else {
                    CharSequence text = "refresh failed!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            refresh_task = null;
        }
    }
}
