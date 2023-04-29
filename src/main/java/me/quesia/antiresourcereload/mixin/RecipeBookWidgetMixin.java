package me.quesia.antiresourcereload.mixin;

import me.quesia.antiresourcereload.AntiResourceReload;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {
    @Inject(method = "initialize", at = @At("HEAD"))
    public void antiresourcereload$updateHasSeenRecipes(CallbackInfo ci){
        AntiResourceReload.HAS_SEEN_RECIPES = true;
    }
}
