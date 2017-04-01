package com.weiping.InteProperty.trademark.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.weiping.InteProperty.trademark.core.Constants;

import java.io.InputStream;

import platform.tyk.weping.com.weipingplatform.R;

public class DownloadTrademarkImageTask extends AsyncTask<String, Void, Bitmap>{

    private Activity mActivity;
    protected ProgressDialog progressDialog;
    public String registerNumber;
    public String categoryNumber;

    ProgressDialog.OnCancelListener progressDialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
            cancel(true);
        }
    };

    public DownloadTrademarkImageTask(Activity activity, String regNum, String cateNum){
        mActivity = activity;
        registerNumber = regNum;
        categoryNumber = cateNum;
    }

    public void doTask(){
        execute(Constants.URL_TRADEMARK_IMAGE + "regNum=" + registerNumber + "&intcls=" + categoryNumber);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, null, mActivity.getResources().getString(R.string.dialog_loading));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(progressDialogCancelListener);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            image = BitmapFactory.decodeStream(in);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap resultImage){
        ImageView bmImage = (ImageView)mActivity.findViewById(R.id.image_trademark_detail);
        bmImage.setImageBitmap(resultImage);
        progressDialog.dismiss();
    }
}
