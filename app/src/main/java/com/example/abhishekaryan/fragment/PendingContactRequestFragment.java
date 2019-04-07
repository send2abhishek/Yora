package com.example.abhishekaryan.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.services.Contacts;
import com.example.abhishekaryan.views.ContactRequestAdapter;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

public class PendingContactRequestFragment extends BaseFragment {

    private View ProgressFrame;
    private ContactRequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_pending_contact_request,container,false);
        ProgressFrame=view.findViewById(R.id.fragment_pending_contact_request_progressFrame);
        adapter=new ContactRequestAdapter((BaseActivity)getActivity());
        ListView listView=(ListView)view.findViewById(R.id.fragment_pending_contact_request_list);
        listView.setAdapter(adapter);
        bus.post(new Contacts.GetContactRequestRequest(true));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe
    public void onGetContactRequest(final Contacts.GetContactRequestResponse response){

        schedular.invokeonResume(Contacts.GetContactRequestResponse.class, new Runnable() {
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
                adapter.addAll(response.Requests);

            }
        });





    }
}
