package org.gtlcore.gtlcore.api.machine.trait.AECraft;

import appeng.api.networking.crafting.ICraftingProvider;
import java.util.Set;
import net.minecraft.core.BlockPos;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.NotifiableMAHandlerTrait;
import org.jetbrains.annotations.NotNull;

public interface IMECraftIOPart extends ICraftingProvider {
   void init(@NotNull Set<BlockPos> var1);

   @NotNull
   NotifiableMAHandlerTrait getNotifiableMAHandlerTrait();
}
