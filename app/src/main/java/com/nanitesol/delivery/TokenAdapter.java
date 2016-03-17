package com.nanitesol.delivery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by praveen kumar on 1/23/2016.
 */
public class TokenAdapter extends
        RecyclerView.Adapter<TokenAdapter.ViewHolder>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.delivery_recycleview, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        Tokens contact = mContacts.get(position);

        // Set item views based on the data model
        TextView textView = holder.nameTextView;
        textView.setText(contact.getName());

      /*  TextView button = holder.messageButton;

        if (contact.isOnline()) {
            button.setText("ABCDE12");
            button.setEnabled(true);
        }
        else {
            button.setText("BDSSFDS23");
            button.setEnabled(false);
        }*//*  TextView button = holder.messageButton;

        if (contact.isOnline()) {
            button.setText("ABCDE12");
            button.setEnabled(true);
        }
        else {
            button.setText("BDSSFDS23");
            button.setEnabled(false);
        }*/

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
    // Store a member variable for the contacts
    private List<Tokens> mContacts;

    // Pass in the contact array into the constructor
    public TokenAdapter(List<Tokens> contacts) {
        mContacts = contacts;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.txtVwOrderId);
           // messageButton = (TextView) itemView.findViewById(R.id.txtVwTokenId);
        }
    }
}
