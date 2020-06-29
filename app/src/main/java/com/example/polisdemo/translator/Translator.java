package com.example.polisdemo.translator;

import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class Translator {

    public final FirebaseTranslator translator;
    private boolean canTranslate;

    public Translator() {
        final FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.RU)
                        .build();
        translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        canTranslate = true;
        final FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        v -> {
                            System.out.println("OK");
                        })
                .addOnFailureListener(
                        e -> {
                            e.printStackTrace();
                            System.out.println("Not ok");
                            canTranslate = false;
                        });
    }

    public CompletableFuture<List<String>> translate(final List<String> list) {
        if (!canTranslate) {
            return CompletableFuture.supplyAsync(() -> list);
        }
        final CompletableFuture<List<String>> result = new CompletableFuture<>();
        translator.translate(list.stream().collect(Collectors.joining(", ")))
                .addOnSuccessListener(s -> result.complete(Arrays.asList(s.split(", "))))
                .addOnFailureListener(e -> e.printStackTrace());
        return result;
    }

    public CompletableFuture<String> translate(final String text) {
        if (!canTranslate) {
            return CompletableFuture.supplyAsync(() -> text);
        }
        final CompletableFuture<String> result = new CompletableFuture<>();
        translator.translate(text)
                .addOnSuccessListener(t -> result.complete(t.trim()))
                .addOnFailureListener(e -> e.printStackTrace());
        return result;
    }
}
