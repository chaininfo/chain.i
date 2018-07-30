package info.chain.chaini.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import info.chain.chaini.gen.AppBean;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private AppBarLayout appbar_layout;
    private Toolbar toolbar;
    private View toolbar_expand;
    private View toolbar_collapse;
    private ImageView iv_scan;
    private LinearLayout ll_scan;
    private RecyclerView recycler_view;
    private int mask_color;

    //private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        iv_scan = view.findViewById(R.id.iv_scan);
        toolbar = view.findViewById(R.id.home_toolbar_layout);
        toolbar_expand = view.findViewById(R.id.home_toolbar_expand);
        toolbar_collapse = view.findViewById(R.id.home_toolbar_collapse);
        appbar_layout = view.findViewById(R.id.home_appbar);
        ll_scan = view.findViewById(R.id.ll_scan);
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
        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //
        ll_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //
        recycler_view.setHasFixedSize(true);
        GridLayoutManager layout_manager = new GridLayoutManager(this.getActivity(), 4 , LinearLayoutManager.VERTICAL, false);
        layout_manager.setSmoothScrollbarEnabled(true);
        recycler_view.setLayoutManager(layout_manager);
        RecyclerView.Adapter adapter = new AppRVAdapter();
        recycler_view.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

    private class AppRVAdapter extends RecyclerView.Adapter<AppRVAdapter.ViewHolder>  {
        //
        List<AppBean> appBeans;

        //
        public AppRVAdapter() {
            appBeans = MyApplication.getApps();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_app, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.iv_app_logo.setImageResource(R.mipmap.ic_launcher);
            holder.tv_app_name.setText(appBeans.get(position).getApp_name());
        }

        @Override
        public int getItemCount() {
            return appBeans.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView iv_app_logo;
            public TextView tv_app_name;
            public ViewHolder(View view) {
                super(view);
                iv_app_logo = view.findViewById(R.id.iv_app_logo);
                tv_app_name = view.findViewById(R.id.tv_app_name);
            }
        }
    }
}
