package code;

import annotations.Alias;
import annotations.Ignored;

import fields.FieldRef;
import fields.SimpleFieldRef;
import fields.ObjectFieldRef;
import fields.CollectionFieldRef;

import reflectors.SerialisationReflector;
import reflectors.DeSerialisationReflector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

public class Json1035 {

    public String serialize(Object o){
        SerialisationReflector reflector = new SerialisationReflector(o);

        if(o== null){
            return "null";
        }
        return reflector.toJson(o);


    }

    public <T> T deserialize(String json, Class<T> toType) {
        try {
            // Crée une instance de la classe cible
            T instance = toType.getDeclaredConstructor().newInstance();
            // Initialise le déserialiseur avec l'instance
            DeSerialisationReflector reflector = new DeSerialisationReflector(instance);
            // Utilisation de StringTokenizer pour analyser le JSON
            StringTokenizer tokenizer = new StringTokenizer(json, " {};:,\n\"");
            reflector.fromTokenizer(instance, tokenizer);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la désérialisation", e);
        }
    }

}




