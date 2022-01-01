package io.github.haykam821.deathswap.game;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;

import io.github.haykam821.deathswap.game.phase.DeathSwapActivePhase;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import xyz.nucleoid.plasmid.game.common.GlobalWidgets;
import xyz.nucleoid.plasmid.game.common.widget.BossBarWidget;

public class DeathSwapTimer {
	private static final BossBar.Style STYLE = BossBar.Style.PROGRESS;
	private static final BossBar.Color NO_SWAP_COLOR = BossBar.Color.GREEN;
	private static final BossBar.Color WARNING_COLOR = BossBar.Color.YELLOW;
	private static final Formatting NO_SWAP_FORMATTING = Formatting.GREEN;
	private static final Formatting WARNING_FORMATTING = Formatting.YELLOW;
	private static final Text NO_SWAP_TITLE = new TranslatableText("text.deathswap.timer.no_swap");

	private final DeathSwapActivePhase phase;
	private final BossBarWidget widget;
	private int swapTicks;

	public DeathSwapTimer(DeathSwapActivePhase phase, GlobalWidgets widgets) {
		this.phase = phase;
		this.swapTicks = this.phase.getConfig().initialSwapTicks();

		this.widget = widgets.addBossBar(this.getBarTitle(NO_SWAP_TITLE, NO_SWAP_FORMATTING), NO_SWAP_COLOR, STYLE);
	}

	public void tick() {
		this.swapTicks -= 1;
		if (this.swapTicks == 0) {
			this.swap();
			this.updateNoSwapBar();
		} else if (this.swapTicks < this.phase.getConfig().swapWarningTicks() && this.swapTicks % 20 == 0) {
			this.updateWarningBar();
		}
	}

	private void swap() {
		// Collect new positions for each player
		List<Vec3d> positions = new ArrayList<>(this.phase.getPlayers().size());

		ServerPlayerEntity previousPlayer = Iterables.getLast(this.phase.getPlayers());
		positions.add(previousPlayer.getPos());
		
		for (ServerPlayerEntity player : this.phase.getPlayers()) {
			// Ensure positions are off by one
			if (player != previousPlayer) {
				positions.add(player.getPos());
			}
		}

		// Teleport players to their new positions
		int index = 0;
		for (ServerPlayerEntity player : this.phase.getPlayers()) {
			Vec3d position = positions.get(index);
			player.teleport(position.getX(), position.getY(), position.getZ());

			Text message = new TranslatableText("text.deathswap.timer.swap", previousPlayer.getDisplayName()).formatted(NO_SWAP_FORMATTING);
			player.sendMessage(message, true);

			previousPlayer = player;
			index += 1;
		} 

		this.swapTicks = this.phase.getConfig().swapTicks();
	}

	private void updateNoSwapBar() {
		this.widget.setStyle(NO_SWAP_COLOR, STYLE);
		this.widget.setProgress(1);
		this.setBarTitle(NO_SWAP_TITLE, NO_SWAP_FORMATTING);
	}

	private void updateWarningBar() {
		this.widget.setStyle(WARNING_COLOR, STYLE);

		int swapWarningTicks = this.phase.getConfig().swapWarningTicks();
		this.widget.setProgress((swapWarningTicks - this.swapTicks) / (float) swapWarningTicks);

		this.setBarTitle(new TranslatableText("text.deathswap.timer.warning", this.swapTicks / 20), WARNING_FORMATTING);
	}

	private void setBarTitle(Text customText, Formatting formatting) {
		this.widget.setTitle(this.getBarTitle(customText, formatting));
	}

	private Text getBarTitle(Text customText, Formatting formatting) {
		Text gameName = new TranslatableText("gameType.deathswap.death_swap").formatted(Formatting.BOLD);
		return new LiteralText("").append(gameName).append(" - ").append(customText).formatted(formatting);
	}
}
