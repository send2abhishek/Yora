package com.example.abhishekaryan.yora.infrastructure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.TreeMap;

public abstract class ServiceResponse {

    private static final String TAG="ServiceResponse" ;

    private String operationError;
    private HashMap<String, String> propertyErrors;
    private TreeMap<String, String> propertyErrorCaseInsensative;
    private boolean iscritical;

    public ServiceResponse(){
        propertyErrors=new HashMap<>();
    }

    public ServiceResponse(String operationError){
        this.operationError=operationError;
    }

    public ServiceResponse(String operationError, boolean iscritical){
        this.operationError=operationError;
        this.iscritical=iscritical;
    }


    public String getOperationError() {
        return operationError;
    }

    public void setCriticalError(String criticalError){

        iscritical=true;
        operationError=criticalError;
    }

    public void setOperationError(String operationError) {
        this.operationError = operationError;
    }

    public boolean iscritical() {
        return iscritical;
    }

    public void setIscritical(boolean iscritical) {
        this.iscritical = iscritical;
    }



    public void setPropertyErrors(String property, String error) {
        propertyErrors.put(property,error);
    }
    public String getPropertyErrors(String property) {
        if(propertyErrorCaseInsensative==null || propertyErrorCaseInsensative.size()!=propertyErrors.size()){

            propertyErrorCaseInsensative=new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            propertyErrorCaseInsensative.putAll(propertyErrors);
        }

        return propertyErrorCaseInsensative.get(property);
    }


    public boolean didSucceed(){
        return (operationError==null || operationError.isEmpty()) && (propertyErrors.size()==0);
    }

    public void showErrorToast(Context context){

        if(context==null || operationError==null || operationError.isEmpty())
            return;

        try{
            Toast.makeText(context,operationError,Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Log.e("Tag","can't create taost");
        }
    }
}
