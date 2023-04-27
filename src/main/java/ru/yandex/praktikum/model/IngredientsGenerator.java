package ru.yandex.praktikum.model;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import ru.yandex.praktikum.client.IngredientsClient;

import java.util.Random;

public class IngredientsGenerator {
    public static String[] getRandom() throws JsonProcessingException {
        JsonNode rootNode = new ObjectMapper().readTree(new IngredientsClient().get().getBody().asString());
        JsonNode dataNode = rootNode.get("data");

        String[] idValues = new String[dataNode.size()];
        int i = 0;
        for (JsonNode node : dataNode) {
            String idValue = node.get("_id").asText();
            idValues[i++] = idValue;
        }

        Random random = new Random();
        String[] randomIds = new String[random.nextInt(dataNode.size()) + 1];
        for (i = 0; i < randomIds.length; i++) {
            int index = random.nextInt(idValues.length);
            randomIds[i] = idValues[index];
        }

        return randomIds;
    }
}
