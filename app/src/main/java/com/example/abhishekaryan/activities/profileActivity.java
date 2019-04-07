package com.example.abhishekaryan.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.abhishekaryan.dialogs.changePassswordDialog;
import com.example.abhishekaryan.services.Account;
import com.example.abhishekaryan.views.mainNavDrawer;
import com.example.abhishekaryan.yora.R;
import com.example.abhishekaryan.yora.infrastructure.User;
import com.squareup.otto.Subscribe;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class profileActivity extends BaseAuthenticateActivity implements View.OnClickListener {



    private static final int STATE_VIEWING=1;
    private static final int STATE_EDITING=2;
    private static final String BUNDLE_STATE="BUNDLE_STATE" ;
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutfile;

    private int currentState;
    private EditText displayNameText;
    private EditText emailText;
    private View changeAvatarButton;
    private ActionMode editProfileActionMode;
    private AlertDialog progressDialog;
    private static boolean isProgressBarVisible;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        setNavDrawer(new mainNavDrawer(this));

        if(!isTablet){
            View textfields=findViewById(R.id.activity_profile_textFields);
            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)textfields.getLayoutParams();
            params.setMargins(0,params.getMarginStart(),0,0);
            params.removeRule(RelativeLayout.RIGHT_OF);
            params.addRule(RelativeLayout.BELOW,R.id.activity_profile_avatar);
            textfields.setLayoutParams(params);
        }

        changeAvatarButton=findViewById(R.id.actvity_profile_changeAvatar);
        displayNameText=(EditText)findViewById(R.id.activity_profile_display_Name);
        emailText=(EditText)findViewById(R.id.activity_profile_email);


        avatarView=(ImageView)findViewById(R.id.activity_profile_avatar);
        avatarProgressFrame=findViewById(R.id.activity_profile_avatarProgressFrame);
        tempOutfile=new File(getExternalCacheDir(),"temp-img.jpg");

        avatarView.setOnClickListener(this);
        changeAvatarButton.setOnClickListener(this);
        avatarProgressFrame.setVisibility(View.GONE);

        User user=application.getAuth().getUser();
        getSupportActionBar().setTitle(user.getDisplayName());
        displayNameText.setText(user.getDisplayName());
        emailText.setText(user.getEmail());

        changeSate(STATE_VIEWING);

        if(isProgressBarVisible)
            setProgressBarVisible(true);

    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event){

        getSupportActionBar().setTitle(event.user.getDisplayName());
    }


    @Override
    public void onClick(View view) {

        int viewId=view.getId();

        if(viewId==R.id.actvity_profile_changeAvatar || viewId==R.id.activity_profile_avatar)
            changeAvatar();

    }

    private void changeAvatar() {

        List<Intent> otherImageCaptureIntent= new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities=getPackageManager()
                .queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),0);
        for(ResolveInfo info : otherImageCaptureActivities){

            Intent captureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName,info.activityInfo.name);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutfile));
            otherImageCaptureIntent.add(captureIntent);
        }
        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser=Intent.createChooser(selectImageIntent,"Choose Avatar");

        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,otherImageCaptureIntent
                .toArray(new Parcelable[otherImageCaptureActivities.size()]));
        startActivityForResult(chooser,REQUEST_SELECT_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode !=RESULT_OK){
            tempOutfile.delete();
            return;
        }

        if(requestCode==REQUEST_SELECT_IMAGE) {

            Uri outputFile;
            Uri tempFileUri = Uri.fromFile(tempOutfile);
            if (data != null && (data.getAction()) == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)) {
                outputFile = data.getData();

            } else
                outputFile = tempFileUri;



            CropImage.activity(outputFile).setOutputUri(tempFileUri)
                    .start(this);




        }
        else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            avatarProgressFrame.setVisibility(View.GONE);
            bus.post(new Account.changeAvatarRequest(Uri.fromFile(tempOutfile)));

        }

    }

    @Subscribe
    public void onAvatarUpdated(Account.changeAvatarResponse response){
        avatarProgressFrame.setVisibility(View.GONE);

        if(!response.didSucceed())
            response.showErrorToast(this);
    }
    @Subscribe
    public void onProfileUpdated(Account.UpdateProfileResponse response){
        if(!response.didSucceed()){

            response.showErrorToast(this);
            changeSate(STATE_EDITING);
        }
        displayNameText.setError(response.getPropertyErrors("displayName"));
        emailText.setError(response.getPropertyErrors("email"));
        setProgressBarVisible(false);
    }

    private void setProgressBarVisible(boolean visible){
        if(visible){

            progressDialog=new ProgressDialog.Builder(this)
                    .setTitle("Updating Profile")
                    .setCancelable(false)
                    .show();


        }
        else if(progressDialog !=null){

            progressDialog.dismiss();
            progressDialog=null;
        }
        isProgressBarVisible=visible;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int itemId=item.getItemId();
        if(itemId==R.id.activity_profile_menuEdit){

            changeSate(STATE_EDITING);
            return true ;
        }
        else if (itemId==R.id.activity_profile_menuChangePassword){

            FragmentTransaction transaction=getFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null);
            changePassswordDialog dialog=new changePassswordDialog();
            dialog.show(transaction,null);
            return true;

        }


        return false;
    }
    private void changeSate(int state){
        if(state==currentState)
            return;
        currentState=state;

        if(state==STATE_VIEWING){
            displayNameText.setEnabled(false);
            emailText.setEnabled(false);
            changeAvatarButton.setVisibility(View.VISIBLE);

            if(editProfileActionMode !=null){
                editProfileActionMode.finish();
                editProfileActionMode=null;
            }


        }
        else if(state==STATE_EDITING){

            displayNameText.setEnabled(true);
            emailText.setEnabled(true);
            changeAvatarButton.setVisibility(View.GONE);
            editProfileActionMode=toolbar.startActionMode(new EditProfileActionCallback());
        }
        else
            throw new IllegalArgumentException("Invalid state" +state);

    }
    private class EditProfileActionCallback implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            getMenuInflater().inflate(R.menu.activity_profile_edit,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int ItemId= item.getItemId();

            if(ItemId==R.id.activity_profile_edit_menudone){

                setProgressBarVisible(true);
                changeSate(STATE_VIEWING);
                bus.post(new Account.UpdateProfileRequest(displayNameText.getText().toString(),
                        emailText.getText().toString()));
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if(currentState != STATE_VIEWING)
                changeSate(STATE_VIEWING);

        }
    }
}
