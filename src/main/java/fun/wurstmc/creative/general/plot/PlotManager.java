package fun.wurstmc.creative.general.plot;

import fun.wurstmc.creative.storage.StorageManager;
import fun.wurstmc.creative.storage.StorageType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.entity.Player;

import java.util.List;

public class PlotManager {

    private StorageManager storageManager;

    public PlotManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    private Int2ObjectMap<Plot> plots = new Int2ObjectOpenHashMap<>();

    public List<Plot> getPlotsByPlayer(Player player) {
        return plots.values().stream().filter(p -> p.getOwnerUuid().equals(player.getUniqueId())).toList();
    }

    public void loadAllPlots() {
        this.plots = storageManager.load(StorageType.PLOTS);
    }

    public void unloadAllPlots() {
        storageManager.save(StorageType.PLOTS, plots);
    }

}
