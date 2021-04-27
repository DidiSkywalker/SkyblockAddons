package codes.biscuit.skyblockaddons.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.Map;

@Getter
public class SkyblockStats {

    @SerializedName("total_kills")
    private int totalKills;

    @SerializedName("total_deaths")
    private int totalDeaths;

    private Map<String, Integer> kills;

    private Map<String, Integer> deaths;

    @SerializedName("highest_critical_damage")
    private double highestCriticalDamage;

    @SerializedName("ender_crystals_destroyed")
    private int enderCrystalsDestroyed;

    @SerializedName("end_race_best_time")
    private float endRaceBestTime;

    @SerializedName("chicken_race_best_time")
    private float chickenRaceBestTime;

    @SerializedName("gifts_given")
    private int giftsGiven;

    @SerializedName("gifts_received")
    private int giftsReceived;

    @SerializedName("items_fished")
    private Map<String, Integer> itemsFished;


    @Getter
    public static class Auctions {
        private int created;
        private int completed;
        @SerializedName("no_bids")
        private int noBids;
        private int won;
        private int bids;
        @SerializedName("highest_bid")
        private double highestBid;
        @SerializedName("total_fees")
        private double totalFees;
        @SerializedName("gold_earned")
        private double goldEarned;
        @SerializedName("gold_spent")
        private double goldSpent;
        private Map<String, Integer> sold;
        private Map<String, Integer> bought;
    }

    @Getter
    public static class WinterRecords {
        private int damage;
        @SerializedName("snowballs_hit")
        private double snowballsHit;
        @SerializedName("magma_cube_damage")
        private double magmaCubeDamage;
        @SerializedName("cannonballs_hit")
        private double cannonballsHit;
    }

}
