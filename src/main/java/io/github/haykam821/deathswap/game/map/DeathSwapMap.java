package io.github.haykam821.deathswap.game.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockBox;

public final class DeathSwapMap {
	private final DeathSwapChunkGenerator chunkGenerator;
	private final BlockBox box;

	public DeathSwapMap(MinecraftServer server, DeathSwapMapConfig mapConfig) {
		this.chunkGenerator = new DeathSwapChunkGenerator(server, mapConfig);
		this.box = new BlockBox(1, -63, 1, mapConfig.x() * 16 - 2, 318, mapConfig.z() * 16 - 2);
	}

	public DeathSwapChunkGenerator getChunkGenerator() {
		return this.chunkGenerator;
	}

	public BlockBox getBox() {
		return this.box;
	}
}
