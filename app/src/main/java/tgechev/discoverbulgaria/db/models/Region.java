package tgechev.discoverbulgaria.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "regions", indices = { @Index(value = "region_id", unique = true) })
public class Region extends BaseEntity {

    @ColumnInfo(name = "region_id")
    public String regionId;

    public String name;

    public Integer population;

    public Double area;

    @ColumnInfo(name = "image_id")
    public String imageId;

    // @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    // private Set<Poi> poi;

    // @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    // private Set<Fact> facts;
}
