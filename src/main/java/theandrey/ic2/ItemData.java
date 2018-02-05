package theandrey.ic2;

import java.util.Objects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemData {

	public final Item item;
	public final short damage;

	public ItemData(Item item, short damage) {
		this.item = Objects.requireNonNull(item, "item");
		this.damage = damage;
	}

	@Override
	public String toString() {
		return Item.itemRegistry.getNameForObject(item) + ":" + damage;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ItemData) {
			ItemData other = (ItemData)obj;
			return this.item.equals(other.item) && this.damage == other.damage;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.item);
		hash = 67 * hash + this.damage;
		return hash;
	}

	public static ItemData create(ItemStack stack) {
		if(stack == null) throw new IllegalArgumentException("stack is null!");
		return new ItemData(stack.getItem(), (short)stack.getItemDamage());
	}

}
