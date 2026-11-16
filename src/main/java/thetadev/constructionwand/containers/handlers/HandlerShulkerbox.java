package thetadev.constructionwand.containers.handlers;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import thetadev.constructionwand.api.IContainerHandler;
import thetadev.constructionwand.basics.WandUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.HolderLookup;


public class HandlerShulkerbox implements IContainerHandler
{
    private final int SLOTS = 27;

    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        return inventoryStack != null && inventoryStack.getCount() == 1 && Block.byItem(inventoryStack.getItem()) instanceof ShulkerBoxBlock;
    }

    @Override
    public int countItems(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        int count = 0;

        for(ItemStack stack : getItemList(inventoryStack, player.registryAccess())) {
            if(WandUtil.stackEquals(stack, itemStack)) count += stack.getCount();
        }

        return count;
    }

    @Override
    public int useItems(Player player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        NonNullList<ItemStack> itemList = getItemList(inventoryStack, player.registryAccess());
        boolean changed = false;

        for(ItemStack stack : itemList) {
            if(WandUtil.stackEquals(stack, itemStack)) {
                int toTake = Math.min(count, stack.getCount());
                stack.shrink(toTake);
                count -= toTake;
                changed = true;
                if(count == 0) break;
            }
        }
        if(changed) {
            setItemList(inventoryStack, itemList, player.registryAccess());
            player.getInventory().setChanged();
        }

        return count;
    }

    private NonNullList<ItemStack> getItemList(ItemStack itemStack, HolderLookup.Provider registries) {
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        CompoundTag rootTag = itemStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        if(rootTag != null && rootTag.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
            CompoundTag entityTag = itemStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
            if(entityTag.contains("Items", Tag.TAG_LIST)) {
                ContainerHelper.loadAllItems(entityTag, itemStacks, registries);
            }
        }
        return itemStacks;
    }

    private void setItemList(ItemStack itemStack, NonNullList<ItemStack> itemStacks, HolderLookup.Provider registries) {
        CompoundTag rootTag = itemStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        if(!rootTag.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
            rootTag.put("BlockEntityTag", new CompoundTag());
        }
        itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(rootTag));
        ContainerHelper.saveAllItems(itemStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag(), itemStacks, registries);
    }
}
