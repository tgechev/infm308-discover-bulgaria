package tgechev.discoverbulgaria.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import tgechev.discoverbulgaria.db.dao.FactDao;
import tgechev.discoverbulgaria.db.dao.PoiDao;
import tgechev.discoverbulgaria.db.dao.RegionDao;
import tgechev.discoverbulgaria.db.models.Fact;
import tgechev.discoverbulgaria.db.models.Poi;
import tgechev.discoverbulgaria.db.models.Region;

@Database(entities = {Region.class, Poi.class, Fact.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RegionDao regionDao();
    public abstract PoiDao poiDao();
    public abstract FactDao factDao();
}
