package fun.wurstmc.creative.command.templates;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fun.wurstmc.creative.command.AbstractCommand;
import fun.wurstmc.creative.gui.Gui;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.util.function.Function;

public abstract class OpenGuiCommand extends AbstractCommand {

    private final Function<Player, Gui> guiFactory;

    protected OpenGuiCommand(String name, Function<Player, Gui> guiFactory, String... aliases) {
        super(name, aliases);
        this.guiFactory = guiFactory;
    }

    @Override
    protected final void init(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.requires(stack -> stack.getSender() instanceof Player);

        executesConsumer(builder, this::executeGuiOpen);
    }

    private void executeGuiOpen(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        Gui playerGui = this.guiFactory.apply(player);
        if (playerGui != null) {
            playerGui.open();
        }
    }

}