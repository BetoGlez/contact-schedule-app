package com.agonzalez.practica3_albertogonzalezhernandez;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements ItemTouchAdapter, View.OnClickListener, Filterable {

    private View.OnClickListener listener;
    private List<ContactItem> contactsList;
    private List<ContactItem> contactsListAll;
    private Context context;

    public ContactsAdapter(List<ContactItem> contactsList, Context context) {
        this.context = context;
        this.contactsList = contactsList;
        this.contactsListAll = new ArrayList<>(contactsList);
    }

    @Override
    public void onInsertItem(ContactItem contactItem) {
        contactsList.add(contactItem);
        notifyItemInserted(contactsList.size() - 1);
    }

    @Override
    public void onDeleteItem(int position) {
        contactsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onEditItem(int position, ContactItem editedItem) {
        contactsList.set(position, editedItem);
        notifyItemChanged(position, editedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        itemView.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        ContactItem item = contactsList.get(pos);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
    public void setOnClickListener(View.OnClickListener listener) { this.listener = listener; }
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        // Run on background
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ContactItem> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(contactsListAll);
            } else {
                for (ContactItem contact: contactsListAll) {
                    if (contact.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(contact);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }
        // Run on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactsList.clear();
            contactsList.addAll((Collection<? extends ContactItem>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, phoneTv, nameLettersTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.contactNameTv);
            phoneTv = itemView.findViewById(R.id.contactPhoneTv);
            nameLettersTv = itemView.findViewById(R.id.contactNameLettersTv);
        }

        public void bindData(final ContactItem contact) {
            nameTv.setText(contact.getName());
            phoneTv.setText(contact.getMobilePhone());
            // Circle label color
            OvalShape ovalShape = new OvalShape();
            ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
            shapeDrawable.getPaint().setColor(getLabelColor(contact.getLabelColor()));
            nameLettersTv.setBackground(shapeDrawable);
            nameLettersTv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            // Circle label text
            String contactName = contact.getName();
            Character firstLetter = contactName.charAt(0);
            nameLettersTv.setText(firstLetter.toString().toUpperCase());
        }

        private int getLabelColor(int labelColorId) {
            int labelColor;
            switch (labelColorId) {
                case 1:
                    labelColor = ContextCompat.getColor(context, R.color.color1);
                    break;
                case 2:
                    labelColor = ContextCompat.getColor(context, R.color.color2);
                    break;
                case 3:
                    labelColor = ContextCompat.getColor(context, R.color.color3);
                    break;
                case 4:
                    labelColor = ContextCompat.getColor(context, R.color.color4);
                    break;
                default:
                    labelColor = ContextCompat.getColor(context, R.color.color5);
                    break;
            }
            return labelColor;
        }
    }
}
