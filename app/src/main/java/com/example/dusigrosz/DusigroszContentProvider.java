package com.example.dusigrosz;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.PrimaryKey;

import java.util.List;

public class DusigroszContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.dusigrosz";

    // URI dla tabeli dłużników
    public static final Uri URI_DEBTOR = Uri.parse("content://" + AUTHORITY + "/" + Debtor.TABLE_NAME);

    // dla elementów tabeli
    private static final int CODE_DEBTOR_DIR = 1;

    // dla jedengo elementu tabeli
    private static final int CODE_DEBTOR_ITEM = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_DEBTOR_DIR) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            DebtorDao debtorDao = DebtorDatabase.getInstance(context).debtorDao();
            final Cursor cursor;
            if (code == CODE_DEBTOR_DIR) {
                cursor = debtorDao.selectAll();
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            }
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_DEBTOR_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Debtor.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_DEBTOR_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }

                if(values != null && values.containsKey(Debtor.COLUMN_NAME) && values.containsKey(Debtor.COLUMN_DEBT)) {
                    String name = values.getAsString(Debtor.COLUMN_NAME);
                    int debt = values.getAsInteger(Debtor.COLUMN_DEBT);
                    Debtor debtor = new Debtor(name, debt);

                    DebtorDatabase.getInstance(context).debtorDao().insert(debtor);

                    context.getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, debtor.getId());

                } else {
                    throw new IllegalArgumentException("Insert failed: " + uri);
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DEBTOR_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                DebtorDao debtorDao = DebtorDatabase.getInstance(context).debtorDao();
                List<Debtor> debtors = debtorDao.getAllDebtors().getValue();
                for(Debtor debtor : debtors) {
                    if (debtor.getId() == ContentUris.parseId(uri)) {
                        debtorDao.delete(debtor);
                        context.getContentResolver().notifyChange(uri, null);
                        return 1;
                    }
                }

                return 0;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DEBTOR_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                if(values != null && values.containsKey(Debtor.COLUMN_NAME) && values.containsKey(Debtor.COLUMN_DEBT)) {
                    String name = values.getAsString(Debtor.COLUMN_NAME);
                    int debt = values.getAsInteger(Debtor.COLUMN_DEBT);
                    int id = values.getAsInteger(Debtor.COLUMN_ID);

                    Debtor debtor = new Debtor(name, debt);
                    debtor.setId(id);

                    DebtorDatabase.getInstance(context).debtorDao().update(debtor);

                    context.getContentResolver().notifyChange(uri, null);
                    return 1;

                } else {
                    throw new IllegalArgumentException("Insert failed: " + uri);
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
