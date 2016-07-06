package com.github.projectscion.common.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by blue on 03/07/16.
 */
public interface IVariantProvider {
    List<Pair<Integer, String>> getVariants();
}
