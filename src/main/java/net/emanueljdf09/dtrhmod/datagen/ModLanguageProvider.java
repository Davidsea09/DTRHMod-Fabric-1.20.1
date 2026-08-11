package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.entity.ModEntities;
import net.emanueljdf09.dtrhmod.item.ModItemGroups;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.emanueljdf09.dtrhmod.world.biome.ModBiomes;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {

        builder.add("itemgroup.dtrh", "Down the Rabbit Hole");

        // Storybooks

        builder.add(ModItems.AURORA_STORYBOOK, "The Sleeping Beauty");
        builder.add("storybook.aurora.title", "The Sleeping Beauty");
        builder.add("storybook.aurora.ogTitle", "Dornröschen");
        builder.add("storybook.aurora.author", "Jacob & Wilhelm Grimm");
        builder.add("storybook.aurora.text", """
        Deep within a forgotten realm, framed by ancient woods and thickets of wild roses, a king and queen welcomed a long-awaited daughter. To celebrate her birth, they summoned the realm's wise guardians—mystic spirits who bestowed upon the child gifts of grace, sharp wit, and unyielding spirit.
        
        Yet, one solitary entity, brooding in the shadows of an uninvited exile, cast a bitter curse over the hall. "Before the sun sets upon her sixteenth harvest," she hissed, her voice cutting through the warmth like a phantom's chill, "a spindle's sharp barb shall prick her blood, and her breath shall fail."
        
        A guardian of gentler counsel tempered the doom, decreeing: "Not unto death, but unto a profound, century-long slumber shall she pass, waiting beneath the dust until a true heart breaks the spell."
        
        To forestall fate, the king commanded every spinning instrument across the land to be cast into the depths of lava or broken to dust. But destiny is a stubborn thing. On her sixteenth birthday, drawn by a strange hum behind a weathered oak door in a forgotten spire, the princess discovered an ancient spindle turning in the dark.
        
        "Let me see how this quaint craft is wrought," she whispered, extending a curious hand.
        
        The iron barb bit deep. Instantly, a darkness as heavy as deepslate fell over her eyes, and she collapsed into an endless sleep. 
        
        With her slumber, a quiet hush swept the citadel. The guards at the gates, the hounds by the hearth, and the hearthfires themselves froze in time. Around the ramparts, a towering wall of cruel, thorned brambles surged upward, sealing the castle away from the waking world.
        
        A hundred winters drifted by like falling leaves, until a wandering traveler—wearied by endless wanderings—heard whispers of the hidden keep. He hacked his way through the dense, clutching thorns, enduring the biting briars that tore at his armor like the claws of a woodland beast.
        
        At last, he reached the grand chamber. There, untouched by time, lay the princess, radiant beneath the pale light of a single candle.
        
        He knelt, his hand trembling, and pressed his lips to hers.
        
        The ancient bond shattered. The dust stirred, the hearths blazed anew with life, and the castle blinked open its eyes from a century-long dream. Hand in hand, they stepped out into the golden dawn, leaving the shadows of the sleep behind forever.
        """);

        builder.add(ModItems.CINDERELLA_STORYBOOK, "Cinderella");
        builder.add("storybook.cinderella.title", "Cinderella");
        builder.add("storybook.cinderella.ogTitle", "Aschenputtel");
        builder.add("storybook.cinderella.author", "Jacob & Wilhelm Grimm");
        builder.add("storybook.cinderella.text", """
        In a quiet homestead embraced by ancient woods and whispering hills, there dwelt a gentle maiden named Aschenputtel. Though her hair fell like strands of spun gold, her days were weighed down by endless grief. When her mother passed beneath the quiet earth, her father brought home a second wife—a woman of proud spirit and bitter heart, accompanied by two daughters cut from the very same cruel cloth.

        "Cinder-drudge, sweep the hearth-stone once more," the stepmother would snap, her voice grating like iron on flint, while the stepsisters rustled in gowns of velvet and silk. "You are fit for nothing better than ashes and soot," they mocked, tossing refuse into the cold fires.
    
        From dawn till dusk, Aschenputtel knelt upon the stone flags, tending the beasts, clearing the grease-smoke from the kitchen, and foraging for wild berries beneath the gloomy canopy of the forest. When night fell like a shroud, she would steal away to the roots of an old hazel tree that grew beside her mother's grave, weeping into the dark and whispering to the silent stars: "If only a path might open to the prince's great hall..."
    
        One evening, as the sun melted into a sea of blood-red clouds over the mountains, a strange, pulsing luminescence stirred among the tree's gnarled roots. A quiet rustle parted the leaves, and a guardian spirit of the earth emerged, shining like a rare diamond buried deep in the bedrock.
    
        "Your quiet endurance has been heard beneath the stone," the spirit murmured, raising a slender hand. "Take heart; a seed of hope may yet sprout from the ashes."
    
        With a sweep of her palm, the woodland magic took hold. A gown woven from the silver threads of moonlit spiderwebs manifested before her, and upon her feet rested slippers of pure, brittle crystal glass, cold and flawless as glacier ice.
    
        Within the castle walls, torchlight danced across vaulted stone as the feast reached its midnight fever. The young lord of the keep turned his gaze across the sea of faces until they locked upon Aschenputtel, standing like a phantom of light in the hall. "Will you tread a measure with me?" he asked, bowing low.
    
        They moved together across the flagstones, the music swelling against the high rafters. But as the towering clock in the courtyard struck the twelfth heavy toll, the spell began to fray. Aschenputtel pulled away, her heart hammering against her ribs like a trapped bird, and fled into the night. On the frost-rimed steps, one fragile shoe of crystal slipped from her foot and remained behind.
    
        Vowing to scour every corner of the realm, the prince set forth with the solitary shoe. When he crossed the threshold of Aschenputtel's home, the ambitious sisters lunged forward.
    
        "Let me force my foot within!" cried the elder, carving at her own heel with a kitchen blade to make the bone fit the narrow vessel, hiding her pain behind a tight, bloodless smile. "See? It fits!"
    
        Yet, the wise doves perched upon the lintel raised a sudden, warning cry, their wings beating a frantic tattoo as they sang of the crimson deceit staining the silver shoe. The prince looked down, saw the falsehood, and turned away in disgust.
    
        At last, quiet Aschenputtel stepped from the shadows and slid her foot into the crystal shell. It settled with the ease of a key turning in an ancient lock, glowing with a soft, warm ember-light. The prince knelt, taking her hand. "You are the one."
    
        And as the wedding bells rang out beneath a vaulted sky, the cruel sisters found their reckoning—blinded by the sharp-eyed birds of the wood as punishment for their malice—while Aschenputtel and her prince walked into the long days together, their bond enduring as the deep, unyielding stone of the earth.
        """);

        builder.add(ModItems.RAPUNZEL_STORYBOOK, "Rapunzel");
        builder.add("storybook.rapunzel.title", "Rapunzel");
        builder.add("storybook.rapunzel.ogTitle", "Rapunzel");
        builder.add("storybook.rapunzel.author", "Jacob & Wilhelm Grimm");
        builder.add("storybook.rapunzel.text", """
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
                """);

        builder.add(ModItems.RED_RIDING_HOOD_STORYBOOK, "Little Red Riding Hood");
        builder.add("storybook.red_riding_hood.title", "Little Red Riding Hood");
        builder.add("storybook.red_riding_hood.ogTitle", "Rotkäppchen");
        builder.add("storybook.red_riding_hood.author", "Jacob & Wilhelm Grimm");
        builder.add("storybook.red_riding_hood.text", """
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
                """);

        builder.add(ModItems.SNOW_WHITE_STORYBOOK, "Snow White");
        builder.add("storybook.snow_white.title", "Snow White");
        builder.add("storybook.snow_white.ogTitle", "Schneewittchen");
        builder.add("storybook.snow_white.author", "Jacob & Wilhelm Grimm");
        builder.add("storybook.snow_white.text", """
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
                """);

        builder.add(ModItems.JACK_AND_THE_BEANSTALK_STORYBOOK, "Jack and the Beanstalk");
        builder.add("storybook.jack_and_the_beanstalk.title", "Jack and the Beanstalk");
        builder.add("storybook.jack_and_the_beanstalk.ogTitle", "Jack and the Beanstalk");
        builder.add("storybook.jack_and_the_beanstalk.author", "Traditional English");
        builder.add("storybook.jack_and_the_beanstalk.text", """
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
                """);

        builder.add(ModItems.THE_LITTLE_MERMAID_STORYBOOK, "The Little Mermaid");
        builder.add("storybook.the_little_mermaid.title", "The Little Mermaid");
        builder.add("storybook.the_little_mermaid.ogTitle", "Den lille Havfrue");
        builder.add("storybook.the_little_mermaid.author", "Hans Christian Andersen");
        builder.add("storybook.the_little_mermaid.text", """
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
                """);

        builder.add(ModItems.THREE_LITTLE_PIGS_STORYBOOK, "Three Little Pigs");
        builder.add("storybook.three_little_pigs.title", "The Three Little Pigs");
        builder.add("storybook.three_little_pigs.ogTitle", "Die drei kleinen Schweinchen");
        builder.add("storybook.three_little_pigs.author", "Traditional English");
        builder.add("storybook.three_little_pigs.text", """
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
                """);

        builder.add("cover.title.open", "Once upon a time...");
        builder.add("storybook.wonderland.intro.text", """
                Down the curving, sunlit path of the upper meadow, Alice spotted something peculiar darting through the tall clover.
                
                A sleek, immaculate rabbit with a crimson waistcoat hurried past, frantically pulling a massive silver pocket-watch from his coat. 
                
                Oh dear! Oh dear! I shall be too late! he cried, his voice echoing over the rolling hills.
                
                Without a second thought, Alice chased after him across the grass, straight toward the base of a massive, ancient oak tree where he tumbled down into the dark earth.
                """);

        // Entities
        builder.add(ModItems.WHITE_RABBIT_SPAWN_EGG, "White Rabbit Spawn Egg (WIP)");
        builder.add(ModEntities.WHITE_RABBIT, "White Rabbit");
        builder.add("entity.dtrhmod.white_rabbit.greet_msg", "Oh dear! Oh dear! I shall be late!");
        builder.add("entity.dtrhmod.white_rabbit.busy_msg", "I'm late, I'm late, for a very important date! No time to say 'hello, goodbye,' I'm late, I'm late, I'm late!");
        builder.add("entity.dtrhmod.white_rabbit.wonderland_remember_msg", "Oh hey, i know who you are; you followed me here.");
        builder.add("entity.dtrhmod.white_rabbit.wonderland_stranger_msg", "Who are you?");

        builder.add(ModItems.WEEPING_PLAYERS_SPAWN_EGG, "Weeping Player Spawn Egg (WIP)");
        builder.add(ModEntities.WEEPING_PLAYER, "Weeping Player");

        // Items
        builder.add(ModItems.POCKETWATCH, "Pocketwatch");
        builder.add(ModItems.EXTERIOR_KEY, "Door Key");
        builder.add(ModItems.EAT_ME, "§d§o§l\"Eat me\"");
        builder.add(ModItems.DRINK_ME, "§d§o§l\"Drink me\"");


        builder.add(ModItems.EMPTY_CUP, "Empty Cup");
        builder.add(ModItems.FILLED_TEA_CUP, "Tea Cup");

        // Blocks

        // Special Blocks
        builder.add(ModBlocks.RABBIT_HOLE, "Rabbit Hole");
        builder.add(ModBlocks.EXTERIOR_PORTAL, "Exterior Gateway");
        builder.add(ModBlocks.MIRROR_BLOCK, "Mirror");
        builder.add(ModBlocks.MAD_HATTER_HAT, "Mad Hatter´s Hat (WIP)");

        // Exterior Chest
        builder.add(ModBlocks.EXTERIOR_CHEST, "A Tiny Chest");
        builder.add("exterior.player.openchest", "§d§o§l\"Eat me\"§r What is this?");
        builder.add("exterior.player.opengrownchest", "§d§o§l\"Drink me\"§r What will this one do?");
        builder.add("exterior.player.failchestGrow", "The chest is empty...");
        builder.add("exterior.player.failchest", "There's nothing here");

        // Exterior Door
        builder.add(ModBlocks.EXTERIOR_DOOR, "A Tiny Door");
        builder.add("exterior.player.faileddoorgrowth", "You're too big to fit in here!");
        builder.add("exterior.player.faileddoor", "I'm locked, did you grab the key?");
        builder.add("exterior.player.opendoor", "Now we're talking");

        // Teapot Block
        builder.add("container.dtrhmod.teapot", "Teapot");
        builder.add(ModBlocks.TEAPOT_BLOCK, "Teapot");

        // Wood Types
        builder.add(ModBlocks.BB_BUTTON, "Tulgey Oak Button");
        builder.add(ModBlocks.BB_DOOR, "Tulgey Oak Door");
        builder.add(ModBlocks.BB_FENCE, "Tulgey Oak Fence");
        builder.add(ModBlocks.BB_FENCE_GATE, "Tulgey Oak Fence Gate");
        builder.add(ModBlocks.BB_LEAVES, "Tulgey Oak Leaves");
        builder.add(ModBlocks.BB_LOG, "Tulgey Oak Log");
        builder.add(ModBlocks.BB_PLANKS, "Tulgey Oak Planks");
        builder.add(ModBlocks.BB_PRESSURE_PLATE, "Tulgey Oak Pressure Plate");
        builder.add(ModBlocks.BB_SAPLING, "Tulgey Oak Sapling");
        builder.add(ModBlocks.BB_SLABS, "Tulgey Oak Slabs");
        builder.add(ModBlocks.BB_STAIRS, "Tulgey Oak Stairs");
        builder.add(ModBlocks.BB_TRAPDOOR, "Tulgey Oak Trapdoor");
        builder.add(ModBlocks.BB_WALL, "Tulgey Oak Wall");
        builder.add(ModBlocks.BB_WOOD, "Tulgey Oak Wood");
        builder.add(ModBlocks.STRIPPED_BB_WOOD, "Stripped Tulgey Oak Wood");
        builder.add(ModBlocks.STRIPPED_BB_LOG, "Stripped Tulgey Oak Log");
        builder.add(ModBlocks.BB_SIGN, "Tulgey Oak Sign");
        builder.add(ModBlocks.BB_HANGING_SIGN, "Tulgey Oak Hanging Sign");

        builder.add(ModBlocks.TH_BUTTON, "Dark Tulgey Oak Button");
        builder.add(ModBlocks.TH_DOOR, "Dark Tulgey Oak Door");
        builder.add(ModBlocks.TH_FENCE, "Dark Tulgey Oak Fence");
        builder.add(ModBlocks.TH_FENCE_GATE, "Dark Tulgey Oak Fence Gate");
        builder.add(ModBlocks.TH_LEAVES, "Dark Tulgey Oak Leaves");
        builder.add(ModBlocks.TH_LOG, "Dark Tulgey Oak Log");
        builder.add(ModBlocks.TH_PLANKS, "Dark Tulgey Oak Planks");
        builder.add(ModBlocks.TH_PRESSURE_PLATE, "Dark Tulgey Oak Pressure Plate");
        builder.add(ModBlocks.TH_SAPLING, "Dark Tulgey Oak Sapling");
        builder.add(ModBlocks.TH_SLABS, "Dark Tulgey Oak Slabs");
        builder.add(ModBlocks.TH_STAIRS, "Dark Tulgey Oak Stairs");
        builder.add(ModBlocks.TH_TRAPDOOR, "Dark Tulgey Oak Trapdoor");
        builder.add(ModBlocks.TH_WALL, "Dark Tulgey Oak Wall");
        builder.add(ModBlocks.TH_WOOD, "Dark Tulgey Oak Wood");
        builder.add(ModBlocks.STRIPPED_TH_WOOD, "Stripped Dark Tulgey Oak Wood");
        builder.add(ModBlocks.STRIPPED_TH_LOG, "Stripped Dark Tulgey Oak Log");
        builder.add(ModBlocks.TH_SIGN, "Dark Tulgey Oak Sign");
        builder.add(ModBlocks.TH_HANGING_SIGN, "Dark Tulgey Oak Hanging Sign");

        builder.add(ModBlocks.WW_BUTTON, "Twisted Willow Button");
        builder.add(ModBlocks.WW_DOOR, "Twisted Willow Door");
        builder.add(ModBlocks.WW_FENCE, "Twisted Willow Fence");
        builder.add(ModBlocks.WW_FENCE_GATE, "Twisted Willow Fence Gate");
        builder.add(ModBlocks.WW_LEAVES, "Twisted Willow Leaves");
        builder.add(ModBlocks.WW_HANGING_LEAVES, "Twisted Willow Hanging Leaves");
        builder.add(ModBlocks.WW_HANGING_LEAVES_PLANT, "Twisted Willow Hanging Leaves");
        builder.add(ModBlocks.WW_LOG, "Twisted Willow Log");
        builder.add(ModBlocks.WW_PLANKS, "Twisted Willow Planks");
        builder.add(ModBlocks.WW_PRESSURE_PLATE, "Twisted Willow Pressure Plate");
        builder.add(ModBlocks.WW_SAPLING, "Twisted Willow Sapling");
        builder.add(ModBlocks.WW_SLABS, "Twisted Willow Slabs");
        builder.add(ModBlocks.WW_STAIRS, "Twisted Willow Stairs");
        builder.add(ModBlocks.WW_TRAPDOOR, "Twisted Willow Trapdoor");
        builder.add(ModBlocks.WW_WALL, "Twisted Willow Wall");
        builder.add(ModBlocks.WW_WOOD, "Twisted Willow Wood");
        builder.add(ModBlocks.STRIPPED_WW_WOOD, "Stripped Twisted Willow Wood");
        builder.add(ModBlocks.STRIPPED_WW_LOG, "Stripped Twisted Willow Log");
        builder.add(ModBlocks.WW_SIGN, "Twisted Willow Sign");
        builder.add(ModBlocks.WW_HANGING_SIGN, "Twisted Willow Hanging Sign");

        // Mushrooms
        builder.add(ModBlocks.BLUE_MUSHROOM_BLOCK, "Blue Mushroom Block");
        builder.add(ModBlocks.BLUE_MUSHROOM, "Blue Mushroom");
        builder.add(ModBlocks.YELLOW_MUSHROOM_BLOCK, "Yellow Mushroom Block");
        builder.add(ModBlocks.YELLOW_MUSHROOM, "Yellow Mushroom");
        builder.add(ModBlocks.MAGENTA_MUSHROOM_BLOCK, "Magenta Mushroom Block");
        builder.add(ModBlocks.MAGENTA_MUSHROOM, "Magenta Mushroom");

        // Foliage
        builder.add(ModBlocks.WONDER_DIRT, "Under Dirt");
        builder.add(ModBlocks.WONDER_GRASS, "Under Grass");
        builder.add(ModBlocks.LAWN_DAISY_PATCH, "Lawn Daisy Patch");

        // Extras

        // Advancements

        builder.add("advancement.dtrhmod.root.title", "Down the Rabbit hole");
        builder.add("advancement.dtrhmod.root.description", "A Very Fairy Tale focused mod!");

        builder.add("advancement.dtrhmod.portal.title", "Follow the tunnel...");
        builder.add("advancement.dtrhmod.portal.description", "...into the portal");

        builder.add("misc.dtrhmod.biome_locked.title", "This Biome is Locked.");
        builder.add("misc.dtrhmod.biome_locked.description", "Maybe you should come back later.");

        // World Gen

        // Dimensions
        builder.add("travelerstitles.dtrhmod.exterior", "The Exterior");
        builder.add("travelerstitles.dtrhmod.wonderland", "Wonderland");
        builder.add("travelerstitles.dtrhmod.storybook", "Once Upon A Time in..");

        // Biomes
        builder.add("biome.dtrhmod.chessboard_fields", "Chessboard Fields");
        builder.add("biome.dtrhmod.enchanted_forest", "The Enchanted Forest");
        builder.add("biome.dtrhmod.the_exterior", "The Exterior");
        builder.add("biome.dtrhmod.tulgey_wood", "Tulgey Woods");
        builder.add("biome.dtrhmod.vale_of_tears", "Vale of Tears");

        // Effects
        builder.add(ModEffects.SHRINK.getTranslationKey(), "Shrink");
        builder.add(ModEffects.GROW.getTranslationKey(), "Grow");

    }
}