import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

//Implémentation du solveur de labyrinthe utilisant l'algorithme BFS (Breadth-First Search) pour trouver le chemin le plus court entre deux points.

public class BFSSolver implements MazeSolver {
    private Maze maze;//Labyrinthe à résoudre 
    private int steps;//Nombre d'étapes pour résoudre le labyrinthe
    private int pathLength;//Longueur du chemin solution
    private List<Position> solution;//Liste des positions formant le chemin solution
    
    
    public BFSSolver(Maze maze) {
        this.maze = maze;
        this.steps = 0;
        this.pathLength = 0;
        this.solution = new ArrayList<>();
    }
    
    @Override
    public boolean solve() {// cette méthode résout le labyrinthe en utilisant l'algorithme BFS (Breadth-First Search) pour trouver le chemin le plus court entre deux points. 
        // Réinitialisation du labyrinthe
        maze.reset();
        steps = 0;
        pathLength = 0;
        solution.clear();// effacer la liste des positions formant le chemin solution
        
        // Création d'une file pour le BFS (FIFO) et initialisation avec la position de départ 
        Queue<Position> queue = new LinkedList<>();
        // Tableau pour stocker le parent de chaque position
        Position[][] parent = new Position[maze.getHeight()][maze.getWidth()];
        
        // Point de départ
        Position start = maze.getStart();// obtenir la position de départ
        queue.add(start);// ajouter la position de départ à la file 
        
        // Boucle principale du BFS
        while (!queue.isEmpty()) {
            Position current = queue.remove();// retirer la position actuelle de la file pour l'explorer 
            steps++;
            
            // Si on a atteint la sortie
            if (maze.isExit(current)) {// vérifier si la position actuelle est la sortie
                // Reconstruction du chemin
                Position pos = current;
                while (pos != null && !pos.equals(start)) {// reconstruire le chemin solution en remontant les parents
                    solution.add(pos);// ajouter la position à la liste des positions formant le chemin solution
                    pos = parent[pos.getY()][pos.getX()];// obtenir le parent de la position actuelle
                    maze.markSolution(pos);// marquer la position comme faisant partie du chemin solution 
                    pathLength++;// incrémenter la longueur du chemin solution
                }
                return true;// retourner vrai si le chemin solution a été trouvé
            }
            
            // Marquer la position comme visitée
            maze.markVisited(current);
            
            // Explorer les 4 directions (haut, droite, bas, gauche)
            for (int dir = 0; dir < 4; dir++) {
                Position next = current.move(dir);
                
                // Vérifier si la position est valide et non visitée
                if (maze.isValidPosition(next) && 
                    (maze.getCell(next) == Maze.PATH || maze.getCell(next) == Maze.EXIT)) {// vérifier si la position est un chemin ou la sortie
                    
                    queue.add(next);// ajouter la position à la file pour l'explorer
                    // Enregistrer le parent pour la reconstruction du chemin
                    parent[next.getY()][next.getX()] = current;
                    // Marquer comme visitée pour éviter de la revisiter
                    if (maze.getCell(next) == Maze.PATH) {
                        maze.markVisited(next);
                    }
                }
            }
        }
        
       
        return false;// retourner false si aucun chemin solution n'a été trouvé
    }
    
    @Override
    public int getSteps() {// cette méthode retourne le nombre d'étapes pour résoudre le labyrinthe
        return steps;
    }
    
    @Override
    public int getPathLength() {// cette méthode retourne la longueur du chemin solution 
        return pathLength;
    }
    
    
    public List<Position> getSolution() {// cette méthode retourne la liste des positions formant le chemin solution 
        return solution;
    }
}