package io.github.haykam821.deathswap.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.haykam821.deathswap.game.map.DeathSwapMapConfig;
import xyz.nucleoid.plasmid.game.common.config.PlayerConfig;

public record DeathSwapConfig(PlayerConfig playerConfig,
							  DeathSwapMapConfig mapConfig, int initialSwapTicks,
							  int swapTicks, int swapWarningTicks) {
	public static final Codec<DeathSwapConfig> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
				PlayerConfig.CODEC.fieldOf("players").forGetter(DeathSwapConfig::playerConfig),
				DeathSwapMapConfig.CODEC.fieldOf("map").forGetter(DeathSwapConfig::mapConfig),
				Codec.INT.optionalFieldOf("initial_swap_ticks", 20 * 60 * 5).forGetter(DeathSwapConfig::initialSwapTicks),
				Codec.INT.optionalFieldOf("swap_ticks", 20 * 60 * 2).forGetter(DeathSwapConfig::swapTicks),
				Codec.INT.optionalFieldOf("swap_warning_ticks", 20 * 30).forGetter(DeathSwapConfig::swapWarningTicks)
		).apply(instance, DeathSwapConfig::new)
	);
}