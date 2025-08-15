package net.emanueljdf09.dtrhmod.item.custom;


import net.emanueljdf09.dtrhmod.menu.screen.StoryBookScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StoryBook extends WrittenBookItem {

    private final String storyType;

    public static final Map<String, StoryData> STORIES = new HashMap<>();

    static {
        STORIES.put("adventure", new StoryData(
                "Aschenputtel",
                "Cinderella",
                "Jacob & Wilhelm Grimm",
                splitIntoPages("""
In a quiet village surrounded by thick forests and rolling hills, there lived a young girl named Aschenputtel. Her hair shone like polished gold, but her life was filled with sorrow. After her mother passed away, her father remarried a woman who was cruel and proud. Along with her two daughters, the stepmother forced Aschenputtel to work endlessly.

“Cinder girl, clean the cobblestone floors again,” the stepmother ordered, pointing to the soot-covered hearth. The stepsisters laughed as they put on their bright, enchanted gowns. “You’re nothing but ashes and dirt,” they sneered.

Each day, Aschenputtel tended the animals, swept the dirt floors, and picked berries in the wild woods for their meager meals. At night, she would sit beneath the ancient oak tree near the village well, whispering to the stars, “If only I could go to the prince’s ball…”

One evening, as the sun dipped behind the mountains, a soft glow appeared among the tree’s roots. A gentle voice whispered, “Your kindness and patience will not go unnoticed.” Suddenly, a tiny sprite emerged, shimmering like a diamond in the moonlight.

“I can grant your wish,” said the sprite, waving her hand. “With dust from the End, water from the enchanted well, and feathers from the phoenix bird, I will make you a gown no eye has seen.”

Aschenputtel watched in awe as a dazzling dress appeared, woven from spider silk and glowing softly with enchanted light. Upon her feet, glass slippers sparkled like the rarest diamonds found deep in the caves.

At the castle, the ball was in full swing. The prince’s eyes searched the crowd until they landed on Aschenputtel. “Would you honor me with a dance?” he asked, extending his hand.

They moved gracefully across the stone floor, the music echoing in the grand hall. As the clock began to strike midnight, Aschenputtel gasped, “I must go!” She fled, leaving behind one slipper that shimmered on the steps.

Determined to find the mysterious girl, the prince traveled through villages, carrying the glass slipper. When he arrived at Aschenputtel’s humble home, the stepsisters rushed to try it on.

“Let me try!” the older one insisted, forcing her foot into the slipper. Blood began to trickle as her toes were too large, but she hid it well. “It fits!” she lied.

The prince’s trusted birds flew overhead, squawking loudly. They spotted the blood and alerted the prince, who uncovered the deception.

Finally, Aschenputtel placed her foot into the slipper. It fit perfectly, glowing warmly with magic. The prince smiled, “You are the one I have searched for.”

At the wedding feast, the enchanted birds pecked at the stepsisters’ eyes for their cruelty and lies, and they were left blind. Aschenputtel and the prince were married, their love brighter than any enchanted jewel, and they ruled their kingdom in peace and kindness.
""")
        ));

        STORIES.put("fairy_tale", new StoryData(
                "Rotkäppchen",
                "Little Red Riding Hood",
                "Jacob & Wilhelm Grimm",
                splitIntoPages("""
Early one morning, a bright-eyed girl wrapped in a scarlet cloak set off through the thick forest carrying a basket of cakes and wine. Her mother cautioned, “Stay on the path. Speak to no strangers.” As she skipped, the sun filtered through the leaves like golden rain.

Suddenly, a wolf with eyes glowing like redstone emerged from the shadows, his voice smooth and sly. “Good day, child,” he said. “Where are you headed?”

“To grandmother’s cottage, beyond the dense oak woods,” she replied cheerfully.

The wolf grinned and took a shortcut through the twisted caves and dark ravines.

At the grandmother’s house, the wolf rapped softly on the door. “Who is it?” came a frail voice.

“Your granddaughter, with cakes and wine,” he answered, disguising his voice.

The door creaked open, and the wolf leapt inside, swallowing the grandmother whole in one gulp. He slipped on her nightgown and cap and climbed into bed.

When Rotkäppchen arrived, she noticed, “Grandmother, what big eyes you have!”

“The better to see you with,” the wolf growled.

“And what big ears you have!”

“The better to hear you.”

“And what sharp teeth!”

“The better to eat you with!”

Just then, a huntsman burst through the door, axe raised high. “Not today, beast!” he cried, slashing open the wolf’s belly.

Out jumped the grandmother and Rotkäppchen, shaken but safe.

They filled the wolf’s belly with heavy stones. When he awoke, he staggered and fell, never to threaten the forest again.

Some versions warn that straying from the path leads to darker fates, swallowed whole without rescue. But here, courage and kindness saved the day.
""")
        ));

        STORIES.put("mystery", new StoryData(
                "Schneewittchen",
                "Snow White",
                "Jacob & Wilhelm Grimm",
                splitIntoPages("""
In a grand castle surrounded by snowy mountains and dark pine forests, a queen sat sewing by a frosted window. Suddenly, a sharp thorn pricked her finger, and three drops of blood fell onto the white snow. “If only my child were as white as snow, as red as blood, and as black as ebony,” she whispered.

Soon, her wish came true. A daughter named Schneewittchen was born with skin as pale as quartz blocks, lips as red as redstone, and hair as black as obsidian. But the queen died shortly after, and the king married a proud and jealous woman who owned a magic mirror.

Every morning, the queen asked the mirror, “Mirror, mirror, on the wall, who is the fairest of all?”

For years, the mirror answered, “You, my queen.” But one day, it replied, “Schneewittchen is the fairest.”

Furious and fearing that Schneewittchen would take her place, the queen ordered a huntsman to take the girl deep into the forest and kill her, bringing back her heart as proof.

Moved by Schneewittchen’s innocence, the huntsman could not harm her. Instead, he brought a wild boar’s heart to the queen.

Alone, Schneewittchen wandered until she found a small, cozy cottage. Inside lived seven dwarfs who mined precious gems and welcomed her warmly.

The queen, discovering Schneewittchen was alive, tried three times to kill her. First, she laced her bodice so tight Schneewittchen fainted, but the dwarfs saved her.

Next, she gave her a poisoned comb tangled in her hair, which again caused her to collapse.

Finally, disguised as an old peddler, the queen offered a shiny red apple coated with deadly poison. When Schneewittchen bit it, she fell into a deep sleep, as still as bedrock.

The dwarfs, unable to revive her, placed her in a glass coffin in the forest clearing, where sunlight sparkled like diamonds on the glass.

One day, a prince passing through saw her and was enchanted. “May I carry her to my castle?” he asked the dwarfs.

As his servants lifted the coffin, a piece of poisoned apple dislodged from her throat, and Schneewittchen awoke.

The prince smiled, “You are the fairest indeed.”

At their wedding, the wicked queen was forced to wear red-hot iron boots and dance until she collapsed, punished for her envy.

Schneewittchen and the prince lived happily ever after, their story shining bright like the rarest diamond in the Minecraft world.
""")
        ));

        STORIES.put("history", new StoryData(
                "Die drei kleinen Schweinchen",
                "The Three Little Pigs ",
                "Traditional English",
                splitIntoPages("""
Three little pigs left their mother’s farm to build their own homes in the wild.

The first pig, eager and quick, gathered dry hay and built a house. “This will be fine!” he said, resting early.

The second pig collected sticks from the nearby forest, stacking them for his house. “Stronger than hay, for sure,” he bragged.

The third pig worked hard, mining stone and baking bricks in a kiln near the village. His house was solid and tall.

One day, a hungry wolf appeared, his eyes glowing like redstone torches. “Little pigs, little pigs, let me come in!” he growled.

“Not by the hair of my chinny chin chin!” they shouted.

The wolf huffed and puffed and blew down the straw house with ease. The first pig ran to his brother’s stick house.

Again, the wolf blew hard and the stick house collapsed. The two pigs ran to the brick house, pounding the door.

The wolf tried to blow down the brick house, but no gust could shake it.

“Little pigs, I’ll climb down your chimney and eat you!” he snarled.

But the clever third pig boiled a huge pot of water over the fire.

When the wolf slid down the chimney, he fell straight into the boiling pot, howling in pain before disappearing.

The pigs rejoiced, safe thanks to hard work and wisdom. Their story is a lesson: strong foundations and effort protect against danger.
""")
        ));

        STORIES.put("science", new StoryData(
                "Jack and the Beanstalk",
                "Jack and the Beanstalk",
                "Traditional English",
                splitIntoPages("""
Jack was a poor boy living with his mother in a small village near sprawling plains and dark forests. One morning, his mother said, “Jack, take our only cow to the market and sell her for emeralds. We need food and supplies.”

On his way, Jack met a mysterious trader cloaked in robes that shimmered like enchanted armor. “Would you trade your cow for these magic beans?” the trader asked, holding up glowing seeds pulsing with power.

Jack hesitated but agreed, hoping for a miracle.

His mother was furious when Jack returned empty-handed, waving the strange beans. “You foolish boy!” she shouted and threw the beans out the window.

Overnight, a colossal beanstalk grew, twisting through the clouds like a great green tower.

Filled with curiosity, Jack climbed the beanstalk, passing layers of clouds and strange birds with feathers like lapis lazuli.

At the top, he found a giant’s castle built of stone and enchanted blocks, glowing faintly with magic.

Inside, a massive giant roared, “Fee-fi-fo-fum! I smell the blood of an Englishman!”

Jack hid but saw a bag of gold coins piled high, a hen that laid golden eggs, and a harp that played melodies on its own.

Jack stole the gold and escaped down the beanstalk, heart pounding.

He climbed again and took the golden hen, narrowly avoiding the giant’s wrath.

On his third climb, Jack took the magical harp, but the giant awoke and chased him.

“Mother, grab the axe!” Jack called.

She chopped down the beanstalk just as the giant reached the bottom, sending him crashing to the earth in defeat.

Jack’s daring brought riches, but also warned of dangers when tempting fate and powerful magic.
""")
        ));

        STORIES.put("aurora", new StoryData(
                "Dornröschen",
                "The Slepping Beauty",
                "Jacob & Wilhelm Grimm",
                splitIntoPages("""
In a magnificent castle surrounded by enchanted forests and blooming gardens, a princess was born to a joyful king and queen. They invited fairies to bless the child with gifts of beauty, grace, and wisdom.

But one wicked fairy, angry at being forgotten, cursed the princess. “Before your sixteenth birthday, you will prick your finger on a spinning wheel and die,” she declared, her voice echoing like thunder.

A kind fairy softened the curse, “Instead of death, you shall fall into a deep sleep for one hundred years, only to be awakened by the kiss of true love.”

The king ordered all spinning wheels destroyed, but on her birthday, the curious princess found a hidden spinning wheel in a dusty tower.

“Let me try,” she said, reaching out.

The spindle pricked her finger, and she fell into a sleep as deep as the ocean trenches.

The entire castle, from servants to animals, fell asleep too, and a thick hedge of thorny vines grew around the castle, hiding it from the world.

A century passed until a brave prince heard stories of the sleeping beauty.

He fought his way through the thick thorns, the branches tearing at his clothes like spider webs.

Finally, he found the princess, lying peaceful and radiant.

He leaned down and kissed her gently.

The curse broke, and the castle awoke with a joyful cheer.

The wicked fairy was punished, and the prince and princess were married, their love blossoming like the spring flowers.
""")
        ));

        STORIES.put("seashell", new StoryData(
                "Den lille Havfrue",
                "The Little Mermaid",
                "Hans Christian Andersen",
                splitIntoPages("""
Beneath the shimmering waves of a vast ocean biome, a young mermaid princess lived in a coral castle adorned with glowing sea lanterns and colorful shells. She had eyes like sapphires and hair flowing like kelp in the current.

Every day, she swam near the surface, watching the world of land dwellers with wonder. One stormy night, she saved a prince who fell from a ship, pulling him to shore with great effort.

Her heart longed to walk on land, to speak with him, but mermaids had no legs—only tails.

A sea witch, dwelling in a dark underwater cave filled with strange potions and glowing mushrooms, offered her a cruel bargain.

“Give me your voice, and I will grant you legs,” the witch hissed. “But every step you take will feel like walking on sharp sea urchins.”

The mermaid agreed, trading her sweet voice for human legs, pain searing with each movement.

She found the prince, but he did not recognize her and soon married another princess.

The mermaid was heartbroken and given a choice: kill the prince to regain her tail or die and dissolve into sea foam.

Unable to harm him, she chose death.

As she faded, she became a spirit of the air, destined to earn a soul through good deeds over centuries before finally rising to heaven.

Her tale is one of sacrifice, longing, and hope beyond the waves.
""")
        ));

        STORIES.put("hair", new StoryData(
                "Rapunzel",
                "Rapunzel",
                "Jacob & Wilhelm Grimm",
                splitIntoPages("""
In a village near thick forests and winding rivers, a couple longed for a child. The wife craved rampion—called “Rapunzel” in the village—that grew in the witch’s garden beyond their fence.

One night, the husband snuck into the garden to steal some. The witch caught him and said, “Give me your child when it is born.”

When the baby girl arrived, the witch took her and locked her in a tall tower deep in the woods. The tower had no doors, only a single window high above the ground.

Each day, the witch called, “Rapunzel, Rapunzel, let down your hair!” and the girl dropped her long, golden hair like glowing vines for the witch to climb.

One day, a prince riding through the forest heard Rapunzel’s enchanting singing. Curious, he found the tower.

“Rapunzel, Rapunzel, let down your hair!” he called.

She let down her shining hair, and the prince climbed up.

They met and fell in love, planning to escape together.

When the witch discovered the prince’s visits, she cut Rapunzel’s hair and banished her to a desolate desert biome.

The prince, deceived by the witch climbing the tower with the cut hair, fell and was blinded by the thorns below.

Wandering blind, he finally found Rapunzel in the desert. Her tears, like healing potions, restored his sight.

Together, they returned to the kingdom, their love stronger than ever.
""")
        ));
    }




    public StoryBook(Settings settings, String storyType) {
        super(settings);
        this.storyType = storyType;
    }





    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.setNbt(createBookNbt(storyType));
        return stack;
    }

    private static NbtCompound createBookNbt(String storyType) {
        StoryData data = STORIES.getOrDefault(storyType,
                new StoryData("Untitled Story", "Unknown", "Unknown", Collections.singletonList("This storybook is blank."))
        );

        NbtCompound nbt = new NbtCompound();
        nbt.putString("title", data.title);
        nbt.putString("ogTitle", data.ogTitle);
        nbt.putString("author", data.author);
        nbt.put("pages", createPages(data.pages));
        nbt.putBoolean("resolved", true);
        return nbt;
    }

    private static NbtList createPages(List<String> pageTexts) {
        NbtList pages = new NbtList();
        for (String text : pageTexts) {
            pages.add(NbtString.of(Text.Serializer.toJson(Text.literal(text))));
        }
        return pages;
    }

    private static List<String> splitIntoPages(String text) {
        List<String> pages = new ArrayList<>();
        String[] words = text.trim().split("\\s+");
        StringBuilder currentPage = new StringBuilder();

        for (String word : words) {
            if (currentPage.length() + word.length() + 1 > 255) {
                pages.add(currentPage.toString().trim());
                currentPage.setLength(0);
            }
            currentPage.append(word).append(" ");
        }
        if (!currentPage.isEmpty()) {
            pages.add(currentPage.toString().trim());
        }

        return pages;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            net.minecraft.client.MinecraftClient.getInstance()
                    .setScreen(new StoryBookScreen(
                            new StoryBookScreen.WrittenBookContents(stack)
                    ));
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt() && stack.getNbt().contains("title") && stack.getNbt().contains("author")) {
            tooltip.add(Text.literal(stack.getNbt().getString("ogTitle")));
            tooltip.add(Text.literal("Author: " + stack.getNbt().getString("author")));
        } else {
            tooltip.add(Text.literal("Storybook (Unknown)"));
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    private record StoryData(String title, String ogTitle, String author, List<String> pages) {}

}
