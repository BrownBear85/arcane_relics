package bonker.arcane_relics.common.item.custom;

import net.minecraft.world.item.ItemStack;

public interface IProgressBarItem {
    int getMaxProgress();

    default int getProgress(ItemStack stack) {
        return stack.getOrCreateTag().getInt("progress");
    }

    default int setProgress(ItemStack stack, int progress) {
        if (stack.getItem() instanceof IProgressBarItem progressBarItem) {
            stack.getOrCreateTag().putInt("progress",
                    Math.max(0, Math.min(progress, progressBarItem.getMaxProgress())));
            return getProgress(stack);
        }
        return 0;
    }

    default int incrementProgress(ItemStack stack, int amount) {
        return setProgress(stack, getProgress(stack) + amount);
    }

    default int subtractProgress(ItemStack stack, int amount) {
        return incrementProgress(stack, -amount);
    }
}
