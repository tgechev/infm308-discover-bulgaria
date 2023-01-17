package tgechev.discoverbulgaria.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tgechev.discoverbulgaria.db.models.Region;

@Dao
public interface RegionDao {
    @Query("SELECT * FROM regions")
    List<Region> getAll();

    @Query("SELECT * FROM regions WHERE region_id = :regionId")
    Region findByRegionId(String regionId);

    @Query("SELECT * FROM regions WHERE id IN (:regionIds)")
    List<Region> loadAllByIds(int[] regionIds);

    @Insert
    void insertAll(Region... regions);

    @Delete
    void delete(Region region);
}
