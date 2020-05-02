package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver.signinsignup
 * @subpackage gofereatsdriver.views.signinsignup
 * @category RegisterCarDetailsActivity
 * @author Trioangle Product Team
 * @version 1.0
 */


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.signinup.VehicleTypeModel;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;


/*************************************************************
 RegisterCarDetailsActivity
 Its used to get the register car details function
 *************************************************************** */
public class RegisterCarDetailsActivity extends AppCompatActivity implements ServiceListener {

    public AlertDialog dialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public ArrayList<VehicleTypeModel> vehicleTypeModels = new ArrayList<>();

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/

    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.spnVehicleType)
    Spinner spnVehicleType;
    public @InjectView(R.id.edtVehicleName)
    EditText edtVehicleName;
    public @InjectView(R.id.edtVehicleNumber)
    EditText edtVehicleNumber;
    public @InjectView(R.id.tilVehicleName)
    TextInputLayout tilVehicleName;
    public @InjectView(R.id.tilVehicleNumber)
    TextInputLayout tilVehicleNumber;
    public @InjectView(R.id.btnNext)
    Button btnNext;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Register Car Details
     */
    @OnClick(R.id.btnNext)
    public void next() {
        if (!validateText(tilVehicleName, edtVehicleName)) {
            return;
        }
        if (!validateText(tilVehicleNumber, edtVehicleNumber)) {
            return;
        }

        if (spnVehicleType.getSelectedItemPosition() == 0) {
            return;
        }

        int vehicleId = vehicleTypeModels.get(spnVehicleType.getSelectedItemPosition()).getVehicleId();
        sessionManager.setVehicleId(vehicleId);

        String vehicleName = edtVehicleName.getText().toString();
        String vehicleNumber = edtVehicleNumber.getText().toString();
        commonMethods.showProgressDialog(this, customDialog);
        apiService.addVehicle(sessionManager.getType(), sessionManager.getVehicleId(), vehicleName, vehicleNumber, sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_car_details);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        getIntents();

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.vehicle));
        vBottom.setVisibility(View.VISIBLE);


        edtVehicleName.addTextChangedListener(new textWatcher(edtVehicleName));
        edtVehicleNumber.addTextChangedListener(new textWatcher(edtVehicleNumber));
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            edtVehicleName.setGravity(Gravity.END);
            edtVehicleName.setTextDirection(View.TEXT_DIRECTION_LTR);
            edtVehicleNumber.setGravity(Gravity.END);
            edtVehicleNumber.setTextDirection(View.TEXT_DIRECTION_LTR);
        }


    }

    public void getIntents() {
        VehicleTypeModel vehicleTypeModel = new VehicleTypeModel();
        vehicleTypeModel.setVehicleId(0);
        vehicleTypeModel.setVehicleName(getResources().getString(R.string.select_vehicle));
        vehicleTypeModels.add(vehicleTypeModel);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        vehicleTypeModels.addAll((ArrayList<VehicleTypeModel>) bundle.getSerializable("vehicleType"));

        loadSpinner();
    }

    public void loadSpinner() {
        String[] vehicleName = new String[vehicleTypeModels.size()];

        for (int i = 0; i < vehicleTypeModels.size(); i++) {
            vehicleName[i] = vehicleTypeModels.get(i).getVehicleName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.vehicle_layout, vehicleName);

        spnVehicleType.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /**
         *  Cancel trip reasons in spinner
         */
        spnVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    private boolean validateText(TextInputLayout inputLayout, EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            if (editText.getId() == R.id.edtVehicleName)
                inputLayout.setError(getString(R.string.error_msg_vehiclename));
            else inputLayout.setError(getString(R.string.error_msg_vehiclenumber));
            requestFocus(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            sessionManager.setDriverStatus(3);
            Intent docHome = new Intent(getApplicationContext(), DocHomeActivity.class);
            docHome.putExtra("check", "register");
            startActivity(docHome);
            overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);

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

    /*
     *   Text watcher for validate vehicle name and number field
     */
    private class textWatcher implements TextWatcher {

        private View view;

        private textWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edtVehicleName:
                    validateText(tilVehicleName, edtVehicleName);
                    break;
                case R.id.edtVehicleNumber:
                    validateText(tilVehicleNumber, edtVehicleNumber);
                    break;
                default:
                    break;
            }
        }
    }
}
