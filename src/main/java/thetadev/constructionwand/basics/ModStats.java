package thetadev.constructionwand.basics;

import net.minecraft.resources.ResourceLocation;
import thetadev.constructionwand.ConstructionWand;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;




public class ModStats {
    private static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, ConstructionWand.MODID);


    public static final ResourceLocation USE_WAND = ResourceLocation.fromNamespaceAndPath(ConstructionWand.MODID, "use_wand");

    static {
        CUSTOM_STATS.register("use_wand", () -> USE_WAND);
    }

    public static void register(IEventBus modEventBus) {
        CUSTOM_STATS.register(modEventBus);
    }

}
