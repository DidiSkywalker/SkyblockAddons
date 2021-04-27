package codes.biscuit.skyblockaddons.api.models;

import codes.biscuit.skyblockaddons.api.SkyblockAPI;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * Represents how a profile is listed in short form for the {@link SkyblockAPI#getProfileList(String)} request.
 */
@Getter @SuppressWarnings("unused")
public class SmallProfile {
    @SerializedName("cute_name")
    private String cuteName;
    @SerializedName("first_join")
    private long firstJoin;
    @SerializedName("last_save")
    private long lastSave;
}
