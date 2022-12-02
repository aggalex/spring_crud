package com.backend.dove.util;

import com.backend.dove.entity.HasId;

import java.util.Set;
import java.util.stream.Collectors;

public class SetUtils {

    public static Set<Long> idsOf(Set<? extends HasId> set) {
        return set.stream()
                .map(HasId::getId)
                .collect(Collectors.toSet());
    }

}
