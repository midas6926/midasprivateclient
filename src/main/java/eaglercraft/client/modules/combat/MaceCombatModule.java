package eaglercraft.client.modules.combat;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class MaceCombatModule {
    private static final Map<String, MaceBehavior> MACE_BEHAVIORS = new HashMap<>();
    private static boolean enabled;

    public interface MaceBehavior {
        void onMaceAttack(PlayerEntity player, ItemStack mace);
        void onMaceSmash(PlayerEntity player, double fallDistance);
        float getSmashDamage(double fallDistance);
    }

    static {
        registerDefaultBehaviors();
    }

    private static void registerDefaultBehaviors() {
        MACE_BEHAVIORS.put("smash_attack", new MaceBehavior() {
            public void onMaceAttack(PlayerEntity player, ItemStack mace) {
                System.out.println("[MaceCombat] Mace attack initiated");
            }

            public void onMaceSmash(PlayerEntity player, double fallDistance) {
                float damage = getSmashDamage(fallDistance);
                System.out.println("[MaceCombat] Smash attack! Fall distance: " + fallDistance + ", Damage: " + damage);
                applySmashEffect(player, damage);
            }

            public float getSmashDamage(double fallDistance) {
                return fallDistance < 1.5 ? 6.0f : 6.0f + (float) ((fallDistance - 1.5f) * 2f);
            }
        });
    }

    public static void enable() { enabled = true; }
    public static void disable() { enabled = false; }
    public static boolean isEnabled() { return enabled; }

    public static boolean isMace(ItemStack item) {
        if (item == null || item.isEmpty() || item.getTag() == null) return false;
        return "mace".equals(item.getTag().getString("via_identifier"));
    }

    public static float calculateSmashDamage(double fallDistance) {
        return fallDistance < 1.5 ? 6.0f : 6.0f + (float) ((fallDistance - 1.5f) * 2f);
    }

    private static void applySmashEffect(PlayerEntity player, float damage) {
        ItemStack item = player.getHeldItemMainhand();
        CompoundNBT tag = item.getOrCreateTag();
        tag.putFloat("smash_damage", damage);
        tag.putLong("last_smash_time", System.currentTimeMillis());
    }

    public static float getLastSmashDamage(ItemStack mace) {
        return mace == null || mace.getTag() == null ? 0.0f : mace.getTag().getFloat("smash_damage");
    }

    public static int getDensityLevel(ItemStack mace) {
        return mace == null || mace.getTag() == null ? 0 : mace.getTag().getInt("enchantment_density");
    }

    public static int getBreachLevel(ItemStack mace) {
        return mace == null || mace.getTag() == null ? 0 : mace.getTag().getInt("enchantment_breach");
    }

    public static int getWindBurstLevel(ItemStack mace) {
        return mace == null || mace.getTag() == null ? 0 : mace.getTag().getInt("enchantment_wind_burst");
    }

    public static void registerBehavior(String name, MaceBehavior behavior) { MACE_BEHAVIORS.put(name, behavior); }
    public static MaceBehavior getBehavior(String name) { return MACE_BEHAVIORS.get(name); }
}
