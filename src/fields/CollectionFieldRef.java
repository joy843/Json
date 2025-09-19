package com.company.fields;

import com.company.reflectors.SerialisationReflector;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;

public class CollectionFieldRef extends FieldRef{

    private SerialisationReflector reflector; // may be null for simple types


    private Field field;

    public CollectionFieldRef(Field field){
        super(field);
        if (field == null) {
            throw new IllegalArgumentException("Le champ 'field' ne peut pas être null");
        }
        this.field = field; // Initialise explicitement `field`
    }

    @Override
    public Object getValue(Object istance) {
        try {
            return super.getValue(istance);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la collection : " + getName(), e);
        }
    }


    public String getName(){
        return field != null ? field.getName() : "";
    }
    public String toJson(Object instance) {
        try {
            if (field == null) {
                throw new RuntimeException("Le champ 'field' n'a pas été initialisé.");
            }
            field.setAccessible(true);
            Object value = getValue(instance); // Récupère la valeur de la collection
            if (value == null) {
                return "\"" + getName() + "\": null";
            }

            // Vérifie que la valeur est bien une Collection
            if (!(value instanceof Collection)) {
                throw new RuntimeException("La valeur du champ '" + getName() + "' n'est pas une Collection.");
            }

            Collection<?> collection = (Collection<?>) value;


            // Sérialise les éléments de la collection
            String serializedElements = collection.stream()
                    .map(item -> item instanceof String ? "\"" + item + "\"" : item.toString())
                    .collect(Collectors.joining(", "));

            return "\"" + getName() + "\": [" + serializedElements + "]";
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sérialisation de la collection : " + getName(), e);
        }
    }
}
