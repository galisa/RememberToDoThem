package com.burakgalisa.android.remembertodothem.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.burakgalisa.android.remembertodothem.Chore;

import java.util.Date;
import java.util.UUID;

import com.burakgalisa.android.remembertodothem.database.ChoreDbSchema;

/**
 * Created by Burak on 20.6.2017.
 */

public class ChoreCursorWrapper extends CursorWrapper{
    public ChoreCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Chore getChore(){
        String uuidString = getString(getColumnIndex(ChoreDbSchema.ChoreTable.Columns.UUID));
        String title = getString(getColumnIndex(ChoreDbSchema.ChoreTable.Columns.TITLE));
        long date = getLong(getColumnIndex(ChoreDbSchema.ChoreTable.Columns.DATE));
        int isDone = getInt(getColumnIndex(ChoreDbSchema.ChoreTable.Columns.SOLVED));

        Chore chore = new Chore(UUID.fromString(uuidString));
        chore.setTitle(title);
        chore.setDate(new Date(date));
        chore.setDone(isDone != 0);

        return chore;
    }
}
