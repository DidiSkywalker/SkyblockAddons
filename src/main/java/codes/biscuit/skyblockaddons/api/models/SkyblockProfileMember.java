package codes.biscuit.skyblockaddons.api.models;

import codes.biscuit.skyblockaddons.utils.EnumUtils;
import lombok.Getter;
import net.minecraftforge.fml.common.FMLLog;

import java.util.List;
import java.util.Map;

public class SkyblockProfileMember {

    @Getter
    private List<Pet> pets;
    private Map<String, SkillInfo> skills;

    public int getSkillLevel(EnumUtils.SkillType skillType) {
        SkillInfo skillInfo = skills.get(skillType.name().toLowerCase());
        if(skillInfo != null) {
            return skillInfo.level;
        } else {
            return -1;
        }
    }

    public void onSkillExp(EnumUtils.SkillType skillType, double added, double newExp) {
        SkillInfo skillInfo = skills.get(skillType.name().toLowerCase());
        if(skillInfo != null) {
            if(skillInfo.xpCurrent != newExp) {
                double diff = newExp - skillInfo.xpCurrent;
                FMLLog.info("%s: %f -> %f (+%f / %f)", skillType, skillInfo.xpCurrent, newExp, diff, added);
                if(skillType != EnumUtils.SkillType.CARPENTRY && skillType != EnumUtils.SkillType.RUNECRAFTING) {
                    // Carpentry and Runecrafting don't grant pet exp
                    getActivePet().addExp(skillType, diff);
                }
                skillInfo.xpCurrent = newExp;
            }
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

    public static class SkillInfo {
        private static final SkillInfo BLANK = new SkillInfo("0", 0);

        private String xp;
        private int level;
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
                "pets=" + pets +
                ", skills=" + skills +
                '}';
    }
}
