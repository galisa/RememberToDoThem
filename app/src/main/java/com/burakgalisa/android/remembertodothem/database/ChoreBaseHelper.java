package com.burakgalisa.android.remembertodothem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.burakgalisa.android.remembertodothem.database.ChoreDbSchema.ChoreTable;

/**
 * Created by Burak on 9.6.2017.
 */

public class ChoreBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "chore";

    public ChoreBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ChoreTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ChoreTable.Columns.UUID + ", " +
                ChoreTable.Columns.TITLE + ", " +
                ChoreTable.Columns.DATE + ", " +
                ChoreTable.Columns.SOLVED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
