package eaglercraft.client.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

/** Prevents fall damage by clearing fall distance before damage is applied. */
public class NoFallModule {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isEnabled = false;
    private static float damageThreshold = 3.0f;

    public static void enable() {
        isEnabled = true;
        System.out.println("[NoFall] Enabled");
    }

    public static void disable() {
        isEnabled = false;
        System.out.println("[NoFall] Disabled");
    }

    public static void onTick() {
        if (!isEnabled) return;

        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        if (player.fallDistance > damageThreshold) {
            player.fallDistance = 0.0f;
            player.velocityChanged = true;
        }
    }

    public static void setDamageThreshold(float threshold) {
        damageThreshold = Math.max(0.0f, threshold);
    }

    public static boolean isActive() {
        return isEnabled;
    }
}
