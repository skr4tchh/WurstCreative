package fun.wurstmc.creative;

import fun.wurstmc.creative.command.AbstractCommand;
import fun.wurstmc.creative.general.commands.MyPlotsCommand;
import fun.wurstmc.creative.general.plot.PlotManager;
import fun.wurstmc.creative.gui.GuiListener;
import fun.wurstmc.creative.storage.StorageManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
public class WurstCreative extends JavaPlugin {

    @Getter
    private static WurstCreative instance;

    private StorageManager storageManager;

    private PlotManager plotManager;

    @Override
    public void onEnable() {
        instance = this;

        this.storageManager = new StorageManager(this);
        this.storageManager.init();

        this.plotManager = new PlotManager(this.storageManager);
        this.plotManager.loadAllPlots();

        registerLocales();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        this.plotManager.unloadAllPlots();
        this.storageManager.shutdown();
    }

    private void registerCommands() {
        List<AbstractCommand> commands = List.of(
                new MyPlotsCommand()
        );
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands registrar = event.registrar();
            for (AbstractCommand command : commands) {
                registrar.register(command.getInstance());
                for (var aliasNode : command.getAliases()) {
                    registrar.register(aliasNode);
                }
            }
        });
    }

    private void registerListeners() {
        List<Listener> listeners = List.of(
                new GuiListener()
        );
        PluginManager pluginManager = this.getServer().getPluginManager();
        listeners.forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    private final List<Locale> locales = List.of(
            new Locale("en", "US"),
            new Locale("ru", "RU")
    );

    private void registerLocales() {
        Key key = Key.key("minecraft", "translations");

        MiniMessageTranslationStore source = MiniMessageTranslationStore.create(key);

        for (Locale locale : locales) {
            String path = "locales/" + locale.toLanguageTag() + ".properties";
            File localeFile = new File(getDataFolder(), path);

            if (getResource(path) != null && !localeFile.exists()) {
                saveResource(path, false);
            }

            try (InputStream stream = new FileInputStream(localeFile);
                 InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {

                Properties properties = new Properties();
                properties.load(reader);

                Map<String, String> registry = properties.entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> String.valueOf(entry.getKey()),
                                entry -> String.valueOf(entry.getValue())
                        ));

                source.registerAll(locale, registry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GlobalTranslator.translator().addSource(source);
    }

}
