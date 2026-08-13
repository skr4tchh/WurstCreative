package fun.wurstmc.creative.gui.items;

import fun.wurstmc.creative.gui.Gui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public class OpenGuiItem implements Consumer<InventoryClickEvent> {

    private final ItemStack displayItem;
    private final Function<Player, Gui> guiProvider;

    @Override
    public void accept(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.1f, 1.25f);

        Gui targetGui = guiProvider.apply(player);

        if (targetGui != null) {
            targetGui.open();
        }
    }

}
