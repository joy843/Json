package com.company.fields;

import java.lang.reflect.Field;

public class SimpleFieldRef  extends FieldRef {


    private Field field;

    public SimpleFieldRef(Field field) {
        super(field);
        if (field == null) {
            throw new IllegalArgumentException("Le champ 'field' ne peut pas être null");
        }
        this.field = field;
    }

    @Override
    public String toJson(Object instance) {

        field.setAccessible(true);
        Object valeur = getValue(instance);
        if (valeur == null) {
            return "\"" + getName() + "\":null";
        }
        if (valeur instanceof String) {
            return "\"" + getName() + "\":\"" + valeur + "\"";
        }
        if (valeur instanceof Number || valeur instanceof Boolean) {
            return "\"" + getName() + "\":" + valeur;
        }

        return "\"" + getName() + "\":\"\"";
    }


    @Override
    public String getName () {
        return super.getName();
    }
}


