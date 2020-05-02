package gofereatsdriver.views.main.profile;
/**
 * @package com.gofereats
 * @subpackage views.main.profile
 * @category VehicleInformation
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.utils.CommonMethods;

/************************************************************
 VehicleInformation
 Its used to get the Vehicle InFormation
 ****************************************************************/

public class VehicleInformation extends AppCompatActivity {


    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @Inject
    CommonMethods commonMethods;
    public @InjectView(R.id.tvCarname)
    TextView tvCarname;
    public @InjectView(R.id.tvCarnumber)
    TextView tvCarnumber;
    public @InjectView(R.id.tvCartype)
    TextView tvCartype;
    public @InjectView(R.id.ivCarimage)
    CircleImageView ivCarimage;
    public @InjectView(R.id.progressBarciv)
    ProgressBar progressBarciv;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicl_information);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        tvTitle.setText(getResources().getString(R.string.vehicleinformation));
        vBottom.setVisibility(View.VISIBLE);
        commonMethods.rotateArrow(ivBack,this);
        tvCarname.setText(getIntent().getStringExtra("vehiclename"));
        tvCarnumber.setText(getIntent().getStringExtra("vehiclenum"));
        tvCartype.setText(getIntent().getStringExtra("vehicletype"));
        String vehicleImage = getIntent().getStringExtra("vehicleimage");

        Glide.with(getApplicationContext()).load(vehicleImage).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBarciv.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBarciv.setVisibility(View.GONE);
                return false;
            }
        }).into(ivCarimage);
    }
}
