package YuGiOh.model;

import YuGiOh.model.card.Card;
import com.google.gson.*;
import lombok.Getter;

import java.lang.reflect.Type;

public class GeneralCard {
    @Getter
    private final Card card;
    @Getter
    private final String className;

    public GeneralCard(Card card, String className) {
        this.card = card;
        this.className = className;
    }

    public GeneralCard(Card card) {
        this.card = card;
        this.className = card.getClass().getName();
    }


    public static class Deserializer implements JsonDeserializer<GeneralCard> {
        @Override
        public GeneralCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String className = jsonObject.get("className").getAsString();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try {
                Class<?> clazz = classLoader.loadClass(className);
                Card card = jsonDeserializationContext.deserialize(jsonObject.get("card"), clazz);
                return new GeneralCard(card, className);
            } catch (Exception e) {
                throw new Error("failed to load the class " + className);
            }
        }
    }
}
