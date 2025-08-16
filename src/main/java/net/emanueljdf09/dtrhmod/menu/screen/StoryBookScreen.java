package net.emanueljdf09.dtrhmod.menu.screen;

import com.google.common.collect.ImmutableList;

import com.mojang.datafixers.util.Pair;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntFunction;

@Environment(EnvType.CLIENT)
public class StoryBookScreen extends Screen {

    public static final StoryBookScreen.Contents EMPTY_PROVIDER = new StoryBookScreen.Contents() {
        @Override
        public int getPageCount() {
            return 0;
        }

        @Override
        public StringVisitable getPageUnchecked(int index) {
            return StringVisitable.EMPTY;
        }
    };
    public static Identifier bookTexture;
    public static final Identifier BOOK_TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/book_middle.png");
    public static final int TEXT_LEFT_X = 22;
    public static final int TEXT_RIGHT_X = 159;
    public static final int TEXT_Y = 21;
    public static final int TEXT_WIDTH = 114;
    public static final int TEXT_HEIGHT = 128;
    protected static final int WIDTH = 295;
    protected static final int HEIGHT = 180;
    private StoryBookScreen.Contents contents;
    private Pair<List<OrderedText>, List<OrderedText>> cachedPage;
    private int cachedSpread;
    protected int leftPos;
    protected int topPos;
    private ButtonWidget nextPageButton;
    private ButtonWidget previousPageButton;
    protected int currentSpread;




    public StoryBookScreen(StoryBookScreen.Contents contents, ItemStack stack) {
        super(NarratorManager.EMPTY);
        this.contents = contents;
        this.cachedPage = Pair.of(Collections.emptyList(), Collections.emptyList());
        this.cachedSpread = -1;

        if (stack.isOf(ModItems.AURORA_STORYBOOK)) {
            bookTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/aurora_open.png");
        } else if (stack.isOf(ModItems.CINDERELLA_STORYBOOK)) {
            bookTexture = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/cinder_open.png");
        } else {
            bookTexture = BOOK_TEXTURE; // fallback
        }
    }

    public StoryBookScreen.Contents getBookAccess() {
        return this.contents;
    }

    public boolean setPage(int index) {
        index = MathHelper.clamp(index, 0, this.getBookAccess().getPageCount() - 1);
        int spreadIndex = (int)((float)index / 2.0F);
        if (spreadIndex != this.currentSpread) {
            this.currentSpread = spreadIndex;
            this.updatePageButtons();
            this.cachedSpread = -1;
            return true;
        } else {
            return false;
        }
    }

    protected boolean jumpToPage(int page) {
        return this.setPage(page);
    }


    @Override
    protected void init() {
        this.addPageButtons();
    }

    protected void addPageButtons() {
        this.leftPos = (this.width - 295) / 2;
        this.topPos = (this.height - 180) / 2;
        createWidgets();
    }

    protected void createWidgets() {
        this.createPrevPageButton();
        this.createNextPageButton();
    }


    protected void createNextPageButton() {
        this.nextPageButton = this.addDrawableChild(new TexturedButtonWidget(this.leftPos + 270, this.topPos + 156, 13, 15, 308, 0, 15, bookTexture, 512, 512, (button) -> this.goToNextPage()));
        nextPageButton.setTooltip(Tooltip.of(Text.translatable("spectatorMenu.next_page")));
        this.nextPageButton = this.addDrawableChild(nextPageButton);
    }

    protected void createPrevPageButton() {
        this.previousPageButton = this.addDrawableChild(new TexturedButtonWidget(this.leftPos + 12, this.topPos + 156, 13, 15, 295, 0, 15, bookTexture, 512, 512, (button) -> this.goToPreviousPage()));
        previousPageButton.setTooltip(Tooltip.of(Text.translatable("spectatorMenu.previous_page")));
        this.previousPageButton = this.addDrawableChild(previousPageButton);
    }


    private int getPageCount() {
        return this.contents.getPageCount();
    }

    public int getSpreadCount() {
        return (int)Math.ceil((double)this.getPageCount() / (double)2.0F);
    }

    protected boolean goToPreviousPage() {
        if (this.currentSpread > 0) {
            this.currentSpread--;
            this.playPageTurnSound(0.8F);
            return true;
        } else {
            return false;
        }
    }

    protected boolean goToNextPage() {
        if (this.currentSpread < this.getSpreadCount() - 1) {
            this.currentSpread++;
            this.playPageTurnSound(1.0F);
            return true;
        } else {
            return false;
        }
    }

    private void updatePageButtons() {
        this.nextPageButton.visible = this.currentSpread < this.getSpreadCount() - 1;
        this.previousPageButton.visible = this.currentSpread > 0;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (!(this.getFocused() instanceof TextFieldWidget)) {

            // Close inventory key
            if (MinecraftClient.getInstance().options.inventoryKey.matchesKey(keyCode, scanCode)) {
                this.close();
                return true;
            }

            // Left arrow / PageUp / Left keybinding
            if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_PAGE_UP
                    || MinecraftClient.getInstance().options.leftKey.matchesKey(keyCode, scanCode)) {
                this.goToPreviousPage();
                return true;
            }

            // Right arrow / PageDown / Right keybinding
            if (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_PAGE_DOWN
                    || MinecraftClient.getInstance().options.rightKey.matchesKey(keyCode, scanCode)) {
                this.goToNextPage();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.updatePageButtons();
       this.renderBackground(context);
       this.renderBook(context, mouseX, mouseY, partialTick);
       this.renderPageNumbers(context, mouseX, mouseY, partialTick, this.currentSpread);
       this.updateAndCacheContentsIfNeeded();
        this.renderPageContents(context, this.cachedPage.getFirst(), this.leftPos + 22, this.topPos + 21);
        this.renderPageContents(context, this.cachedPage.getSecond(), this.leftPos + 159, this.topPos + 21);

        Style style = this.getClickedComponentStyleAt((double) mouseX, (double) mouseY);
       if (style != null) {
           context.drawHoverEvent(this.textRenderer, style, mouseX, mouseY);
        }

       super.render(context, mouseX, mouseY, partialTick);

    }

    public void renderBook(DrawContext context, int mouseX, int mouseY, float partialTick) {
        context.drawTexture(bookTexture, this.leftPos, this.topPos, 295, 180, 0.0F, 0.0F, 295, 180, 512, 512);
    }

    public void renderPageNumbers(DrawContext context, int mouseX, int mouseY, float partialTick, int currentSpread) {
        this.renderLeftPageNumber(context, mouseX, mouseY, partialTick, currentSpread);
        this.renderRightPageNumber(context, mouseX, mouseY, partialTick, currentSpread);
    }

    protected void renderLeftPageNumber(DrawContext context, int mouseX, int mouseY, float partialTick, int currentSpread) {
        String leftPageNumber = Integer.toString(currentSpread * 2 + 1);
        context.drawText(this.textRenderer, leftPageNumber, this.leftPos + 69 + (8 - this.textRenderer.getWidth(leftPageNumber) / 2), this.topPos + 157, 0x000000, false);
    }


    protected void renderRightPageNumber(DrawContext context, int mouseX, int mouseY, float partialTick, int currentSpread) {
        String rightPageNumber = Integer.toString(currentSpread * 2 + 2);
        context.drawText(this.textRenderer, rightPageNumber, this.leftPos + 208 + (8 - this.textRenderer.getWidth(rightPageNumber) / 2), this.topPos + 157, 0x000000, false);
    }

    protected void updateAndCacheContentsIfNeeded() {
        if (this.cachedSpread != this.currentSpread) {
            StringVisitable leftFormattedText = this.getBookAccess().getPage(this.currentSpread * 2);
            StringVisitable rightFormattedText = this.getBookAccess().getPageCount() > this.currentSpread * 2 + 1
                    ? this.getBookAccess().getPage(this.currentSpread * 2 + 1)
                    : Text.empty();
            this.cachedPage = Pair.of(
                    this.textRenderer.wrapLines(leftFormattedText, 114),
                    this.textRenderer.wrapLines(rightFormattedText, 114)
            );
            this.cachedSpread = this.currentSpread;
        }
    }


    protected void renderPageContents(DrawContext guiGraphics, List<OrderedText> lines, int x, int y) {
        Objects.requireNonNull(this.textRenderer);
        int maxLines = Math.min(128 / 9, lines.size());

        for (int i = 0; i < maxLines; ++i) {
            OrderedText text = lines.get(i);
            TextRenderer renderer = this.textRenderer;
            Objects.requireNonNull(this.textRenderer);
            guiGraphics.drawText(renderer, text, x, y + i * 9, 0x000000, false);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            Style style = this.getClickedComponentStyleAt(mouseX, mouseY);
            if (style != null && this.handleTextClick(style)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean handleTextClick(Style style) {
        ClickEvent clickEvent = style.getClickEvent();
        if (clickEvent == null) {
            return false;
        } else if (clickEvent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String string = clickEvent.getValue();

            try {
                int i = Integer.parseInt(string) - 1;
                return this.jumpToPage(i);
            } catch (Exception var5) {
                return false;
            }
        } else {
            boolean bl = super.handleTextClick(style);
            if (bl && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }

            return bl;
        }
    }

    protected void closeScreen() {
        this.client.setScreen(null);
    }


    protected void playPageTurnSound(float pitch) {
        MinecraftClient.getInstance().getSoundManager()
                .play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, pitch, 1.0F));
    }


    @Nullable
    public Style getClickedComponentStyleAt(double mouseX, double mouseY) {
        // Check if mouse is inside the vertical bounds of the page area
        if (!(mouseY < (double) (this.topPos + 21)) && !(mouseY >= (double) (this.topPos + 21 + 128))) {
            boolean isOverRightPage;

            // Check if mouse is over right page
            if (mouseX >= (double) (this.leftPos + 159) && mouseX < (double) (this.leftPos + 159 + 114)) {
                isOverRightPage = true;
            } else {
                // Check if mouse is over left page
                if (!(mouseX >= (double) (this.leftPos + 22)) || !(mouseX < (double) (this.leftPos + 22 + 114))) {
                    return null;
                }
                isOverRightPage = false;
            }

            // Select cached lines for the page being hovered
            List<OrderedText> pageContents = isOverRightPage
                    ? (List<OrderedText>) this.cachedPage.getSecond()
                    : (List<OrderedText>) this.cachedPage.getFirst();

            if (pageContents.isEmpty()) {
                return null;
            } else {
                int x = (int) mouseX - (this.leftPos + (isOverRightPage ? 159 : 22));
                int y = (int) mouseY - (this.topPos + 21);
                Objects.requireNonNull(this.textRenderer);

                int linesCount = Math.min(128 / 9, pageContents.size());
                Objects.requireNonNull(this.textRenderer);

                // Check if within text area height
                if (y < 9 * linesCount + linesCount) {
                    Objects.requireNonNull(this.textRenderer);
                    int clickedLine = y / 9;
                    if (clickedLine >= 0 && clickedLine < pageContents.size()) {
                        OrderedText text = pageContents.get(clickedLine);
                        return this.textRenderer.getTextHandler().getStyleAt(text, x);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    static List<String> readPages(NbtCompound nbt) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        Objects.requireNonNull(builder);
        Objects.requireNonNull(builder);
        filterPages(nbt, builder::add);
        return builder.build();
    }

    public static void filterPages(NbtCompound nbt, Consumer<String> pageConsumer) {
        NbtList nbtList = nbt.getList("pages", 8).copy();
        IntFunction<String> intFunction;
        if (MinecraftClient.getInstance().shouldFilterText() && nbt.contains("filtered_pages", 10)) {
            NbtCompound nbtCompound = nbt.getCompound("filtered_pages");
            intFunction = page -> {
                String string = String.valueOf(page);
                return nbtCompound.contains(string) ? nbtCompound.getString(string) : nbtList.getString(page);
            };
        } else {
            Objects.requireNonNull(nbtList);
            Objects.requireNonNull(nbtList);
            intFunction = nbtList::getString;
        }

        for (int i = 0; i < nbtList.size(); i++) {
            pageConsumer.accept((String)intFunction.apply(i));
        }
    }

    @Environment(EnvType.CLIENT)
    public interface Contents {
        int getPageCount();

        StringVisitable getPageUnchecked(int index);

        default StringVisitable getPage(int index) {
            return index >= 0 && index < this.getPageCount() ? this.getPageUnchecked(index) : StringVisitable.EMPTY;
        }

        static StoryBookScreen.Contents create(ItemStack stack) {
            if (stack.isOf(Items.WRITTEN_BOOK)) {
                return new StoryBookScreen.WrittenBookContents(stack);
            } else {
                return (StoryBookScreen.Contents)(stack.isOf(Items.WRITABLE_BOOK) ? new StoryBookScreen.WritableBookContents(stack) : StoryBookScreen.EMPTY_PROVIDER);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class WritableBookContents implements StoryBookScreen.Contents {
        private final List<String> pages;

        public WritableBookContents(ItemStack stack) {
            this.pages = getPages(stack);
        }

        private static List<String> getPages(ItemStack stack) {
            NbtCompound nbtCompound = stack.getNbt();
            return (List<String>)(nbtCompound != null ? StoryBookScreen.readPages(nbtCompound) : ImmutableList.of());
        }

        @Override
        public int getPageCount() {
            return this.pages.size();
        }

        @Override
        public StringVisitable getPageUnchecked(int index) {

            return index >= this.pages.size() ? Text.empty() : StringVisitable.plain((String)this.pages.get(index));
        }
    }

    @Environment(EnvType.CLIENT)
    public static class WrittenBookContents implements StoryBookScreen.Contents {
        private final List<String> pages;

        public WrittenBookContents(ItemStack stack) {
            this.pages = getPages(stack);
        }

        private static List<String> getPages(ItemStack stack) {
            NbtCompound nbtCompound = stack.getNbt();
            return (List<String>)(nbtCompound != null && WrittenBookItem.isValid(nbtCompound)
                    ? StoryBookScreen.readPages(nbtCompound)
                    : ImmutableList.of(Text.Serializer.toJson(Text.translatable("book.invalid.tag").formatted(Formatting.DARK_RED))));
        }

        @Override
        public int getPageCount() {
            return this.pages.size();
        }

        @Override
        public @NotNull StringVisitable getPageUnchecked(int index) {
            String string = (String)this.pages.get(index);

            try {
                StringVisitable stringVisitable = Text.Serializer.fromJson(string);
                if (stringVisitable != null) {
                    return stringVisitable;
                }
            } catch (Exception var4) {
            }

            return StringVisitable.plain(string);
        }
    }


}