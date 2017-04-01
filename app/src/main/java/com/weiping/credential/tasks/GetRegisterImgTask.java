package com.weiping.credential.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.List;

import platform.tyk.weping.com.weipingplatform.R;

public class GetRegisterImgTask extends AsyncTask<String, Void, Bitmap> {
    private Activity mActivity;
    private HttpGet httpGet;
    private String sessionid;
    public GetRegisterImgTask(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap image = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpGet = new HttpGet(urlDisplay);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            //InputStream in = new java.net.URL(urlDisplay).openStream();
            image = BitmapFactory.decodeStream(in);
            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            for (Cookie cookie: cookies){
                if (cookie.getName().equalsIgnoreCase("sessionid")){
                    sessionid = cookie.getValue();
                    break;
                }
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap resultImage){
        ImageView bmImage = (ImageView)mActivity.findViewById(R.id.img_register_code);
        bmImage.setImageBitmap(resultImage);
        bmImage.setTag(sessionid);
    }
}
