package com.example.abhishekaryan.views;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.abhishekaryan.activities.BaseActivity;

import java.util.List;
//surface View is resposible for drawing views also handles callbacks

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private static final String TAG = "CameraPreview";
        private  SurfaceHolder sufaceHolder;
        private Camera camera;
        private Camera.CameraInfo cameraInfo;
        private Boolean isSurfaceCreated;
        public CameraPreview(BaseActivity context) {
            super(context);
            isSurfaceCreated=false;
            sufaceHolder=getHolder();
            sufaceHolder.addCallback(this);

        }

        public void setCamera(Camera camera, Camera.CameraInfo cameraInfo){


            if(this.camera!=null){


                try {
                    this.camera.stopPreview();
                }
                catch (Exception e){
                    Log.e(TAG,"could not stop camera preview");
                }

            }
            this.camera=camera;
            this.cameraInfo=cameraInfo;
            
            if(camera==null) {
                return;
            }

            if(!isSurfaceCreated){
                return;
            }
            try{
                camera.setPreviewDisplay(sufaceHolder);
                configureCamera();
                camera.startPreview();
            }
            catch (Exception e){
                Log.e(TAG,"could not start camera preview",e);
            }

        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            if(holder!=sufaceHolder){
                sufaceHolder=holder;
                sufaceHolder.addCallback(this);
            }

            isSurfaceCreated=true;
            if(camera!=null) {
                setCamera(camera, cameraInfo);
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            isSurfaceCreated=false;
            sufaceHolder.removeCallback(this);
            sufaceHolder=null;
            if(camera==null ){
                return;
            }

            try {
                this.camera.stopPreview();
                camera=null;
                cameraInfo=null;
            }
            catch (Exception e){
                Log.e(TAG,"could not stop camera preview",e);
            }

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            widthMeasureSpec=resolveSize(getSuggestedMinimumWidth(),widthMeasureSpec);
            heightMeasureSpec=resolveSize(getSuggestedMinimumHeight(),heightMeasureSpec);
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        }

        private void configureCamera(){
            Camera.Parameters parameters=camera.getParameters();
            Camera.Size targetPreviewSize=getClosestSize(getWidth(),getHeight(),parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(targetPreviewSize.width,targetPreviewSize.height);
            Camera.Size targetImageSize=getClosestSize(1024,1280,parameters.getSupportedPictureSizes());
            parameters.setPictureSize(targetImageSize.width,targetImageSize.height);
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
        }

        private Camera.Size getClosestSize(int width, int height, List<Camera.Size> supportedSizes){
            final double ASPECT_TOLERANCE=.1;
            double targetRatio=(double)(width/height);

            Camera.Size targetSize=null;
            double minDifference=Double.MAX_VALUE;
            for(Camera.Size Size:supportedSizes){
                double ratio=(double)Size.width/Size.height;
                if(Math.abs((ratio-targetRatio))> ASPECT_TOLERANCE){
                    continue;
                }

                int heightDifference=Math.abs(Size.height-height);
                if(heightDifference < minDifference){
                    targetSize=Size;
                    minDifference=heightDifference;
                }
            }
            if(targetSize==null){
                minDifference=Double.MAX_VALUE;
                for(Camera.Size Size:supportedSizes) {
                    int heightDifference=Math.abs(Size.height-height);
                    if(heightDifference < minDifference){
                        targetSize=Size;
                        minDifference=heightDifference;
                    }
                }
            }

            return targetSize;
        }
    }
