package tgechev.discoverbulgaria.db.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import java.util.Objects;

@Entity(tableName = "facts", indices = { @Index("region_id")})
public class Fact extends BaseEntity implements Parcelable {

    public String title;

    public String description;

    public Type type;

    @ColumnInfo(name = "image_id")
    public String imageId;

    @ColumnInfo(name = "read_more")
    public String readMore;

    @ColumnInfo(name = "video_id")
    public String videoId;

    @NonNull
    @ColumnInfo(name = "region_id")
    public String regionId;

    public Fact(@NonNull String regionId) {
        this.regionId = regionId;
    }

    protected Fact(Parcel in) {
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
        }
    }

    public static final Creator<Fact> CREATOR = new Creator<Fact>() {
        @Override
        public Fact createFromParcel(Parcel in) {
            return new Fact(in);
        }

        @Override
        public Fact[] newArray(int size) {
            return new Fact[size];
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
        parcel.writeBundle(dataBundle);
    }
}
