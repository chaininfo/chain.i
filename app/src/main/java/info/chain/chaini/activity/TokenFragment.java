package info.chain.chaini.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TokenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SwipeRefreshLayout refresh;
    private AppBarLayout appbar_layout;
    private Toolbar toolbar_layout;
    private View toolbar_expand;
    private View toolbar_collapse;
    private ImageView iv_token_add;
    private LinearLayout ll_account;
    private RecyclerView recycler_view;
    private ImageView iv_account_image;
    private TextView tv_account_name;
    private TextView tv_token_total_asset;
    private TextView toolbar_title;

    //
    private ChainBean cur_chain;
    private AccountBean cur_account;
    private List<AccountTokenBean> account_tokens;

    //
    private int mask_color;
    private TokenRVAdapter token_rv_adapter;
    private AssetManager asset_manager;

    //
    private boolean auto_refresh;
    private TokenRefreshTask refresh_task;

    //
    private double total_asset;

    public TokenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TokenFragment newInstance(String param1, String param2) {
        TokenFragment fragment = new TokenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //
        asset_manager = this.getContext().getAssets();

        //
        if(account_tokens == null) {
            account_tokens = new ArrayList<>();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAccountToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        refresh = view.findViewById(R.id.application_swipe_layout);
        appbar_layout = view.findViewById(R.id.token_appbar);
        toolbar_layout = view.findViewById(R.id.token_toolbar_layout);
        toolbar_expand = view.findViewById(R.id.token_toolbar_expand);
        toolbar_collapse = view.findViewById(R.id.token_toolbar_collapse);
        iv_token_add = view.findViewById(R.id.iv_token_add);
        ll_account = view.findViewById(R.id.ll_account);
        recycler_view = view.findViewById(R.id.recycler_view);
        iv_account_image = view.findViewById(R.id.iv_account_image);
        tv_account_name = view.findViewById(R.id.tv_account_name);
        tv_token_total_asset = view.findViewById(R.id.tv_token_total_asset);
        toolbar_title = view.findViewById(R.id.toolbar_title);

        //
        mask_color = getResources().getColor(R.color.black);

        //
        appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int offset = Math.abs(verticalOffset);
                int total = appbar_layout.getTotalScrollRange();
                System.out.println("verticalOffset = " + verticalOffset + " total = " + total);
                int alphaIn = offset;
                int alphaOut = (200 - offset) < 0 ? 0 : (200 - offset);
                int maskColorIn = Color.argb(alphaIn, Color.red(mask_color), Color.green(mask_color), Color.blue(mask_color));
                int maskColorInDouble = Color.argb(alphaIn * 2, Color.red(mask_color), Color.green(mask_color), Color.blue(mask_color));
                int maskColorOut = Color.argb(alphaOut * 2, Color.red(mask_color), Color.green(mask_color), Color.blue(mask_color));
                if (offset <= total / 2) {
                    toolbar_expand.setVisibility(View.VISIBLE);
                    toolbar_collapse.setVisibility(View.GONE);
                    //toolbar_expand_mask.setBackgroundColor(maskColorInDouble);
                } else {
                    toolbar_expand.setVisibility(View.GONE);
                    toolbar_collapse.setVisibility(View.VISIBLE);
                    //toolbar_collapse_mask.setBackgroundColor(maskColorOut);
                }
                //bar_mask.setBackgroundColor(maskColorIn);

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
                refreshAccountToken();
            }
        });

        //
        iv_token_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cur_account == null) {
                    CharSequence text = "There is not account yet, can not add token!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                    toast.show();
                    return;
                }
                final Bundle bundle = new Bundle();
                bundle.putLong("chain_id", cur_chain.getId());
                bundle.putLong("account_id", cur_account.getId());
                ActivityUtils.next(getActivity(), TokenAddActivity.class, bundle);
            }
        });

        //
        ll_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                ActivityUtils.next(getActivity(), AccountActivity.class, bundle);
            }
        });

        //
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(this.getActivity());
        recycler_view.setLayoutManager(layout_manager);
        token_rv_adapter = new TokenRVAdapter();
        recycler_view.setAdapter(token_rv_adapter);

        //
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur_account == null) {
                    CharSequence text = "There is not account yet, can not add token!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                    toast.show();
                    return;
                }
                final Bundle bundle = new Bundle();
                bundle.putLong("chain_id", cur_chain.getId());
                bundle.putLong("account_id", cur_account.getId());
                ActivityUtils.next(getActivity(), TokenAddActivity.class, bundle);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void refreshAccountToken() {
        if(refresh_task != null)
            return;

        refresh_task = new TokenRefreshTask();
        refresh_task.execute((Void) null);
    }

    private class TokenRVAdapter extends RecyclerView.Adapter<TokenRVAdapter.ViewHolder> implements TokenRVAdapterItemClickListener {
        //
        public TokenRVAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_token_content_item, parent, false);
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
    public class TokenRefreshTask extends AsyncTask<Void, Void, Boolean> {

        TokenRefreshTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if(cur_chain == null) {
                cur_chain = MyApplication.getChain();
            }
            if(cur_chain == null) {
                return true;
            }

            {
                cur_account = MyApplication.getAccount();
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
            if(cur_account != null) {
                tv_account_name.setText(cur_account.getAccount_name());
            }

            //
            tv_token_total_asset.setText(String.format("$%.2f", total_asset));

            //
            toolbar_title.setText(String.format("$%.2f", total_asset));

            //
            if(auto_refresh == false) {
                refresh.setRefreshing(false);
                auto_refresh = true;
                if (success) {
                    CharSequence text = "refresh successful!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                    toast.show();
                } else {
                    CharSequence text = "refresh failed!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
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
