package com.example.nate.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.WindowManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.content.pm.ActivityInfo;
import android.util.Log;
/*
    remember to start with an empty activity for drag and drop to work
*/

public class MainActivity extends AppCompatActivity {
    public double total = 0.0;

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

    public void processPerPerson(int progress, TipCalculator tp, TextView tvPerPerson){
        double perPerson = tp.getPerPerson(progress,total);
        tvPerPerson.setText("$"+String.format("%.2f",perPerson));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // prevent switch to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // set focus on total bill field on start
        final EditText txtTotalBill = (EditText) (findViewById(R.id.txtTotalBill));
        txtTotalBill.requestFocus();

        // bring up keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // underline perPersonLabel
        TextView textView = (TextView) (findViewById(R.id.perPersonLabel));
        SpannableString content = new SpannableString("Per Person");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        //load tip calculator controller
        final TipCalculator tp = new TipCalculator();

        final TextView tvTotalTip = (TextView) (findViewById(R.id.tvTotalTip));
        final TextView tvTotalToPay = (TextView) (findViewById(R.id.tvTotalToPay));
        final TextView tvPerPerson = (TextView) (findViewById(R.id.tvPerPerson));
        final TextView tvTip = (TextView) (findViewById(R.id.tvTip));
        final TextView tvSplit = (TextView) (findViewById(R.id.tvSplit));

        final SeekBar sbTip = (SeekBar) (findViewById(R.id.sbTip));
        sbTip.setMax(50);
        sbTip.setProgress(0);

        final SeekBar sbSplit = (SeekBar) (findViewById(R.id.sbSplit));
        sbSplit.setMax(20);
        sbSplit.setProgress(0);

        // seekBar for tip percentage change event
        sbTip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // log tip percentage to screen when bar is moved
                String tipPercentage = Integer.toString(progress) + "%";
                tvTip.setText(tipPercentage);

                processTotalAndTip(progress, txtTotalBill, tvTotalTip, tp, tvTotalToPay);
                processPerPerson(Integer.parseInt(tvSplit.getText().toString()), tp, tvPerPerson);
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

                processPerPerson(progress,tp,tvPerPerson);
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
                processTotalAndTip(sbTip.getProgress(),txtTotalBill,tvTotalTip,tp,tvTotalToPay);
                processPerPerson(sbSplit.getProgress(),tp,tvPerPerson);
            }
        });
    }
}
