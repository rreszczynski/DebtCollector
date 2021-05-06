package com.example.dusigrosz;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DebtorDao {

    @Insert
    void insert(Debtor debtor);

    @Update
    void update(Debtor Debtor);

    @Delete
    void delete(Debtor debtor);

    @Query("SELECT * FROM debtor_table ORDER BY debt DESC")
    LiveData<List<Debtor>> getAllDebtors();


    // uzywany w ContentProviderze
    @Query("SELECT * FROM " + Debtor.TABLE_NAME)
    Cursor selectAll();
}
