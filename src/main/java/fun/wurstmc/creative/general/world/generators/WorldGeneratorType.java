package fun.wurstmc.creative.general.world.generators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum WorldGeneratorType implements WorldGeneratorCreator {

    VOID(Material.STRUCTURE_VOID) {
        @Override
        public void generate(@NotNull WorldGenerator generator, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
            if (chunkX != generator.getCenter() || chunkZ != generator.getCenter()) {
                return;
            }
            generator.fillY(chunkData, generator.getSpawnHeight() - 1, 1, Material.STONE);
        }
    },

    FLAT(Material.GRASS_BLOCK) {
        @Override
        public void generate(@NotNull WorldGenerator generator, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
            generator.fillY(chunkData, generator.getSpawnHeight() - 5, 1, Material.BEDROCK);
            generator.fillY(chunkData, generator.getSpawnHeight() - 4, 3, Material.DIRT);
            generator.fillY(chunkData, generator.getSpawnHeight() - 1, 1, Material.GRASS_BLOCK);
        }
    };

    private final Material material;

}