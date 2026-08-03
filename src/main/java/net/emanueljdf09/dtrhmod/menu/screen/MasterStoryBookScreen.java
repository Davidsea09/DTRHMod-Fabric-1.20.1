package net.emanueljdf09.dtrhmod.menu.screen;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MasterStoryBookScreen extends StoryBookScreen {
    private static final Identifier MASTER_BOOK_BASE_TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/master_book_open.png");
    private final ItemStack masterBookStack;
    private final List<TabInfo> unlockedTabs = new ArrayList<>();
    private TabInfo activeTab;
    private boolean isInitializing = false;

    public MasterStoryBookScreen(ItemStack masterBookStack) {
        super(StoryBookScreen.EMPTY_PROVIDER, ItemStack.EMPTY);
        this.masterBookStack = masterBookStack;

        determineUnlockedTabs();

    }

    private void determineUnlockedTabs() {
        NbtCompound nbt = masterBookStack.getNbt();

        unlockedTabs.add(new TabInfo("Index",
                StoryBookScreen.BOOK_TEXTURE,
                "", net.minecraft.item.Items.BOOK));

        unlockedTabs.add(new TabInfo("Wonderland",
                new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/enter_wonder_open.png"),
                "storybook.wonderland.intro.text", ModItems.POCKETWATCH));

        if (nbt == null) return;

        if (nbt.getBoolean("unlocked_snow_white")) {
            unlockedTabs.add(new TabInfo("Snow White", new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/sw_open.png"), "storybook.snow_white.text", ModItems.SNOW_WHITE_STORYBOOK));
        }
        if (nbt.getBoolean("unlocked_cinderella")) {
            unlockedTabs.add(new TabInfo("Cinderella", new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/cinder_open.png"), "storybook.cinderella.text", ModItems.CINDERELLA_STORYBOOK));
        }
        if (nbt.getBoolean("unlocked_aurora")) {
            unlockedTabs.add(new TabInfo("Sleeping Beauty", new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/aurora_open.png"), "storybook.aurora.text", ModItems.AURORA_STORYBOOK));
        }
        if (nbt.getBoolean("unlocked_red_riding_hood")) {
            unlockedTabs.add(new TabInfo("Red Riding Hood", new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/lrrh_open.png"), "storybook.lrrh.text", ModItems.RED_RIDING_HOOD_STORYBOOK));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        super.render(context, mouseX, mouseY, partialTick);

        int startY = this.topPos + 10;
        int itemX = this.leftPos - 30 + 7;
        int itemY = startY + 5;

        TabInfo indexTab = unlockedTabs.get(0);
        context.drawItem(indexTab.iconStack, itemX, itemY);
    }

    @Override
    protected void init() {
        super.init();

        if (this.activeTab == null && !unlockedTabs.isEmpty() && !isInitializing) {
            this.isInitializing = true;
            switchToTab(unlockedTabs.get(0));
            this.isInitializing = false;
            return;
        }

        int startY = this.topPos + 10;
        int tabWidth = 30;
        int tabHeight = 26;

        if (this.activeTab != null && this.activeTab.titleName.equals("Index")) {
            int indexX = this.leftPos + 32;
            int indexY = this.topPos + 25;

            int renderCount = 0;
            for (int i = 0; i < unlockedTabs.size(); i++) {
                TabInfo tab = unlockedTabs.get(i);
                if (tab.titleName.equals("Index")) continue;

                this.addDrawableChild(ButtonWidget.builder(Text.literal("📖 " + tab.titleName), (button) -> {
                    this.switchToTab(tab);
                }).dimensions(indexX, indexY + (renderCount * 16), 110, 14).build());

                renderCount++;
            }
        }

        TabInfo indexTab = unlockedTabs.get(0);
        int uOffset = 295;

        TexturedButtonWidget indexTabButton = new TexturedButtonWidget(
                this.leftPos - tabWidth + 4, startY,
                tabWidth, tabHeight,
                uOffset, 0, tabHeight,
                MASTER_BOOK_BASE_TEXTURE, 512, 512,
                (button) -> {
                    this.switchToTab(indexTab);
                }
        ) {
            @Override
            public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
                int renderV = 0;

                if (MasterStoryBookScreen.this.activeTab == indexTab) {
                    renderV = 52;
                } else if (this.isSelected()) {
                    renderV = 26;
                } else {
                    renderV = 0;
                }

                context.drawTexture(this.texture, this.getX(), this.getY(), uOffset, renderV, this.width, this.height, 512, 512);
            }
        };

        this.addDrawableChild(indexTabButton);
    }

    private void switchToTab(TabInfo tab) {
        this.activeTab = tab;
        StoryBookScreen.bookTexture = tab.textureLocation;

        if (tab.isUsingRawKey) {
            if (tab.titleName.equals("Index")) {
                this.contents = StoryBookScreen.EMPTY_PROVIDER;
            } else {
                this.contents = createTranslatableContents(tab.textKeyIdentifier);
            }
        } else {
            this.contents = new StoryBookScreen.WrittenBookContents(new ItemStack(tab.associatedItem));
        }

        this.currentSpread = 0;
        this.setPage(0);
        this.setFocused(null);

        this.clearAndInit();
    }

    private static class TabInfo {
        final String titleName;
        final Identifier textureLocation;
        final ItemStack iconStack;
        String textKeyIdentifier;
        Item associatedItem;
        final boolean isUsingRawKey;

        TabInfo(String title, Identifier tex, String textKey, Item iconItem) {
            this.titleName = title;
            this.textureLocation = tex;
            this.textKeyIdentifier = textKey;
            this.iconStack = new ItemStack(iconItem);
            this.isUsingRawKey = true;
        }

        TabInfo(String title, Identifier tex, Item item) {
            this.titleName = title;
            this.textureLocation = tex;
            this.associatedItem = item;
            this.iconStack = new ItemStack(item);
            this.isUsingRawKey = false;
        }
    }
}
