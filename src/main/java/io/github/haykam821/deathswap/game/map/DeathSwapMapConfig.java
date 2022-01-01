package io.github.haykam821.deathswap.game.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.haykam821.deathswap.Main;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public record DeathSwapMapConfig(Identifier chunkGeneratorSettingsId,
								 BlockStateProvider barrier,
								 BlockStateProvider topBarrier, int x, int z) {
	public static final Codec<DeathSwapMapConfig> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
				Identifier.CODEC.fieldOf("settings").forGetter(DeathSwapMapConfig::chunkGeneratorSettingsId),
				BlockStateProvider.TYPE_CODEC.optionalFieldOf("barrier", BlockStateProvider.of(Blocks.BARRIER)).forGetter(DeathSwapMapConfig::barrier),
				BlockStateProvider.TYPE_CODEC.optionalFieldOf("top_barrier", BlockStateProvider.of(Main.BARRIER_AIR)).forGetter(DeathSwapMapConfig::topBarrier),
				Codec.INT.optionalFieldOf("x", 32).forGetter(DeathSwapMapConfig::x),
				Codec.INT.optionalFieldOf("z", 32).forGetter(DeathSwapMapConfig::z)
		).apply(instance, DeathSwapMapConfig::new)
	);
}
