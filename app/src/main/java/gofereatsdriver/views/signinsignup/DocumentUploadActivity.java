package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage gofereatsdriver.views.signinsignup
 * @category DocumentUploadActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.BuildConfig;
import gofereatsdriver.R;
import gofereatsdriver.backgroundtask.FilePath;
import gofereatsdriver.backgroundtask.ImageCompressAsyncTask;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ImageListener;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import okhttp3.RequestBody;


/*************************************************************
 Document Upload Activity
 Its used to get upload carriage document details
 *************************************************************** */
public class DocumentUploadActivity extends AppCompatActivity implements ServiceListener, ImageListener {

    private static final int RESULT_LOAD_FILE = 8;
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
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    RunTimePermission runTimePermission;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/

    public @InjectView(R.id.tvTapToAdd)
    TextView tvTapToAdd;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvDocTitle)
    TextView tvDocTitle;
    public @InjectView(R.id.ivImage)
    ImageView ivImage;
    public @InjectView(R.id.rltTapToAdd)
    RelativeLayout rltTapToAdd;
    public @InjectView(R.id.tvPdfName)
    TextView tvPdfName;
    public int docType = 0;
    private File imageFile = null;
    private String imagePath = "";
    private String selectedFilePath;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Add Docs
     */
    @OnClick(R.id.rltTapToAdd)
    public void uploadImage() {
        checkAllPermission(Constants.PERMISSIONS_PHOTO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        tvTitle.setText(getResources().getString(R.string.documents));
        vBottom.setVisibility(View.VISIBLE);

        getIntents();
    }

    /**
     * Get Intent data from previous activity for to set document title, image.
     **/
    public void getIntents() {
        /**
         * @param type  1 for licence back,
         *              2 for licence front,
         *              3 for insurance
         *              4 for registration
         *              5 for carriage
         *
         * @param title Document title
         */
        tvDocTitle.setText(getIntent().getStringExtra("title"));
        docType = getIntent().getIntExtra("type", 0);
        getImage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }


    /**
     * Document upload functions started
     */

    public void pickProfileImg() {
        View view = getLayoutInflater().inflate(R.layout.camera_dialog_layout, null);
        LinearLayout lltCamera = view.findViewById(R.id.llt_camera);
        LinearLayout lltLibrary = view.findViewById(R.id.llt_library);
        LinearLayout llt_file = view.findViewById(R.id.llt_file);

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
                Uri imageUri = FileProvider.getUriForFile(DocumentUploadActivity.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);

                try {
                    List<ResolveInfo> resolvedIntentActivities = DocumentUploadActivity.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
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
                commonMethods.refreshGallery(DocumentUploadActivity.this, imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(DocumentUploadActivity.this);

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
            }
        });

        llt_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                /*String[] docsTypes =
                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/pdf"};*/
                String[] docsTypes = {
                        /*"application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",*/
                        "application/pdf"};

                Intent intent = new Intent();
                //sets the select file to all types of files
                //intent.setType("application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(docsTypes.length == 1 ? docsTypes[0] : "*/*");
                    if (docsTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, docsTypes);
                    }
                } else {
                    String docsStr = "";
                    for (String mimeType : docsTypes) {
                        docsStr += mimeType + "|";
                    }
                    intent.setType(docsStr.substring(0, docsStr.length() - 1));
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(intent, RESULT_LOAD_FILE);
            }
        });
    }

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
                            Bitmap mbitmap = BitmapFactory.decodeFile(imagePath);
                            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                            Canvas canvas = new Canvas(imageRounded);
                            Paint mpaint = new Paint();
                            mpaint.setAntiAlias(true);
                            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 25, 25, mpaint);// Round Image Corner 100 100 100 100
                            commonMethods.showProgressDialog(this, customDialog);
                            new ImageCompressAsyncTask(docType, DocumentUploadActivity.this, imagePath, this, 0).execute();
                        }
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case RESULT_LOAD_FILE:
                    if (data == null) {
                        //no data present
                        return;
                    }
                    Uri selectedFileUri = data.getData();
                    try {
                        selectedFilePath = FilePath.getRealPath(this, selectedFileUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //selectedFilePath = FileUtils.getRealPath(this, selectedFileUri);
                    if (selectedFilePath != null) {
                        commonMethods.showProgressDialog(this, customDialog);
                        new ImageCompressAsyncTask(docType, DocumentUploadActivity.this, selectedFilePath, this, 1).execute();
                    } else {
                        Toast.makeText(this, "Please make sure that selected file is in mobile storage", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void saveImage(String imageUrl) {
        if (docType == 1) sessionManager.setDoc1(imageUrl);
        else if (docType == 2) sessionManager.setDoc2(imageUrl);
        else if (docType == 3) sessionManager.setDoc3(imageUrl);
        else if (docType == 4) sessionManager.setDoc4(imageUrl);
        else if (docType == 5) sessionManager.setDoc5(imageUrl);
    }

    public void getImage() {
        if (docType == 1) loadImage(sessionManager.getDoc1());
        else if (docType == 2) loadImage(sessionManager.getDoc2());
        else if (docType == 3) loadImage(sessionManager.getDoc3());
        else if (docType == 4) loadImage(sessionManager.getDoc4());
        else if (docType == 5) loadImage(sessionManager.getDoc5());
    }

    public void loadImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            String docModel = imageUrl.substring(imageUrl.length() - 4);
            tvTapToAdd.setText(R.string.taptochange);
            if (docModel.equalsIgnoreCase(".pdf")) {
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.pdf));
                imageUrl = imageUrl.replaceAll(getResources().getString(R.string.pdfDoc_url), "");
                tvPdfName.setText(imageUrl);
                ellipsizeText(tvPdfName, docModel);
                tvPdfName.setVisibility(View.VISIBLE);
            } else {
                Glide.with(this).load(imageUrl)
                        //.bitmapTransform(new RoundedCornersTransformation( this,5, 1))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
                tvPdfName.setVisibility(View.GONE);
            }

        } else {
            tvTapToAdd.setText(R.string.taptoadd);
            if (docType == 1 || docType == 2) {
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.driver_doc));
            } else {
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.v_doc));
            }
        }
    }

    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public void ellipsizeText(TextView tv, String format) {
        int maxLength = 20;
        if (tv.getText().length() > maxLength) {
            String currentText = tv.getText().toString();
            String ellipsizedText = currentText.substring(0, maxLength - 3) + "..." + " " + format;
            tv.setText(ellipsizedText);
        }
    }

    private void startCropImage() {
        if (imageFile == null) return;
        //int[] minimumSquareDimen = ImageMinimumSizeCalculator.getMinSquarDimension(Uri.fromFile(imageFile), this);
        CropImage.activity(Uri.fromFile(imageFile))
                //.setDefaultlyCropEnabled(true)
                .setAspectRatio(10, 10).setOutputCompressQuality(100)
                //.setMinCropResultSize(minimumSquareDimen[0], minimumSquareDimen[1])
                .start(this);
    }

    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.uploadDocumentImage(requestBody, sessionManager.getToken()).enqueue(new RequestCallback(this));
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
            onSuccessUploadImage(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    private void onSuccessUploadImage(JsonResponse jsonResponse) {

        String imageurl = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "document_url", String.class);
        sessionManager.setDriverStatus(3);
        saveImage(imageurl);
        loadImage(imageurl);
    }

    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        Log.v("blockedPermission", "blockedPermission" + blockedPermission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            pickProfileImg();
            //checkGpsEnable();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        Log.e("permission", "permission" + permission);
        Log.e("permission", "grantResults" + grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            pickProfileImg();
        }
    }

    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
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

    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }
}
