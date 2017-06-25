package com.burakgalisa.android.remembertodothem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.burakgalisa.android.remembertodothem.database.ChoreBaseHelper;
import com.burakgalisa.android.remembertodothem.database.ChoreCursorWrapper;

import com.burakgalisa.android.remembertodothem.database.ChoreDbSchema;




public class ChoreArchive {
    private static ChoreArchive sChoreArchive;


    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ChoreArchive getChoreArchive(Context context){
        if (sChoreArchive == null){
            sChoreArchive = new ChoreArchive(context);
        }
        return sChoreArchive;
    }


    private ChoreArchive(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ChoreBaseHelper(mContext)
                .getWritableDatabase();


    }

    public void addChore(Chore c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(ChoreDbSchema.ChoreTable.NAME, null, values);
    }

    public void deleteChore(Chore c){
        getChores().remove(c);
    }

    public List<Chore> getChores(){
        List<Chore> chores = new ArrayList<>();

        ChoreCursorWrapper cursor = queryChores(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                chores.add(cursor.getChore());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return chores;
    }

    public Chore getChore(UUID id){

        ChoreCursorWrapper cursor = queryChores(
                ChoreDbSchema.ChoreTable.Columns.UUID + " = ?",
                new String[] { id.toString()}
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getChore();
        } finally {
            cursor.close();
        }
    }

    public void updateChore(Chore chore){
        String uuidString = chore.getUUID().toString();
        ContentValues values = getContentValues(chore);

        mDatabase.update(ChoreDbSchema.ChoreTable.NAME, values,
                ChoreDbSchema.ChoreTable.Columns.UUID + " = ?",
                new String[]{ uuidString});
    }

    private static ContentValues getContentValues(Chore chore){
        ContentValues values = new ContentValues();
        values.put(ChoreDbSchema.ChoreTable.Columns.UUID, chore.getUUID().toString());
        values.put(ChoreDbSchema.ChoreTable.Columns.TITLE, chore.getTitle());
        values.put(ChoreDbSchema.ChoreTable.Columns.DATE, chore.getDate().toString());
        values.put(ChoreDbSchema.ChoreTable.Columns.SOLVED, chore.isDone() ? 1 : 0);

        return values;
    }

    private ChoreCursorWrapper queryChores(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ChoreDbSchema.ChoreTable.NAME,
                null, // Columns -- null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new ChoreCursorWrapper(cursor);
    }


}
