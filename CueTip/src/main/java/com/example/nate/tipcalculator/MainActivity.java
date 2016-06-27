package com.example.nate.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.widget.Button;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
/*
    remember to start with an empty activity for drag and drop to work
*/

public class MainActivity extends AppCompatActivity{
    public double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("CueTip");

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544/6300978111\n");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // prevent switch to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText txtTotalBill = (EditText) (findViewById(R.id.txtTotalBill));

        //load tip calculator controller
        final TipCalculator tp = new TipCalculator();

        final TextView tvTotalTip = (TextView) (findViewById(R.id.tvTotalTip));
        final TextView tvTotalToPay = (TextView) (findViewById(R.id.tvTotalToPay));
        final TextView tvPerPerson = (TextView) (findViewById(R.id.tvPerPerson));
        final TextView tvTip = (TextView) (findViewById(R.id.tvTip));
        final TextView tvSplit = (TextView) (findViewById(R.id.tvSplit));
        final TextView tvTipPerPerson = (TextView) (findViewById(R.id.tvTipPerPerson));

        final SeekBar sbTip = (SeekBar) (findViewById(R.id.sbTip));
        sbTip.setMax(50);
        sbTip.setProgress(15);
        tvTip.setText(Integer.toString(sbTip.getProgress()) +"%");

        final SeekBar sbSplit = (SeekBar) (findViewById(R.id.sbSplit));
        sbSplit.setMax(20);
        sbSplit.setProgress(1);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.dp:{
                        if(txtTotalBill.getText().toString().length() < 1) {
                            txtTotalBill.setText("0.");
                        }

                        int hasPoint = txtTotalBill.getText().toString().indexOf(".");

                        if(hasPoint != -1 )
                            return;
                        else
                            txtTotalBill.setText(txtTotalBill.getText().toString() + ".");

                        break;
                    }
                    case R.id.clear:{
                        txtTotalBill.setText("");
                        tvPerPerson.setText("$0.00");
                        tvTotalTip.setText("$0.00");
                        tvTotalToPay.setText("$0.00");
                        tvTipPerPerson.setText("$0.00");
                        break;
                    }
                    case R.id.button0:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "0");
                        break;
                    }
                    case R.id.button1:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "1");
                        break;
                    }
                    case R.id.button2:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "2");
                        break;
                    }
                    case R.id.button3:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "3");
                        break;
                    }
                    case R.id.button4:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "4");
                        break;
                    }
                    case R.id.button5:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "5");
                        break;
                    }
                    case R.id.button6:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "6");
                        break;
                    }
                    case R.id.button7:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "7");
                        break;
                    }
                    case R.id.button8:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "8");
                        break;
                    }
                    case R.id.button9:{
                        txtTotalBill.setText(txtTotalBill.getText().toString() + "9");
                        break;
                    }
                }
            }
        };

        Button decimalPoint = (Button) (findViewById(R.id.dp));
        decimalPoint.setOnClickListener(listener);
        Button clear = (Button) (findViewById(R.id.clear));
        clear.setOnClickListener(listener);
        Button button0 = (Button) (findViewById(R.id.button0));
        button0.setOnClickListener(listener);
        Button button1 = (Button) (findViewById(R.id.button1));
        button1.setOnClickListener(listener);
        Button button2 = (Button) (findViewById(R.id.button2));
        button2.setOnClickListener(listener);
        Button button9 = (Button) (findViewById(R.id.button9));
        button9.setOnClickListener(listener);
        Button button3 = (Button) (findViewById(R.id.button3));
        button3.setOnClickListener(listener);
        Button button4 = (Button) (findViewById(R.id.button4));
        button4.setOnClickListener(listener);
        Button button5 = (Button) (findViewById(R.id.button5));
        button5.setOnClickListener(listener);
        Button button6 = (Button) (findViewById(R.id.button6));
        button6.setOnClickListener(listener);
        Button button7 = (Button) (findViewById(R.id.button7));
        button7.setOnClickListener(listener);
        Button button8 = (Button) (findViewById(R.id.button8));
        button8.setOnClickListener(listener);

        // seekBar for tip percentage change event
        sbTip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // log tip percentage to screen when bar is moved
                String tipPercentage = Integer.toString(progress) + "%";
                tvTip.setText(tipPercentage);

                processTotalAndTip(progress, txtTotalBill, tvTotalTip, tp, tvTotalToPay);
                processPerPerson(sbSplit.getProgress(), tp, tvPerPerson,tvTipPerPerson, sbTip, txtTotalBill);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // seekBar for split number of poeple
        sbSplit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String splitNumber = Integer.toString(progress);
                tvSplit.setText(splitNumber);

                processPerPerson(progress, tp, tvPerPerson, tvTipPerPerson, sbTip, txtTotalBill);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txtTotalBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processTotalAndTip(sbTip.getProgress(), txtTotalBill, tvTotalTip, tp, tvTotalToPay);
                processPerPerson(sbSplit.getProgress(), tp, tvPerPerson, tvTipPerPerson, sbTip,txtTotalBill);
            }
        });
    }

    public void processTotalAndTip(int progress , EditText txtTotalBill, TextView tvTotalTip, TipCalculator tp, TextView tvTotalToPay){
        if (txtTotalBill.getText().toString().length() < 1) {
            return;
        }

        double totalToPay = Double.parseDouble(txtTotalBill.getText().toString());
        double tip = tp.getTip(progress, totalToPay);

        tvTotalTip.setText("$" + (String.format("%.2f", tip)));

        totalToPay = tp.getTotalToPay(progress, totalToPay);
        total = totalToPay;

        tvTotalToPay.setText("$" + String.format("%.2f", totalToPay));

    }

    public void processPerPerson(int progress, TipCalculator tp, TextView tvPerPerson, TextView tvTipPerPerson, SeekBar sbTip, EditText txtTotalBill){
        if (txtTotalBill.getText().toString().length() < 1) {
            return;
        }

        double perPerson = tp.getPerPerson(progress, total);
        tvPerPerson.setText("$"+String.format("%.2f",perPerson));

        double totalToPay = Double.parseDouble(txtTotalBill.getText().toString());
        double tip = tp.getTip(sbTip.getProgress(),totalToPay);

        double tipSplit = tp.getTipPerPerson(progress,tip);
        Log.i("MainActivity",Double.toString(tipSplit));
        tvTipPerPerson.setText("$"+String.format("%.2f",tipSplit));
    }

}
