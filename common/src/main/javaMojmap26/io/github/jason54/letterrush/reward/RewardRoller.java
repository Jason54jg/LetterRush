package io.github.jason54.letterrush.reward;

import java.util.List;
import java.util.Random;

public final class RewardRoller {
    private RewardRoller() {
    }

    public static RewardDefinition roll(Random random) {
        List<RewardDefinition> pool = DatapackRewardLoader.activeRewards();
        if (pool.isEmpty()) {
            return RewardDefinition.item("fallback", 1, "minecraft:emerald", 1, "Emerald");
        }
        int total = pool.stream().mapToInt(r -> Math.max(1, r.weight)).sum();
        int pick = random.nextInt(total);
        int cursor = 0;
        for (RewardDefinition reward : pool) {
            cursor += Math.max(1, reward.weight);
            if (pick < cursor) {
                return reward;
            }
        }
        return pool.getLast();
    }
}
