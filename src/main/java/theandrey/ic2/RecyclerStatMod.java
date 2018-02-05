package theandrey.ic2;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.apache.logging.log4j.Level;

/**
 * Мод выполняет сбор статистики использования утилизатора
 * @author TheAndrey
 */
@Mod(modid = RecyclerStatMod.MOD_ID, name = RecyclerStatMod.MOD_NAME, version = RecyclerStatMod.MOD_VERSION, acceptableRemoteVersions = "*")
public final class RecyclerStatMod {

	public static final String MOD_ID = "ic2_recyclerstat";
	public static final String MOD_NAME = "IC2 Recycler Stat";
	public static final String MOD_VERSION = "1.3";

	static final Map<ItemData, AtomicInteger> itemStats = new ConcurrentHashMap<>();
	static final Map<BlockPosition, AtomicInteger> blockStats = new ConcurrentHashMap<>();

	@Mod.EventHandler
	public void handleServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDumpStats());
	}

	@Mod.EventHandler
	public void handleServerStop(FMLServerStoppingEvent event) {
		if(!itemStats.isEmpty()) {
			FMLRelaunchLog.info("[RecyclerStat] Saving stats dump...");
			try {
				Utils.saveItemStatDump(itemStats, "item-count");
				Utils.saveItemStatDump(blockStats, "block-count");
			} catch (IOException ex) {
				FMLRelaunchLog.log(Level.ERROR, ex, "[RecyclerStat] Save dump error");
			}
		}
	}

	public static void hook_getOutputFor(ItemStack stack, boolean adjustInput) {
		if(!adjustInput || stack == null) return; // ждём завершения обработки

		ItemData key = ItemData.create(stack);
		AtomicInteger counter = itemStats.get(key);
		if(counter == null) {
			counter = new AtomicInteger(0);
			itemStats.put(key, counter);
		}
		counter.incrementAndGet();
	}

	public static void hook_operateOnce(TileEntity tile) {
		BlockPosition key = BlockPosition.create(tile);
		AtomicInteger counter = blockStats.get(key);
		if(counter == null) {
			counter = new AtomicInteger(0);
			blockStats.put(key, counter);
		}
		counter.incrementAndGet();
	}

}
