package thetadev.constructionwand.containers.handlers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import thetadev.constructionwand.api.IContainerHandler;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.BlockProvider;

public class HandlerBotania implements IContainerHandler {

    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        return inventoryStack != null && inventoryStack.getCapability(BotaniaForgeCapabilities.BLOCK_PROVIDER) != null;
    }

    @Override
    public int countItems(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        BlockProvider provOptional = inventoryStack.getCapability(BotaniaForgeCapabilities.BLOCK_PROVIDER);
        if (provOptional == null)
            return 0;
        BlockProvider prov = provOptional;
        int provCount = prov.getBlockCount(player, inventoryStack, Block.byItem(itemStack.getItem()));
        if (provCount == -1)
            return Integer.MAX_VALUE;
        return provCount;
    }

    @Override
    public int useItems(Player player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        BlockProvider provOptional = inventoryStack.getCapability(BotaniaForgeCapabilities.BLOCK_PROVIDER);
        if (provOptional == null)
            return 0;
        BlockProvider prov = provOptional;
        if (prov.provideBlock(player, inventoryStack, Block.byItem(itemStack.getItem()), true))
            return 0;
        return count;
    }
}
