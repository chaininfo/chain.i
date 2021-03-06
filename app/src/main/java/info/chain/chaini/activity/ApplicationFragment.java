package info.chain.chaini.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.app.MyApplication;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.gen.ChainBean;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ApplicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private EditText et_search;
    private ViewFlow view_flow;
    private CircleFlowIndicator indicator;
    private AppBarLayout appbar_layout;
    private CollapsingToolbarLayout bar_layout;
    private Toolbar toolbar_layout;
    private View toolbar_collapse;
    private View toolbar_expand;
    private ImageView iv_transfer;
    private ImageView iv_collection;
    private ImageView iv_account_import;
    private ImageView iv_account_add;
    private LinearLayout ll_transfer;
    private LinearLayout ll_collection;
    private LinearLayout ll_account_import;
    private LinearLayout ll_account_add;
    private RecyclerView recycler_view;
    private SwipeRefreshLayout refresh;
    private LinearLayout ll_applicatoin_bar;
    private View toolbar_expand_mask;
    private View toolbar_collapse_mask;
    private View bar_mask;
    private int mask_color;

    public ApplicationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicationFragment newInstance(String param1, String param2) {
        ApplicationFragment fragment = new ApplicationFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application, container, false);
        et_search = view.findViewById(R.id.et_search_dialog);
        refresh = view.findViewById(R.id.application_swipe_layout);
        view_flow = view.findViewById(R.id.viewflow);
        indicator = view.findViewById(R.id.viewflowindic);
        appbar_layout = view.findViewById(R.id.application_appbar);
        iv_transfer = view.findViewById(R.id.iv_transfer);
        iv_collection = view.findViewById(R.id.iv_collection);
        iv_account_import = view.findViewById(R.id.iv_account_import);
        iv_account_add = view.findViewById(R.id.iv_account_add);
        ll_transfer = view.findViewById(R.id.ll_transfer);
        ll_collection = view.findViewById(R.id.ll_collection);
        ll_account_import = view.findViewById(R.id.ll_account_import);
        ll_account_add = view.findViewById(R.id.ll_account_add);
        toolbar_expand = view.findViewById(R.id.application_toolbar_expand);
        toolbar_collapse = view.findViewById(R.id.application_toolbar_collapse);
        recycler_view = view.findViewById(R.id.recycler_view);
        ll_applicatoin_bar = view.findViewById(R.id.ll_application_bar);
        toolbar_expand_mask = view.findViewById(R.id.v_application_toolbar_expand_mask);
        toolbar_collapse_mask = view.findViewById(R.id.v_application_toolbar_collapse_mask);
        bar_mask = view.findViewById(R.id.v_application_bar_mask);
        toolbar_layout = view.findViewById(R.id.application_toolbar_layout);
        mask_color = getResources().getColor(R.color.black);
        //
        AppCompatActivity ac = (AppCompatActivity)(getActivity());
        ac.setSupportActionBar(toolbar_layout);

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
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //
                refresh.setRefreshing(false);
            }
        });

        /*
        //
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onSearchRequested();
            }
        });
        */

        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getActivity().onSearchRequested();
                return true;
            }
        });

        //
        view_flow.setAdapter(new ApplicationVFAdapter());
        view_flow.setFlowIndicator(indicator);

        //
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(this.getActivity());
        recycler_view.setLayoutManager(layout_manager);
        RecyclerView.Adapter adapter = new ApplicationRVAdapter();
        recycler_view.setAdapter(adapter);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        view_flow.onConfigurationChanged(newConfig);
    }

    private class ApplicationRVAdapter extends RecyclerView.Adapter<ApplicationRVAdapter.ViewHolder>  {
        //
        List<AccountBean> accountBeans;

        //
        public ApplicationRVAdapter() {
            accountBeans = MyApplication.getAccounts();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_application_application, parent, false);
            ViewHolder vh = new ViewHolder(view);
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

    private class ApplicationVFAdapter extends BaseAdapter {
        private final int[] ids = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};

        public ApplicationVFAdapter() {
        }
        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_application_fv_item, parent, false);
            }
            ImageView image_view = (ImageView)convertView.findViewById(R.id.imgView);
            image_view.setImageResource(ids[position]);
            image_view.setScaleType(ImageView.ScaleType.FIT_XY);
            return convertView;
        }
    }
}
