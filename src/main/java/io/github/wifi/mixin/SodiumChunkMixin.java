package io.github.wifi.mixin;

import io.github.wifi.ModOnLoad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.math.ChunkPos;
import java.util.stream.LongStream;
import it.unimi.dsi.fastutil.longs.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkTracker.class, priority = 999, remap = false)
public abstract class SodiumChunkMixin {
	@Shadow
	private final Long2IntOpenHashMap single = new Long2IntOpenHashMap();;
	@Shadow
    private final Long2IntOpenHashMap merged = new Long2IntOpenHashMap();;
	@Shadow
    private final LongLinkedOpenHashSet dirty = new LongLinkedOpenHashSet();
	@Overwrite
	public void onLightDataAdded(int x, int z) {
        var key = ChunkPos.toLong(x, z);
        var existingFlags = this.single.get(key);

        if ((existingFlags & ChunkStatus.FLAG_HAS_BLOCK_DATA) == 0) {
            // throw new IllegalStateException("Tried to mark light data as ready for chunk [%s, %s] but it hasn't been loaded yet".formatted(x, z));
            return;
        }

        this.single.put(key, existingFlags | ChunkStatus.FLAG_HAS_LIGHT_DATA);
        this.dirty.add(key);
    }
}
