package eaglercraft.client.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.math.Vec3d;

/** Basic client-side flight controls using the Eaglercraft 1.14 API. */
public class FlightModule {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isFlying = false;
    private static float flightSpeed = 0.1f;

    public static void enable() {
        ClientPlayerEntity player = mc.player;
        if (player != null) {
            PlayerAbilities abilities = player.abilities;
            abilities.isFlying = true;
            abilities.allowFlying = true;
            isFlying = true;
            System.out.println("[Flight] Enabled");
        }
    }

    public static void disable() {
        ClientPlayerEntity player = mc.player;
        if (player != null) {
            PlayerAbilities abilities = player.abilities;
            abilities.isFlying = false;
            if (!abilities.isCreativeMode) {
                abilities.allowFlying = false;
            }
            isFlying = false;
            System.out.println("[Flight] Disabled");
        }
    }

    public static void onTick() {
        if (!isFlying) return;

        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        double motionX = 0.0D;
        double motionY = 0.0D;
        double motionZ = 0.0D;

        KeyBinding forward = mc.gameSettings.keyBindForward;
        KeyBinding back = mc.gameSettings.keyBindBack;
        KeyBinding left = mc.gameSettings.keyBindLeft;
        KeyBinding right = mc.gameSettings.keyBindRight;
        KeyBinding jump = mc.gameSettings.keyBindJump;
        KeyBinding sneak = mc.gameSettings.keyBindSneak;

        if (forward.isKeyDown()) motionZ += flightSpeed;
        if (back.isKeyDown()) motionZ -= flightSpeed;
        if (left.isKeyDown()) motionX -= flightSpeed;
        if (right.isKeyDown()) motionX += flightSpeed;
        if (jump.isKeyDown()) motionY += flightSpeed;
        if (sneak.isKeyDown()) motionY -= flightSpeed;

        player.setMotion(new Vec3d(motionX, motionY, motionZ));
    }

    public static boolean isActive() {
        return isFlying;
    }

    public static void setSpeed(float speed) {
        flightSpeed = Math.max(0.0f, Math.min(speed, 1.0f));
    }

    public static float getSpeed() {
        return flightSpeed;
    }
}
