package theandrey.ic2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class Utils {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final OpenOption[] WRITE_OPTIONS = {StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

	private Utils() {
	}

	private static Path getLogFileName(String prefix) {
		String date = DATE_FORMAT.format(System.currentTimeMillis());
		int counter = 0; // За сутки может быть создано несколько архивов, поэтому присваиваем им номер

		Path dir = Paths.get("logs", "IC2RecyclerStat");
		if(!Files.isDirectory(dir)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException ex) {
				throw new RuntimeException("Failed to create directory: " + dir, ex);
			}
		}

		Path path;
		do {
			path = dir.resolve(prefix + "-" + date + "-" + (counter++) + ".log");
		} while(Files.exists(path));
		return path;
	}

	public static void saveItemStatDump() throws IOException {
		ArrayList<StatEntry<ItemData>> list = new ArrayList<>(RecyclerStatMod.itemStats.size());
		for(Map.Entry<ItemData, AtomicInteger> entry : RecyclerStatMod.itemStats.entrySet()) {
			list.add(new StatEntry(entry.getKey(), entry.getValue().get()));
		}
		Collections.sort(list);

		Path file = getLogFileName("item-count");
		try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, WRITE_OPTIONS)) {
			PrintWriter pw = new PrintWriter(writer, true);
			for(StatEntry entry : list) pw.println(entry);
			pw.flush();
		}
	}

}
