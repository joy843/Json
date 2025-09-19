package com.company.reflectors;

import com.company.fields.FieldRef;
import com.company.fields.SimpleFieldRef;
import com.company.fields.ObjectFieldRef;
import com.company.fields.CollectionFieldRef;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DeSerialisationReflector {

    private List<FieldRef> simpleFiels;
    private List<FieldRef> objectFields;
    private List<FieldRef> collectionFields;

    public DeSerialisationReflector(Object instance) {
        simpleFiels = new ArrayList<>();
        objectFields = new ArrayList<>();
        collectionFields = new ArrayList<>();

        // Remplir les listes de champs en fonction de leur type
        for (Class<?> currentClass = instance.getClass(); currentClass != null; currentClass = currentClass.getSuperclass()) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);

                if (field.getType().isPrimitive() || field.getType() == String.class ||
                        field.getType() == Boolean.class || Number.class.isAssignableFrom(field.getType())) {
                    simpleFiels.add(new SimpleFieldRef(field));
                } else if (List.class.isAssignableFrom(field.getType())) {
                    collectionFields.add(new CollectionFieldRef(field));
                } else {
                    objectFields.add(new ObjectFieldRef(field));
                }
            }
        }
    }

    private void assignToField(Object instance, String key, String value) {
        try {
            for (FieldRef fieldRef : simpleFiels) {
                if (fieldRef.getFieldName().equals(key)) {
                    Field field = fieldRef.getField();
                    if (!field.canAccess(instance)) {
                        field.setAccessible(true);
                    }
                    if (field.getType() == String.class) {
                        field.set(instance, value);
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(instance, Integer.parseInt(value));
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        field.set(instance, Boolean.parseBoolean(value));
                    } else {
                        System.out.println("Type non supporté pour le champ : " + field.getName());
                    }
                    System.out.println("Assignation réussie : " + key + " = " + value);
                    return;
                }
            }
            System.out.println("Aucun champ trouvé pour la clé : " + key);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'assignation du champ : " + key + " avec la valeur : " + value);
            e.printStackTrace();
        }
    }


    private void assignValue(Object instance, Field field, String value) throws IllegalAccessException {
        if (field.getType() == String.class) {
            field.set(instance, value);
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.set(instance, Integer.parseInt(value));
        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            field.set(instance, Boolean.parseBoolean(value));
        } else {
            System.out.println("Type non supporté pour : " + field.getName());
        }
    }
    public void fromTokenizer(Object instance, StringTokenizer tokenizer) {
        try {
            String currentKey = null;

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();

                if (token.isEmpty()) {
                    continue;
                }

                if (token.startsWith("\"") && token.endsWith("\"")) {
                    // Gestion des clés et des valeurs entourées de guillemets
                    String strippedToken = token.substring(1, token.length() - 1);
                    if (currentKey == null) {
                        currentKey = strippedToken; // Détection de la clé
                        System.out.println("Clé détectée : " + currentKey);
                    } else {
                        // Assignation de la valeur pour un champ simple
                        System.out.println("Valeur détectée pour la clé " + currentKey + " : " + strippedToken);
                        assignToField(instance, currentKey, strippedToken);
                        currentKey = null;
                    }
                } else if (token.equals(":")) {
                    continue;
                } else if (token.equals("{")) {
                    if (currentKey != null) {
                        // Gestion des objets imbriqués
                        assignObject(instance, currentKey, tokenizer);
                        currentKey = null;
                    }
                } else if (token.equals("[")) {
                    if (currentKey != null) {
                        // Gestion des collections
                        assignCollection(instance, currentKey, tokenizer);
                        currentKey = null;
                    }
                } else if (token.equals("}") || token.equals("]")) {
                    return; // Fin de l'objet ou de la collection
                } else {
                    if (currentKey != null) {
                        // Gestion des valeurs brutes
                        System.out.println("Valeur brute détectée pour la clé " + currentKey + " : " + token);
                        assignToField(instance, currentKey, token);
                        currentKey = null;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la désérialisation", e);
        }
    }


    private FieldRef findObjectField(String key) {
        for (FieldRef fieldRef : objectFields) {
            if (fieldRef.getFieldName().equals(key)) {
                return fieldRef;
            }
        }
        return null; // Aucun champ trouvé pour la clé donnée
    }
    private FieldRef findCollectionField(String key) {
        for (FieldRef fieldRef : collectionFields) {
            if (fieldRef.getFieldName().equals(key)) {
                return fieldRef;
            }
        }
        return null; // Aucun champ trouvé pour la clé donnée
    }



    private void assignCollection(Object instance, String key, StringTokenizer tokenizer) throws Exception {
        for (FieldRef fieldRef : collectionFields) {
            if (fieldRef.getFieldName().equals(key)) {
                Field field = fieldRef.getField();
                if (!field.canAccess(instance)) {
                    field.setAccessible(true);
                }

                List<String> collection = new ArrayList<>();
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken().trim();
                    if (token.equals("]")) {
                        break; // Fin de la collection
                    } else if (!token.equals(",")) {
                        collection.add(token.replace("\"", ""));
                    }
                }
                field.set(instance, collection);
                System.out.println("Collection assignée pour la clé : " + key + " avec les valeurs : " + collection);
                return;
            }
        }
        System.out.println("Aucun champ trouvé pour la collection : " + key);
    }



    // Méthode pour trouver la fin d'un objet '{...}'
    private int findMatchingBrace(String json, int start) {
        int count = 1;
        for (int i = start + 1; i < json.length(); i++) {
            if (json.charAt(i) == '{') count++;
            else if (json.charAt(i) == '}') count--;
            if (count == 0) return i;
        }
        throw new IllegalArgumentException("Aucune accolade fermante trouvée");
    }

    // Méthode pour trouver la fin d'une collection '[...]'
    private int findMatchingBracket(String json, int start) {
        int count = 1;
        for (int i = start + 1; i < json.length(); i++) {
            if (json.charAt(i) == '[') count++;
            else if (json.charAt(i) == ']') count--;
            if (count == 0) return i;
        }
        throw new IllegalArgumentException("Aucun crochet fermant trouvé");
    }

    private List<String> parseCollection(StringTokenizer tokenizer) {
        List<String> list = new ArrayList<>();
        StringBuilder currentElement = new StringBuilder();
        boolean inQuotes = false;

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();

            if (token.equals("\"")) {
                inQuotes = !inQuotes;
                continue;
            }

            if (inQuotes) {
                currentElement.append(token);
                continue;
            }

            switch (token) {
                case ",":
                    if (currentElement.length() > 0) {
                        list.add(currentElement.toString());
                        currentElement.setLength(0);
                    }
                    break;
                case "]":
                    if (currentElement.length() > 0) {
                        list.add(currentElement.toString());
                    }
                    return list;
                default:
                    currentElement.append(token);
            }
        }
        return list;
    }


    private void assignObject(Object instance, String key, StringTokenizer tokenizer) throws Exception {
        for (FieldRef fieldRef : objectFields) {
            if (fieldRef.getFieldName().equals(key)) {
                Object nestedInstance = fieldRef.getField().getType().getDeclaredConstructor().newInstance();
                fieldRef.getField().set(instance, nestedInstance);

                // Désérialisation récursive
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken().trim();
                    if (token.equals("{")) {
                        fromTokenizer(nestedInstance, tokenizer);
                    } else if (token.equals("}")) {
                        break; // Fin de l'objet imbriqué
                    }
                }
                System.out.println("Objet imbriqué assigné pour la clé : " + key);
                return;
            }
        }
        System.out.println("Aucun champ trouvé pour l'objet imbriqué : " + key);
    }

    private String extractNestedJson(StringTokenizer tokenizer) {
        StringBuilder nestedJsonBuilder = new StringBuilder("{");
        int braceCount = 1; // Pour suivre les accolades ouvertes/fermées

        while (tokenizer.hasMoreTokens() && braceCount > 0) {
            String token = tokenizer.nextToken();
            if (token.equals("{")) {
                braceCount++;
            } else if (token.equals("}")) {
                braceCount--;
            }
            nestedJsonBuilder.append(token);
        }
        return nestedJsonBuilder.toString();
    }


    private String extractArrayJson(StringTokenizer tokenizer) {
        StringBuilder arrayJsonBuilder = new StringBuilder("[");
        int bracketCount = 1; // Pour suivre les crochets ouverts/fermés

        while (tokenizer.hasMoreTokens() && bracketCount > 0) {
            String token = tokenizer.nextToken();
            if (token.equals("[")) {
                bracketCount++;
            } else if (token.equals("]")) {
                bracketCount--;
            }
            arrayJsonBuilder.append(token);
        }
        return arrayJsonBuilder.toString();
    }




}






