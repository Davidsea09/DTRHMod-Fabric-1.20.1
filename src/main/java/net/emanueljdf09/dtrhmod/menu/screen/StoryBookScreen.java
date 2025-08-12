package net.emanueljdf09.dtrhmod.menu.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class StoryBookScreen extends Screen {

    private final String storyType;
    private final List<Text> pages;
    private int pageIndex = -1; // -1 = cover, pages start at 0

    private static final int BG_WIDTH = 192;
    private static final int BG_HEIGHT = 192;

    public StoryBookScreen(String storyType, List<Text> pages) {
        super(Text.literal("Story Book"));
        this.storyType = storyType;
        this.pages = pages;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        int x = (this.width - BG_WIDTH) / 2;
        int y = (this.height - BG_HEIGHT) / 2;

        Identifier texture;
        if (pageIndex == -1) {
            texture = new Identifier("dtrhmod", "textures/gui/book_" + storyType + "_cover.png");
        } else if (pageIndex >= pages.size()) {
            texture = new Identifier("dtrhmod", "textures/gui/book_" + storyType + "_back.png");
        } else {
            texture = new Identifier("dtrhmod", "textures/gui/book_" + storyType + "_pages.png");
        }

        RenderSystem.setShaderTexture(0, texture);
        context.drawTexture(texture, x, y, 0, 0, BG_WIDTH, BG_HEIGHT);

        // Render page text
        if (pageIndex >= 0 && pageIndex < pages.size()) {
            // Left page
            String leftText = pages.get(pageIndex).getString();
            context.drawText(this.textRenderer, leftText, x + 20, y + 20, 0x000000, false);

            // Right page
            if (pageIndex + 1 < pages.size()) {
                String rightText = pages.get(pageIndex + 1).getString();
                context.drawText(this.textRenderer, rightText, x + 110, y + 20, 0x000000, false);
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            pageIndex += 2; // advance by spread
            if (pageIndex > pages.size()) {
                this.close();
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}