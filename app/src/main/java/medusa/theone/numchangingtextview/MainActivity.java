package medusa.theone.numchangingtextview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import medusa.theone.numchangingtextlib.NumChangingTextView;


public class MainActivity extends Activity {
    private NumChangingTextView textView;
    private Button btnDes;
    private Button btnIns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (NumChangingTextView) findViewById(R.id.out);
        btnIns = (Button) findViewById(R.id.btnins);
        btnIns.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textView.withNumber(500f).setDuration(10000).startIncrease(true);
            }
        });
    }

}
