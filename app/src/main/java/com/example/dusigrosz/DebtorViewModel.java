package com.example.dusigrosz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DebtorViewModel extends AndroidViewModel {
    private DebtorRepository repository;
    private LiveData<List<Debtor>> allDebtors;

    public DebtorViewModel(@NonNull Application application) {
        super(application);
        repository = new DebtorRepository(application);
        allDebtors = repository.getAllDebtors();
    }

    public void insert(Debtor debtor) {
        repository.insert(debtor);
    }

    public void update(Debtor debtor) {
        repository.update(debtor);
    }

    public void delete(Debtor debtor) {
        repository.delete(debtor);
    }

    public LiveData<List<Debtor>> getAllDebtors() {
        return allDebtors;
    }
}
