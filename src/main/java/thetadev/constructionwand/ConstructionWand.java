package thetadev.constructionwand;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thetadev.constructionwand.basics.ConfigClient;
import thetadev.constructionwand.basics.ConfigServer;
import thetadev.constructionwand.basics.ModStats;
import thetadev.constructionwand.client.ClientEvents;
import thetadev.constructionwand.client.RenderBlockPreview;
import thetadev.constructionwand.containers.ContainerManager;
import thetadev.constructionwand.containers.ContainerRegistrar;
import thetadev.constructionwand.items.ModItems;
import thetadev.constructionwand.network.PacketQueryUndo;
import thetadev.constructionwand.network.PacketUndoBlocks;
import thetadev.constructionwand.network.PacketWandOption;
import thetadev.constructionwand.wand.undo.UndoHistory;
import net.neoforged.fml.ModContainer;
import thetadev.constructionwand.network.ModNetwork;
import net.neoforged.bus.api.IEventBus;


@Mod(ConstructionWand.MODID)
public class ConstructionWand {

    public static final String MODID = "constructionwand";

    public static final String MODNAME = "ConstructionWand";

    public static ConstructionWand instance;

    public static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = "1";

public ContainerManager containerManager;

    public UndoHistory undoHistory;

    public RenderBlockPreview renderBlockPreview;

    @SuppressWarnings("this-escape")
    public ConstructionWand(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;
        containerManager = new ContainerManager();
        undoHistory = new UndoHistory();
        // Register setup methods for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
// Register Item DeferredRegister
        ModItems.ITEMS.register(modEventBus);
        ModStats.register(modEventBus);
        modEventBus.addListener(thetadev.constructionwand.items.ModItems::addCreative);
        if (net.neoforged.fml.loading.FMLLoader.getDist() == net.neoforged.api.distmarker.Dist.CLIENT) {
            modEventBus.addListener(thetadev.constructionwand.items.ModItems::registerItemColors);
        }
        modEventBus.addListener(thetadev.constructionwand.items.ModItems::registerRecipeSerializers);
        modEventBus.addListener(thetadev.constructionwand.data.ModData::gatherData);
        // Config setup
        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigServer.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigClient.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("ConstructionWand says hello - may the odds be ever in your favor.");
        // Register packets
// Container registry
        ContainerRegistrar.register();
        // Stats
}

    private void clientSetup(final FMLClientSetupEvent event) {
        renderBlockPreview = new RenderBlockPreview();
        NeoForge.EVENT_BUS.register(renderBlockPreview);
        NeoForge.EVENT_BUS.register(new ClientEvents());
        event.enqueueWork(ModItems::registerModelProperties);
    }

    public static ResourceLocation loc(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
