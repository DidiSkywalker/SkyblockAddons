package codes.biscuit.skyblockaddons.api.models;

import codes.biscuit.skyblockaddons.utils.EnumUtils;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraftforge.fml.common.FMLLog;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SkyblockProfileMember {

    /**
     * Unix timestamp when the profile was last saved
     */
    @SerializedName("last_save")
    private long lastSave;

    /**
     * Unix timestamp when the player joined the profile
     */
    @SerializedName("first_join")
    private long firstJoin;

    /**
     * Amount of coins in the players purse
     */
    @SerializedName("coin_purse")
    private int purse;

    /**
     * The players inventory.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     *
     * May be null if disabled in the API settings.
     */
    @Nullable private List<SkyblockItem> inventory;

    /**
     * The players armor.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     */
    private List<SkyblockItem> armor;

    /**
     * The wardrobe inventory.
     * Items listed go left to right, up to down, meaning it will first list all helmets, then all chestplates
     * and so on.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     *
     * May be null if disabled in the API settings.
     */
    @Nullable private List<SkyblockItem> wardrobe;

    /**
     * The players enderchest.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     *
     * May be null if disabled in the API settings.
     */
    @Nullable private List<SkyblockItem> enderchest;

    /**
     * The players potion bag.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     *
     * May be null if disabled in the API settings.
     */
    @SerializedName("potion_bag")
    @Nullable private List<SkyblockItem> potionBag;

    /**
     * The players talisman bag.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     *
     * May be null if disabled in the API settings.
     */
    @SerializedName("talisman_bag")
    @Nullable private List<SkyblockItem> talismanBag;

    /**
     * The players quiver.
     * Empty slots are represented by empty SkyblockItems ({@link SkyblockItem#isEmpty()}).
     *
     * May be null if disabled in the API settings.
     */
    @Nullable private List<SkyblockItem> quiver;

    /**
     * Total amount of fairy souls the player has collected
     */
    @SerializedName("fairy_souls_collected")
    private int fairySoulsCollected;

    /**
     * Currently unexchanged fairy souls
     */
    @SerializedName("fairy_souls")
    private int fairySouls;

    /**
     * Amount of times the player exchanged 5 fairy souls for stats
     */
    @SerializedName("fairy_exchanges")
    private int fairyExchanges;

    /**
     * List of pets the player has in their pet menu
     */
    private List<Pet> pets;

    /**
     * Maps skill type (lower case) to skill information like level and experience.
     * @see SkillInfo
     */
    @Nullable private Map<String, SkillInfo> skills;

    /**
     * Maps COLLECTION TYPE (upper case) to amount of items in the collection
     */
    private Map<String, Integer> collection;

    /**
     * Maps COLLECTION TYPE (upper case) to the unlocked tier of that collection
     */
    @SerializedName("collection_tiers")
    private Map<String, Integer> collectionTiers;

    /**
     * Amount of collections the player has unlocked
     */
    @SerializedName("collections_unlocked")
    private int collectionsUnlocked;

    /**
     * Maps MINION TYPE (upper case) to the highest level the player has crafted of that type
     */
    private Map<String, Integer> minions;

    private SkyblockStats stats;
    // TODO: slayer information

    public int getSkillLevel(EnumUtils.SkillType skillType) {
        SkillInfo skillInfo = skills.get(skillType.name().toLowerCase());
        if(skillInfo != null) {
            return skillInfo.level;
        } else {
            return -1;
        }
    }

    public void onSkillExp(EnumUtils.SkillType skillType, double added, double newExp) {
        if(skills == null) {
            skills = new HashMap<>();
        }
        SkillInfo skillInfo = skills.get(skillType.name().toLowerCase());
        if (skillInfo != null) {
            if (skillInfo.xpCurrent != newExp) {
                double diff = newExp - skillInfo.xpCurrent;
                FMLLog.info("%s: %f -> %f (+%f / %f)", skillType, skillInfo.xpCurrent, newExp, diff, added);
                if (diff > 0 && skillType != EnumUtils.SkillType.CARPENTRY && skillType != EnumUtils.SkillType.RUNECRAFTING) {
                    // Carpentry and Runecrafting don't grant pet exp
                    final Pet activePet = getActivePet();
                    if (activePet != null) {
                        getActivePet().addExp(skillType, diff);
                    }
                }
                skillInfo.xpCurrent = newExp;
            }
        } else {
            skills.put(skillType.name().toLowerCase(), new SkillInfo(String.valueOf(newExp), 0));
        }
    }

    public Pet getActivePet() {
        for (Pet pet : pets) {
            if(pet.isActive()) {
                return pet;
            }
        }
        return null;
    }

    public void setActivePet(Pet activePet) {
        for (Pet pet : pets) {
            pet.setActive(pet.equals(activePet));
        }
    }

    /**
     * Get how many arrows are in the quiver in total.
     *
     * @return Amount of arrows or -1 if quiver API is disabled
     */
    public int getArrowsInQuiver() {
        if(quiver != null) {
            int arrows = 0;
            for (SkyblockItem skyblockItem : quiver) {
                if("ARROW".equals(skyblockItem.getAttributes().getId())) {
                    arrows += skyblockItem.getCount();
                }
            }
            return arrows;
        }
        return -1;
    }

    @Getter
    public static class SkillInfo {
        private static final SkillInfo BLANK = new SkillInfo("0", 0);

        private final String xp;
        private final int level;
        private int maxLevel;
        private double xpCurrent;
        private double xpForNext;
        private String progress;

        public SkillInfo(String xp, int level) {
            this.xp = xp;
            this.level = level;
        }

        @Override
        public String toString() {
            return "SkillInfo{" +
                    "xp='" + xp + '\'' +
                    ", level=" + level +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SkyblockProfileMember{" +
                "purse=" + purse +
                ", inventory=" + inventory +
                ", fairySoulsCollected=" + fairySoulsCollected +
                ", fairySouls=" + fairySouls +
                ", fairyExchanges=" + fairyExchanges +
                ", pets=" + pets +
                ", skills=" + skills +
                ", collection=" + collection +
                ", collectionTiers=" + collectionTiers +
                ", collectionsUnlocked=" + collectionsUnlocked +
                ", minions=" + minions +
                '}';
    }
}
