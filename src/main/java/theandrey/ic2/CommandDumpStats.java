package theandrey.ic2;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import java.io.IOException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Level;

/**
 * Команда сохранения дампа
 * @author TheAndrey
 */
public class CommandDumpStats extends CommandBase {

	@Override
	public String getCommandName() {
		return "saveRecyclerDump";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(RecyclerStatMod.itemStats.isEmpty()) {
			sender.addChatMessage(new ChatComponentText("Данные статистики пока отсутствуют."));
		} else {
			sender.addChatMessage(new ChatComponentText("Сохранение статистики..."));
			new Thread(new SaveTask(sender), "Recycled Dump Save").start();
		}
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	private static class SaveTask implements Runnable {

		private final ICommandSender sender;

		public SaveTask(ICommandSender sender) {
			this.sender = sender;
		}

		@Override
		public void run() {
			try {
				Utils.saveItemStatDump();
				sender.addChatMessage(new ChatComponentText("Дамп статистики успешно сохранён"));
			} catch (IOException ex) {
				sender.addChatMessage(new ChatComponentText("Ошибка записи файла: " + ex.toString()));
				FMLRelaunchLog.log(Level.ERROR, ex, "[RecyclerStat] Save dump error");
			}
		}

	}

}
