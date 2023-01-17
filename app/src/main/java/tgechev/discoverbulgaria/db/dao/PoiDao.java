package tgechev.discoverbulgaria.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tgechev.discoverbulgaria.db.models.Poi;
import tgechev.discoverbulgaria.db.models.Type;

@Dao
public interface PoiDao {
    @Query("SELECT * FROM poi")
    List<Poi> getAll();

    @Query("SELECT * FROM poi WHERE region_id IN (:regionIds) AND type IN (:types)")
    List<Poi> getAllByRegionsAndTypes(String[] regionIds, Type[] types);

    @Query("SELECT * FROM poi WHERE id IN (:poiIds)")
    List<Poi> loadAllByIds(int[] poiIds);

    @Insert
    void insertAll(Poi... pois);

    @Delete
    void delete(Poi poi);
}
