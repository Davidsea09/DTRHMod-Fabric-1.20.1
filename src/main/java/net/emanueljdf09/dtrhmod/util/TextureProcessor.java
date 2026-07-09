package net.emanueljdf09.dtrhmod.util;

import net.minecraft.client.texture.NativeImage;

public class TextureProcessor {

    public static NativeImage convertToStone(NativeImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        NativeImage stoneImage = new NativeImage(width, height, false);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 1. Extract the original pixel color channels (RGBA)
                int abgr = originalImage.getColor(x, y);
                int a = (abgr >> 24) & 0xFF;
                int b = (abgr >> 16) & 0xFF;
                int g = (abgr >> 8) & 0xFF;
                int r = abgr & 0xFF;

                // 2. Grayscale standard luminosity conversion
                int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                // 3. Optional: Give it a slight cold-stone blue/gray tint
                int finalR = Math.min(255, (int)(gray * 0.9));
                int finalG = Math.min(255, (int)(gray * 0.95));
                int finalB = Math.min(255, (int)(gray * 1.0));

                // 4. Pack back into ABGR format
                int stonePixel = (a << 24) | (finalB << 16) | (finalG << 8) | finalR;
                stoneImage.setColor(x, y, stonePixel);
            }
        }
        return stoneImage;
    }

}
