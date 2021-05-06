package com.example.dusigrosz;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Debtor.class}, version = 1)
public abstract class DebtorDatabase extends RoomDatabase {

    private static DebtorDatabase instance;

    public abstract DebtorDao debtorDao();

    public static synchronized DebtorDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DebtorDatabase.class, "debtor_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DebtorDao debtorDao;

        private PopulateDbAsyncTask(DebtorDatabase db) {
            debtorDao = db.debtorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            debtorDao.insert(new Debtor("Rysiek", 1000));
            debtorDao.insert(new Debtor("Magda", 500));
            debtorDao.insert(new Debtor("Leszek", 750));
            debtorDao.insert(new Debtor("Marta", 600));
            return null;
        }
    }

}
