package br.ufpe.cin.if710.podcast.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by isaacdouglas1 on 13/12/2017.
 */


@Database(entities = {ItemFeed.class}, version = 1)
public abstract class BaseDados extends RoomDatabase {
    private static BaseDados INSTANCE;

    public static BaseDados getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BaseDados.class,"podcast").build();
        }
        return INSTANCE;
    }

    public abstract ItemFeedDaoRoom itemFeedDaoRoom();
}


