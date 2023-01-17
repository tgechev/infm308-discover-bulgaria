package tgechev.discoverbulgaria;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


//An activity which appears as a dialog when the user selects the help option from the action menu
public class activity_info extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking device orientation and setting the correct layout
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.info);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.info_landscape);
        }
    }
    //method which is called when a button in the activity is clicked. It simply finishes the activity.
    public void onClick(View v){
        finish();
    }
}
