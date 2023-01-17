package tgechev.discoverbulgaria.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tgechev.discoverbulgaria.db.models.Fact;
import tgechev.discoverbulgaria.db.models.Type;

@Dao
public interface FactDao {
    @Query("SELECT * FROM facts")
    List<Fact> getAll();

    @Query("SELECT * FROM facts WHERE region_id IN (:regionIds) AND type IN (:types)")
    List<Fact> getAllByRegionsAndTypes(String[] regionIds, Type[] types);

    @Query("SELECT * FROM facts WHERE id IN (:factIds)")
    List<Fact> loadAllByIds(int[] factIds);

    @Insert
    void insertAll(Fact... facts);

    @Delete
    void delete(Fact fact);
}
