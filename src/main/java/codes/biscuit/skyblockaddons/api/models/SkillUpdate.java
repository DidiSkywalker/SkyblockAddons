package codes.biscuit.skyblockaddons.api.models;

import codes.biscuit.skyblockaddons.utils.EnumUtils;
import lombok.Getter;

/**
 * Represents a skill progress message in the action bar that looks something like this
 * <p>{@code +[increase] [skill name] ([current progress]/[max progress])}</p>
 * or in a concrete example:
 * <p>{@code +3.5 Combat (355/1,000)}</p>
 */
@Getter
public class SkillUpdate {

    /**
     * Name like "Combat", "Mining", "Farming", "Foraging", "Alchemy", "Enchanting", "Runecrafting", "Carpentry".
     */
    private final EnumUtils.SkillType skillType;

    /**
     * The current XP of type {@link #skillType}
     */
    private final double currentProgress;

    /**
     * The amount of XP needed to reach the next level
     */
    private final double maxProgress;

    /**
     * The XP that was just earned
     */
    private final double increase;

    /**
     * Timestamp when this update occurred
     */
    private final long timestamp;

    /**
     * Create a new skill update.
     *
     * @param skillType Name of the skill
     * @param currentProgress Current XP of that skill
     * @param maxProgress XP until the next level of that skill
     * @param increase XP that was just earned
     */
    public SkillUpdate(EnumUtils.SkillType skillType, double currentProgress, double maxProgress, double increase) {
        this.skillType = skillType;
        this.currentProgress = currentProgress;
        this.maxProgress = maxProgress;
        this.increase = increase;
        timestamp = System.currentTimeMillis();
    }
}