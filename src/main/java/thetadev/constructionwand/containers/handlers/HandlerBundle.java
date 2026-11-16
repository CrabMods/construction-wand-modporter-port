package thetadev.constructionwand.containers.handlers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import thetadev.constructionwand.api.IContainerHandler;
import thetadev.constructionwand.basics.WandUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.HolderLookup;


public class HandlerBundle implements IContainerHandler
{
    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        return inventoryStack != null && inventoryStack.getCount() == 1 && inventoryStack.getItem() == Items.BUNDLE;
    }

    @Override
    public int countItems(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        return getContents(inventoryStack, player.registryAccess()).filter((stack) -> WandUtil.stackEquals(stack, itemStack))
                .map(ItemStack::getCount).reduce(0, Integer::sum);
    }

    @Override
    public int useItems(Player player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        AtomicInteger newCount = new AtomicInteger(count);

        List<ItemStack> itemStacks = getContents(inventoryStack, player.registryAccess()).filter((stack -> {
            if(WandUtil.stackEquals(stack, itemStack)) {
                int toTake = Math.min(newCount.get(), stack.getCount());
                stack.shrink(toTake);
                newCount.set(newCount.get() - toTake);
            }
            return !stack.isEmpty();
        })).toList();

        setItemList(inventoryStack, itemStacks, player.registryAccess());

        return newCount.get();
    }

    private Stream<ItemStack> getContents(ItemStack bundleStack, HolderLookup.Provider registries) {
        CompoundTag compoundtag = bundleStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        if(compoundtag == null) {
            return Stream.empty();
        }
        else {
            ListTag listtag = compoundtag.getList("Items", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(tag -> ItemStack.parseOptional(registries, tag));
        }
    }

    private void setItemList(ItemStack itemStack, List<ItemStack> itemStacks, HolderLookup.Provider registries) {
        CompoundTag rootTag = itemStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        ListTag listTag = new ListTag();
        rootTag.put("Items", listTag);
        itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(rootTag));

        for(ItemStack stack : itemStacks) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(registries, itemTag);
            listTag.add(itemTag);
        }
    }
}
