package theandrey.ic2;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
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
		if(RecyclerStatMod.stats.isEmpty()) {
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

	public static File saveStatDump() throws IOException {
		List<StatEntry> list = new LinkedList<>();
		for(Map.Entry<Item, Integer> entry : RecyclerStatMod.stats.entrySet()) {
			String itemId = Item.itemRegistry.getNameForObject(entry.getKey());
			list.add(new StatEntry(itemId, entry.getValue()));
		}
		Collections.sort(list);

		String fileName = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		int counter = 0; // За сутки может быть создано несколько архивов, поэтому присваиваем им номер

		File file;
		do {
			file = new File("logs", "RecyclerStat-" + fileName + "-" + (counter++) + ".log");
		} while(file.exists());

		try (FileOutputStream fos = new FileOutputStream(file)) {
			PrintWriter writer = new PrintWriter(fos, true);
			for(StatEntry entry : list) {
				writer.println(entry);
			}
			writer.flush();
		}

		return file;
	}

	private static class SaveTask implements Runnable {

		private final ICommandSender sender;

		public SaveTask(ICommandSender sender) {
			this.sender = sender;
		}

		@Override
		public void run() {
			try {
				File file = saveStatDump();
				sender.addChatMessage(new ChatComponentText("Дамп статистики успешно сохранён в " + file));
			} catch (IOException ex) {
				sender.addChatMessage(new ChatComponentText("Ошибка записи файла: " + ex.toString()));
				FMLRelaunchLog.log(Level.ERROR, ex, "[RecyclerStat] Save dump error");
			}
		}

	}

}
