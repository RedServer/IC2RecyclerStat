package theandrey.ic2;

public final class StatEntry implements Comparable<StatEntry> {

	public final String itemId;
	public final int count;

	StatEntry(String itemId, int count) {
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
