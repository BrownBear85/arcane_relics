package bonker.arcane_relics.common.command;

import bonker.arcane_relics.common.worldevent.EvilSkullWorldEvent;
import bonker.arcane_relics.common.worldevent.WorldEvent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class ArcaneRelicsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        LiteralCommandNode<CommandSourceStack> literalCommandNode = pDispatcher.register(Commands.literal("ar")
                .then(Commands.argument("parameter", EnumArgument.enumArgument(Parameter.class))
                .executes(ArcaneRelicsCommand::execute)));
        pDispatcher.register(Commands.literal("arcane_relics").redirect(literalCommandNode));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();
        ServerPlayer player = context.getSource().getPlayerOrException();
        switch (context.getArgument("parameter", Parameter.class)) {
            case clear_world_events -> WorldEvent.clearEvents(level);
            case evil_skull_test -> new EvilSkullWorldEvent(level, player.position());
        }
        return 0;
    }

    public enum Parameter {
        evil_skull_test, clear_world_events
    }
}
