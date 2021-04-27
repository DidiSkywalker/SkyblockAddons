package codes.biscuit.skyblockaddons.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A Skyblock Profile as provided by the Slothpixel API.
 */
@Getter @SuppressWarnings("unused")
public class SkyblockProfile {

    /**
     * The unique profile ID
     */
    @SerializedName("profile_id")
    private String profileId;

    /**
     * Cute profile name (eg Apple, Kiwi, etc)
     */
    @Setter
    private String cuteName;

    /**
     * Map of player uuids and {@link SkyblockProfileMember} objects for all profile members if it's a coop.
     * If the profile is not coop this Map only contains the local player.
     */
    private Map<String, SkyblockProfileMember> members;

    /**
     * The profiles banking information like balance and transactions.
     *
     * May be null if banking API is disabled.
     */
    @Nullable private SkyblockBanking banking;


    /**
     * @return The member object for the local player
     */
    public SkyblockProfileMember getLocalMember() {
        return members.get(Minecraft.getMinecraft().getSession().getPlayerID());
    }

    @Override
    public String toString() {
        return "SkyblockProfile{" +
                "profileId='" + profileId + '\'' +
                ", members=" + members +
                '}';
    }
}
