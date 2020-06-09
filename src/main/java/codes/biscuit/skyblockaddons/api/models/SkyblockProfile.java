package codes.biscuit.skyblockaddons.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class SkyblockProfile {

    @Getter
    @SerializedName("profile_id")
    private String profileId;
    private Map<String, SkyblockProfileMember> members;

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
