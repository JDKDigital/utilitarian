package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.TPSMeter;
import cy.jdkdigital.utilitarian.common.block.entity.TPSMeterBlockEntity;
import cy.jdkdigital.utilitarian.common.item.TPSMeterItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public class TPSMeterModule
{
    public static RegistryObject<Block> TPS_METER;
    public static RegistryObject<BlockEntityType<TPSMeterBlockEntity>> TPS_METER_BLOCK_ENTITY;
    public static RegistryObject<Item> TPS_METER_ITEM;

    public static void register() {
        TPS_METER = Utilitarian.BLOCKS.register("tps_meter", () -> new TPSMeter(BlockBehaviour.Properties.copy(Blocks.STONE)));
        TPS_METER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("tps_meter", () -> BlockEntityType.Builder.of(TPSMeterBlockEntity::new, TPS_METER.get()).build(null));
        TPS_METER_ITEM = Utilitarian.ITEMS.register("tps_meter", () -> new TPSMeterItem(TPS_METER.get(), new Item.Properties()));
    }

    public static double getTPS(ServerLevel level) {
        long[] times = level.getServer().getTickTime(level.dimension());

        if (times == null) {
            times = new long[]{0};
        }

        double worldTickTime = mean(times) * 1.0E-6D;

        return Math.min(1000.0 / worldTickTime, 20);
    }

    private static long mean(long[] values)
    {
        long sum = 0L;
        for (long v : values)
            sum += v;
        return sum / values.length;
    }
}
