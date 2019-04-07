package com.example.abhishekaryan.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.yora.R;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> implements View.OnClickListener {
    private final LayoutInflater layoutInflater;
    private final BaseActivity activity;
    private final onMessageClickListener listener;
    public final ArrayList<Message> messages;

    public MessageAdapter(BaseActivity activity, onMessageClickListener listener) {
        this.activity = activity;
        this.listener = listener;
        messages=new ArrayList<>();
        layoutInflater=activity.getLayoutInflater();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.list_item_message,parent,false);
        view.setOnClickListener(this);
        MessageViewHolder viewHolder= new MessageViewHolder(view);
        viewHolder.getBackground().setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Message message=messages.get(position);
        holder.getBackground().setTag(message);
        holder.populate(activity,message);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onClick(View view) {

        if (view.getTag() instanceof  Message){
            Message message=(Message)view.getTag();
            listener.OnmessageClicked(message);
        }

    }

    public interface onMessageClickListener{

        void OnmessageClicked(Message message);
    }
}
