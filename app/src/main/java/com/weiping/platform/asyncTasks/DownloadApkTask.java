package com.weiping.platform.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.xml.sax.InputSource;

public class DownloadApkTask extends AsyncTask<String, Void, File> {

    private Activity mActivity;
    protected ProgressDialog progressDialog;
    private static String TAG = "DownloadApkTask";

    public DownloadApkTask(Activity a){
        mActivity = a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载更新");
        progressDialog.show();
    }

    @Override
    protected File doInBackground(String... params) {
        try{
            File file = new File(Environment.getExternalStorageDirectory(), "WeiWei.apk");
            if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
                FTPClient ftpClient = new FTPClient();
                ftpClient.connect(params[0], 21);
                ftpClient.setConnectTimeout(5000);
                ftpClient.login("appUser", "appUser");
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.changeWorkingDirectory("/app");
                long fileSize = getFileSize(ftpClient, "app-release.apk");
                progressDialog.setMax((int)fileSize);
                if(fileSize != 0){
                    InputStream is = ftpClient.retrieveFileStream("app-release.apk");
                    int reply = ftpClient.getReplyCode();
                    if(is == null || (!FTPReply.isPositivePreliminary(reply) && !FTPReply.isPositiveCompletion(reply))){
                        throw new Exception(ftpClient.getReplyString());
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    int total = 0;
                    while((len = bis.read(buffer)) > 0){
                        fos.write(buffer, 0, len);
                        total += len;
                        progressDialog.setProgress(total);
                    }
                    fos.close();
                    bis.close();
                    is.close();
                }
                if(!ftpClient.completePendingCommand()){
                    throw new Exception("Pending command failed: " + ftpClient.getReplyString());
                }
                /*
                URL url = new URL(params[0]);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(5000);
                progressDialog.setMax(conn.getContentLength());
                InputStream is = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), "WeiWei.apk");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while((len = bis.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                    total += len;
                    progressDialog.setProgress(total);
                }
                fos.close();
                bis.close();
                is.close();
                */
                file.setReadable(true, false);
                return file;
            }
            return null;
        }catch (Exception e) {
            Log.e(TAG, "doInBackground");
            e.printStackTrace();
            return null;
        }finally {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onPostExecute(File apkFile) {
        if (apkFile != null){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            mActivity.startActivity(intent);
        }
    }

    private long getFileSize(FTPClient ftp, String filePath) throws Exception{
        long fileSize = 0;
        FTPFile[] files = ftp.listFiles(filePath);
        if(files.length == 1 && files[0].isFile()){
            fileSize = files[0].getSize();
        }
        Log.i("tag", "File size = " + fileSize);
        return fileSize;
    }
}
