package codes.biscuit.skyblockaddons.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * A Skyblock Item as represented in the Slothpixel API. For example in the inventory or enderchest.
 */
@Getter @SuppressWarnings("unused")
public class SkyblockItem {

    /**
     * Minecraft item ID
     */
    @SerializedName("item_id")
    private int itemId;

    /**
     * Amount of items in the stack
     */
    private int count;

    /**
     * Minecraft damage value (does not mean item damage or anything game relevant)
     */
    private int damage;

    /**
     * Formatted display name
     */
    private String name;

    /**
     * List of formatted lore lines
     */
    private List<String> lore;

    /**
     * The items attributes like reforge, enchantments, Hot Potato Books etc.
     */
    private Attributes attributes;

    /**
     * Empty slots in the API are represented as empty objects. Use this method to check whether this item is empty or
     * an actual item.
     *
     * @return Whether this item represents an empty slot
     */
    public boolean isEmpty() {
        return name == null;
    }

    @Override
    public String toString() {
        return isEmpty() ? "SkyblockItem{empty}" : "SkyblockItem{"+name+"}";
    }

    @Getter
    public static class Attributes {
        /**
         * Item modifier (reforge)
         */
        @Nullable private String modifier;

        /**
         * Maps lower case enchantments to their levels
         */
        @Nullable private Map<String, Integer> enchantments;

        /**
         * Amount of anvil uses
         */
        @SerializedName("anvil_uses")
        private int anvilUses;

        /**
         * Amount of Hot Potato Books used on this item
         */
        @SerializedName("hot_potato_count")
        private int hotPotatoCount;

        /**
         * Origin of the item
         */
        @Nullable private String origin;

        /**
         * Skyblock Item ID, i.e. "ASPECT_OF_THE_DRAGON"
         */
        private String id;

        /**
         * The items unique id
         */
        @Nullable private String uuid;

        /**
         * Texture string
         */
        @Nullable private String texture;
    }
}
