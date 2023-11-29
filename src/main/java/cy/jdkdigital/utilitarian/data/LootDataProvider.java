package cy.jdkdigital.utilitarian.data;

import com.google.common.collect.Maps;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class LootDataProvider implements DataProvider
{
    private final PackOutput.PathProvider pathProvider;
    private final List<LootTableProvider.SubProviderEntry> subProviders;

    public LootDataProvider(PackOutput output, List<LootTableProvider.SubProviderEntry> providers) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        this.subProviders = providers;
    }

    @Override
    public String getName() {
        return "Productive Trees Block Loot Table datagen";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        final Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        this.subProviders.forEach((providerEntry) -> {
            providerEntry.provider().get().generate((resourceLocation, builder) -> {
                builder.setRandomSequence(resourceLocation);
                if (map.put(resourceLocation, builder.setParamSet(providerEntry.paramSet()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + resourceLocation);
                }
            });
        });

        return CompletableFuture.allOf(map.entrySet().stream().map((entry) -> {
            return DataProvider.saveStable(cache, LootDataType.TABLE.parser().toJsonTree(entry.getValue()), this.pathProvider.json(entry.getKey()));
        }).toArray(CompletableFuture[]::new));
    }

    public static class LootProvider extends BlockLootSubProvider
    {
        private static final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();
        private List<Block> knownBlocks = new ArrayList<>();

        public LootProvider() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            for (DyeColor color: DyeColor.values()) {
                dropSelf(NoSolicitingModule.SOLICITING_CARPET.get(color).get());
                dropSelf(NoSolicitingModule.TRAPPED_SOLICITING_CARPET.get(color).get());
            }
            dropSelf(NoSolicitingModule.NO_SOLICITING_BANNER.get());
            dropOther(NoSolicitingModule.NO_SOLICITING_WALL_BANNER.get(), NoSolicitingModule.NO_SOLICITING_BANNER.get());
        }

        @Override
        protected void add(Block block, LootTable.Builder builder) {
            super.add(block, builder);
            knownBlocks.add(block);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return knownBlocks;
        }

        protected void add(Block block, Function<Block, LootTable.Builder> builderFunction) {
            this.add(block, builderFunction.apply(block));
        }

        public void dropSelf(@NotNull Block block) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, LootProvider::genOptionalBlockDrop);
            this.add(block, func.apply(block));
        }

        public void dropOther(@NotNull Block block, @NotNull Block otherBlock) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, LootProvider::genOptionalBlockDrop);
            this.add(block, func.apply(otherBlock));
        }

        protected static LootTable.Builder genOptionalBlockDrop(Block block) {
            LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(block).when(ExplosionCondition.survivesExplosion());

            return LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(builder));
        }
    }
}
