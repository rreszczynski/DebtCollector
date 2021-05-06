package com.example.dusigrosz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SimulatePaymentActivity extends AppCompatActivity {
    private EditText editTextInterestRate;
    private EditText editTextFlatRate;

    private TextView textViewDebtorNameSim;
    private TextView textViewDebtSim;

    private ImageButton imageButtonClose;
    private ImageButton imageButtonStartStop;

    private boolean simulationRunning;
    private Handler simulationHandler;
    private Runnable runnable;

    private int debt;
    private int interestRate;
    private int flatRate;
    private int totalInterestPayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulate_payment);

        setTitle("Symulacja spłaty długu");
        totalInterestPayed = 0;

        editTextInterestRate = findViewById(R.id.editText_interest_rate);
        editTextFlatRate = findViewById(R.id.editText_flat_rate);

        textViewDebtorNameSim = findViewById(R.id.text_view_debtor_name_sim);
        textViewDebtSim = findViewById(R.id.text_view_debt_sim);

        imageButtonClose = findViewById(R.id.imageButton_close);
        imageButtonStartStop = findViewById(R.id.imageButton_start_stop);

        Intent intent = getIntent();
        textViewDebtorNameSim.setText(intent.getStringExtra(AddEditDebtorActivity.EXTRA_DEBTOR_NAME));
        textViewDebtSim.setText(intent.getStringExtra(AddEditDebtorActivity.EXTRA_DEBT));

        // klikniecie w przycisk zakończ
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulationHandler.removeCallbacks(runnable);
                finish();
            }
        });

        // klikniecie w przycisk start/stop symulacji
        imageButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataCorrect() == false) return;

                if (simulationRunning) {
                    stopSimulation();
                } else {
                    startSimulation();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        simulationHandler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void startSimulation() {
        simulationRunning = true;
        imageButtonStartStop.setImageResource(R.drawable.ic_stop);
        simulationHandler = new Handler();

        debt = Integer.parseInt(textViewDebtSim.getText().toString());
        flatRate = Integer.parseInt(editTextFlatRate.getText().toString());
        interestRate = Integer.parseInt(editTextInterestRate.getText().toString());

        runnable = new Runnable() {
            @Override
            public void run() {
                if(simulationRunning) {
                    System.out.println("simulation running");
                    totalInterestPayed += flatRate;
                    debt -= flatRate;
                    if(debt <= 0) {
                        totalInterestPayed += debt;
                        debt = 0;
                        textViewDebtSim.setText(debt+"");
                        stopSimulation();
                    } else {
                        debt = debt + debt * interestRate / 100;
                        textViewDebtSim.setText(debt + "");
                        simulationHandler.postDelayed(this, 1000);
                    }
                }
            }
        };

        simulationHandler.postDelayed(runnable, 1000);
    }

    private void stopSimulation() {
        simulationRunning = false;
        imageButtonStartStop.setImageResource(R.drawable.ic_start);

        new AlertDialog.Builder(SimulatePaymentActivity.this)
                .setTitle("Zapłacone raty")
                .setMessage("Zapłacono: " + totalInterestPayed + " " + getString(R.string.currency_symbol))
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    // sprawdza czy poprawnie wypelniono pola formularza
    private boolean isDataCorrect() {
        String flatRate = editTextFlatRate.getText().toString();
        String interestRate = editTextInterestRate.getText().toString();

        if(flatRate.trim().isEmpty() || interestRate.trim().isEmpty()) {
            Toast.makeText(SimulatePaymentActivity.this, "Niewypełnione pole/pola", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
