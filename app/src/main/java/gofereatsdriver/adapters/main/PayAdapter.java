package gofereatsdriver.adapters.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.trips.WeeklyListModel;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.main.paymentstatement.PayStatementDetails;

/**
 * Created by trioangle on 11/6/18.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {

    public Activity context;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;


    private List<WeeklyListModel> modelItems;
    Context baseContext;
    public PayAdapter(Activity context, Context baseContext,List<WeeklyListModel> Items) {
        this.modelItems = Items;
        this.context = context;
        this.baseContext=baseContext;
    }

    @NonNull
    @Override
    public PayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pay_statement_itemlayout, viewGroup, false);
        AppController.getAppComponent().inject(this);
        return new ViewHolder(view);
    }

    /*
    *  Driver pay details bind data
    */
    @Override
    public void onBindViewHolder(@NonNull PayAdapter.ViewHolder viewHolder, final int i) {
        WeeklyListModel currentItem = getItem(i);
        viewHolder.tripdatetime.setText(currentItem.getWeek());
        viewHolder.tripamount.setText(sessionManager.getCurrencySymbol() + currentItem.getTotalFare());
        viewHolder.paystateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paystatement_details = new Intent(context, PayStatementDetails.class);
                paystatement_details.putExtra("weekDate", getItem(i).getDate());
                context.startActivity(paystatement_details);
                context.overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
        commonMethods.rotateArrowText(viewHolder.weekarrow,context);
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    private WeeklyListModel getItem(int position) {
        return modelItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout paystateRl;
        private TextView tripdatetime, tripamount,weekarrow;;

        public ViewHolder(View view) {
            super(view);
            tripdatetime = view.findViewById(R.id.tripdatetime);
            tripamount = view.findViewById(R.id.tripamount);
            paystateRl = view.findViewById(R.id.paystateRl);
            weekarrow=view.findViewById(R.id.weekarrow);
        }

    }
}
