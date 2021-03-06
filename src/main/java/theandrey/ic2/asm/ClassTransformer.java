package theandrey.ic2.asm;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import java.util.ListIterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author TheAndrey
 */
public final class ClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String origName, String name, byte[] bytes) {
		if(name.equals("ic2.core.block.machine.tileentity.TileEntityRecycler") || name.equals("ic2.advancedmachines.TileEntityAdvancedRecycler")) {
			ClassNode node = readClass(bytes);
			processRecycler(node);
			bytes = writeClass(node);
		} else if(name.equals("ic2.core.block.machine.tileentity.TileEntityRecycler$RecyclerRecipeManager")) {
			ClassNode node = readClass(bytes);
			processRecyclerRecipeManager(node);
			bytes = writeClass(node);
		}
		return bytes;
	}

	private void processRecycler(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("operateOnce")) {

				ListIterator<AbstractInsnNode> it = method.instructions.iterator();
				while(it.hasNext()) {
					AbstractInsnNode insn = it.next();
					if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
						MethodInsnNode mcall = (MethodInsnNode)insn;
						if(mcall.name.equals("consume") && mcall.owner.equals("ic2/core/block/invslot/InvSlotProcessable")) {

							InsnList insert = new InsnList();
							insert.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
							insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "theandrey/ic2/RecyclerStatMod", "hook_operateOnce", "(Lnet/minecraft/tileentity/TileEntity;)V", false));
							method.instructions.insert(insn, insert);
							FMLRelaunchLog.info("[RecyclerStat] Method TileEntityRecycler.operateOnce() hooked.");
							break;
						}
					}
				}
				break;
			}
		}
	}

	private void processRecyclerRecipeManager(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getOutputFor")) {
				InsnList list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 1)); // #1 параметр
				list.add(new VarInsnNode(Opcodes.ILOAD, 2)); // #2 параметр
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "theandrey/ic2/RecyclerStatMod", "hook_getOutputFor", "(Lnet/minecraft/item/ItemStack;Z)V", false));
				method.instructions.insert(list);
				FMLRelaunchLog.info("[RecyclerStat] Method RecyclerRecipeManager.getOutputFor() hooked.");
				break;
			}
		}
	}

	private static ClassNode readClass(byte[] bytes) {
		ClassNode node = new ClassNode();
		ClassReader reader = new ClassReader(bytes);
		reader.accept(node, 0);
		return node;
	}

	private static byte[] writeClass(ClassNode node) {
		ClassWriter writer = new ClassWriter(0);
		node.accept(writer);
		return writer.toByteArray();
	}

}
