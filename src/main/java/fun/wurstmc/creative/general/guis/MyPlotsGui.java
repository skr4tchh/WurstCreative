package fun.wurstmc.creative.general.guis;

import fun.wurstmc.creative.WurstCreative;
import fun.wurstmc.creative.general.plot.Plot;
import fun.wurstmc.creative.gui.ItemBuilder;
import fun.wurstmc.creative.gui.PageGui;
import fun.wurstmc.creative.gui.items.OpenGuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class MyPlotsGui extends PageGui {

    public MyPlotsGui(Player player) {
        super(player, 54, "menu.ownplots.title", 3);

        List<Plot> plots = WurstCreative.getInstance().getPlotManager().getPlotsByPlayer(player);

        int currentPlotsCount = plots.size();
        int totalAllowedPlots = 4;

        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);
            addContent(new ItemBuilder(Material.GRASS_BLOCK).name("join").build());
        }

        if (currentPlotsCount < totalAllowedPlots) {
            int emptySlots = totalAllowedPlots - currentPlotsCount;
            for (int j = 0; j < emptySlots; j++) {
                OpenGuiItem createItem = new OpenGuiItem(
                        new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS)
                                .name(player, "menu.ownplots.create.name")
                                .lore(player, "menu.ownplots.create.lore")
                                .build(),
                        WorldGeneratorGui::new
                );
                addContent(createItem.getDisplayItem(), createItem);
            }
        }

        if (currentPlotsCount >= totalAllowedPlots) {
            addContent(new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS).name("buy").build());
        }

        previousPageItem(45, new ItemBuilder(Material.ARROW).name("Назад").build());
        nextPageItem(53, new ItemBuilder(Material.ARROW).name("Вперед").build());
    }

}
