package com.aditi.reform2;

import android.content.Context;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<Items> mItems;
    private OnItemClickListener mListener;

    public ItemAdapter(Context mContext, List<Items> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Items itemCurrent = mItems.get(position);
        holder.product.setText(itemCurrent.getItemname());
        holder.quantity.setText(itemCurrent.getItemquantity());
        holder.description.setText(itemCurrent.getItemdescription());
        Glide.with(holder.imageview.getContext()).load(itemCurrent.getItemimage()).into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onUpdateClick(int position);

        void onDeleteClick(int position);

        void onShareClick(int position);

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView product, quantity, description;
        ImageView imageview;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.product);
            quantity = itemView.findViewById(R.id.quantity);
            description = itemView.findViewById(R.id.description);
            imageview = itemView.findViewById(R.id.imageview);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);

                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem update = menu.add(Menu.NONE, 1, 1, "Update Item");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete Item");
            MenuItem share = menu.add(Menu.NONE, 3, 3, "Share Item");

            update.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            share.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onUpdateClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                        case 3:
                            mListener.onShareClick(position);
                            return true;

                    }

                }
            }
            return false;
        }
    }
}
