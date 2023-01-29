package bonker.arcane_relics.common.entity.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class SummonedMonsterDespawnGoal extends Goal {

    private final Mob mob;
    private int age = 0;

    private static final int despawnTime = 250;

    public SummonedMonsterDespawnGoal(Mob mob) {
        this.mob = mob;
        this.mob.targetSelector.removeAllGoals((goal) -> goal != this);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (++age >= despawnTime) {
            mob.discard();
        }

        if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
            age = Math.max(age, despawnTime - 15);
        }
    }
}
