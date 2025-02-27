package io.bluestaggo.voxelthing.gui;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.assets.FontManager;
import io.bluestaggo.voxelthing.renderer.screen.Screen;

public class DebugGui extends GuiScreen {
	public DebugGui(Game game) {
		super(game);
	}

	@Override
	public void draw() {
		FontManager fonts = game.renderer.fonts;
		Screen screen = game.renderer.screen;

		fonts.outlined.print("§00ffffVOXEL THING    §00ff00" + Game.VERSION, 5, 5, 1.0f, 1.0f, 1.0f);

		long freeMB = Runtime.getRuntime().freeMemory() / 1000000L;
		long totalMB = Runtime.getRuntime().totalMemory() / 1000000L;
		long maxMB = Runtime.getRuntime().maxMemory() / 1000000L;

		String[] lines = {
				"Speed", (int)(game.window.getDeltaTime() * 1000.0D) + "ms",
				"Memory", (totalMB - freeMB) + " / " + maxMB + " MB",
				"Render Distance", String.valueOf(game.renderer.worldRenderer.renderDistance),
				"GUI Scale", String.valueOf(screen.scale <= 0.0f ? "auto" : screen.scale)
		};

		StringBuilder debugBuilder = new StringBuilder();

		for (int i = 0; i < lines.length / 2; i++) {
			String label = lines[i * 2];
			String value = lines[i * 2 + 1];

			if (!debugBuilder.isEmpty()) {
				debugBuilder.append('\n');
			}

			debugBuilder.append("§ffff7f");
			debugBuilder.append(label);
			debugBuilder.append(": §ffffff");
			debugBuilder.append(value);
		}

		fonts.shadowed.print(debugBuilder.toString(), 5, 15);
	}
}
