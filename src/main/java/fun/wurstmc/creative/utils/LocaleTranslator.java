package fun.wurstmc.creative.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@UtilityClass
public class LocaleTranslator {

    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component translate(Player player, String key) {
        return GlobalTranslator.render(Component.translatable(key), player.locale());
    }

    public List<Component> translateList(Player player, String key) {
        Locale locale = player.locale();
        Component translated = GlobalTranslator.render(Component.translatable(key), locale);

        String serialized = MINI_MESSAGE.serialize(translated);

        if (!serialized.contains("<br>")) {
            return Collections.singletonList(translated);
        }

        String[] lines = serialized.split("<br>");
        List<Component> translatedLore = new ArrayList<>(lines.length);

        for (String line : lines) {
            translatedLore.add(MINI_MESSAGE.deserialize(line));
        }

        return translatedLore;
    }

}
