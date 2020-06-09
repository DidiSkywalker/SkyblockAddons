package codes.biscuit.skyblockaddons.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class SmallProfile {

    @Getter @Setter @SerializedName("profile_id")
    private String profileId;
    @Getter @Setter @SerializedName("cute_name")
    private String cuteName;
    @Getter @Setter @SerializedName("first_join")
    private long firstJoin;
    @Getter @Setter @SerializedName("last_save")
    private long lastSave;

    public SmallProfile(String profileId, String cuteName, long firstJoin, long lastSave) {
        this.profileId = profileId;
        this.cuteName = cuteName;
        this.firstJoin = firstJoin;
        this.lastSave = lastSave;
    }
}
