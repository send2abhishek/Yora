package com.example.abhishekaryan.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.abhishekaryan.services.Messages;
import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

public class SendMessageActivity extends BaseAuthenticateActivity implements View.OnClickListener {

    public static final String EXTRA_IMAGE_PATH="EXTRA_IMAGE_PATH";
    public static final String EXTRA_CONTACT="EXTRA_CONTACT";
    public static final int MAX_IMAGE_HEIGHT=1280;
    public static final String RESULT_MESSAGE="RESULT_MESSAGE";
    private static final String STATE_REQUEST ="STATE_REQUEST" ;
    private Messages.sendMessageRequest request;
    private EditText messageEditText;
    private Button recipentButton;
    private View ProgressBar;


    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {

     setContentView(R.layout.activity_sendmessage);
        getSupportActionBar().setTitle("Send Message");

        if(savedInstanceState!=null){
            request=savedInstanceState.getParcelable(STATE_REQUEST);
        }
        if(request==null){

            request=new Messages.sendMessageRequest();
            request.setRecipient((UserDetails)getIntent().getParcelableExtra(EXTRA_CONTACT));
        }


        Uri imageUri=getIntent().getParcelableExtra(EXTRA_IMAGE_PATH);
        if(imageUri!=null){

            ImageView imageView=(ImageView)findViewById(R.id.activity_send_message_image);
            Picasso picasso=Picasso.with(this);
            picasso.invalidate(imageUri);
           picasso.load(imageUri).into(imageView);
            request.setImagePath(imageUri);
        }
        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT){

            View optionFrame=findViewById(R.id.activity_send_message_options_frame);
            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) optionFrame.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.include_main_activity_toolbar);
            //converts device independent pixel to device pixel
            params.width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
            optionFrame.setLayoutParams(params);
        }

        messageEditText=(EditText)findViewById(R.id.activity_send_message_message);
        recipentButton=(Button)findViewById(R.id.activity_send_message_recipient);
        ProgressBar=findViewById(R.id.activity_send_message_progressFrame);
        ProgressBar.setVisibility(View.GONE);

        recipentButton.setOnClickListener(this);
        updateButton();

    }

    private void updateButton() {

        UserDetails recepitent=request.getRecipient();
        if(recepitent!=null){
            recipentButton.setText("To:"+recepitent.getDisplayName());
        }
        else {
            recipentButton.setText("Choose Recipient");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_REQUEST,request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.send_message_sent_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.menu.send_message_sent_button){
            sendMessage();
            return true;
        }
        return false;
    }

    private void sendMessage() {

        String message=messageEditText.getText().toString();
        if(message.length()<2){

            messageEditText.setError("Please enter a long message");
            return;
        }

        messageEditText.setError(null);

        if(request.getRecipient()==null){
            Toast.makeText(this,"Slect recipient ",Toast.LENGTH_SHORT).show();
            selectRecipient();
            return;
        }


        ProgressBar.setVisibility(View.GONE);
        ProgressBar.animate()
                .alpha(1)
                .setDuration(250)
                .start();
        request.setMessage(message);
        bus.post(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe
    public void onMessageSent(Messages.sendMessageRespons respone){
        if(!respone.didSucceed()){

            respone.showErrorToast(this);
            messageEditText.setError(respone.getPropertyErrors("message"));

            ProgressBar.animate()
                    .alpha(0)
                    .setDuration(200)
                   .withEndAction(new Runnable() {
                       @Override
                       public void run() {
                           ProgressBar.setVisibility(View.GONE);
                       }
                   })
                    .start();

            return;
        }

        Intent data=new Intent();
        data.putExtra(RESULT_MESSAGE,respone.message);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void onClick(View view) {

        if(view==recipentButton){
            selectRecipient();
        }

    }

    private void selectRecipient() {


    }
}
