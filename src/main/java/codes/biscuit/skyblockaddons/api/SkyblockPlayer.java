package codes.biscuit.skyblockaddons.api;

import codes.biscuit.skyblockaddons.api.models.Pet;
import codes.biscuit.skyblockaddons.api.models.SkillUpdate;
import codes.biscuit.skyblockaddons.api.models.SkyblockProfile;
import codes.biscuit.skyblockaddons.api.models.SkyblockProfileMember;
import codes.biscuit.skyblockaddons.core.Attribute;
import codes.biscuit.skyblockaddons.core.Location;
import codes.biscuit.skyblockaddons.utils.EnumUtils;
import lombok.Getter;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Representation of the players Skyblock profile based on a combination of local and API information.
 */
@Getter
public class SkyblockPlayer {

    /**
     *  Get a player's attributes. This includes health, mana, and defence.
     */
    // local, maybe calculate using API info?
    private Map<Attribute, MutableInt> attributes = new EnumMap<>(Attribute.class);

    // API and local
    private Map<EnumUtils.SkillType, SkyblockProfileMember.SkillInfo> skills;

    // local
    private SkillUpdate lastSkillUpdate;

    /**
     * The players currently active pet.
     * Can be null if no pet is selected or the pet API is disabled.
     */
    // API and local
    private @Nullable Pet activePet;

    // API and local
    private SkyblockProfile skyblockProfile;

    // local
    private Location location;

    // local
    private String serverId;

    // local
    private double purse;

    // local
    private int jerryWave = -1;

    // local
    private int lastHoveredSlot = -1;

    // local
    private boolean inAlpha;

    // local
    private boolean inDungeon;

    public void updateWithApiData(final SkyblockProfile skyblockProfile) {

    }

    public void setLastSkillUpdate(final SkillUpdate skillUpdate) {
        this.lastSkillUpdate = skillUpdate;
//        this.skills.get(skillUpdate.getSkillType()).
    }

}
