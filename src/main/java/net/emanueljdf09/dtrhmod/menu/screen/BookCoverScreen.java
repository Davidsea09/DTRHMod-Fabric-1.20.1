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
            }  else if (bookStack.isOf(ModItems.RED_RIDING_HOOD_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/lrrh_cover.png");
            }  else if (bookStack.isOf(ModItems.THE_LITTLE_MERMAID_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/seashell_cover.png");
            }   else if (bookStack.isOf(ModItems.JACK_AND_THE_BEANSTALK_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/jb_cover.png");
            }  else if (bookStack.isOf(ModItems.RAPUNZEL_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/hair_cover.png");
            }  else if (bookStack.isOf(ModItems.THREE_LITTLE_PIGS_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/tlp_cover.png");
            }  else if (bookStack.isOf(ModItems.SNOW_WHITE_STORYBOOK)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/sw_cover.png");
            } else {
                coverTexture = COVER_TEXTURE;
            }
        }

        private String triggerKey = null;

        public BookCoverScreen(String triggerKey) {
            super(NarratorManager.EMPTY);
            this.bookStack = ItemStack.EMPTY;
            this.triggerKey = triggerKey;

            if ("wonderland_intro".equals(triggerKey)) {
                coverTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/enter_wonder_cover.png");
            } else {
                coverTexture = COVER_TEXTURE;
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
        this.openBookButton = this.addDrawableChild(new TexturedButtonWidget(this.leftPos + 22, this.topPos + 67, 107, 33, 149, 0, 37, coverTexture, 256, 256, (button) -> this.openBook()));
        openBookButton.setTooltip(Tooltip.of(Text.translatable("cover.title.open")));
        this.openBookButton = this.addDrawableChild(openBookButton);
    }

    protected void openBook() {
        if (this.triggerKey != null) {
            MinecraftClient.getInstance().setScreen(new StoryBookScreen(this.triggerKey));
        } else {
            MinecraftClient.getInstance()
                    .setScreen(new StoryBookScreen(
                            new StoryBookScreen.WrittenBookContents(bookStack), bookStack
                    ));
        }
    }

    public void renderCover(DrawContext context, int mouseX, int mouseY, float partialTick) {
        context.drawTexture(coverTexture, this.leftPos, this.topPos, 150, 180, 0.0F, 0.0F, 150, 180, 256, 256);
    }

}
