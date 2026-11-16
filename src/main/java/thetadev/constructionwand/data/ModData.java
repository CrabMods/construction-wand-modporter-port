package thetadev.constructionwand.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.minecraft.core.HolderLookup;


public class ModData
{
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        if(event.includeServer()) {
            generator.addProvider(true, new RecipeGenerator(packOutput, event.getLookupProvider()));
        }

        if(event.includeClient()) {
            generator.addProvider(true, new ItemModelGenerator(packOutput, fileHelper));
        }
    }
}
