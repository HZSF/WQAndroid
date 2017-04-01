package com.weiping.credential.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.weiping.credential.Common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GetUserInfoImgTask extends AsyncTask<String, Void, byte[]> {
    private Activity mActivity;
    private AbstractHttpClient mClient;
    private HttpPost httpPost;
    public GetUserInfoImgTask(Activity a){
        super();
        mActivity = a;
        mClient = new DefaultHttpClient();
    }

    @Override
    protected byte[] doInBackground(String... params) {
        try {
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(Constants.HEAD_AUTH_TOKEN, "");
            httpPost = new HttpPost(params[0]);
            httpPost.setHeader(Constants.HEAD_AUTH_TOKEN, AUTH_SESSION_TOKEN);
            HttpResponse response = mClient.execute(httpPost);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            response.getEntity().writeTo(baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] result) {
        if (result.length<=0){
            return;
        }
        try{
            byte[] byteResult = result;
            //Log.i("byte", String.valueOf(byteResult.length));
            //StringBuilder ab = new StringBuilder();
            //for (byte b:byteResult)
            //    ab.append(b);
            //Log.i("byteResult", ab.toString());
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteResult, 0, byteResult.length);
            Drawable d = new BitmapDrawable(mActivity.getResources(), bitmap);
            mActivity.getActionBar().setIcon(d);

            File cacheDir = mActivity.getBaseContext().getCacheDir();
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(Constants.SHARE_PREFERENCE_USERNAME, "");
            File f = new File(cacheDir, "portrait_"+username);
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
