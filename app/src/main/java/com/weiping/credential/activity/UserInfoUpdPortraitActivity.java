package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.credential.Common.Constants;
import com.weiping.credential.tasks.UpdUserInfoImgTask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import platform.tyk.weping.com.weipingplatform.R;

public class UserInfoUpdPortraitActivity extends Activity {
    private static final int PHOTO_LIBRARY = 1;
    private static final int CAMERA_CAPTURE = 2;
    private static final int PIC_CROP = 3;
    private Bitmap thePic;
    private UpdUserInfoImgTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_user_info_update));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_user_info_upd_portrait);
        ImageView imageView = (ImageView)findViewById(R.id.img_portrait);
        imageView.setImageResource(R.mipmap.ic_portrait);
    }

    public void onClickUpdInfoField(View view){
        if (thePic != null) {
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArray = baos.toByteArray();
            //    task = new UpdUserInfoImgTask(this, byteArray);
            //    task.execute(Constants.URL_UPD_USER_INFO_IMG);

                String saveDir = Environment.getExternalStorageDirectory() + "/temple";
                File dir = new File(saveDir);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(saveDir, "temp.jpg");
                file.delete();
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(byteArray);

                task = new UpdUserInfoImgTask(this, file);
                task.execute(Constants.URL_UPD_USER_INFO_IMG);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onClickPortrait(View view){
        final CharSequence[] items = { "从相册中选取", "拍照" };
        AlertDialog dlg = new AlertDialog.Builder(UserInfoUpdPortraitActivity.this)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == PHOTO_LIBRARY-1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, PHOTO_LIBRARY);
                            //Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                            //getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            //getImage.setType("image/*");
                            //startActivityForResult(getImage, PHOTO_LIBRARY);
                        } else if (item == CAMERA_CAPTURE-1){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                            startActivityForResult(intent, CAMERA_CAPTURE);

                            /*
                            try {
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    Intent captureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                                } else {
                                    Toast.makeText(UserInfoUpdPortraitActivity.this, "SD卡不可用!", Toast.LENGTH_LONG).show();
                                }
                                //Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            }catch(ActivityNotFoundException anfe){
                                String errorMessage = "Whoops - your device doesn't support capturing images!";
                                Toast toast = Toast.makeText(UserInfoUpdPortraitActivity.this, errorMessage, Toast.LENGTH_SHORT);
                                toast.show();
                            }*/
                        }
                    }
                }).create();
        dlg.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_LIBRARY && resultCode == Activity.RESULT_OK) {
            try {
                Uri originalUri = data.getData();
                if (originalUri != null) {
                    performCrop(originalUri);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                Uri picUri = data.getData();
                if (picUri != null) {
                    performCrop(picUri);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == PIC_CROP){
            try {
                Bundle extras = data.getExtras();
                if(extras != null){
                    thePic = extras.getParcelable("data");
                    ImageView picView = (ImageView) findViewById(R.id.img_portrait);
                    picView.setImageBitmap(thePic);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void performCrop(Uri picUri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 100);
            cropIntent.putExtra("outputY", 100);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);

        } catch(ActivityNotFoundException anfe){
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
