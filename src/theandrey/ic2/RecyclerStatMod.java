package theandrey.ic2;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

/**
 * Мод выполняет сбор статистики использования утилизатора
 * @author TheAndrey
 */
@Mod(modid = RecyclerStatMod.MOD_ID, name = RecyclerStatMod.MOD_NAME, version = RecyclerStatMod.MOD_VERSION)
public final class RecyclerStatMod {

	public static final String MOD_ID = "ic2_recyclerstat";
	public static final String MOD_NAME = "IC2 Recycler Stat";
	public static final String MOD_VERSION = "1.0";

	@Mod.EventHandler
	public void handleServerStart(FMLServerStartingEvent event) {
	}

}
