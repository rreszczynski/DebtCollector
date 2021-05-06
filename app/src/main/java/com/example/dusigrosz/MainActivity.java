package com.example.dusigrosz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_DEBTOR_REQUEST = 1;
    public static final int EDIT_DEBTOR_REQUEST = 2;

    private DebtorViewModel debtorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_app_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        ImageButton imageButtonAddDebtor = findViewById(R.id.imageButtonAddDebtor);
        imageButtonAddDebtor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditDebtorActivity.class);
                startActivityForResult(intent, ADD_DEBTOR_REQUEST);

            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final DebtorAdapter adapter = new DebtorAdapter();
        recyclerView.setAdapter(adapter);

        debtorViewModel = ViewModelProviders.of(this).get(DebtorViewModel.class);
        debtorViewModel.getAllDebtors().observe(this, new Observer<List<Debtor>>() {
            @Override
            public void onChanged(List<Debtor> debtors) {
                adapter.setDebtors(debtors);

                int sumDebt = 0;
                for(Debtor debtor : debtors) {
                    sumDebt += debtor.getDebt();
                }

                TextView sumDebtTextView = findViewById(R.id.textView_debt_sum);
                sumDebtTextView.setText("Suma długów: " + sumDebt);
            }
        });

        // po krotkim kliknieciu na element listy
        adapter.setOnItemClickListener(new DebtorAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Debtor debtor) {
                Intent intent = new Intent(MainActivity.this, AddEditDebtorActivity.class);
                intent.putExtra(AddEditDebtorActivity.EXTRA_ID, debtor.getId());
                intent.putExtra(AddEditDebtorActivity.EXTRA_DEBTOR_NAME, debtor.getName());
                intent.putExtra(AddEditDebtorActivity.EXTRA_DEBT, debtor.getDebt());
                startActivityForResult(intent, EDIT_DEBTOR_REQUEST);
            }
        });

        // po dlugim kliknieciu na elemencie listy
        adapter.setOnItemLongClickListener(new DebtorAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(final Debtor debtor) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Skasuj dłużnika")
                        .setMessage("Czy na pewno usunąć dłużnika?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                debtorViewModel.delete(debtor);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_delete)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DEBTOR_REQUEST && resultCode == RESULT_OK) {
            String debtorName = data.getStringExtra(AddEditDebtorActivity.EXTRA_DEBTOR_NAME);
            int debt = data.getIntExtra(AddEditDebtorActivity.EXTRA_DEBT, -1);

            if(debt == -1) {
                Toast.makeText(this, "Błąd przy dodawaniu dłużnika.", Toast.LENGTH_SHORT).show();
                return;
            }

            Debtor debtor = new Debtor(debtorName, debt);
            debtorViewModel.insert(debtor);

            Toast.makeText(this, "Dodano nowego dłużnika.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_DEBTOR_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditDebtorActivity.EXTRA_ID, -1);
            String debtorName = data.getStringExtra(AddEditDebtorActivity.EXTRA_DEBTOR_NAME);
            int debt = data.getIntExtra(AddEditDebtorActivity.EXTRA_DEBT, -1);

            if(id == -1 || debt == -1) {
                Toast.makeText(this, "Błąd przy edycji dłużnika.", Toast.LENGTH_SHORT).show();
                return;
            }

            Debtor debtor = new Debtor(debtorName, debt);
            debtor.setId(id);
            debtorViewModel.update(debtor);
            Toast.makeText(this, "Zapisano zmiany.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
        }
    }
}
