package info.chain.chaini.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import info.chain.chaini.bean.EosAccountBean;
import info.chain.chaini.eosapi.crypto.ec.EosPrivateKey;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.gen.AccountTokenBean;
import info.chain.chaini.gen.ChainBean;
import info.chain.chaini.gen.TokenBean;
import info.chain.chaini.gen.UserBean;
import io.reactivex.observers.DisposableObserver;

public class TokenAddActivity extends AppCompatActivity {

    private Long chainId;
    private Long accountId;
    private ChainBean cur_chain;
    private AccountBean cur_account;
    private List<TokenBean> tokens;
    private List<TokenBean> token_finds;
    private List<TokenBean> token_selected;

    TokenAddRVAdapter tokenadd_adapter;

    private TokenSearchTask search_task = null;
    private AddTokenTask add_token_task = null;

    private View import_process_view;

    //
    private AssetManager asset_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        asset_manager = this.getAssets();

        chainId = new Long(0);
        chainId = getIntent().getLongExtra("chain_id", chainId);
        accountId = new Long(0);
        accountId = getIntent().getLongExtra("account_id", accountId);

        //
        import_process_view = findViewById(R.id.import_progress);

        //
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recycle_view);
        recycler.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        tokenadd_adapter = new TokenAddRVAdapter();
        recycler.setAdapter(tokenadd_adapter);

        //
        SearchView searchView = (SearchView) findViewById(R.id.searchEdit);
        ImageView iv_search = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        iv_search.setImageResource(R.drawable.ic_search_black_24dp);
        ImageView iv_search_close = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        iv_search_close.setImageResource(R.drawable.ic_clear_black_24dp);
        TextView tv_serach = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        tv_serach.setTextColor(getResources().getColor(R.color.black));
        tv_serach.setHintTextColor(getResources().getColor(R.color.gray));
        tv_serach.setHint("search your token");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        doSearch("");
    }

    private void doSearch(String searchQuery) {
        if (search_task != null) {
            return;
        }
        search_task = new TokenSearchTask(searchQuery);
        search_task.execute((Void) null);
    }

    private void doAddToken(Long token_id) {
        if (add_token_task != null) {
            return;
        }
        showProgress(true);
        add_token_task = new AddTokenTask(token_id);
        add_token_task.execute((Void)null);
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

    class TokenAddRVAdapter extends RecyclerView.Adapter<TokenAddRVAdapter.ViewHolder> implements TokenRVAdapterItemClickListener {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView iv_token_img;
            public TextView tv_token_name;
            public TextView tv_token_info;
            public TextView tv_add;
            public TokenRVAdapterItemClickListener callback;

            public ViewHolder(View view, TokenRVAdapterItemClickListener callback) {
                super(view);
                view.setOnClickListener(this);
                iv_token_img = view.findViewById(R.id.iv_token_img);
                tv_token_name = view.findViewById(R.id.tv_token_name);
                tv_token_info = view.findViewById(R.id.tv_token_info);
                tv_add = view.findViewById(R.id.tv_add);
                this.callback = callback;
            }

            @Override
            public void onClick(View v) {
                callback.onItemClick(v, getPosition());
            }
        }

        public TokenAddRVAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_token_add_content_item, parent, false);
            ViewHolder vh = new ViewHolder(view, this);
            return vh;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            TokenBean token = token_finds.get(position);
            Boolean bSelected = false;
            for(TokenBean temp_token : token_selected) {
                if(token.getToken_name().equals(temp_token.getToken_name())) {
                    bSelected = true;
                    break;
                }
            }
            holder.tv_token_name.setText(token.getToken_name());
            if(bSelected) {
                holder.tv_token_name.setTextColor(getResources().getColor(R.color.gray));
                holder.tv_add.setTextColor(getResources().getColor(R.color.gray));
            } else {
                holder.tv_token_name.setTextColor(getResources().getColor(R.color.black));
                holder.tv_add.setTextColor(getResources().getColor(R.color.black));
            }
            holder.tv_token_info.setText(token.getToken_info());
            InputStream token_icon = null;
            try {
                token_icon = asset_manager.open(token.getToken_icon());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(token_icon != null) {
                holder.iv_token_img.setImageBitmap(BitmapFactory.decodeStream(token_icon));
            }
        }

        public int getItemCount() {
            return token_finds.size();
        }

        @Override
        public void onItemClick(View view, int position) {
            TokenBean token = token_finds.get(position);
            for(TokenBean temp_token : token_selected) {
                if(token.getToken_name().equals(temp_token.getToken_name())) {
                    return;
                }
            }

            doAddToken(token_finds.get(position).getId());
        }
    }

    public interface TokenRVAdapterItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class TokenSearchTask extends AsyncTask<Void, Void, Boolean> {
        private String search_query;

        TokenSearchTask(String search_query) {
            this.search_query = search_query;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //
            //
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

            if(tokens == null) {
                tokens = new ArrayList<>();
                List<TokenBean> temp_tokens = MyApplication.getTokens(cur_chain.getId());
                for(TokenBean temp_token : temp_tokens) {
                    tokens.add(temp_token);
                }
            }

            if(token_selected == null) {
                token_selected = new ArrayList<>();
                List<AccountTokenBean> account_tokens = MyApplication.getAccountTokens(cur_chain.getId(), cur_account.getId());
                for (AccountTokenBean account_token : account_tokens) {
                    for (TokenBean token : tokens) {
                        if (account_token.getToken_id().equals(token.getId())) {
                            token_selected.add(token);
                            break;
                        }
                    }
                }
            }

            if(token_finds == null) {
                token_finds = new ArrayList<>();
            }

            token_finds.clear();
            for(TokenBean token : tokens) {
                if(token.getToken_name().toUpperCase().contains(search_query.toUpperCase())) {
                    token_finds.add(token);
                    continue;
                }

                if(token.getToken_info().toUpperCase().contains(search_query.toUpperCase())) {
                    token_finds.add(token);
                    continue;
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            search_task = null;
            tokenadd_adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            search_task = null;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AddTokenTask extends AsyncTask<Void, Void, Boolean> {

        private final Long token_id;

        AddTokenTask(Long token_id) {
            this.token_id = token_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //
            AccountTokenBean account_token = new AccountTokenBean();
            account_token.setAccount_id(cur_account.getId());
            account_token.setMaster(new Long(0));
            account_token.setToken_info("XXX");
            account_token.setToken_balance(new Double(0));
            account_token.setToken_id(token_id);
            MyApplication.addAccountToken(cur_chain.getId(), cur_account.getId(), account_token);

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
            showProgress(false);
            add_token_task = null;
            if (success) {
                //
                finish();
            } else {
                CharSequence text = "add failed!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            add_token_task = null;
        }
    }
}
