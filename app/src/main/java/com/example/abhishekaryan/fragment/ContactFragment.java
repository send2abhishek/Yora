package com.example.abhishekaryan.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abhishekaryan.activities.AddContactActivity;
import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.activities.ContactActivity;
import com.example.abhishekaryan.services.Contacts;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.views.UserDetailAdapter;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

public class ContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private UserDetailAdapter adapter;
    private View ProgressFrame;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view=inflater.inflate(R.layout.fragment_contact,container,false);
        adapter=new UserDetailAdapter((BaseActivity)getActivity());
        ListView listView=(ListView)view.findViewById(R.id.fragment_contact_list);
        ProgressFrame=view.findViewById(R.id.fragment_contact_list_request_progressFrame);
        listView.setEmptyView(view.findViewById(R.id.fragment_contact_list_emptyList));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Contacts.GetContactRequest(false));
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        UserDetails details=adapter.getItem(position);
        Intent intent=new Intent(getActivity(),ContactActivity.class);
        intent.putExtra(ContactActivity.EXTRA_USER_DETAILS,details);
        startActivity(intent);

    }

    @Subscribe
    public void onContactResponse(final Contacts.GetContactResponse response){

        schedular.invokeonResume(Contacts.GetContactResponse.class, new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {



                ProgressFrame.animate()
                        .alpha(0)
                        .setDuration(250)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                ProgressFrame.setVisibility(View.GONE);
                            }
                        }).start();
                if(!response.didSucceed()){
                    response.showErrorToast(getActivity());
                    return;
                }
                adapter.clear();
                adapter.addAll(response.contacts);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_contact,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.fragment_contact_menu_addContact){
            startActivity(new Intent(getActivity(),AddContactActivity.class));
            return true;
        }
        return true;
    }
}
