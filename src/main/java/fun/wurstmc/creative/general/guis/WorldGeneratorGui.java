package fun.wurstmc.creative.general.guis;

import fun.wurstmc.creative.general.world.generators.WorldGeneratorType;
import fun.wurstmc.creative.gui.Gui;
import fun.wurstmc.creative.gui.ItemBuilder;
import fun.wurstmc.creative.gui.items.SelectorItem;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorldGeneratorGui extends Gui {

    private final SelectorItem<WorldGeneratorType> worldGeneratorSelector;

    private final SelectorItem<World.Environment> environmentSelector;

    public WorldGeneratorGui(Player player) {
        super(player, 27, "menu.worldcreate.title");

        this.worldGeneratorSelector = getWorldGeneratorSelectorItem();
        this.environmentSelector = getEnvironmentSelectorItem();

        setItem(10, worldGeneratorSelector.render(player), worldGeneratorSelector);
        setItem(11, environmentSelector.render(player), environmentSelector);
        setItem(16, new ItemBuilder(Material.PUFFERFISH_BUCKET)
                .name(player, "menu.worldcreate.create.name")
                .lore(player,"menu.worldcreate.create.lore")
                .build(), event -> {

        });

        setItems(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).build(), 7, 25);
    }

    private @NotNull SelectorItem<World.Environment> getEnvironmentSelectorItem() {
        List<SelectorItem.Entry<World.Environment>> environmentEntries = List.of(
                new SelectorItem.Entry<>("normal", World.Environment.NORMAL, Material.MOSS_BLOCK),
                new SelectorItem.Entry<>("nether", World.Environment.NETHER, Material.NETHERRACK),
                new SelectorItem.Entry<>("end", World.Environment.THE_END, Material.END_STONE)
        );

        SelectorItem<World.Environment> environmentSelector = new SelectorItem<>(
                "menu.worldcreate.environment.name",
                "menu.worldcreate.environment.lore",
                "menu.worldcreate.environment.type",
                environmentEntries
        );

        return environmentSelector;
    }

    private @NotNull SelectorItem<WorldGeneratorType> getWorldGeneratorSelectorItem() {
        List<SelectorItem.Entry<WorldGeneratorType>> generatorEntries = new ArrayList<>();
        for(WorldGeneratorType generatorType : WorldGeneratorType.values()) {
            generatorEntries.add(new SelectorItem.Entry<>(generatorType.name().toLowerCase(), generatorType, generatorType.getMaterial()));
        }

        SelectorItem<WorldGeneratorType> worldGeneratorSelector = new SelectorItem<>(
                "menu.worldcreate.generator.name",
                "menu.worldcreate.generator.lore",
                "menu.worldcreate.generator.type",
                generatorEntries
        );

        return worldGeneratorSelector;
    }

}
