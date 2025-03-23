//Interface pour les algorithmes de résolution de labyrinthe
public interface MazeSolver {
    //Résout le labyrinthe return true si une solution a été trouvée
    boolean solve();
    
    //Obtient le nombre d'étapes effectuées , return Le nombre d'étapes
    int getSteps();
    
    //Obtient la longueur du chemin solution , return La longueur du chemin
    int getPathLength();
}