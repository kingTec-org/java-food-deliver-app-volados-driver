package gofereatsdriver.views.main.commondialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.obs.CustomTextView;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.views.main.home.RequestAcceptActivity;
import gofereatsdriver.views.main.tripdetails.TripDetails;

public class ShowDialog extends Activity {
    public @Inject
    SessionManager sessionManager;
    private TextView tvMessage;
    private TextView tvOk;
    private int type = 0;
    private int tripid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_dialog);
        this.setFinishOnTouchOutside(false);

        tvMessage = (CustomTextView) findViewById(R.id.tvMessage);
        tvOk = findViewById(R.id.tvOk);

        tvMessage.setText(getIntent().getStringExtra("message"));
        type = getIntent().getIntExtra("type", 0);
        tripid = getIntent().getIntExtra("tripid", 0);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNotificationType();
            }
        });
    }


    private void getNotificationType() {
        if (type == 2) {
            RequestAcceptActivity.isTripCancelled = true;
            Intent cancelled = new Intent(getApplicationContext(), TripDetails.class);
            cancelled.putExtra("tripid", tripid);
            cancelled.putExtra("type", 1);
            startActivity(cancelled);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
