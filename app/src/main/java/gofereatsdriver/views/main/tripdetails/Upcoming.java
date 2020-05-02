package gofereatsdriver.views.main.tripdetails;
/**
 * @package com.gofereats
 * @subpackage views.main.tripdetails
 * @category Upcoming
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsdriver.R;
import gofereatsdriver.adapters.main.TripsAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.datamodels.trips.TripListModel;
import gofereatsdriver.interfaces.YourTripsListener;

/************************************************************
 Upcoming
 Its shows the Drivers Upcoming Trips
 ****************************************************************/
public class Upcoming extends Fragment {

    public @InjectView(R.id.rcView)
    RecyclerView rcView;
    public @InjectView(R.id.listempty)
    TextView emView;
    private View view;
    private YourTripsListener listener;
    private YourTrips mActivity;

    public static Upcoming newInstance() {
        Upcoming fragment = new Upcoming();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (YourTripsListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Past must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        AppController.getAppComponent().inject(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_yourtrips, container, false);

        }
        ButterKnife.inject(this, view);

        rcView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcView.setLayoutManager(layoutManager);

        ArrayList<TripListModel> trips = new ArrayList<>();
        YourTrips activity = (YourTrips) getActivity();
        trips = activity.getTodayTripDetailsList();

        if (trips.size() <= 0) {
            emView.setVisibility(View.VISIBLE);
        } else {
            emView.setVisibility(View.GONE);
        }

        RecyclerView.Adapter adapter = new TripsAdapter(trips, mActivity);

        rcView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

    }

    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (YourTrips) getActivity();
    }

}
