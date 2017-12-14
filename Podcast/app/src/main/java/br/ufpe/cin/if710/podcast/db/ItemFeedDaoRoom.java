package br.ufpe.cin.if710.podcast.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by isaacdouglas1 on 13/12/2017.
 */

@Dao
public interface ItemFeedDaoRoom {

    @Insert
    void inserir(ItemFeed item);

    @Query("SELECT * FROM ItemFeed")
    LiveData<List<ItemFeed>> todos();

    @Query("SELECT * FROM ItemFeed WHERE id = :id")
    ItemFeed getFromId(int id);

    @Update
    void atualizar(ItemFeed item);

    @Delete
    void remover(ItemFeed item);
}
