package com.company;
import java.util.ArrayList;
import java.util.List;

public class list{
    // Liste d'objets (par exemple, une liste de chaînes de caractères)
    public List<Person> listeObjets;

    // Tableau de type simple (par exemple, un tableau de nombres entiers)
    public int[] tableauEntiers;

    // Constructeur
    public list() {
        // Initialisation de la liste d'objets
        this.listeObjets = new ArrayList<>();

        // Initialisation du tableau
        this.tableauEntiers = new int[10]; // Vous pouvez ajuster la taille selon le besoin
    }
}
