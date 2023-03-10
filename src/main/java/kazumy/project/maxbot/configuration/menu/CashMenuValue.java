package kazumy.project.maxbot.configuration.menu;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.val;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

@Getter @Accessors(fluent = true)
@ConfigSection("menu")
@ConfigFile("menu.yml")
public class CashMenuValue implements ConfigurationInjectable {

    @Getter private static final CashMenuValue instance = new CashMenuValue();

    @ConfigField("cash") private ConfigurationSection section;

    @SneakyThrows
    public SelectMenu toMenu() {
        val menu = SelectMenu.create("menu-cash");
        if (section == null) return menu.build();

        val name = new StringBuilder();
        val value = new StringBuilder();
        val description = new StringBuilder();
        val emoji = new StringBuilder();
        this.section.getKeys(true).stream()
                .filter(field -> !section.getString(field).startsWith("MemorySection"))
                .distinct()
                .forEach(field -> {
                    switch (field.split("\\.")[1]) {
                        case "name":
                            name.append(this.section.getString(field));
                            break;

                        case "value":
                            value.append(this.section.getString(field));
                            break;

                        case "description":
                            description.append(this.section.getString(field));
                            break;

                        case "emoji":
                            emoji.append(this.section.getString(field));
                            break;
                    }
                    if (!name.toString().isEmpty() && !value.toString().isEmpty() && !description.toString().isEmpty() && !emoji.toString().isEmpty()) {
                        menu.addOption(name.toString(), value.toString(), description.toString(), Emoji.fromUnicode(emoji.toString()));
                        name.setLength(0);
                        value.setLength(0);
                        description.setLength(0);
                        emoji.setLength(0);
                    }
                });
        return menu.build();
    }

    public static <T> T get(Function<CashMenuValue, T> function) {
        return function.apply(instance);
    }
}
