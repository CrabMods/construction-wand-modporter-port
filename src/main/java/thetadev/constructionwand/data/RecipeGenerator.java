package thetadev.constructionwand.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import thetadev.constructionwand.ConstructionWand;
import thetadev.constructionwand.crafting.RecipeWandUpgrade;
import thetadev.constructionwand.items.ModItems;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;



public class RecipeGenerator extends RecipeProvider
{
    public RecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput consumer) {
        wandRecipe(consumer, ModItems.WAND_STONE.get(), Inp.fromTag(ItemTags.STONE_TOOL_MATERIALS));
        wandRecipe(consumer, ModItems.WAND_IRON.get(), Inp.fromTag(Tags.Items.INGOTS_IRON));
        wandRecipe(consumer, ModItems.WAND_DIAMOND.get(), Inp.fromTag(Tags.Items.GEMS_DIAMOND));
        wandRecipe(consumer, ModItems.WAND_INFINITY.get(), Inp.fromTag(Tags.Items.NETHER_STARS));

        coreRecipe(consumer, ModItems.CORE_ANGEL.get(), Inp.fromTag(Tags.Items.FEATHERS), Inp.fromTag(Tags.Items.INGOTS_GOLD));
        coreRecipe(consumer, ModItems.CORE_DESTRUCTION.get(), Inp.fromTag(Tags.Items.STORAGE_BLOCKS_DIAMOND), Inp.fromItem(Items.DIAMOND_PICKAXE));

        specialRecipe(consumer, RecipeWandUpgrade::new, BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeWandUpgrade.SERIALIZER));
    }

    private void wandRecipe(RecipeOutput consumer, ItemLike wand, Inp material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
                .define('X', material.ingredient)
                .define('#', Tags.Items.RODS_WOODEN)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_item", inventoryTrigger(material.predicate))
                .save(consumer);
    }

    private void coreRecipe(RecipeOutput consumer, ItemLike core, Inp item1, Inp item2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, core)
                .define('O', item1.ingredient)
                .define('X', item2.ingredient)
                .define('#', Tags.Items.GLASS_PANES)
                .pattern(" #X")
                .pattern("#O#")
                .pattern("X# ")
                .unlockedBy("has_item", inventoryTrigger(item1.predicate))
                .save(consumer);
    }

    private void specialRecipe(RecipeOutput consumer, java.util.function.Function<net.minecraft.world.item.crafting.CraftingBookCategory, net.minecraft.world.item.crafting.Recipe<?>> recipeFactory, ResourceLocation name) {
        SpecialRecipeBuilder.special(recipeFactory).save(consumer, ConstructionWand.loc("dynamic/" + name.getPath()).toString());
    }
}