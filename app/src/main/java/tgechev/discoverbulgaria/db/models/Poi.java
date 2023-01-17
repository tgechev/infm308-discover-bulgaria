package tgechev.discoverbulgaria.db.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Objects;

@Entity(tableName = "poi")
public class Poi extends BaseEntity implements Parcelable {
    public String title;

    public String address;

    public String description;

    public Type type;

    @ColumnInfo(name = "image_id")
    public String imageId;

    @ColumnInfo(name = "read_more")
    public String readMore;

    @ColumnInfo(name = "video_id")
    public String videoId;

    @ColumnInfo(name = "region_id")
    @NonNull
    public String regionId;

    @NonNull
    public Double latitude;

    @NonNull
    public Double longitude;

    public Poi(@NonNull String regionId, @NonNull Double latitude, @NonNull Double longitude) {
        this.regionId = regionId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected Poi(Parcel in) {
        Bundle dataBundle = in.readBundle(getClass().getClassLoader());
        if (dataBundle != null) {
            title = dataBundle.getString("title");
            description = dataBundle.getString("description");
            String typeName = dataBundle.getString("type");
            type = Type.valueOf(typeName);
            imageId = dataBundle.getString("imageId");
            readMore = dataBundle.getString("readMore");
            videoId = dataBundle.getString("videoId");
            regionId = Objects.requireNonNull(dataBundle.getString("regionId"));
            address = dataBundle.getString("address");
            longitude = dataBundle.getDouble("longitude");
            latitude = dataBundle.getDouble("latitude");
        }
    }

    public static final Creator<Poi> CREATOR = new Creator<Poi>() {
        @Override
        public Poi createFromParcel(Parcel in) {
            return new Poi(in);
        }

        @Override
        public Poi[] newArray(int size) {
            return new Poi[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        Bundle dataBundle = new Bundle();
        dataBundle.putString("title", title);
        dataBundle.putString("description", description);
        dataBundle.putString("type", type.name());
        dataBundle.putString("imageId", imageId);
        dataBundle.putString("readMore", readMore);
        dataBundle.putString("videoId", videoId);
        dataBundle.putString("regionId", regionId);
        dataBundle.putString("address", address);
        dataBundle.putDouble("longitude", longitude);
        dataBundle.putDouble("latitude", latitude);
        parcel.writeBundle(dataBundle);
    }
}
