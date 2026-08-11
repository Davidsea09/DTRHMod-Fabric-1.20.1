package net.emanueljdf09.dtrhmod.item.custom;

import net.emanueljdf09.dtrhmod.item.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DynamicTeaCupItem extends Item {
    public DynamicTeaCupItem(Settings settings) {
        super(settings);
    }

    public static List<StatusEffectInstance> getEffectsFromStack(ItemStack stack) {
        List<StatusEffectInstance> effects = new ArrayList<>();
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("Effects", 9)) {
            NbtList effectsList = nbt.getList("Effects", 10);
            for (int i = 0; i < effectsList.size(); i++) {
                StatusEffectInstance effect = StatusEffectInstance.fromNbt(effectsList.getCompound(i));
                if (effect != null) {
                    effects.add(effect);
                }
            }
        }
        return effects;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, java.util.List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("Effects", 9)) {
            NbtList effectsList = nbt.getList("Effects", 10);
            for (int i = 0; i < effectsList.size(); i++) {
                StatusEffectInstance effect = StatusEffectInstance.fromNbt(effectsList.getCompound(i));
                if (effect != null) {
                    MutableText effectText = Text.translatable(effect.getEffectType().getTranslationKey());

                   if (effect.getAmplifier() > 0) {
                        effectText.append(" ")
                                .append(Text.translatable("potion.potency." + effect.getAmplifier()));
                    }

                    int durationSeconds = effect.getDuration() / 20;
                    String timeString = String.format("%d:%02d", durationSeconds / 60, durationSeconds % 60);

                    effectText.append(" (").append(timeString).append(")");

                    tooltip.add(effectText.formatted(effect.getEffectType().getCategory().getFormatting()));
                }
            }
        } else {
            tooltip.add(Text.translatable("tooltip.dtrhmod.empty_tea").formatted(Formatting.GRAY));
        }
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {

            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains("Effects", 9)) {
                NbtList effectsList = nbt.getList("Effects", 10);
                for (int i = 0; i < effectsList.size(); i++) {
                    NbtCompound effectNbt = effectsList.getCompound(i);
                    StatusEffectInstance effect = StatusEffectInstance.fromNbt(effectNbt);
                    if (effect != null) {
                        user.addStatusEffect(new StatusEffectInstance(effect));
                    }
                }
            }
        }


        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            stack.decrement(1);
            if (stack.isEmpty()) {
                return new ItemStack(ModItems.EMPTY_CUP);
            }
            player.getInventory().insertStack(new ItemStack(ModItems.EMPTY_CUP));
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
