package gofereatsdriver.views.main.pickuporderdetails;
/**
 * @package com.gofereats
 * @subpackage views.main.pickuporderdetails
 * @category PickupOrderActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

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
import gofereatsdriver.datamodels.accept.AcceptOrderItemModel;
import gofereatsdriver.datamodels.dropoffrating.DropoffModel;
import gofereatsdriver.datamodels.dropoffrating.DropoffissuesList;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.Enums;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.cancel.CancelActivity;

/*************************************************************
 PickupOrderActivity
 Its used to get the Order Details and Verify it and Rate the restaurant
 ****************************************************************/
public class PickupOrderActivity extends AppCompatActivity implements ServiceListener {

    public static ArrayList<DropoffissuesList> issuesListArrayList = new ArrayList<>();
    public int selectedissue;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    Gson gson;
    public @InjectView(R.id.ivChecktick)
    ImageView ivChecktick;
    public @InjectView(R.id.ivClose)
    ImageView ivClose;
    public @InjectView(R.id.rltFeedback)
    RelativeLayout rltFeedback;
    public @InjectView(R.id.rvIssues)
    RecyclerView rvIssues;
    public @InjectView(R.id.ivThumbs_up)
    CircleImageView ivThumbsup;
    public @InjectView(R.id.ivThumbs_down)
    CircleImageView ivThumbsdown;
    public @InjectView(R.id.rltOrderNotready)
    RelativeLayout rltOrderNotready;
    public @InjectView(R.id.rltpickupRating)
    RelativeLayout rltpickupRating;
    public @InjectView(R.id.btnNext)
    CustomButton btnNext;
    public @InjectView(R.id.tvOrderId)
    TextView tvOrderId;
    public @InjectView(R.id.tvRestaurantname)
    TextView tvRestaurantname;
    public @InjectView(R.id.tvEaterName)
    TextView tvEaterName;
    public @InjectView(R.id.lltOrderList)
    LinearLayout lltOrderList;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public FeedBackAdapter feedBackAdapter;
    public boolean isThumbsdown;
    public int isthumbs;
    public String reason = "";
    public DropoffModel dropoffModel;
    public int orderId;
    public String restaurantname, eatername;
    public boolean isClicked = true;
    public ArrayList<AcceptOrderItemModel> itemModels = new ArrayList<>();
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Check the order
     */
    @OnClick(R.id.ivChecktick)
    public void checktick() {
       // ivChecktick.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), android.graphics.PorterDuff.Mode.SRC_IN);
        ivChecktick.setImageDrawable(getResources().getDrawable(R.drawable.selected_check_mark));
        //ivChecktick.setColorFilter(null);
        //ivClose.setColorFilter(getResources().getColor(R.color.line_gray));
        ivClose.setImageDrawable(getResources().getDrawable(R.drawable.unselect));
        rltpickupRating.setVisibility(View.VISIBLE);
        rltOrderNotready.setVisibility(View.GONE);
    }

    /**
     * Check Order is not Fine
     */
    @OnClick(R.id.ivClose)
    public void close() {
        ivChecktick.setImageDrawable(getResources().getDrawable(R.drawable.untick));
        //ivChecktick.setColorFilter(getResources().getColor(R.color.line_gray));
        //ivClose.setColorFilter(null);
        ivClose.setImageDrawable(getResources().getDrawable(R.drawable.close_error));
        rltpickupRating.setVisibility(View.GONE);
        // rltOrderNotready.setVisibility(View.VISIBLE);
        Intent cancel = new Intent(getApplicationContext(), CancelActivity.class);
        cancel.putExtra("orderId", orderId);
        startActivity(cancel);
    }

    /**
     * Thumbs up for Pickup
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
     * ThumbsDown For PickUp
     */
    @OnClick(R.id.ivThumbs_down)
    public void thumbsDown() {
        if (isClicked) {
            getIssues();
        }
        ivThumbsdown.setImageResource(R.drawable.thumbs_down_selected);
        ivThumbsup.setImageResource(R.drawable.thumbs_up_normal);
        rltFeedback.setVisibility(View.VISIBLE);
        isThumbsdown = true;
        isthumbs = 0;
        /*btnNext.setEnabled(false);
        btnNext.setBackground(getResources().getDrawable(R.drawable.background_dim_blue));*/
        btnNext.setEnabled(true);
        btnNext.setBackground(getResources().getDrawable(R.drawable.background_blue));
    }

    /**
     * Confirm Pickup
     */
    @OnClick(R.id.btnNext)
    public void confirm() {
        confirmOrder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_order);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setAutoMeasureEnabled(false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvIssues.setLayoutManager(gridLayoutManager);

        getintent();
        loaddata();
    }

    /**
     * Get PickUp Issues
     */
    private void getIssues() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.pickupData(sessionManager.getToken(), orderId).enqueue(new RequestCallback(Enums.REQ_PICKUP_ISSUES, this));
    }

    /**
     * Confirms the order
     * changes the Trip status to 1
     */
    private void confirmOrder() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.confirmOrder(sessionManager.getToken(), orderId, isthumbs, reason).enqueue(new RequestCallback(Enums.REQ_CONFIRM_ORDER, this));
    }

    /**
     * On success Result For Api
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_PICKUP_ISSUES:
                if (jsonResp.isSuccess()) {
                    onSuccessDropOffList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case Enums.REQ_CONFIRM_ORDER:
                if (jsonResp.isSuccess()) {
                    // finish();
                    sessionManager.setTripStatus(1);
                    onBackPressed();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * get Response from API and set issues to the adapter
     */
    private void onSuccessDropOffList(JsonResponse jsonResp) {
        dropoffModel = gson.fromJson(jsonResp.getStrResponse(), DropoffModel.class);
        if (dropoffModel.getDriverRestaurantIssues() != null) {
            issuesListArrayList.clear();
            issuesListArrayList.addAll(dropoffModel.getDriverRestaurantIssues());
            feedBackAdapter = new FeedBackAdapter(getApplicationContext(), issuesListArrayList);
            feedBackAdapter.setOnItemClickListener(new FeedBackAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, int positionz) {
                    reason = "";
                    for (int i = 0; i < issuesListArrayList.size(); i++) {
                        if (!issuesListArrayList.get(i).isSelected()) {
                            issuesListArrayList.get(i).getId();

                            if (TextUtils.isEmpty(reason)) {
                                reason = reason + issuesListArrayList.get(i).getId();
                            } else {
                                reason = reason + "," + issuesListArrayList.get(i).getId();
                                selectedissue = issuesListArrayList.get(i).getId();
                            }

                        }
                    }
                }
            });
            rvIssues.setAdapter(feedBackAdapter);
            feedBackAdapter.notifyDataSetChanged();
            isClicked = false;
        }
    }

    /**
     * Enable Or Set Views
     */
    public void loaddata() {
        //ivClose.setColorFilter(getResources().getColor(R.color.line_gray));
        //ivChecktick.setColorFilter(getResources().getColor(R.color.line_gray));
        if ("0".equalsIgnoreCase(getString(R.string.layout_direction)))
            tvOrderId.setText(getString(R.string.order_id) + " #" + orderId);
        else
            tvOrderId.setText(getString(R.string.order_id) + " " + orderId+" #");
        tvTitle.setText(getString(R.string.pick_up_order));
        tvRestaurantname.setText(restaurantname);
        tvEaterName.setText(eatername);
        btnNext.setEnabled(false);
        lltOrderList.removeAllViews();
        if (itemModels != null) {
            for (int i = 0; i < itemModels.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.order_item, null);
                CustomTextView tvQuantity = view.findViewById(R.id.tvQuantity);
                CustomTextView tvOrderItem = view.findViewById(R.id.tvOrderItem);

                tvQuantity.setText(String.valueOf(itemModels.get(i).getQuantity())/*+"X"*/);
                tvOrderItem.setText(itemModels.get(i).getName());
                lltOrderList.setTag(i);
                lltOrderList.addView(view);
            }
        }

    }

    /**
     * Get Datas From Intent
     */
    public void getintent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        orderId = intent.getIntExtra("orderId", 0);
        eatername = intent.getStringExtra("eatername");
        restaurantname = intent.getStringExtra("restaurant");
        itemModels.addAll((ArrayList<AcceptOrderItemModel>) bundle.getSerializable("menu_item"));
    }

    /**
     * On resume
     */
    @Override
    public void onResume() {
        super.onResume();
        ivChecktick.setImageDrawable(getResources().getDrawable(R.drawable.untick));
        ivClose.setImageDrawable(getResources().getDrawable(R.drawable.unselect));
        ivThumbsup.setImageResource(R.drawable.thumbs_up_normal);
        ivThumbsdown.setImageResource(R.drawable.thumbs_down_normal);
        btnNext.setEnabled(false);
        btnNext.setBackground(getResources().getDrawable(R.drawable.background_dim_blue));
    }
}
