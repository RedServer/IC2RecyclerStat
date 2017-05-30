package theandrey.ic2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;

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
		sender.addChatMessage(new ChatComponentText("Сохранение статистики..."));
		new Thread(new SaveTask(sender), "Recycled Dump Save").start();
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
			List<StatEntry> list = new LinkedList<>();
			for(Map.Entry<Item, Integer> entry : RecyclerStatMod.stats.entrySet()) {
				String itemId = Item.itemRegistry.getNameForObject(entry.getKey());
				list.add(new StatEntry(itemId, entry.getValue()));
			}
			Collections.sort(list);

			try (FileOutputStream fos = new FileOutputStream(new File("logs/RecyclerStat.log"))) {
				PrintWriter writer = new PrintWriter(fos, true);
				for(StatEntry entry : list) {
					writer.println(entry);
				}
				writer.flush();
				sender.addChatMessage(new ChatComponentText("Дамп статистики успешно сохранён."));
			} catch (IOException ex) {
				sender.addChatMessage(new ChatComponentText("Ошибка записи файла: " + ex.toString()));
				ex.printStackTrace();
			}
		}

	}

	private static class StatEntry implements Comparable<StatEntry> {

		public final String itemId;
		public final int count;

		public StatEntry(String itemId, int count) {
			this.itemId = itemId;
			this.count = count;
		}

		@Override
		public int compareTo(StatEntry other) {
			return Integer.compare(other.count, this.count);
		}

		@Override
		public String toString() {
			return itemId + " x " + count;
		}

	}

}
