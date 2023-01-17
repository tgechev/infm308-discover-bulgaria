package tgechev.discoverbulgaria.db.models;

import androidx.room.PrimaryKey;

public class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
}
