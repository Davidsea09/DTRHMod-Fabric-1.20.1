package net.emanueljdf09.dtrhmod.menu;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.menu.handler.TeapotScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<TeapotScreenHandler> TEAPOT_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(DownTheRabbitHole.MOD_ID, "teapot_screen"),
                    new ExtendedScreenHandlerType<>(TeapotScreenHandler::new));

    public static void registerScreenHandlers() {
        DownTheRabbitHole.LOGGER.info("Registering Screen Handlers for " + DownTheRabbitHole.MOD_ID);
    }
}
