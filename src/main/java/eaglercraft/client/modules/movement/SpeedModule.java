package eaglercraft.client.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/** Simple horizontal speed adjustment using the Eaglercraft 1.14 motion API. */
public class SpeedModule {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isEnabled = false;
    private static float speedMultiplier = 1.5f;
    private static float maxSpeed = 1.0f;

    public static void enable() {
        isEnabled = true;
        System.out.println("[Speed] Enabled with multiplier: " + speedMultiplier);
    }

    public static void disable() {
        isEnabled = false;
        System.out.println("[Speed] Disabled");
    }

    public static void onTick() {
        if (!isEnabled) return;

        ClientPlayerEntity player = mc.player;
        if (player == null || player.isInWater()) return;

        KeyBinding forward = mc.gameSettings.keyBindForward;
        KeyBinding back = mc.gameSettings.keyBindBack;
        KeyBinding left = mc.gameSettings.keyBindLeft;
        KeyBinding right = mc.gameSettings.keyBindRight;

        if (forward.isKeyDown() || back.isKeyDown() || left.isKeyDown() || right.isKeyDown()) {
            Vec3d motion = player.getMotion();
            double currentSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            if (currentSpeed < maxSpeed) {
                double motionLength = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                if (motionLength > 0.0D) {
                    player.setMotion(
                            motion.x / motionLength * maxSpeed * speedMultiplier,
                            motion.y,
                            motion.z / motionLength * maxSpeed * speedMultiplier);
                }
            }
        }
    }

    public static void setMultiplier(float multiplier) {
        speedMultiplier = Math.max(0.1f, multiplier);
    }

    public static float getMultiplier() {
        return speedMultiplier;
    }

    public static boolean isActive() {
        return isEnabled;
    }
}
