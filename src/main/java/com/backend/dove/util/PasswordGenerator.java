package com.backend.dove.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class PasswordGenerator {

    public static class Characters {
        public static final String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String lowercase = "abcdefghijklmnopqrstuvwxyz";
        public static final String numbers = "1234567890";
        public static final String special = "!@#$%^&*()+-={}[]:\"';\\|/?.>,<";
    }

    public final Set<Character> characters;

    private int length;

    public PasswordGenerator(Set<Character> characters) {
        this.characters = characters;
    }

    public PasswordGenerator(String ...characters) {
        this(
                String.join("", characters)
                    .chars()
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toSet())
        );
    }

    public String generate() {
        return generate(this.length);
    }

    public String generate(int length) {
        var characters = this.characters
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining())
                .toCharArray();
        return RandomStringUtils.random(
                length,
                0,
                characters.length-1,
                false,
                false,
                characters,
                new SecureRandom()
        );
    }

    public int getLength() {
        return length;
    }

    public PasswordGenerator setLength(int length) {
        this.length = length;
        return this;
    }

}
