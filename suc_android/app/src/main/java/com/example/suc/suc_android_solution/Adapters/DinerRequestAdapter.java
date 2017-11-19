package com.example.suc.suc_android_solution.Adapters;

import android.content.Context;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suc.suc_android_solution.Models.DinerRequest;
import com.example.suc.suc_android_solution.R;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by efridman on 18/11/17.
 */

public class DinerRequestAdapter extends RecyclerView.Adapter<DinerRequestAdapter.DinerRequestViewHolder> {

    private List<DinerRequest> mData;
    private Context context;

    final private DinerRequestAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface DinerRequestAdapterOnClickHandler {
        void onClick(BigInteger idDinerRequest);
    }

    public DinerRequestAdapter(List<DinerRequest> mData, Context context, DinerRequestAdapterOnClickHandler clickHandler) {
        this.mData = mData;
        this.context = context;
        this.mClickHandler = clickHandler;

    }

    @Override
    public DinerRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diner_request_item, parent, false);
        return new DinerRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DinerRequestViewHolder holder, final int position) {
        holder.dinerRequestTitle.setText(mData.get(position).title + "");
        holder.dinerRequestDescription.setText(mData.get(position).description + "");
        if(position % 2 != 0){
            holder.container.setBackground(context.getResources().getDrawable(R.drawable.rv_item_background_dark));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHandler.onClick(mData.get(position).idDinerRequest);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(List<DinerRequest> models) {
        mData.addAll(models);
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
    class DinerRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView dinerRequestTitle;
        final TextView dinerRequestDescription;
        final ConstraintLayout container;



        DinerRequestViewHolder(View view) {
            super(view);

            dinerRequestTitle = (TextView) view.findViewById(R.id.diner_request_item_title);
            dinerRequestDescription = (TextView) view.findViewById(R.id.diner_request_item_description);
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
            mClickHandler.onClick(mData.get(adapterPosition).idDinerRequest);
        }
    }
}
