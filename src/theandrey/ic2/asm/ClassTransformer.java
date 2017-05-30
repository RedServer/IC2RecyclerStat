package theandrey.ic2.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import scala.tools.asm.Type;
import theandrey.ic2.RecyclerStatMod;

/**
 * @author TheAndrey
 */
public final class ClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String origName, String name, byte[] bytes) {
		if(name.equals("ic2.core.block.machine.tileentity.TileEntityRecycler$RecyclerRecipeManager")) {
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(bytes);
			reader.accept(node, 0);
			processRecyclerRecipeManager(node);
			ClassWriter writer = new ClassWriter(0);
			node.accept(writer);
			return writer.toByteArray();
		}
		return bytes;
	}

	private void processRecyclerRecipeManager(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getOutputFor")) {
				InsnList list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 1)); // #1 параметр
				list.add(new VarInsnNode(Opcodes.ILOAD, 2)); // #2 параметр
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(RecyclerStatMod.class), "hook_getOutputFor", "(Lnet/minecraft/item/ItemStack;Z)V", false)); // #1 param
				method.instructions.insert(list);
				System.out.println("getOutputFor() PATCHED!");
				break;
			}
		}
	}

}
