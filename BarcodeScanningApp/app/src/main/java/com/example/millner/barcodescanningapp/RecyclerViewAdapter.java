package com.example.millner.barcodescanningapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.util.List;

/**
 * RecyclerView Adapter used to populate our RecyclerView on the HomeFragment
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List <InventoryItem> inventoryItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description, tag, building, roomNumber;

        public MyViewHolder (View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            tag = (TextView) view.findViewById(R.id.tag);
            building = (TextView) view.findViewById(R.id.building);
            roomNumber = (TextView) view.findViewById(R.id.room_number);
        }
    }

    public RecyclerViewAdapter (List<InventoryItem> inventoryItemList) {
        this.inventoryItemList = inventoryItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_inventory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (MyViewHolder holder, int position) {
        InventoryItem item = inventoryItemList.get(position);
        holder.description.setText(item.getDescription());
        holder.tag.setText(item.getTag());
        holder.building.setText(item.getBuilding());
        holder.roomNumber.setText(Integer.toString(item.getRoomNumber()));

    }
    @Override
    public int getItemCount() {
        return inventoryItemList.size();
    }
}
