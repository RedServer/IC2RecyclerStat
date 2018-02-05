package theandrey.ic2;

import java.util.Objects;
import net.minecraft.tileentity.TileEntity;

public final class BlockPosition {

	public final String worldName;
	public final int x;
	public final int y;
	public final int z;

	public BlockPosition(String worldName, int x, int y, int z) {
		this.worldName = Objects.requireNonNull(worldName, "worldName");
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return String.format("%s[%d %d %d]", worldName, x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BlockPosition) {
			BlockPosition other = (BlockPosition)obj;
			return this.worldName.equals(other.worldName) && this.x == other.x && this.y == other.y && this.z == other.z;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.worldName);
		hash = 17 * hash + this.x;
		hash = 17 * hash + this.y;
		hash = 17 * hash + this.z;
		return hash;
	}

	public static BlockPosition create(TileEntity tile) {
		if(tile == null) throw new IllegalArgumentException("tile is null!");
		return new BlockPosition(tile.getWorldObj().getWorldInfo().getWorldName(), tile.xCoord, tile.yCoord, tile.zCoord);
	}

}
