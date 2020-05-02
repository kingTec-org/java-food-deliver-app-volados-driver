package gofereatsdriver.views.main.fragments;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage view.main.fragments
 * @category RatingFragment
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.interfaces.ActivityListener;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 RatingFragment
 Driver Ratings Details  Page
 *************************************************************** */

public class RatingFragment extends Fragment {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    private View view;
    private ActivityListener listener;

    public static RatingFragment newInstance() {
        RatingFragment fragment = new RatingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Rating must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppController.getAppComponent().inject(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_rating, container, false);

        }

        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

    }


    @Override
    public void onDestroy() {
        if (listener != null) listener = null;
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
