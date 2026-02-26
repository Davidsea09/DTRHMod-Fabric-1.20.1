package net.emanueljdf09.dtrhmod.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class ExteriorDimensionEffects extends DimensionEffects {

    public ExteriorDimensionEffects(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened) {
        super(
                192.0f, // cloud height
                true,   // alternate sky color
                SkyType.END,
                false,  // brighten lighting
                false   // darken sky
        );
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return color;
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }
}
