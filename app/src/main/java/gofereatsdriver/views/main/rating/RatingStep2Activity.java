package gofereatsdriver.views.main.rating;
/**
 * @package com.gofereats
 * @subpackage views.main.profile
 * @category RatingStep2Activity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsdriver.R;
import gofereatsdriver.adapters.main.FeedBackAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.dropoffrating.DropoffissuesList;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/************************************************************
 RatingStep2Activity
 Its used to give Rating for Receiptant
 ****************************************************************/
public class RatingStep2Activity extends AppCompatActivity implements ServiceListener {

    public static ArrayList<DropoffissuesList> dropoffissuesListArrayList = new ArrayList<>();
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
    public @InjectView(R.id.ivThumbs_up)
    CircleImageView ivThumbsup;
    public @InjectView(R.id.ivThumbs_down)
    CircleImageView ivThumbsdown;
    public @InjectView(R.id.rltFeedback)
    RelativeLayout rltFeedback;
    public @InjectView(R.id.rvIssues)
    RecyclerView rvIssues;
    public @InjectView(R.id.ivClosepage)
    ImageView ivClosepage;
    public @InjectView(R.id.btnNext)
    CustomButton btnNext;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public RatingStep1Activity ratingStep1Activity;
    public FeedBackAdapter feedBackAdapter;
    public boolean isThumbsup, isThumbsdown;
    public int isthumbs;
    public String reason = "";
    public int getId = 0;
    public int Id = -1;
    public String addtionalcomments = "";
    private AlertDialog dialog;

    @OnClick(R.id.ivClosepage)
    public void close() {
        onBackPressed();
    }

    @OnClick(R.id.ivBack)
    public void back() {
        onBackPressed();
    }

    /**
     * DropOff thumbs up
     */
    @OnClick(R.id.ivThumbs_up)
    public void thumbsUp() {
        ivThumbsup.setImageResource(R.drawable.thumbs_up_selected);
        ivThumbsdown.setImageResource(R.drawable.thumbs_down_normal);
        rltFeedback.setVisibility(View.GONE);
        btnNext.setEnabled(true);
        btnNext.setBackground(getResources().getDrawable(R.drawable.background_blue));
        isthumbs = 1;
    }

    /**
     * DropOff thumbs down
     */
    @OnClick(R.id.ivThumbs_down)
    public void thumbsDown() {
        ivThumbsdown.setImageResource(R.drawable.thumbs_down_selected);
        ivThumbsup.setImageResource(R.drawable.thumbs_up_normal);
        rltFeedback.setVisibility(View.VISIBLE);
        isThumbsdown = true;
        isthumbs = 0;
        btnNext.setEnabled(true);
        btnNext.setBackground(getResources().getDrawable(R.drawable.background_blue));

        //Issues Adapter
        feedBackAdapter = new FeedBackAdapter(getApplicationContext(), dropoffissuesListArrayList);
        feedBackAdapter.setOnItemClickListener(new FeedBackAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int id, int positionz) {
                Id = id;
                next();
            }
        });
        rvIssues.setAdapter(feedBackAdapter);
        feedBackAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_step2);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setAutoMeasureEnabled(false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvIssues.setLayoutManager(gridLayoutManager);
        /*rvIssues.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvIssues.setLayoutManager(layoutManager);*/

        getId = getIntent().getIntExtra("dropoffid", 0);
        addtionalcomments = getIntent().getStringExtra("comments");

        ratingStep1Activity = new RatingStep1Activity();
        dropoffissuesListArrayList = ratingStep1Activity.getIssues();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isThumbsdown) {
                    next();

                }
                dropOff();

            }
        });
    }

    public void next() {
        reason = "";
        for (int i = 0; i < dropoffissuesListArrayList.size(); i++) {
            if (!dropoffissuesListArrayList.get(i).isSelected()) {
                dropoffissuesListArrayList.get(i).getId();

                if (TextUtils.isEmpty(reason)) {
                    reason = reason + dropoffissuesListArrayList.get(i).getId();
                } else {
                    reason = reason + "," + dropoffissuesListArrayList.get(i).getId();
                }
            }
        }
    }

    /**
     * Get the Drop off Issues
     */
    private void dropOff() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.dropOff(sessionManager.getToken(), sessionManager.getTripId(), getId, isthumbs, reason).enqueue(new RequestCallback(this));
    }


    /**
     * Success Response From API
     *
     * @param jsonResp Json Response from Api
     * @param data     Request data (optional)
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            sessionManager.setTripStatus(4);
            onBackPressed();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }
}
