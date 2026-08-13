package fun.wurstmc.creative.storage;

import fun.wurstmc.creative.general.plot.Plot;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StorageType {

    PLOTS("plots", Plot.class);

    private final String name;
    private final Class<?> classType;

}
