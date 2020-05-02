package gofereatsdriver.utils;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage utils
 * @category ImageUtils
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;

/*****************************************************************
 ImageUtils
 ****************************************************************/
public class ImageUtils {

    public static int sCorner = 5;
    public static int sMargin = 1;

    public @Inject
    SessionManager sessionManager;

    public ImageUtils() {
        AppController.getAppComponent().inject(this);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void loadGalleryImage(Context context, ImageView imageView, String url, int size) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url).override(size, size).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGalleryImage(Context context, ImageView imageView, String url, boolean isHeightPixel, int size) {
        try {
            int width = context.getResources().getDisplayMetrics().widthPixels;
            if (!isHeightPixel) {
                size = width - (int) (12 * context.getResources().getDisplayMetrics().density);
            }
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url).override(width, size)
                        //.error(R.color.white)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }/* else {
                //imageView.setImageResource(R.color.white);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(Context context, ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                /*Glide.with(context)
                        .load(url)
                        //.placeholder(R.color.text_light_gray)
                        //.error(R.color.text_light_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);*/
                Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            } /*else {
                //imageView.setImageResource(R.color.text_light_gray);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImageCurve(Context context, ImageView imageView, String url, int image_id) {
        try {
            if (!TextUtils.isEmpty(url)) {
                /*Glide.with(context)
                        .load(url)
                        //.placeholder(R.color.text_light_gray)
                        //.error(R.color.text_light_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);*/
                Glide.with(context).load(url).bitmapTransform(new RoundedCornersTransformation(context, sCorner, sMargin)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }/* else {
                // imageView.setImageResource(R.color.text_light_gray);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSliderImage(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context).load(url)
                    //.centerCrop()
                    // .error(R.color.black)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } /*else {
            // imageView.setImageResource(R.color.black);
        }*/
    }

    public void loadCircleImage(Context context, ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url)
                        //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        //.placeholder(R.color.gray_color)
                        //.error(R.color.gray_color)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            } /*else {
                // imageView.setImageResource(R.color.gray_color);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
