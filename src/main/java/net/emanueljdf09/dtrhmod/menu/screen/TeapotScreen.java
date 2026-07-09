package net.emanueljdf09.dtrhmod.menu.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.menu.handler.TeapotScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TeapotScreen extends HandledScreen {
    private static final Identifier TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/teapot_screen.png");

    public TeapotScreen(TeapotScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // Match these exactly to your image's width and height dimensions
        this.backgroundWidth = 256;
        this.backgroundHeight = 256;

        // Adjusts where the title text renders on the GUI box
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        // Centers the GUI on the player's monitor automatically
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, partialTick);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // 1. Draw the clean main background box (assumed 176x166 pixels)
        int bgWidth = 176;
        int bgHeight = 166;
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, bgWidth, bgHeight, 256, 256);

        // 2. Draw the Boiling Steam/Fire Indicator
        if (this.handler.isCrafting()) {
            // Calculate how much of the fire should be drawn based on the recipe progress
            // Let's assume your fire graphic height is 14 pixels max
            int burnHeight = this.handler.getScaledProgress(14);

            // Target position on your GUI (above the middle slot):
            int fireTargetX = this.x + 44; // Match center slot X alignment
            int fireTargetY = this.y + 36; // Position between ingredient slots and middle base

            // UV Coordinates where your floating fire asset lives on the texture sheet:
            int fireSourceU = 176; // Just past the right edge of the main GUI box
            int fireSourceV = 0;   // At the top edge of the image
            int fireWidth = 14;    // Width of your flame asset

            // Draw the burning overlay scaling from the bottom up
            context.drawTexture(TEXTURE,
                    fireTargetX, fireTargetY + 14 - burnHeight,
                    fireSourceU, fireSourceV + 14 - burnHeight,
                    fireWidth, burnHeight,
                    256, 256
            );
        }

        // 3. Draw the Progress Arrow Overlay
        if (this.handler.isCrafting()) {
            // Let's assume your arrow width is 24 pixels max
            int arrowWidth = this.handler.getScaledProgress(24);

            // Target position on your GUI (the gray arrow pointing to the output slot):
            int arrowTargetX = this.x + 92; // Positioning it between the slots and output
            int arrowTargetY = this.y + 35;

            // UV Coordinates where your floating arrow asset lives:
            int arrowSourceU = 176; // Stored next to the fire asset
            int arrowSourceV = 14;  // Sitting right beneath the fire asset
            int arrowHeight = 17;   // Height of your arrow asset

            // Draw the progress filling in from left to right
            context.drawTexture(TEXTURE,
                    arrowTargetX, arrowTargetY,
                    arrowSourceU, arrowSourceV,
                    arrowWidth, arrowHeight,
                    256, 256
            );
        }
    }
}

