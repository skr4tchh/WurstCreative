package fun.wurstmc.creative.general.world.generators;

import org.bukkit.generator.ChunkGenerator;

@FunctionalInterface
public interface WorldGeneratorCreator {

    void generate(WorldGenerator generator, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData);

}
