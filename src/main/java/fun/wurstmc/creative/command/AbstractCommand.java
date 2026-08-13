package fun.wurstmc.creative.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

@Getter
public abstract class AbstractCommand {

    private final LiteralCommandNode<CommandSourceStack> instance;

    protected AbstractCommand(String name) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(name);
        init(builder);
        this.instance = builder.build();
    }

    protected abstract void init(LiteralArgumentBuilder<CommandSourceStack> builder);

    public <T extends ArgumentBuilder<CommandSourceStack, T>> T executesConsumer(
            T builder,
            Consumer<CommandContext<CommandSourceStack>> consumer
    ) {
        return builder.executes(context -> {
            consumer.accept(context);
            return Command.SINGLE_SUCCESS;
        });
    }

}
