package fun.wurstmc.creative.gui.items;

import fun.wurstmc.creative.gui.Gui;
import fun.wurstmc.creative.gui.ItemBuilder;
import fun.wurstmc.creative.utils.LocaleTranslator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectorItem<T> implements Consumer<InventoryClickEvent> {

    private static final Component ACTIVE_BULLET = Component.text("● ").color(NamedTextColor.GREEN);
    private static final Component INACTIVE_BULLET = Component.text("○ ").color(NamedTextColor.GRAY);

    private final String titleKey;
    private final String defaultLoreKey;
    private final String entryKeyPath;
    private final List<Entry<T>> entries;

    @Getter
    private T currentValue;

    private int currentIndex = 0;

    public SelectorItem(String titleKey,
                        String defaultLoreKey,
                        String entryKeyPath,
                        List<Entry<T>> entries) {

        this.titleKey = titleKey;
        this.defaultLoreKey = defaultLoreKey;
        this.entryKeyPath = entryKeyPath;
        this.entries = entries;

        if (entries.isEmpty()) {
            throw new IllegalArgumentException("Entries list cannot be empty");
        }

        this.currentValue = entries.get(0).value();
    }

    public ItemStack render(Player player) {
        Entry<T> currentEntry = entries.get(currentIndex);

        ItemBuilder builder = new ItemBuilder(currentEntry.material());
        builder.name(player, titleKey);

        List<Component> translatedLore = LocaleTranslator.translateList(player, defaultLoreKey);

        List<Component> finalLore = new ArrayList<>(translatedLore.size() + 1 + entries.size());
        finalLore.addAll(translatedLore);
        finalLore.add(Component.empty());

        for (int i = 0; i < entries.size(); i++) {
            Entry<T> entry = entries.get(i);
            Component bullet = (i == currentIndex) ? ACTIVE_BULLET : INACTIVE_BULLET;

            Component entryName = LocaleTranslator.translate(player, entryKeyPath + "." + entry.nameKey());
            finalLore.add(bullet.append(entryName));
        }

        builder.lore(finalLore);
        return builder.build();
    }

    @Override
    public void accept(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof Gui gui)) {
            return;
        }

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        ClickType click = event.getClick();
        int size = entries.size();

        if (click.isLeftClick()) {
            currentIndex = (currentIndex + 1) % size;
        } else if (click.isRightClick()) {
            currentIndex = (currentIndex - 1 + size) % size;
        } else {
            return;
        }

        this.currentValue = entries.get(currentIndex).value();

        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.1f, 1.25f);

        ItemStack freshItem = this.render(player);
        gui.setItem(event.getSlot(), freshItem, this);
    }

    public record Entry<T>(String nameKey, T value, Material material) {}

}
