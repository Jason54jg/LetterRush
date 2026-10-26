package io.github.jason54.letterrush.reward;

import com.google.gson.annotations.SerializedName;

/**
 * One weighted reward entry from config or datapack.
 */
public final class RewardDefinition {
    public String id = "reward";
    public int weight = 1;
    public RewardType type = RewardType.ITEM;

    /** Item rewards */
    public String item = "minecraft:diamond";
    public int count = 1;

    /** Experience rewards */
    public int amount = 100;

    /** Command rewards — executed as the server at winner position (@p = winner). */
    public String command = "";

    /** Display name shown in chat / titles / roulette. */
    public String display = "Mystery Prize";

    public enum RewardType {
        @SerializedName("item")
        ITEM,
        @SerializedName("experience")
        EXPERIENCE,
        @SerializedName("command")
        COMMAND
    }

    public static RewardDefinition item(String id, int weight, String itemId, int count, String display) {
        RewardDefinition reward = new RewardDefinition();
        reward.id = id;
        reward.weight = weight;
        reward.type = RewardType.ITEM;
        reward.item = itemId;
        reward.count = count;
        reward.display = display;
        return reward;
    }

    public static RewardDefinition experience(String id, int weight, int amount, String display) {
        RewardDefinition reward = new RewardDefinition();
        reward.id = id;
        reward.weight = weight;
        reward.type = RewardType.EXPERIENCE;
        reward.amount = amount;
        reward.display = display;
        return reward;
    }

    public static RewardDefinition command(String id, int weight, String command, String display) {
        RewardDefinition reward = new RewardDefinition();
        reward.id = id;
        reward.weight = weight;
        reward.type = RewardType.COMMAND;
        reward.command = command;
        reward.display = display;
        return reward;
    }

    public String displayLabel() {
        return display == null || display.isBlank() ? id : display;
    }
}
