package gofereatsdriver.views.main.profile;
/**
 * @package com.gofereats
 * @subpackage views.main.profile
 * @category DriverProfile
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.obs.CustomEditText;
import com.obs.CustomTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsdriver.BuildConfig;
import gofereatsdriver.R;
import gofereatsdriver.backgroundtask.ImageCompressAsyncTask;
import gofereatsdriver.backgroundtask.ImageMinimumSizeCalculator;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.driverprofile.DriverModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ImageListener;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;
import gofereatsdriver.views.signinsignup.MobileActivity;
import okhttp3.RequestBody;

import static gofereatsdriver.utils.Enums.REQ_UPDATE_PROFILE;
import static gofereatsdriver.utils.Enums.REQ_UPLOAD_PROFILE_IMG;

/************************************************************
 DriverProfile
 Its used to get Driver's Profile and details
 ****************************************************************/
public class DriverProfile extends AppCompatActivity implements ImageListener, ServiceListener {

    public String userChoosenTask;
    public DriverModel driverModel;
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
    public @Inject
    RunTimePermission runTimePermission;
    public @InjectView(R.id.input_first)
    CustomEditText input_first;
    public @InjectView(R.id.input_last)
    CustomEditText input_last;
    public @InjectView(R.id.emaitext)
    EditText emaitext;
    public @InjectView(R.id.mobile_code)
    CountryCodePicker countryCodePicker;
    public @InjectView(R.id.edtStreetAddress)
    EditText edtStreetAddress;
    public @InjectView(R.id.edtAreaAddress)
    EditText edtAreaAddress;
    public @InjectView(R.id.edtCity)
    EditText edtcity;
    public @InjectView(R.id.edtState)
    EditText edtState;
    public @InjectView(R.id.edtPostal)
    EditText edtPostal;
    public @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    public @InjectView(R.id.fllayout)
    RelativeLayout fllayout;
    public @InjectView(R.id.edtmobile)
    EditText edtmobile;
    public @InjectView(R.id.ivProfileimage)
    CircleImageView ivProfileimage;
    public @InjectView(R.id.tvCheckTick)
    CustomTextView tvCheckTick;
    public @InjectView(R.id.ivBackarrow)
    ImageView ivBackarrow;
    public String imageurl;
    public int docType = 6;          //By Default Set 6 for Profile Image
    public String[] name;
    public String fullName;
    public String firstName;
    public String lastName;
    private AlertDialog dialog;
    private File imageFile = null;
    private Uri imageUri;
    private String imagePath = "";

    /**
     * Mobile Number Change
     */
    @OnClick(R.id.fllayout)
    public void mobileAct() {
        Intent editPhone = new Intent(this, MobileActivity.class);
        editPhone.putExtra("isChange", true);
        startActivity(editPhone);
    }

    /**
     * Edit mobile Number
     */
    @OnClick(R.id.edtmobile)
    public void edtmobile() {
        mobileAct();
    }

    /**
     * Change Profile Image
     */
    @OnClick(R.id.ivProfileimage)
    public void changeProfile() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        checkAllPermission(permissions);
    }

    @OnClick(R.id.ivBackarrow)
    public void backpressed() {
        onBackPressed();
    }

    /**
     * Save all Changes
     */
    @OnClick(R.id.tvCheckTick)
    public void updateProfile() {
        updateDriverProfile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBackarrow,this);
        getProfileDetails();
    }

    /**
     * Getting Driver Details to Update
     *
     * @return hashmap Datas
     */
    private HashMap<String, String> getDetails() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("first_name", input_first.getText().toString().trim());
        hashMap.put("last_name", input_last.getText().toString().trim());
        hashMap.put("email", emaitext.getText().toString().trim());
        hashMap.put("mobile_number", edtmobile.getText().toString().trim());
        hashMap.put("country_code", countryCodePicker.getSelectedCountryCode().trim());
        hashMap.put("street", edtStreetAddress.getText().toString().trim());
        hashMap.put("first_address", edtAreaAddress.getText().toString().trim());
        hashMap.put("city", edtcity.getText().toString().trim());
        hashMap.put("state", edtState.getText().toString().trim());
        hashMap.put("postal_code", edtPostal.getText().toString().trim());

        return hashMap;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("type", "account");
        startActivity(intent);
    }

    /**
     * Update Driver Profile
     */
    private void updateDriverProfile() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.updateProfile(getDetails(), sessionManager.getToken()).enqueue(new RequestCallback(REQ_UPDATE_PROFILE, this));
    }

    /**
     * Getting and setting The Profile details
     */
    public void getProfileDetails() {
        fullName = getIntent().getStringExtra("name");

        /* Spliting the First and last name */

        name = fullName.split(" ", 2);
        if (name.length > 1) {
            firstName = name[0];
            lastName = name[1];
        } else {
            firstName = name[0];
            lastName = "";
        }

        input_first.setText(firstName);
        input_last.setText(lastName);
        emaitext.setText(getIntent().getStringExtra("email"));
        edtmobile.setText(getIntent().getStringExtra("mobile"));
        countryCodePicker.setCountryForPhoneCode(Integer.valueOf(getIntent().getStringExtra("countrycode")));
        Glide.with(this).load(getIntent().getStringExtra("profile")).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(ivProfileimage);

        edtStreetAddress.setText(getIntent().getStringExtra("street"));
        edtAreaAddress.setText(getIntent().getStringExtra("area"));
        edtState.setText(getIntent().getStringExtra("state"));
        edtcity.setText(getIntent().getStringExtra("city"));
        edtPostal.setText(getIntent().getStringExtra("postalcode"));
    }

    /**
     * API ResPonse
     *
     * @param jsonResp JsonResponse
     * @param data     Request Data(Optional)
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
            case REQ_UPLOAD_PROFILE_IMG:
                if (jsonResp.isSuccess()) {
                    onSuccessUploadImage(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;

            case REQ_UPDATE_PROFILE:
                if (jsonResp.isSuccess()) {
                    onSuccessUpdateDriverProfile(jsonResp);
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
     * Success Message For Driver Update Profile
     */
    private void onSuccessUpdateDriverProfile(JsonResponse jsonResp) {
        driverModel = gson.fromJson(jsonResp.getStrResponse(), DriverModel.class);
        Toast.makeText(this, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show();
    }

    /**
     * Upload Image using Post Method
     */
    private void onSuccessUploadImage(JsonResponse jsonResponse) {
        Toast.makeText(this, getString(R.string.image_upload_successfully), Toast.LENGTH_SHORT).show();
        imageurl = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.PROFILEIMAGE, String.class);
        loadImage(imageurl);
    }

    /**
     * Check Permissions
     *
     * @param permission
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            pickProfileImg();


        }
    }

    /**
     * Permission granted or denied
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);

        } else {
            pickProfileImg();
        }
    }

    /**
     * If Deny open and Enable the permission
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(getString(R.string.alert), message, getString(R.string.cast_tracks_chooser_dialog_ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Opens the APP Permission Settings Page
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    public void pickProfileImg() {
        View view = getLayoutInflater().inflate(R.layout.camera_dialog_layout, null);
        LinearLayout lltCamera = view.findViewById(R.id.llt_camera);
        LinearLayout lltLibrary = view.findViewById(R.id.llt_library);
        LinearLayout llt_file = view.findViewById(R.id.llt_file);
        llt_file.setVisibility(View.GONE);

        final Dialog bottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        if (bottomSheetDialog.getWindow() == null) return;
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.show();

        lltCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFile = commonMethods.cameraFilePath();
                imageUri = FileProvider.getUriForFile(DriverProfile.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);

                try {
                    List<ResolveInfo> resolvedIntentActivities = DriverProfile.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;
                        grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    cameraIntent.putExtra("return-data", true);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 1);
                commonMethods.refreshGallery(DriverProfile.this, imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(DriverProfile.this);

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
            }
        });
    }

    /**
     * OnActivity Result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startCropImage();
                    break;
                case Constants.REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        if (inputStream != null) inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        imagePath = result.getUri().getPath();
                        if (!TextUtils.isEmpty(imagePath)) {
                            commonMethods.showProgressDialog(this, customDialog);
                            new ImageCompressAsyncTask(docType, DriverProfile.this, imagePath, this, 0).execute();
                        }
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Input output Stream
     */
    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Crop Profile Image
     */
    private void startCropImage() {
        if (imageFile == null) return;
        int[] minimumSquareDimen = ImageMinimumSizeCalculator.getMinSquarDimension(Uri.fromFile(imageFile), this);
        CropImage.activity(Uri.fromFile(imageFile)).setAspectRatio(10, 10).setOutputCompressQuality(100).setMinCropResultSize(minimumSquareDimen[0], minimumSquareDimen[1]).start(this);
    }

    /**
     * Image To COmpress and Update Using Post Method
     */
    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            System.out.println("request object " + requestBody);

            commonMethods.showProgressDialog(this, customDialog);
            apiService.uploadImage(requestBody, sessionManager.getToken(), "driver").enqueue(new RequestCallback(REQ_UPLOAD_PROFILE_IMG, this));
        }
    }

    /**
     * Load Image
     */
    private void loadImage(String imageurl) {
        commonMethods.hideProgressDialog();

        Glide.with(this).load(imageurl).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivProfileimage);
    }
}