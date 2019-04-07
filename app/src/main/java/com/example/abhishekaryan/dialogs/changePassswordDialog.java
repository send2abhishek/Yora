package com.example.abhishekaryan.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishekaryan.services.Account;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

import java.security.PrivateKey;

public class changePassswordDialog extends BaseDialogFragment implements View.OnClickListener {

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private AlertDialog progressDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View DialogView =getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password,null,false);

        currentPassword=(EditText)DialogView.findViewById(R.id.dialog_change_password_current_password);
        newPassword=(EditText)DialogView.findViewById(R.id.dialog_change_password_new_password);
        confirmPassword=(EditText)DialogView.findViewById(R.id.dialog_change_password_confirm_password);

     /*   if(!application.getAuth().getUser().getHasPassword());
        currentPassword.setVisibility(View.GONE);

        */

        AlertDialog dialog=new AlertDialog.Builder(getActivity())

                .setView(DialogView)
                .setPositiveButton("Update",null)
                .setNegativeButton("Cancel",null)
                .setTitle("Change Password")
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onClick(View view) {


        progressDialog=new ProgressDialog.Builder(getActivity())
                .setTitle("Changing Password")
                .setCancelable(false)
                .show();
        Log.e("Tag","Inside password onclick");
       bus.post(new Account.UpdateChangePasswordRequest(currentPassword.getText().toString(),

                newPassword.getText().toString(),
                confirmPassword.getText().toString()

        ));


    }
    @Subscribe
    public void passwordChanged(Account.UpdateChangePasswordResponse response){



        progressDialog.dismiss();
        progressDialog=null;

        if(response.didSucceed()){

            Log.e("Tag","Inside password changed");

            Toast.makeText(getActivity(),"Password Updated", Toast.LENGTH_SHORT).show();
            dismiss();

            application.getAuth().getUser().setHasPassword(true);
            return;
        }



        currentPassword.setError(response.getPropertyErrors("currentPassword"));
        newPassword.setError(response.getPropertyErrors("newPassword"));
        confirmPassword.setError(response.getPropertyErrors("confirmPassword"));
        response.showErrorToast(getActivity());
    }

}


