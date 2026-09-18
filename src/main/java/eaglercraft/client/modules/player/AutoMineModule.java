package eaglercraft.client.modules.player;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

public class AutoMineModule {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isEnabled = false;
    private static float breakSpeed = 1.0f;
    private static BlockPos targetBlock;
    private static float blockBreakProgress;

    public static void enable() {
        isEnabled = true;
        blockBreakProgress = 0.0f;
        targetBlock = null;
        System.out.println("[AutoMine] Enabled");
    }

    public static void disable() {
        isEnabled = false;
        blockBreakProgress = 0.0f;
        targetBlock = null;
        System.out.println("[AutoMine] Disabled");
    }

    public static void onTick() {
        if (!isEnabled || mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;
        RayTraceResult rayTrace = mc.objectMouseOver;
        if (rayTrace == null || rayTrace.getType() != RayTraceResult.Type.BLOCK) {
            blockBreakProgress = 0.0f;
            targetBlock = null;
            return;
        }

        BlockPos blockPos = ((BlockRayTraceResult) rayTrace).getPos();
        BlockState blockState = mc.world.getBlockState(blockPos);
        if (blockState.isAir() || blockState.getBlock() == Blocks.AIR) {
            blockBreakProgress = 0.0f;
            targetBlock = null;
            return;
        }

        if (!blockPos.equals(targetBlock)) {
            targetBlock = blockPos;
            blockBreakProgress = 0.0f;
        }

        blockBreakProgress += blockState.getPlayerRelativeBlockHardness(player, mc.world, blockPos) * breakSpeed;
        if (blockBreakProgress >= 1.0f) {
            mc.playerController.onPlayerDestroyBlock(blockPos);
            blockBreakProgress = 0.0f;
            targetBlock = null;
        }
    }

    public static void setBreakSpeed(float speed) {
        breakSpeed = Math.max(0.1f, Math.min(speed, 10.0f));
    }

    public static boolean isActive() {
        return isEnabled;
    }
}
