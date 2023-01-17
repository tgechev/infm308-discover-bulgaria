package tgechev.discoverbulgaria.db;

import androidx.room.TypeConverter;

import tgechev.discoverbulgaria.db.models.Type;

public class Converters {
    @TypeConverter
    public static Type toType(String value) {
            return Type.valueOf(value);
    }

    @TypeConverter
    public static String fromType(Type type) {
        return type.name();
    }
}
