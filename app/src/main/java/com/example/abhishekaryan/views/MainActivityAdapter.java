package com.example.abhishekaryan.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.services.entites.ConatctRequest;
import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.yora.R;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE=1;
    private static final int VIEW_TYPE_CONATCT_REQUEST=2;
    private static final int VIEW_TYPE_HEADER=3;
    private ArrayList<Message> messages;
    private ArrayList<ConatctRequest> conatctRequests;
    private BaseActivity activity;
    private MainActivityListener listener;
    private LayoutInflater inflater;

    public MainActivityAdapter(BaseActivity activity, MainActivityListener listener) {
        this.activity = activity;
        this.listener = listener;
        messages=new ArrayList<>();
        conatctRequests =new ArrayList<>();
        inflater=activity.getLayoutInflater();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<ConatctRequest> getConatctRequests() {
        return conatctRequests;
    }


    @SuppressWarnings("UnusedAssignment")
    @Override
    public int getItemViewType(int position) {

        if(conatctRequests.size() > 0){

            if(position==0){
                return VIEW_TYPE_HEADER;
            }

            position--;
            if(position < conatctRequests.size()){
                return VIEW_TYPE_CONATCT_REQUEST;
            }

            position -= conatctRequests.size();

        }

        if(messages.size() > 0){
            if(position == 0){

                return VIEW_TYPE_HEADER;
            }
            position--;
            if(position < messages.size()){
                return VIEW_TYPE_MESSAGE;
            }

            position -= messages.size();
        }
        throw new IllegalArgumentException("We are being asked for an item type from postion, which not avaible" + position);


    }

    /*HEADER -> Incoming contact Request

    CONTACT_REQUEST - FROM USER 1
    CONTACT_REQUEST - FROM USER 2
    CONTACT_REQUEST - FROM USER 3
    CONTACT_REQUEST - FROM USER 4

    HEADER -> messages
    HEADER -> Messages 1
    HEADER -> Messages 2

     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==VIEW_TYPE_MESSAGE){
            final MessageViewHolder viewHolder=new MessageViewHolder(inflater.inflate(R.layout.list_item_message,parent,false));

            viewHolder.getBackground().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMessageClicked((Message) view.getTag());
                }
            });

            return viewHolder;
        }

        else if (viewType==VIEW_TYPE_CONATCT_REQUEST){

            final ContactRequestViewHolder viewHolder=new ContactRequestViewHolder(inflater,parent);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConatctRequest request=(ConatctRequest)viewHolder.itemView.getTag();
                    listener.onContactRequestClicked(request, conatctRequests.indexOf(request));
                }
            });
            return viewHolder;

        }

        else if(viewType == VIEW_TYPE_HEADER){
            return new HeaderViewHolder(inflater,parent);
        }


        throw new IllegalArgumentException("view type not supported" + viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ContactRequestViewHolder){

            position --;

            ConatctRequest request= conatctRequests.get(position);
            holder.itemView.setTag(request);
            ((ContactRequestViewHolder) holder).populate(activity,request);
        }

        else if(holder instanceof MessageViewHolder){
            position --;

            if(conatctRequests.size() > 0){
                position=position -1- conatctRequests.size();
            }

            Message message=messages.get(position);
            MessageViewHolder viewHolder=(MessageViewHolder)holder;
            viewHolder.getBackground().setTag(message);
            viewHolder.populate(activity,message);
        }

        else if(holder instanceof HeaderViewHolder){

            HeaderViewHolder viewHolder=(HeaderViewHolder)holder;

            if(position==0 && conatctRequests.size() > 0){

                viewHolder.populate("Recieved Contact Request");
            }
            else {
                viewHolder.populate("Recieved Message");
            }


        }

        else {

            throw new IllegalArgumentException("cannot populate holder of type" + holder.getClass().getName());
        }

    }

    @Override
    public int getItemCount() {

        int count=0;
        if(conatctRequests.size() > 0){
            count +=1 + conatctRequests.size();
        }

        if(messages.size() > 0){
            count +=1 + messages.size();
        }
        return count;
    }


    public interface MainActivityListener{

         void onMessageClicked(Message message);
         void onContactRequestClicked(ConatctRequest request,int position);
    }
}
