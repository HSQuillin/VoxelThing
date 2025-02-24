package io.bluestaggo.voxelthing.world.generation;

import io.bluestaggo.voxelthing.world.ChunkStorage;
import io.bluestaggo.voxelthing.world.World;

public class GenCache {
	public static final int RADIUS_POW2 = ChunkStorage.RADIUS_POW2;
	public static final int AREA = 1 << RADIUS_POW2 * 2;
	public static final int POS_MASK = (1 << RADIUS_POW2) - 1;

	private final World world;
	private final GenerationInfo[] cache = new GenerationInfo[AREA];

	public GenCache(World world) {
		this.world = world;
	}

	public static int cacheCoords(int x, int z) {
		x &= POS_MASK;
		z &= POS_MASK;
		return (x << RADIUS_POW2 | z);
	}

	public GenerationInfo getGenerationAt(int x, int z) {
		GenerationInfo entry = cache[cacheCoords(x, z)];
		if (entry != null && entry.chunkX == x && entry.chunkZ == z) {
			return entry;
		}

		entry = new GenerationInfo(world.seed, x, z);
		cache[cacheCoords(x, z)] = entry;
		return entry;
	}
}
