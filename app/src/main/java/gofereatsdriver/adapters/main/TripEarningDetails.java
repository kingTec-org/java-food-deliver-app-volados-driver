package gofereatsdriver.adapters.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.InjectView;
import gofereatsdriver.R;
import gofereatsdriver.utils.CommonMethods;

/**
 * Created by trioangle on 11/6/18.
 */

public class TripEarningDetails extends AppCompatActivity {

    public @Inject
    CommonMethods commonMethods;
    public @InjectView(R.id.insurance_back)
    ImageView insurance_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_earning_details);
        commonMethods.rotateArrow(insurance_back,this);
    }
}
