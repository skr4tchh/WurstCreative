package fun.wurstmc.creative.general.commands;

import fun.wurstmc.creative.command.templates.OpenGuiCommand;
import fun.wurstmc.creative.general.guis.MyPlotsGui;

public class MyPlotsCommand extends OpenGuiCommand {

    public MyPlotsCommand() {
        super("myplots", MyPlotsGui::new, "myworlds");
    }

}
