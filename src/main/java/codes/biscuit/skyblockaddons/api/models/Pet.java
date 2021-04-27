package codes.biscuit.skyblockaddons.api.models;

import codes.biscuit.skyblockaddons.api.SkyblockAPI;
import codes.biscuit.skyblockaddons.constants.game.Rarity;
import codes.biscuit.skyblockaddons.utils.EnumUtils;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static codes.biscuit.skyblockaddons.utils.EnumUtils.SkillType.*;

/*
§r§aYou despawned your §r§6Wolf§r§a!§r
§r§aYou summoned your §r§9Ocelot§r§a!§r
§r§aSuccessfully added §r§9Blue Whale §r§ato your pet menu!§r
§r§aYum! Your pet ate §r§aSimple Carrot Candy §r§afor 20000 exp (1/10)!§r
 */
public class Pet {

    @Getter
    private final Type type;
    @Getter
    private double exp;
    @Getter @Setter
    private boolean active;
    @Getter @SerializedName("tier")
    private final Rarity rarity;
    @Getter @SerializedName("heldItem")
    private String item;

    public Pet(final String type, final double exp, final Rarity rarity, final String item, final boolean active) {
        this.type = Type.valueOf(type);
        this.exp = exp;
        this.rarity = rarity;
        this.active = active;
        this.item = item;
    }

    public ExpProgress getExpProgress() {
        // from https://github.com/LeaPhant/skyblock-stats/blob/master/src/lib.js
        final int rarityOffset = PET_RARITY_OFFSET.get(rarity);
        final List<Integer> levels = PET_LEVELS.subList(rarityOffset, Math.min(rarityOffset+99, PET_LEVELS.size()));

        double xpTotal = 0;
        int level = 1;

        double xpForNext;

        for(int i = 0; i < 100; i++){
            xpTotal += levels.get(i);

            if(xpTotal > this.exp){
                xpTotal -= levels.get(i);
                break;
            } else{
                level++;
            }
        }

        double xpCurrent = Math.floor(this.exp - xpTotal);
        double progress;

        if(level < 100){
            xpForNext = Math.ceil(levels.get(level -1));
            progress = Math.max(0, Math.min(xpCurrent / xpForNext, 1));
        }else{
            level = 100;
            xpCurrent = this.exp - levels.get(99);
            xpForNext = 0;
            progress = 1;
        }

        return new ExpProgress(
                level,
                xpCurrent,
                xpForNext,
                progress * 100
        );
    }

    public String getName() {
        return String.format("§%s%s", rarity.getColor(), type.getFriendlyName());
    }

    public void addExp(final EnumUtils.SkillType skillType, final double exp) {
        final int tamingLevel = SkyblockAPI.getActiveProfile().getLocalMember().getSkillLevel(TAMING);
        double skillTypeModifier;
        if(skillType == type.getSkillType()) {
            skillTypeModifier = 1;
        } else {
            if(skillType == ALCHEMY) {
                skillTypeModifier = 1d/12d;
            } else {
                skillTypeModifier = 0.25;
            }
        }

        double tamingModifier = 1 + (tamingLevel * 0.01);
        double itemModifier = 1;
        try {
            SkillBoostItem skillBoostItem = SkillBoostItem.valueOf(item);
            if(skillBoostItem.skillType == skillType) {
                itemModifier = 1 + skillBoostItem.expBoost;
                FMLLog.info("Skill Boost Item: %f%%", skillBoostItem.expBoost*100);
            }
        } catch (IllegalArgumentException ignored) { }

        final double totalModifier = skillTypeModifier * tamingModifier * itemModifier;
        final double effectiveExp = exp * (Math.round(totalModifier * 100.0) / 100.0);

        FMLLog.info("Earned %f pet exp from %f %s exp (%f modifier)", effectiveExp, exp, skillType.name(), totalModifier);
        FMLLog.info("PET: %f -> %f (+%f)", this.exp, this.exp+effectiveExp, effectiveExp);
        this.exp += effectiveExp;
    }

    @Getter
    public static class ExpProgress {
        private final int level;
        private final double xpCurrent;
        private final double xpForNext;
        private final double progress;

        public ExpProgress(int level, double xpCurrent, double xpForNext, double progress) {
            this.level = level;
            this.xpCurrent = xpCurrent;
            this.xpForNext = xpForNext;
            this.progress = progress;
        }
    }

    @Getter
    public enum Type {
        BAT(MINING, "Bat"),
        BLAZE(COMBAT, "Blaze"),
        CHICKEN(FARMING, "Chicken"),
        HORSE(COMBAT, "Horse"),
        JERRY(COMBAT, "Jerry"),
        OCELOT(FORAGING, "Ocelot"),
        PIGMAN(COMBAT, "Pigman"),
        RABBIT(FARMING, "Rabbit"),
        SHEEP(ALCHEMY, "Sheep"),
        SILVERFISH(MINING, "Silverfish"),
        WITHER_SKELETON(MINING, "Wither Skeleton"),
        SKELETON_HORSE(COMBAT, "Skeleton Horse"),
        WOLF(COMBAT, "Wolf"),
        ENDERMAN(COMBAT, "Enderman"),
        PHOENIX(COMBAT, "Phoenix"),
        MAGMA_CUBE(COMBAT, "Magma Cube"),
        FLYING_FISH(FISHING, "Flying Fish"),
        BLUE_WHALE(FISHING, "Blue Whale"),
        TIGER(COMBAT, "Tiger"),
        LION(FORAGING, "Lion"),
        PARROT(ALCHEMY, "Parrot"),
        SNOWMAN(COMBAT, "Snowman"),
        TURTLE(COMBAT, "Turtle"),
        BEE(FARMING, "Bee"),
        ENDER_DRAGON(COMBAT, "Ender Dragon"),
        SQUID(FISHING, "Squid"),
        GIRAFFE(FORAGING, "Giraffe"),
        ELEPHANT(FARMING, "Elephant"),
        MONKEY(FORAGING, "Monkey"),
        SPIDER(COMBAT, "Spider"),
        ENDERMITE(MINING, "Endermite"),
        GHOUL(COMBAT, "Ghoul"),
        JELLYFISH(ALCHEMY, "Jellyfish"),
        PIG(FARMING, "Pig"),
        ROCK(MINING, "Rock"),
        SKELETON(COMBAT, "Skeleton"),
        ZOMBIE(COMBAT, "Zombie"),
        DOLPHIN(FISHING, "Dolphin"),
        BABY_YETI(FISHING, "Baby Yeti"),
        GOLEM(COMBAT, "Golem"),
        HOUND(COMBAT, "Hound"),
        TARANTULA(COMBAT, "Tarantula"),
        BLACK_CAT(COMBAT, "Black Cat");

        private final EnumUtils.SkillType skillType;
        private final String friendlyName;
        Type(final EnumUtils.SkillType skillType, final String friendlyName) {
            this.skillType = skillType;
            this.friendlyName = friendlyName;
        }

        public ResourceLocation getIcon() {
            return new ResourceLocation("skyblockaddons", "pets/"+name()+".png");
        }
    }

    public enum SkillBoostItem {
        PET_ITEM_ALL_SKILLS_BOOST_COMMON(null, 0.1),
        PET_ITEM_COMBAT_SKILL_BOOST_COMMON(COMBAT, 0.2),
        PET_ITEM_COMBAT_SKILL_BOOST_UNCOMMON(COMBAT, 0.3),
        PET_ITEM_COMBAT_SKILL_BOOST_RARE(COMBAT, 0.4),
        PET_ITEM_COMBAT_SKILL_BOOST_EPIC(COMBAT, 0.5),
        PET_ITEM_FISHING_SKILL_BOOST_COMMON(FISHING, 0.2),
        PET_ITEM_FISHING_SKILL_BOOST_UNCOMMON(FISHING, 0.3),
        PET_ITEM_FISHING_SKILL_BOOST_RARE(FISHING, 0.4),
        PET_ITEM_FISHING_SKILL_BOOST_EPIC(FISHING, 0.5),
        PET_ITEM_FORAGING_SKILL_BOOST_COMMON(FORAGING, 0.2),
        PET_ITEM_FORAGING_SKILL_BOOST_EPIC(FORAGING, 0.5),
        PET_ITEM_MINING_SKILL_BOOST_COMMON(MINING, 0.2),
        PET_ITEM_MINING_SKILL_BOOST_RARE(MINING, 0.4),
        PET_ITEM_FARMING_SKILL_BOOST_COMMON(FARMING, 0.2),
        PET_ITEM_FARMING_SKILL_BOOST_RARE(FARMING, 0.4);

        final EnumUtils.SkillType skillType;
        final double expBoost;
        SkillBoostItem(final EnumUtils.SkillType skillType, final double expBoost) {
            this.skillType = skillType;
            this.expBoost = expBoost;
        }
    }

    private static final Map<Rarity, Integer> PET_RARITY_OFFSET = new HashMap<Rarity, Integer>() {{
       put(Rarity.COMMON, 0);
       put(Rarity.UNCOMMON, 6);
       put(Rarity.RARE, 11);
       put(Rarity.EPIC, 16);
       put(Rarity.LEGENDARY, 20);
    }};

    private static final List<Integer> PET_LEVELS = Arrays.asList(
            100,
            110,
            120,
            130,
            145,
            160,
            175,
            190,
            210,
            230,
            250,
            275,
            300,
            330,
            360,
            400,
            440,
            490,
            540,
            600,
            660,
            730,
            800,
            880,
            960,
            1050,
            1150,
            1260,
            1380,
            1510,
            1650,
            1800,
            1960,
            2130,
            2310,
            2500,
            2700,
            2920,
            3160,
            3420,
            3700,
            4000,
            4350,
            4750,
            5200,
            5700,
            6300,
            7000,
            7800,
            8700,
            9700,
            10800,
            12000,
            13300,
            14700,
            16200,
            17800,
            19500,
            21300,
            23200,
            25200,
            27400,
            29800,
            32400,
            35200,
            38200,
            41400,
            44800,
            48400,
            52200,
            56200,
            60400,
            64800,
            69400,
            74200,
            79200,
            84700,
            90700,
            97200,
            104200,
            111700,
            119700,
            128200,
            137200,
            146700,
            156700,
            167700,
            179700,
            192700,
            206700,
            221700,
            237700,
            254700,
            272700,
            291700,
            311700,
            333700,
            357700,
            383700,
            411700,
            441700,
            476700,
            516700,
            561700,
            611700,
            666700,
            726700,
            791700,
            861700,
            936700,
            1016700,
            1101700,
            1191700,
            1286700,
            1386700,
            1496700,
            1616700,
            1746700,
            1886700
    );

    @Override
    public String toString() {
        return "Pet{" +
                "type=" + type +
                ", exp=" + exp +
                ", active=" + active +
                ", rarity=" + rarity +
                ", item='" + item + '\'' +
                '}';
    }
}
