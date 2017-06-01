package com.burakgalisa.android.remembertodothem;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Burak on 21.5.2017.
 */

public class ChoreArchive {
    private static ChoreArchive sChoreArchive;

    private ArrayList<Chore> mChores;

    public static ChoreArchive getChoreArchive(Context context){
        if (sChoreArchive == null){
            sChoreArchive = new ChoreArchive(context);
        }
        return sChoreArchive;
    }


    private ChoreArchive(Context context){
        mChores = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            Chore chore = new Chore();
            chore.setTitle("Chore " + i);
            chore.setDone(i%2 == 0);
            mChores.add(chore);
        }
    }

    public List<Chore> getChores(){
        return mChores;
    }

    public Chore getChore(UUID id){
        for (Chore chore : mChores){
            if (chore.getUUID().equals(id)){
                return chore;
            }
        }
        return null;
    }
}
