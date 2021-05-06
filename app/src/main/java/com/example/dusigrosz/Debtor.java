package com.example.dusigrosz;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Debtor.TABLE_NAME)
public class Debtor {
    public static final String TABLE_NAME = "debtor_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEBT = "debt";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    private int id;

    @NonNull
    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @NonNull
    @ColumnInfo(name = COLUMN_DEBT)
    private int debt;

    public Debtor(@NonNull String name, int debt) {
        this.name = name;
        this.debt = debt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getDebt() {
        return debt;
    }
}
