package com.example.abhishekaryan.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.views.CameraPreview;
import com.example.abhishekaryan.yora.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class NewMessageActivity extends BaseAuthenticateActivity implements View.OnClickListener, Camera.PictureCallback {

    private static final String TAG="NEW_MESSAGE_ACTIVITY";
    //this is for when device is rotated ,know which currently camera is looking at
    private static final String STATE_CAMERA_INDEX="STATE_CAMERA_INDEX";

    public static final String EXTRA_CONTACT="EXTRA_CONTACT";
    private static final int REQUEST_SEND_MESSAGE = 1;

    private Camera camera;
    private Camera.CameraInfo cameraInfo;
    private int currentCameraIndex;
    private CameraPreview cameraPreview;
    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_new_message);

        //code to always scrren on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(savedInstanceState!=null){
            currentCameraIndex=savedInstanceState.getInt(STATE_CAMERA_INDEX);
        }
        else {
            currentCameraIndex=0;
        }
        cameraPreview=new CameraPreview(this);

        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.activity_new_message_frame);
        frameLayout.addView(cameraPreview,0);
        findViewById(R.id.activity_new_mesage_switch_camera).setOnClickListener(this);
        findViewById(R.id.activity_new_mesage_take_picture).setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {

        int id=view.getId();
        if (id==R.id.activity_new_mesage_switch_camera){

            switchCamera();
        }
        else if (id==R.id.activity_new_mesage_take_picture){

            takePicture();
        }

    }

    private void  switchCamera(){
        currentCameraIndex=currentCameraIndex + 1 < Camera.getNumberOfCameras() ? currentCameraIndex +1 : 0;
        etablishCamera();

    }
    private void takePicture(){

        camera.takePicture(null,null,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        etablishCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(camera!=null){
            //if activty is paused then we want to realse the camera resource. Because it locks exclusively

            cameraPreview.setCamera(null,null);
            camera.release();
            camera=null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CAMERA_INDEX,currentCameraIndex);
    }

    private void etablishCamera(){

        if(camera!=null){
            cameraPreview.setCamera(null,null);
            camera.release();
            camera=null;
        }



        try{

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                //ask for authorisation
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
            else
                //start your camera


            camera=Camera.open(currentCameraIndex);

        }
        catch (Exception e){
            Log.e(TAG,"Could no open camera" + currentCameraIndex,e);
            Toast.makeText(this,"Error opening camera" +e,Toast.LENGTH_LONG).show();
            return;
        }
        cameraInfo=new Camera.CameraInfo();
       Camera.getCameraInfo(currentCameraIndex,cameraInfo);
        cameraPreview.setCamera(camera,cameraInfo);
        if(cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_BACK){

            Toast.makeText(this,"Using Back Camera",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Using front Camera",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap=processBitmap(data);

        ByteArrayOutputStream output=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,output);

        File outputfile=new File(getCacheDir(),"temp-image");
            //outputfile.delete();
        try{
            FileOutputStream fileoutput=new FileOutputStream(outputfile);
            fileoutput.write(output.toByteArray());
            fileoutput.close();
        }
        catch (Exception e){
            Log.e(TAG,"Could not save the image",e);
            Toast.makeText(this,"Could not save the image"+e,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this,SendMessageActivity.class);
        intent.putExtra(SendMessageActivity.EXTRA_IMAGE_PATH, Uri.fromFile(outputfile));

        UserDetails user=getIntent().getParcelableExtra(EXTRA_CONTACT);
        if(user!=null){
            intent.putExtra(SendMessageActivity.EXTRA_CONTACT,user);
        }
        startActivityForResult(intent,REQUEST_SEND_MESSAGE);
        bitmap.recycle();
    }

    private Bitmap processBitmap(byte[] data) {


       Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);

        if(bitmap.getWidth() > SendMessageActivity.MAX_IMAGE_HEIGHT){

            float scale=(float)SendMessageActivity.MAX_IMAGE_HEIGHT / bitmap.getWidth();
            int finalWidth=(int)(bitmap.getHeight()* scale);
            Bitmap resizedBitmap=Bitmap.createScaledBitmap(bitmap,SendMessageActivity.MAX_IMAGE_HEIGHT,finalWidth,false);

            if(resizedBitmap !=bitmap){
                bitmap.recycle();
                bitmap=resizedBitmap;
            }
        }

        Matrix matrix=new Matrix();
        if(cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){

            Matrix matrixMirror=new Matrix();
            matrixMirror.setValues(new float[]
                    {
                       -1,0,0,
                        0,1,0,
                        0,0,1

                    });
            matrix.postConcat(matrixMirror);
        }

        matrix.postRotate(90);

        Bitmap processedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        if(bitmap !=processedBitmap){
            bitmap.recycle();
        }
        return processedBitmap;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_SEND_MESSAGE && resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();

            Message message=data.getParcelableExtra(SendMessageActivity.RESULT_MESSAGE);
            Intent intent=new Intent(this,MessageActivity.class);
            intent.putExtra(MessageActivity.EXTRA_MESSAGES,message);
            startActivity(intent);
        }
    }
}
