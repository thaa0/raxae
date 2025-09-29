package com.divertech.raxae.auth.config;


import java.util.function.Predicate;

public class ValidaConteudoAuthorizationHeader implements Predicate<String> {

    @Override
    public boolean test(String ConteudoAuthorizationHeader) {
        return ConteudoAuthorizationHeader.startsWith("Bearer");
    }
}
