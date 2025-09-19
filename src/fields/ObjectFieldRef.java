package com.company.fields;

import com.company.reflectors.SerialisationReflector;

import java.lang.reflect.Field;

public class ObjectFieldRef extends FieldRef{

    private SerialisationReflector reflector;

    private Field field;

    public ObjectFieldRef(Field field){
        super(field);
        if (field == null) {
            throw new IllegalArgumentException("Le champ 'field' ne peut pas être null");
        }
        this.field=field;
    }

    public Object getValue(Object istance){
        return null;
    }



    @Override
    public String getName() {
        return field != null ? field.getName() : "undefined_field";
    }


    @Override
    public String toJson(Object instance) {
        try {
            Object value = super.getValue(instance); // Récupère la valeur de l'objet imbriqué
            if (value == null) {
                return "\"" + field.getName() + "\":null";
            }
            SerialisationReflector reflector = new SerialisationReflector(value);
            return "\"" + field.getName() + "\":" + reflector.toJson(value);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sérialisation de l'objet complexe : " + field.getName(), e);
        }
    }


}
