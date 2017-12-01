package com.example.suc.suc_android_solution.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.R;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by efridman on 18/11/17.
 */

public class DonationsAdapter extends RecyclerView.Adapter<DonationsAdapter.DonationViewHolder> {

    private List<Donation> mData;
    private Context context;

    final private DonationsAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface DonationsAdapterOnClickHandler {
        void onClick(BigInteger idDinerRequest);
    }

    public DonationsAdapter(List<Donation> mData, Context context, DonationsAdapterOnClickHandler clickHandler) {
        this.mData = mData;
        this.context = context;
        this.mClickHandler = clickHandler;

    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_item, parent, false);
        return new DonationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DonationViewHolder holder, final int position) {
        holder.donationTitle.setText(mData.get(position).getTitle() + "");
        holder.donationDescription.setText(mData.get(position).getDescription() != null ? mData.get(position).getDescription() : "");
        holder.donationDinerName.setText(mData.get(position).getDiner().getName());
        holder.donationDinerLabel.setText(R.string.donation_diner);
        if (position % 2 != 0) {
            holder.container.setBackground(context.getResources().getDrawable(R.drawable.rv_item_background_dark));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHandler.onClick(mData.get(position).getIdDonation());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(List<Donation> models) {
        mData.addAll(models);
        mData = mData.stream().distinct().collect(Collectors.<Donation>toList());
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
    class DonationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView donationDinerLabel;
        final TextView donationDinerName;
        final TextView donationTitle;
        final TextView donationDescription;
        final ConstraintLayout container;


        DonationViewHolder(View view) {
            super(view);
            donationDinerLabel = (TextView) view.findViewById(R.id.donation_item_diner_label);
            donationTitle = (TextView) view.findViewById(R.id.donation_item_title);
            donationDescription = (TextView) view.findViewById(R.id.donation_item_description);
            donationDinerName = (TextView) view.findViewById(R.id.donation_item_diner_name);
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
            mClickHandler.onClick(mData.get(adapterPosition).getIdDonation());
        }
    }
}