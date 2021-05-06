package com.example.dusigrosz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddEditDebtorActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.dusigrosz.EXTRA_ID";
    public static final String EXTRA_DEBTOR_NAME = "com.example.dusigrosz.EXTRA_DEBTOR_NAME";
    public static final String EXTRA_DEBT = "com.example.dusigrosz.EXTRA_DEBT";

    private EditText editTextDebtorName;
    private EditText editTextDebt;

    private ImageButton imageButtonClose;
    private ImageButton imageButtonShare;
    private ImageButton imageButtonSimulate;
    private ImageButton imageButtonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_debtor);

        editTextDebtorName = findViewById(R.id.edit_text_debtor_name);
        editTextDebt = findViewById(R.id.edit_text_debt);

        imageButtonClose = findViewById(R.id.imageButton_close);
        imageButtonShare = findViewById(R.id.imageButton_share);
        imageButtonSimulate = findViewById(R.id.imageButton_simulate);
        imageButtonSave = findViewById(R.id.imageButton_save);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edytuj dłużnika");
            editTextDebtorName.setText(intent.getStringExtra(EXTRA_DEBTOR_NAME));
            editTextDebt.setText(intent.getIntExtra(EXTRA_DEBT, -1)+"");
        } else {
            setTitle("Dodaj dłużnika");
        }

        // po kliknięciu w przycisk anuluj
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(EXTRA_ID)) {
                    new AlertDialog.Builder(AddEditDebtorActivity.this)
                            .setTitle("Anuluj zmiany")
                            .setMessage("Czy na pewno chcesz anulować?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(RESULT_CANCELED);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_delete)
                            .show();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });

        // po kliknięciu w przycisk udostepnij
        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataCorrect() == false) return;

                String textToSend = editTextDebtorName.getText().toString() + ": "
                        + editTextDebt.getText().toString() + " " + getString(R.string.currency_symbol);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, textToSend);

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                };
            }
        });

        // po kliknięciu w przycisk symulacji spłaty
        imageButtonSimulate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isDataCorrect() == false) return;

                Intent intent = new Intent(AddEditDebtorActivity.this, SimulatePaymentActivity.class);
                intent.putExtra(AddEditDebtorActivity.EXTRA_DEBTOR_NAME, editTextDebtorName.getText().toString());
                intent.putExtra(AddEditDebtorActivity.EXTRA_DEBT, editTextDebt.getText().toString());
                startActivity(intent);
            }
        });

        // po kliknięciu w przycisj zapisz
        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataCorrect() == false) return;

                String debtorName = editTextDebtorName.getText().toString();
                String debtString = editTextDebt.getText().toString();

                int debt = Integer.parseInt(debtString);

                Intent data = new Intent();
                data.putExtra(EXTRA_DEBTOR_NAME, debtorName);
                data.putExtra(EXTRA_DEBT, debt);

                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) { // jezeli edycja uzytkownika
                    data.putExtra(EXTRA_ID, id);
                }
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    // sprawdza czy poprawnie wypelniono pola formularza
    private boolean isDataCorrect() {
        String debtorName = editTextDebtorName.getText().toString();
        String debtString = editTextDebt.getText().toString();

        if(debtorName.trim().isEmpty() || debtString.trim().isEmpty()) {
            Toast.makeText(AddEditDebtorActivity.this, "Niepoprawnie wypełnione pola", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
