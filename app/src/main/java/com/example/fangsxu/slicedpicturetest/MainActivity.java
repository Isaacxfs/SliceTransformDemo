package com.example.fangsxu.slicedpicturetest;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangsxu
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private LinearLayout llMain;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView ivPrint;
    private List<ImageView> imageViewList;
    private Button btnTrans;
    private Button btnPrint;
    private Button btnLoad;
    private Button btnOld;
    private int singleWid = 0;
    private int singleHei = 0;
    private int count = 1;
    private LocalMedia localMedia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsStyle.setTranslateStatusBar(this);
        UtilsStyle.setStatusBarMode(this, true);
        resetContentView();
        initViews();
        loadPic();
    }

    private void resetContentView(){
        setContentView(R.layout.activity_main);
    }

    public void loadPic() {
        for (int i=0;i<imageViewList.size();i++){
            imageViewList.get(i).clearAnimation();
        }

        InputStream is = null;
        try {
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            if (localMedia == null) {
                is = getResources().getAssets().open(Consts.strDefaultPic);
                BitmapFactory.decodeStream(is, null, tmpOptions);
            } else {
                BitmapFactory.decodeFile(localMedia.getPath(), tmpOptions);
            }
            tmpOptions.inJustDecodeBounds = true;
            int width = tmpOptions.outWidth;
            singleHei = tmpOptions.outHeight;
            singleWid = width / 4;
            BitmapRegionDecoder bitmapRegionDecoder = null;
            if (localMedia == null) {
                bitmapRegionDecoder = BitmapRegionDecoder.newInstance(is, false);
            } else {
                bitmapRegionDecoder = BitmapRegionDecoder.newInstance(localMedia.getPath(),false);
            }

            for (int i = 0; i < imageViewList.size(); i++) {
                loadSlicedPictureIntoImageView(i, bitmapRegionDecoder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadSlicedPictureIntoImageView(int index, BitmapRegionDecoder bitmapRegionDecoder) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(index * singleWid, 0, (index + 1) * singleWid, singleHei), options);
        imageViewList.get(index).setImageBitmap(bitmap);
    }


    public void resetImagViews(){
        iv1 = null;
        iv2 = null;
        iv3 = null;
        iv4 = null;

        iv1 = findViewById(R.id.iv_1);
        iv2 = findViewById(R.id.iv_2);
        iv3 = findViewById(R.id.iv_3);
        iv4 = findViewById(R.id.iv_4);

        imageViewList = null;
        imageViewList = new ArrayList<>();
        imageViewList.add(iv1);
        imageViewList.add(iv2);
        imageViewList.add(iv3);
        imageViewList.add(iv4);


    }

    public void resetPosition(){
        float curTranslationX0 = imageViewList.get(0).getTranslationX();
        float curTranslationX1 = imageViewList.get(1).getTranslationX();
        float curTranslationX2 = imageViewList.get(2).getTranslationX();
        float curTranslationX3 = imageViewList.get(3).getTranslationX();

        ObjectAnimator oa =null;
        ObjectAnimator oa1 =null;
        ObjectAnimator oa2=null;
        ObjectAnimator oa3=null;

        if (singleWid >= UtilsStyle.getScreenWidth(MainActivity.this)/4){
            singleWid = UtilsStyle.getScreenWidth(MainActivity.this)/4;
        }

        if (count % 2 == 0) {
            oa = ObjectAnimator.ofFloat(imageViewList.get(0), "translationX",curTranslationX0,singleWid*3);
            oa1 = ObjectAnimator.ofFloat(imageViewList.get(1), "translationX",curTranslationX1,singleWid*1);
            oa2 = ObjectAnimator.ofFloat(imageViewList.get(2), "translationX",curTranslationX2,-singleWid*1);
            oa3 = ObjectAnimator.ofFloat(imageViewList.get(3), "translationX",curTranslationX3,-singleWid*3);
        }

        oa.start();
        oa1.start();
        oa2.start();
        oa3.start();
    }

    public void initViews() {
        resetImagViews();
        llMain = findViewById(R.id.ll_main);
        ivPrint = findViewById(R.id.iv_print);
        btnTrans = findViewById(R.id.btn_trans);
        btnTrans.setOnClickListener(this);
        btnPrint = findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);
        btnLoad = findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(this);
        btnOld = findViewById(R.id.btn_old);
        btnOld.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_trans:
                doTrans();
                break;
            case R.id.btn_print:
                doPrint();
                break;
            case R.id.btn_load:
                load();
                break;
            case R.id.btn_old:
                doOld();
                break;
        }
    }


    private void doOld(){
        float degree = 180f;
        if (count % 2 == 0) {
            degree = 360f;
        }
        ObjectAnimator oa = ObjectAnimator.ofFloat(imageViewList.get(0), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageViewList.get(1), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageViewList.get(2), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(imageViewList.get(3), "rotationY",
                new float[]{90f, 180f, 270f, degree});

        oa.setDuration(300);
        oa1.setDuration(300);
        oa2.setDuration(300);
        oa3.setDuration(300);

        oa.start();
        oa1.start();
        oa2.start();
        oa3.start();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Drawable d1 = imageViewList.get(0).getDrawable();
                Drawable d2 = imageViewList.get(1).getDrawable();
                Drawable d3 = imageViewList.get(2).getDrawable();
                Drawable d4 = imageViewList.get(3).getDrawable();

                imageViewList.get(0).setImageDrawable(d4);
                imageViewList.get(1).setImageDrawable(d3);
                imageViewList.get(2).setImageDrawable(d2);
                imageViewList.get(3).setImageDrawable(d1);
            }
        });


        count++;
    }


    public void load() {
        PictureSelector.create(MainActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(3)
                .isGif(false)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .compress(true)
                .hideBottomControls(false)
                .previewEggs(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    public void doPrint() {
        llMain.setDrawingCacheEnabled(true);
        Bitmap screenShotAsBitmap = Bitmap.createBitmap(llMain.getDrawingCache());
        llMain.setDrawingCacheEnabled(false);
        ivPrint.setImageBitmap(screenShotAsBitmap);
    }


    public void doTrans() {
        float degree = Consts.defaultDegree180;
        if (count % 2 == 0) {
            degree = Consts.defaultDegree360;
        }
        ObjectAnimator oaPic1 = ObjectAnimator.ofFloat(imageViewList.get(0), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        ObjectAnimator oaPic2 = ObjectAnimator.ofFloat(imageViewList.get(1), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        ObjectAnimator oaPic3 = ObjectAnimator.ofFloat(imageViewList.get(2), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        ObjectAnimator oaPic4 = ObjectAnimator.ofFloat(imageViewList.get(3), "rotationY",
                new float[]{90f, 180f, 270f, degree});
        oaPic1.start();
        oaPic2.start();
        oaPic3.start();
        oaPic4.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                float curTranslationX0 = imageViewList.get(0).getTranslationX();
                float curTranslationX1 = imageViewList.get(1).getTranslationX();
                float curTranslationX2 = imageViewList.get(2).getTranslationX();
                float curTranslationX3 = imageViewList.get(3).getTranslationX();

                ObjectAnimator oa =null;
                ObjectAnimator oa1 =null;
                ObjectAnimator oa2=null;
                ObjectAnimator oa3=null;

                if (singleWid >= UtilsStyle.getScreenWidth(MainActivity.this)/4){
                     singleWid = UtilsStyle.getScreenWidth(MainActivity.this)/4;
                 }

                if (count % 2 == 0) {
                    oa = ObjectAnimator.ofFloat(imageViewList.get(0), "translationX",curTranslationX0,singleWid*3);
                    oa1 = ObjectAnimator.ofFloat(imageViewList.get(1), "translationX",curTranslationX1,singleWid*1);
                    oa2 = ObjectAnimator.ofFloat(imageViewList.get(2), "translationX",curTranslationX2,-singleWid*1);
                    oa3 = ObjectAnimator.ofFloat(imageViewList.get(3), "translationX",curTranslationX3,-singleWid*3);
                }else {
                    oa = ObjectAnimator.ofFloat(imageViewList.get(0), "translationX",curTranslationX0,-singleWid*0);
                    oa1 = ObjectAnimator.ofFloat(imageViewList.get(1), "translationX",curTranslationX1,-singleWid*0);
                    oa2 = ObjectAnimator.ofFloat(imageViewList.get(2), "translationX",curTranslationX2,singleWid*0);
                    oa3 = ObjectAnimator.ofFloat(imageViewList.get(3), "translationX",curTranslationX3,singleWid*0);
                }

                oa.start();
                oa1.start();
                oa2.start();
                oa3.start();
            }
        },500);
        count++;
    }



    private void clearSlicedPictureFromImageView(){
        for (int i = 0; i<imageViewList.size();i++){
            imageViewList.get(i).setImageBitmap(null);
            imageViewList.get(i).clearAnimation();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    localMedia = selectList.get(0);
                    resetContentView();
                    initViews();

                    count = 1;
                    loadPic();
                    break;
            }
        }
    }
}
