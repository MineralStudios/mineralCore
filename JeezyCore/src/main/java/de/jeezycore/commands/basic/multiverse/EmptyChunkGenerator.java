package de.jeezycore.commands.basic.multiverse;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;

import java.util.Random;


public class EmptyChunkGenerator extends ChunkGenerator {
    @Override
    @Nonnull
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return createChunkData(world);
    }
}
