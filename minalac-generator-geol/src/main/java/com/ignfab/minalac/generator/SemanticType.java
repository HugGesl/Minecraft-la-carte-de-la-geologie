package com.ignfab.minalac.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum SemanticType {
    Grass, Stone, Air, Water, Dirt, White, Grey, Dark_Grey, Black, Blue, Cyan, Green, Dark_Green, Yellow, Orange, Brown, Red, Pink, Magenta, Violet;

    // Méthode pour obtenir un ensemble de types sémantiques de couleur
    public static Set<SemanticType> getColorSemanticTypes() {
        return new HashSet<>(Arrays.asList(White, Grey, Dark_Grey, Black, Blue, Cyan, Green, Dark_Green, Yellow, Orange, Brown, Red, Pink, Magenta, Violet));
    }
    

    // Méthode pour obtenir un ensemble de tous les types sémantiques
    public static Set<SemanticType> getAllSemanticTypes() {
        return new HashSet<>(Arrays.asList(values()));
    }


}