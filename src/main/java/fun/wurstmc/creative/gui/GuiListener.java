package fun.wurstmc.creative.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof Gui && e.getClickedInventory() != null) {
            Gui gui = (Gui) e.getInventory().getHolder();

            boolean wasCancelled = e.isCancelled();
            e.setCancelled(true);

            gui.handleClick(e);

            if (!wasCancelled && !e.isCancelled()) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof Gui) {
            boolean wasCancelled = e.isCancelled();
            e.setCancelled(true);

            if (!wasCancelled && !e.isCancelled()) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof Gui) {
            Gui gui = (Gui) e.getInventory().getHolder();
            gui.handleClose(e);
            gui.clearItems();
        }
    }

}
