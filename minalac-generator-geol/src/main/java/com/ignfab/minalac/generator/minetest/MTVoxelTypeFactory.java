package com.ignfab.minalac.generator.minetest;

import com.ignfab.minalac.generator.SemanticType;
import com.ignfab.minalac.generator.VoxelTypeFactory;
import com.ignfab.minalac.generator.VoxelType;
import com.ignfab.minalac.generator.minetest.voxelType.MTSimpleVoxelType;

public class MTVoxelTypeFactory implements VoxelTypeFactory {
    private MTVoxelWorld world;

    public MTVoxelTypeFactory(MTVoxelWorld world) {
        this.world = world;
    }

    @Override
    public VoxelType createVoxelType(SemanticType semanticType) {
        //Node string can be found on https://wiki.minetest.net/Games/Minetest_Game/Nodes
        switch (semanticType) {
            case Grass:
                return new MTSimpleVoxelType(this.world, "default:dirt_with_grass");
            case Stone:
                return new MTSimpleVoxelType(this.world, "default:stone");
            case Dirt:
                return new MTSimpleVoxelType(this.world, "default:dirt");
            case Water:
                return new MTSimpleVoxelType(this.world, "default:water_source");
            case White:
                return new MTSimpleVoxelType(this.world, "wool:white");
            case Grey:
                return new MTSimpleVoxelType(this.world, "wool:grey");
            case Dark_Grey:
                return new MTSimpleVoxelType(this.world, "wool:dark_grey");
            case Black:
                return new MTSimpleVoxelType(this.world, "wool:black");
            case Blue:
                return new MTSimpleVoxelType(this.world, "wool:blue");
            case Cyan:
                return new MTSimpleVoxelType(this.world, "wool:cyan");
            case Green:
                return new MTSimpleVoxelType(this.world, "wool:green");
            case Dark_Green:
                return new MTSimpleVoxelType(this.world, "wool:dark_green");
            case Yellow:
                return new MTSimpleVoxelType(this.world, "wool:yellow");
            case Orange:
                return new MTSimpleVoxelType(this.world, "wool:orange");
            case Brown:
                return new MTSimpleVoxelType(this.world, "wool:brown");
            case Red:
                return new MTSimpleVoxelType(this.world, "wool:red");
            case Pink:
                return new MTSimpleVoxelType(this.world, "wool:pink");
            case Magenta:
                return new MTSimpleVoxelType(this.world, "wool:magenta");
            case Violet:
                return new MTSimpleVoxelType(this.world, "wool:violet");
            default:
                return new MTSimpleVoxelType(this.world, "air");
        }
    }
}