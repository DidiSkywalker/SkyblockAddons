package codes.biscuit.skyblockaddons.mixins;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.utils.CooldownManager;
import codes.biscuit.skyblockaddons.utils.Feature;
import codes.biscuit.skyblockaddons.utils.InventoryUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    @Redirect(method = "showDurabilityBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemDamaged()Z", ordinal = 0))
    private boolean showDurabilityBar(ItemStack stack) { //Item item, ItemStack stacks
        SkyblockAddons main = SkyblockAddons.getInstance();
        if (main.getUtils().isOnSkyblock() && main.getConfigValues().isEnabled(Feature.SHOW_ITEM_COOLDOWNS)) {
            if(CooldownManager.isOnCooldown(stack)) {
                return true;
            }
            // Combat timer displaying on the Warp to: xxx skulls in the Skyblock Menu
            if(main.getConfigValues().isEnabled(Feature.SHOW_ITEM_COOLDOWNS)
                    && stack.hasDisplayName()
                    && stack.getDisplayName().startsWith("§bWarp to")
                    && CooldownManager.isOnCooldown(CooldownManager.COMBAT_TIMER_ID)) {
                return true;
            }

            // Combat timer displaying on the Skyblock Menu as long as it's below 3s
            if(main.getConfigValues().isEnabled(Feature.COMBAT_TIMER)
                    && InventoryUtils.SKYBLOCK_MENU_ID.equals(InventoryUtils.getSkyBlockItemID(stack))
                    && CooldownManager.isOnCooldown(CooldownManager.COMBAT_TIMER_ID)
                    && CooldownManager.getElapsedTime(CooldownManager.COMBAT_TIMER_ID) < CooldownManager.COMBAT_TIMER_SKYBLOCK_MENU_COOLDOWN) {
                return true;
            }
        }
        return stack.isItemDamaged();
    }

    @Inject(method = "getDurabilityForDisplay", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void showDurabilityBar(ItemStack stack, CallbackInfoReturnable<Double> cir) { //Item item, ItemStack stack
        SkyblockAddons main = SkyblockAddons.getInstance();
        if (main.getUtils().isOnSkyblock() && main.getConfigValues().isEnabled(Feature.SHOW_ITEM_COOLDOWNS)) {
            if(CooldownManager.isOnCooldown(stack)) {
                cir.setReturnValue(CooldownManager.getRemainingCooldownPercent(stack));
            }
            // Combat timer displaying on the Warp to: xxx skulls in the Skyblock Menu
            if(main.getConfigValues().isEnabled(Feature.SHOW_ITEM_COOLDOWNS)
                    && stack.hasDisplayName()
                    && stack.getDisplayName().startsWith("§bWarp to")
                    && CooldownManager.isOnCooldown(CooldownManager.COMBAT_TIMER_ID)) {
                cir.setReturnValue(CooldownManager.getRemainingCooldownPercent(CooldownManager.COMBAT_TIMER_ID));
            }
            // Combat timer displaying on the Skyblock Menu as long as it's below 3s
            if(main.getConfigValues().isEnabled(Feature.COMBAT_TIMER)
                    && InventoryUtils.SKYBLOCK_MENU_ID.equals(InventoryUtils.getSkyBlockItemID(stack))
                    && CooldownManager.isOnCooldown(CooldownManager.COMBAT_TIMER_ID)
                    && CooldownManager.getElapsedTime(CooldownManager.COMBAT_TIMER_ID) < CooldownManager.COMBAT_TIMER_SKYBLOCK_MENU_COOLDOWN) {
                cir.setReturnValue(CooldownManager.getElapsedTime(CooldownManager.COMBAT_TIMER_ID) / 3000d);
            }
        }
    }
}
