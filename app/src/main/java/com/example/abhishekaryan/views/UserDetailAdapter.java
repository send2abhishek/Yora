package com.example.abhishekaryan.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.yora.R;
import com.squareup.picasso.Picasso;

public class UserDetailAdapter extends ArrayAdapter<UserDetails> {

    private LayoutInflater inflater;
    public UserDetailAdapter(BaseActivity activity){

        super(activity,0);
        inflater=activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        UserDetails details=getItem(position);

        ViewHolder view;

        if(convertView==null){

            convertView=inflater.inflate(R.layout.list_item_user_details,parent,false);
            view=new ViewHolder(convertView);
            convertView.setTag(view);
        }
        else{
            view=(ViewHolder)convertView.getTag();
        }

        view.DisplayName.setText(details.getDisplayName());
        Picasso.with(getContext()).load(details.getAvatarUrl()).into(view.Avatar);

        return convertView;

    }


    private class ViewHolder{

        public TextView DisplayName;
        public ImageView Avatar;

        public ViewHolder(View view){

            DisplayName=(TextView)view.findViewById(R.id.list_item_user_details_displayName);
            Avatar=(ImageView)view.findViewById(R.id.list_item_user_details_avatar);

        }
    }

}
