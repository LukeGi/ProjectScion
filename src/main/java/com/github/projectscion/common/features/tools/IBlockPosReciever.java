package com.github.projectscion.common.features.tools;

import com.mojang.realmsclient.util.Pair;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public interface IBlockPosReciever {
    public Pair<BlockPos, BlockPos> revieveBlockPositions();
}
