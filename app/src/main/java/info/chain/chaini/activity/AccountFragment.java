package info.chain.chaini.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.gen.ChainBean;
import info.chain.chaini.utils.ActivityUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AppBarLayout appbar_layout;
    private Toolbar toolbar;
    private View toolbar_expand;
    private View toolbar_collapse;
    private ImageView iv_transfer;
    private ImageView iv_collection;
    private ImageView iv_account_import;
    private ImageView iv_account_add;
    private LinearLayout ll_transfer;
    private LinearLayout ll_collection;
    private LinearLayout ll_account_import;
    private LinearLayout ll_account_add;
    private RecyclerView recycler_view;
    private int mask_color;
    private AccountRVAdapter account_rv_adapter;

    public AccountFragment() {
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
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        account_rv_adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        appbar_layout = view.findViewById(R.id.account_appbar);
        toolbar = view.findViewById(R.id.account_toolbar_layout);
        toolbar_expand = view.findViewById(R.id.account_toolbar_expand);
        toolbar_collapse = view.findViewById(R.id.account_toolbar_collapse);
        iv_transfer = view.findViewById(R.id.iv_transfer);
        iv_collection = view.findViewById(R.id.iv_collection);
        iv_account_import = view.findViewById(R.id.iv_account_import);
        iv_account_add = view.findViewById(R.id.iv_account_add);
        ll_transfer = view.findViewById(R.id.ll_transfer);
        ll_collection = view.findViewById(R.id.ll_collection);
        ll_account_import = view.findViewById(R.id.ll_account_import);
        ll_account_add = view.findViewById(R.id.ll_account_add);
        recycler_view = view.findViewById(R.id.recycler_view);
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
            }
        });

        //
        iv_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //
        iv_collection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //
        iv_account_import.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                ActivityUtils.next(getActivity(), AccountImportActivity.class, bundle);
            }
        });

        //
        iv_account_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //
        ll_transfer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //
        ll_collection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //
        ll_account_import.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                ActivityUtils.next(getActivity(), AccountImportActivity.class, bundle);
            }
        });

        //
        ll_account_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(this.getActivity());
        recycler_view.setLayoutManager(layout_manager);
        account_rv_adapter = new AccountRVAdapter();
        recycler_view.setAdapter(account_rv_adapter);

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

    private class AccountRVAdapter extends RecyclerView.Adapter<AccountRVAdapter.ViewHolder> implements View.OnClickListener {
        //
        List<AccountBean> accountBeans;

        //
        public AccountRVAdapter() {
            accountBeans = MyApplication.getAccounts();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_account_account, parent, false);
            ViewHolder vh = new ViewHolder(view);
            vh.itemView.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ChainBean chainBean = MyApplication.getChain(accountBeans.get(position).getChain_id());
            holder.iv_coin_img.setImageResource(R.mipmap.ic_launcher);
            holder.tv_account_name.setText(accountBeans.get(position).getAccount_name());
            holder.tv_account_name.setText(chainBean.getChain_type());
        }

        @Override
        public int getItemCount() {
            return accountBeans.size();
        }

        @Override
        public void onClick(View v) {

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView iv_coin_img;
            public TextView tv_account_name;
            public TextView tv_chain_name;
            public ViewHolder(View view) {
                super(view);
                iv_coin_img = view.findViewById(R.id.iv_coin_img);
                tv_account_name = view.findViewById(R.id.tv_account_name);
                tv_chain_name = view.findViewById(R.id.tv_chain_name);
            }
        }
    }
}
