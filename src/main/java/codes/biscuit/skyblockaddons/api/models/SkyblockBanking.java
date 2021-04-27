package codes.biscuit.skyblockaddons.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

/**
 * Represents a Skyblock profiles banking information as provided by the API.
 */
@Getter
public class SkyblockBanking {

    /**
     * Coin balance currently in the bank
     */
    private double balance;

    /**
     * List of most recent transactions
     */
    private List<Transaction> transactions;

    @Getter
    public static class Transaction {

        /**
         * Amount of coins moved in this transaction
         */
        private double amount;

        /**
         * Unix timestamp when the transaction took place
         */
        private long timestamp;

        /**
         * Type of transaction (DEPOSIT/WITHDRAW)
         */
        private String action;

        /**
         * Who initiated the transaction, e.g. a player name or 'Bank Interest'.
         * Player names are formatted with their rank color.
         */
        @SerializedName("initiator_name")
        private String initiatorName;
    }
}
