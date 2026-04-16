package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.*;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelProvider extends net.minecraft.client.data.models.ModelProvider
{
    protected final Map<Identifier, ModelInstance> models = new HashMap<>();

    public ModelProvider(PackOutput output) {
        super(output, Utilitarian.MODID);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // No Soliciting
        var carpetTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Utilitarian.MODID, "block/soliciting_carpet")), Optional.empty(), TextureSlot.WOOL);
        for (DyeColor color : DyeColor.values()) {
            Block woolBlock = BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(color.getSerializedName() + "_wool"));
            TexturedModel carpetModel = new TexturedModel(TextureMapping.wool(woolBlock), carpetTemplate);
            blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(NoSolicitingModule.SOLICITING_CARPET.get(color).get(), BlockModelGenerators.plainVariant(carpetModel.create(NoSolicitingModule.SOLICITING_CARPET.get(color).get(), blockModels.modelOutput))));
            blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(NoSolicitingModule.TRAPPED_SOLICITING_CARPET.get(color).get(), BlockModelGenerators.plainVariant(carpetModel.create(NoSolicitingModule.TRAPPED_SOLICITING_CARPET.get(color).get(), blockModels.modelOutput))));
        }
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(NoSolicitingModule.NO_SOLICITING_BANNER.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.WHITE_BANNER))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(NoSolicitingModule.NO_SOLICITING_WALL_BANNER.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.WHITE_WALL_BANNER))));
        blockModels.createTrivialCube(NoSolicitingModule.NO_RAIDER_BLOCK.get());

        Identifier restrainingOrderModelLocation = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "item/restraining_order");
        Identifier activeRestrainingOrderModelLocation = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "item/restraining_order_active");
        blockModels.itemModelOutput
                .accept(
                        NoSolicitingModule.RESTRAINING_ORDER.get(),
                        ItemModelUtils.conditional(
                                ItemModelUtils.hasComponent(NoSolicitingModule.ACTIVE.get()),
                                ItemModelUtils.plainModel(activeRestrainingOrderModelLocation),
                                ItemModelUtils.plainModel(restrainingOrderModelLocation)
                        )
                );

        // Snad module
        createGrassLikeBlock(blockModels, SnadModule.GRRASS_BLOCK.get());
        blockModels.registerSimpleTintedItemModel(SnadModule.GRRASS_BLOCK.get(), ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK), new GrassColorSource());
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(SnadModule.CURSED_GRRASS_BLOCK.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(SnadModule.CURSED_GRRASS_BLOCK.get()))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(SnadModule.SNAD_BLOCK.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.SAND))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(SnadModule.RED_SNAD_BLOCK.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.RED_SAND))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(SnadModule.SOUL_SNAD_BLOCK.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.SOUL_SAND))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(SnadModule.DRIT_BLOCK.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.DIRT))));

        // Utility module
        blockModels.createTrivialCube(UtilityBlockModule.ANGEL_BLOCK.get());
        blockModels.createTrivialCube(UtilityBlockModule.SOUND_MUFFLER.get());
        createHopper(blockModels, UtilityBlockModule.FLUID_HOPPER_BLOCK.get());
        createLamp(blockModels, UtilityBlockModule.INVERTED_REDSTONE_LAMP.get(), Blocks.REDSTONE_LAMP, true);
        createLamp(blockModels, UtilityBlockModule.INVERTED_LAPIS_LAMP.get(), UtilityBlockModule.LAPIS_LAMP.get(), true);
        createLamp(blockModels, UtilityBlockModule.LAPIS_LAMP.get(), UtilityBlockModule.LAPIS_LAMP.get(), false);
        blockModels.createTrivialCube(UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get());
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(UtilityBlockModule.WELL_BEHAVED_DROPPER.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(UtilityBlockModule.WELL_BEHAVED_DROPPER.get()))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(UtilityBlockModule.MAGNET.get(), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(UtilityBlockModule.MAGNET.get()))));

        // TPS meter
        blockModels.createTrivialCube(TPSMeterModule.TPS_METER.get());

        // Utility items
        blockModels.registerSimpleFlatItemModel(UtilityItemModule.TINY_COAL.get());
        blockModels.registerSimpleFlatItemModel(UtilityItemModule.TINY_CHARCOAL.get());
        blockModels.registerSimpleItemModel(UtilityItemModule.UNNAME_TAG.get(), Identifier.withDefaultNamespace("item/name_tag"));
        Identifier trowelModelLocation = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "item/trowel");
        Identifier activeTrowelModelLocation = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "item/trowel_extended");
        blockModels.itemModelOutput
                .accept(
                        UtilityItemModule.TROWEL.get(),
                        ItemModelUtils.conditional(
                                ItemModelUtils.hasComponent(NoSolicitingModule.ACTIVE.get()),
                                ItemModelUtils.plainModel(activeTrowelModelLocation),
                                ItemModelUtils.plainModel(trowelModelLocation)
                        )
                );
        Identifier slimeBucketModelLocation = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "item/slime_bucket");
        Identifier activeSlimeBucketModelLocation = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "item/slime_bucket_active");
        blockModels.itemModelOutput
                .accept(
                        UtilityItemModule.SLIME_BUCKET.get(),
                        ItemModelUtils.conditional(
                                ItemModelUtils.hasComponent(NoSolicitingModule.ACTIVE.get()),
                                ItemModelUtils.plainModel(activeSlimeBucketModelLocation),
                                ItemModelUtils.plainModel(slimeBucketModelLocation)
                        )
                );
    }

    private static void createGrassLikeBlock(BlockModelGenerators blockModels, Block block) {
        MultiVariant snowyGrass = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK).withPath(p -> p + "_snowy"));
        Identifier plainGrassModel = ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK);
        blockModels.createGrassLikeBlock(block, BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(plainGrassModel)), snowyGrass);
    }

    private static void createHopper(BlockModelGenerators blockModels, Block block) {
        MultiVariant downBlock = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block));
        MultiVariant sideBlock = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block, "_side"));
        blockModels.registerSimpleFlatItemModel(block.asItem());
        blockModels.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(block)
                                .with(
                                        PropertyDispatch.initial(BlockStateProperties.FACING_HOPPER)
                                                .select(Direction.DOWN, downBlock)
                                                .select(Direction.NORTH, sideBlock)
                                                .select(Direction.EAST, sideBlock.with(BlockModelGenerators.Y_ROT_90))
                                                .select(Direction.SOUTH, sideBlock.with(BlockModelGenerators.Y_ROT_180))
                                                .select(Direction.WEST, sideBlock.with(BlockModelGenerators.Y_ROT_270))
                                )
                );
    }

    private static void createLamp(BlockModelGenerators blockModels, Block block, Block base, boolean inverted) {
        MultiVariant off = BlockModelGenerators.plainVariant(TexturedModel.CUBE.get(base).create(block, blockModels.modelOutput));
        MultiVariant on = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_ALL.createWithSuffix(block, "_on", TextureMapping.cube(TextureMapping.getBlockTexture(base, "_on")), blockModels.modelOutput));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, inverted ? off : on, inverted ? on : off)));
    }

    @Override
    public String getName() {
        return "Utilitarian Blockstate and Model generator";
    }
}
