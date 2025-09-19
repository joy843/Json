package reflectors;

import fields.FieldRef;
import fields.CollectionFieldRef;
import fields.ObjectFieldRef;
import fields.SimpleFieldRef;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SerialisationReflector {

    private List<FieldRef> simpleFiels;
    private List<FieldRef> objectFields;
    private List<FieldRef> collectionFields;


    public SerialisationReflector(Object instance){
        simpleFiels = new  ArrayList<>();
        objectFields = new ArrayList<>();
        collectionFields = new ArrayList<>();
        for (Class<?> currentClass = instance.getClass(); currentClass != null; currentClass = currentClass.getSuperclass()) {
            for (Field field : currentClass.getDeclaredFields()) {
                try {
                    if (Modifier.isStatic(field.getModifiers()) || field.getName().startsWith("ANNOTATION")) {
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.getType().isPrimitive() || field.getType() == String.class || field.getType() == Boolean.class || Number.class.isAssignableFrom(field.getType())) {
                        simpleFiels.add(new SimpleFieldRef(field));
                    } else if (Collection.class.isAssignableFrom(field.getType())) {
                        collectionFields.add(new CollectionFieldRef(field));
                    } else {
                        objectFields.add(new ObjectFieldRef(field));
                    }
                } catch (InaccessibleObjectException e) {
                    System.out.println("impossible d'acceder au champ" + field.getName());
                }
                System.out.println("Champ analysé : " + field.getName() + " de type " + field.getType().getSimpleName());

            }
        }



        System.out.println("Champs simples : " + simpleFiels.size());
        System.out.println("Champs objets : " + objectFields.size());
        System.out.println("Champs collections : " + collectionFields.size());
    }
    public String toJson(Object instance) {
        StringBuilder result = new StringBuilder("{\n");
        int indentLevel = 1; // Pour gérer l'indentation

        // Champs simples
        for (int i = 0; i < simpleFiels.size(); i++) {
            FieldRef field = simpleFiels.get(i);
            result.append(getIndentation(indentLevel))
                    .append(field.toJson(instance));
            if (i < simpleFiels.size() - 1 || !objectFields.isEmpty() || !collectionFields.isEmpty()) {
                result.append(",");
            }
            result.append("\n");
        }

        // Champs objets
        for (int i = 0; i < objectFields.size(); i++) {
            FieldRef field = objectFields.get(i);
            result.append(getIndentation(indentLevel))
                    .append(field.toJson(instance));
            if (i < objectFields.size() - 1 || !collectionFields.isEmpty()) {
                result.append(",");
            }
            result.append("\n");
        }

        // Champs collections
        for (int i = 0; i < collectionFields.size(); i++) {
            FieldRef field = collectionFields.get(i);
            result.append(getIndentation(indentLevel))
                    .append(field.toJson(instance));
            if (i < collectionFields.size() - 1) {
                result.append(",");
            }
            result.append("\n");
        }

        result.append("}");
        return result.toString();
    }

    // Méthode pour gérer l'indentation
    private String getIndentation(int level) {
        return "  ".repeat(level); // 2 espaces par niveau d'indentation
    }



    public void writeToFile(String filePath, Object instance) throws IOException{
        String json = toJson(instance);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write(json);
        }
    }



}
