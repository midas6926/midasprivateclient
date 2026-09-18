package eaglercraft.client.viaitems;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ViaItemsManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean initialized;

    public static void init() {
        if (initialized) return;
        ViaItemsRegistry.enable();
        initialized = true;
    }

    public static void shutdown() {
        ViaItemsRegistry.disable();
        initialized = false;
    }

    public static ItemStack createViaItem(String identifier, int count) {
        if (!ViaItemsRegistry.isEnabled()) return null;
        Integer itemId = ViaItemsMappings.getItemId(identifier);
        if (itemId == null || ViaItemsMappings.getMapping(itemId) == null) return null;

        ItemStack item = new ItemStack(null, count);
        CompoundNBT tag = item.getOrCreateTag();
        tag.putString("via_identifier", identifier);
        tag.putInt("via_item_id", itemId);
        tag.putBoolean("is_via_item", true);

        ViaItemsRegistry.ViaItemHandler handler = ViaItemsRegistry.getHandler(identifier);
        if (handler != null) handler.onItemCreated(item);
        return item;
    }

    public static ItemStack createViaItem(String identifier) { return createViaItem(identifier, 1); }

    public static boolean isViaItem(ItemStack item) {
        return item != null && item.getTag() != null && item.getTag().getBoolean("is_via_item");
    }

    public static String getViaItemIdentifier(ItemStack item) {
        return isViaItem(item) ? item.getTag().getString("via_identifier") : null;
    }

    public static void useViaItem(ItemStack item) {
        if (!isViaItem(item)) return;
        ViaItemsRegistry.ViaItemHandler handler = ViaItemsRegistry.getHandler(getViaItemIdentifier(item));
        if (handler != null) handler.onItemUsed(item);
    }

    public static void listAllViaItems() {
        System.out.println("[ViaItems] Available items: " + ViaItemsMappings.size());
    }

    public static boolean isInitialized() { return initialized; }
}
