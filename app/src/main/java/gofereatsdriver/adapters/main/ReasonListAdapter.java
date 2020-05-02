package gofereatsdriver.adapters.main;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage adapters.main
 * @category ReasonListAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereatsdriver.R;
import gofereatsdriver.datamodels.dropoffrating.DropoffList;

/*****************************************************************
 Adapter For Reasons to cancel or Dropoff issues
 ****************************************************************/

public class ReasonListAdapter extends RecyclerView.Adapter<ReasonListAdapter.ViewHolder> {


    private ArrayList<DropoffList> dropoffListArrayList;
    private int clickedPos = -1;

    private OnItemClickListener mItemClickListener;

    public ReasonListAdapter(ArrayList<DropoffList> dropoffListArrayList) {

        this.dropoffListArrayList = dropoffListArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ReasonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_reason_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReasonListAdapter.ViewHolder holder, final int position) {
        holder.tvreasons.setText(dropoffListArrayList.get(position).getName());

        holder.cvReasonlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickedPos = position;
                notifyDataSetChanged();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListener(dropoffListArrayList.get(position).getId(), position);
                }

            }
        });


        if (clickedPos == position) {

            holder.ivReasontick.setVisibility(View.VISIBLE);
        } else {
            holder.ivReasontick.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return dropoffListArrayList.size();
    }


    public interface OnItemClickListener {
        void onItemClickListener(int id, int positionz);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvreasons;
        private ImageView ivReasontick;
        private CardView cvReasonlist;

        public ViewHolder(View itemView) {
            super(itemView);
            tvreasons = itemView.findViewById(R.id.tvreasons);
            ivReasontick = itemView.findViewById(R.id.ivReasontick);
            cvReasonlist = itemView.findViewById(R.id.cvReasonlist);
        }
    }
}
