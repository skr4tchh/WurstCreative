package fun.wurstmc.creative.general.world.generators;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
public class WorldGenerator extends ChunkGenerator {

    public final int spawnHeight = 128;
    private final int size;
    private final int center;
    private final WorldGeneratorCreator generatorCreator;

    public WorldGenerator(short size, @NotNull WorldGeneratorCreator generatorCreator) {
        this.size = Short.toUnsignedInt(size);
        this.center = (this.size - 1) / 2;
        this.generatorCreator = generatorCreator;
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random,
                                int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

        if (chunkX < 0 || chunkX >= size || chunkZ < 0 || chunkZ >= size) {
            return;
        }

        generatorCreator.generate(this, chunkX, chunkZ, chunkData);
    }

    public void fillY(@NotNull ChunkData chunkData, int from, int height, @NotNull Material material) {
        chunkData.setRegion(0, from, 0, 16, from + height, 16, material);
    }

}
