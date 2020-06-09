package codes.biscuit.skyblockaddons.constants.game;

import lombok.Getter;

/**
 * Skyblock item rarity definitions
 */
@Getter
public enum Rarity {
    COMMON("§f§lCOMMON", 'f'),
    UNCOMMON("§a§lUNCOMMON", 'a'),
    RARE("§9§lRARE", '9'),
    EPIC("§5§lEPIC", '5'),
    LEGENDARY("§6§lLEGENDARY", '6'),
    SPECIAL("§d§lSPECIAL", 'd');

    private final String tag;
    private final char color;

    Rarity(String s, char c) {
        this.tag = s;
        this.color = c;
    }
}
