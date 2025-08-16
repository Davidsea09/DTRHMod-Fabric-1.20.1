package net.emanueljdf09.dtrhmod.menu.screen;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class BookCoverScreen extends Screen {

    public static Identifier coverTexture;
    public static final Identifier COVER_TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/book_first.png");
    private final ItemStack bookStack;
    protected int leftPos;
    protected int topPos;
    private ButtonWidget openBookButton;


        public BookCoverScreen(ItemStack bookStack) {
            super(NarratorManager.EMPTY);
            this.bookStack = bookStack;

            if (bookStack.isOf(ModItems.AURORA_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/aurora_cover.png");
            } else if (bookStack.isOf(ModItems.CINDERELLA_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/cinder_cover.png");
            } else {
                coverTexture = COVER_TEXTURE; // fallback
            }
        }

        @Override
        protected void init() {
            this.leftPos = (this.width - 150) / 2;
            this.topPos = (this.height - 180) / 2;
            createOpenBookButton();

        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.renderBackground(context);
            this.renderCover(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
        }

    protected void createOpenBookButton() {
        this.openBookButton = this.addDrawableChild(new TexturedButtonWidget(this.leftPos + 63, this.topPos + 89, 27, 29, 197, 4, 35, coverTexture, 256, 256, (button) -> this.openBook()));
        openBookButton.setTooltip(Tooltip.of(Text.translatable("spectatorMenu.next_page")));
        this.openBookButton = this.addDrawableChild(openBookButton);
    }

    protected void openBook() {
        MinecraftClient.getInstance()
                .setScreen(new StoryBookScreen(
                        new StoryBookScreen.WrittenBookContents(bookStack), bookStack
                ));
    }

    public void renderCover(DrawContext context, int mouseX, int mouseY, float partialTick) {
        context.drawTexture(coverTexture, this.leftPos, this.topPos, 150, 180, 0.0F, 0.0F, 150, 180, 256, 256);
    }

}
