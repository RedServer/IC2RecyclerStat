package theandrey.ic2;

import java.util.Objects;

public final class StatEntry<K> implements Comparable<StatEntry> {

	public final K key;
	public final int count;

	StatEntry(K key, int count) {
		this.key = Objects.requireNonNull(key, "key");
		this.count = count;
	}

	@Override
	public int compareTo(StatEntry other) {
		return Integer.compare(other.count, this.count);
	}

	@Override
	public String toString() {
		return String.format("%9d - %s", count, String.valueOf(key));
	}

}
