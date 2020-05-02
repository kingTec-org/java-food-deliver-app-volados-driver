package gofereatsdriver.adapters.main;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage adapters.main
 * @category DropDownMenuAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereatsdriver.R;
import gofereatsdriver.datamodels.dropoffrating.DropoffissuesList;


/*****************************************************************
 Adapter Feedback and issues
 ****************************************************************/

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {

    public Context context;
    public ArrayList<DropoffissuesList> dropoffissuesListArrayList = new ArrayList<>();


    private OnItemClickListener mItemClickListener;

    public FeedBackAdapter(Context context, ArrayList<DropoffissuesList> dropoffissuesListArrayList) {
        this.context = context;
        this.dropoffissuesListArrayList = dropoffissuesListArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public FeedBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedBackAdapter.ViewHolder holder, final int position) {
        holder.tvFeedback.setText(dropoffissuesListArrayList.get(position).getIssue());
        dropoffissuesListArrayList.get(position).setSelected(true);

        holder.rltFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropoffissuesListArrayList.get(position).isSelected()) {
                    holder.rltFeedback.setBackground(context.getResources().getDrawable(R.drawable.oval_border_black));
                    dropoffissuesListArrayList.get(position).setSelected(false);
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickListener(dropoffissuesListArrayList.get(position).getId(), position);
                    }
                } else {
                    holder.rltFeedback.setBackground(context.getResources().getDrawable(R.drawable.oval_border_grey));
                    dropoffissuesListArrayList.get(position).setSelected(true);
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickListener(dropoffissuesListArrayList.get(position).getId(), position);
                    }
                }
            }
        });

    }

    //
    @Override
    public int getItemCount() {
        return dropoffissuesListArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int id, int positionz);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rltFeedback;
        private CustomTextView tvFeedback;

        public ViewHolder(View itemView) {
            super(itemView);

            rltFeedback = itemView.findViewById(R.id.rltFeedback);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
        }
    }
}
