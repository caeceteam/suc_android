package com.example.suc.suc_android_solution.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.DinerRequest;
import com.example.suc.suc_android_solution.R;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by efridman on 18/11/17.
 */

public class DinersAdapter extends RecyclerView.Adapter<DinersAdapter.DinerViewHolder> {

    private List<Diner> mData;
    private Context context;

    final private DinersAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface DinersAdapterOnClickHandler {
        void onClick(BigInteger idDinerRequest);
    }

    public DinersAdapter(List<Diner> mData, Context context, DinersAdapter.DinersAdapterOnClickHandler clickHandler) {
        this.mData = mData;
        this.context = context;
        this.mClickHandler = clickHandler;

    }

    @Override
    public DinersAdapter.DinerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diner_item, parent, false);
        return new DinersAdapter.DinerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DinersAdapter.DinerViewHolder holder, final int position) {
        holder.dinerName.setText(mData.get(position).getName() + "");
        holder.dinerAddress.setText(buildDinerAddress(mData.get(position)) + "");
        if (position % 2 != 0) {
            holder.container.setBackground(context.getResources().getDrawable(R.drawable.rv_item_background_dark));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHandler.onClick(mData.get(position).idDiner);
            }
        });

    }

    private String buildDinerAddress(Diner diner) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Dirección: %s %d", diner.getStreet(),
                diner.getStreetNumber()));

        if (diner.getDoor() != null && !diner.getDoor().equals("")) {
            builder.append(String.format(" Puerta: %s", diner.getDoor()));
        }

        if (diner.getFloor() != null && !diner.getFloor().equals("")) {
            builder.append(String.format(" Piso: %s", diner.getFloor()));
        }
        builder.append(String.format(" - CP: %s", diner.getZipcode()));

        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(List<Diner> models) {
        mData.addAll(models);
        mData = mData.stream().distinct().collect(Collectors.<Diner>toList());
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class DinerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView dinerName;
        final TextView dinerAddress;
        final ConstraintLayout container;


        DinerViewHolder(View view) {
            super(view);

            dinerName = (TextView) view.findViewById(R.id.diner_item_name);
            dinerAddress = (TextView) view.findViewById(R.id.diner_item_address);
            container = (ConstraintLayout) view.findViewById(R.id.item_container);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mData.get(adapterPosition).idDiner);
        }
    }
}