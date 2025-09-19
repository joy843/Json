package fields;

import annotations.Alias;
import annotations.Ignored;

import java.lang.reflect.Field;

public abstract class FieldRef {

    protected String name;
    protected Alias aliasAnnotation;
    protected Ignored ignoredAnnotation;

    protected Field field;

    protected FieldRef(Field field){
        this.field = field;
        this.aliasAnnotation = field.getAnnotation(Alias.class);
        this.ignoredAnnotation = field.getAnnotation(Ignored.class);
        this.name = field.getName();
    }
    public Object getValue(Object istance)  {
        try{
            field.setAccessible(true);
            Object b = field.get(istance);
            return b;
        } catch (IllegalAccessException e){

            e.printStackTrace();
            return null;
        }

    }

    // Nouvelle méthode publique pour récupérer le nom du champ
    public String getFieldName() {
        return getName();
    }

    // Nouvelle méthode publique pour accéder directement au champ
    public Field getField() {
        return field;
    }


    public abstract  String toJson(Object istance);
    protected String getName(){
        if(aliasAnnotation != null){
            return aliasAnnotation.value();
        }else {
            return name;
        }

    }

}
