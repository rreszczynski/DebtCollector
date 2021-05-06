package com.example.dusigrosz;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DebtorRepository {
    private DebtorDao debtorDao;
    private LiveData<List<Debtor>> allDebtors;

    public DebtorRepository(Application application) {
        DebtorDatabase database = DebtorDatabase.getInstance(application);
        debtorDao = database.debtorDao();
        allDebtors = debtorDao.getAllDebtors();
    }

    public void insert(Debtor debtor) {
        new InsertDebtorAsyncTask(debtorDao).execute(debtor);
    }

    public void update(Debtor debtor) {
        new UpdateDebtorAsyncTask(debtorDao).execute(debtor);
    }

    public void delete(Debtor debtor) {
        new DeleteDebtorAsyncTask(debtorDao).execute(debtor);
    }

    public LiveData<List<Debtor>> getAllDebtors() {
        return allDebtors;
    }

    private static class InsertDebtorAsyncTask extends AsyncTask<Debtor, Void, Void> {
        private DebtorDao debtorDao;

        private InsertDebtorAsyncTask(DebtorDao debtorDao) {
            this.debtorDao = debtorDao;
        }

        @Override
        protected Void doInBackground(Debtor... debtors) {
            debtorDao.insert(debtors[0]);
            return null;
        }
    }

    private static class UpdateDebtorAsyncTask extends AsyncTask<Debtor, Void, Void> {
        private DebtorDao debtorDao;

        private UpdateDebtorAsyncTask(DebtorDao debtorDao) {
            this.debtorDao = debtorDao;
        }

        @Override
        protected Void doInBackground(Debtor... debtors) {
            debtorDao.update(debtors[0]);
            return null;
        }
    }

    private static class DeleteDebtorAsyncTask extends AsyncTask<Debtor, Void, Void> {
        private DebtorDao debtorDao;

        private DeleteDebtorAsyncTask(DebtorDao debtorDao) {
            this.debtorDao = debtorDao;
        }

        @Override
        protected Void doInBackground(Debtor... debtors) {
            debtorDao.delete(debtors[0]);
            return null;
        }
    }
}
