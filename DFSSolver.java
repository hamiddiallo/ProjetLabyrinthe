import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

//Cette classe implémente le solveur de labyrinthe utilisant l'algorithme DFS (Depth-First Search) pour trouver le chemin entre deux points.
public class DFSSolver implements MazeSolver {
    private Maze maze;//Labyrinthe à résoudre
    private int steps;//    Nombre d'étapes pour résoudre le labyrinthe
    private int pathLength;//Longueur du chemin solution
    private List<Position> solution;//Liste des positions formant le chemin solution
    
    public DFSSolver(Maze maze) {
        this.maze = maze;
        this.steps = 0;
        this.pathLength = 0;
        this.solution = new ArrayList<>();
    }
    
    @Override
    public boolean solve() {// cette méthode résout le labyrinthe en utilisant l'algorithme DFS (Depth-First Search) pour trouver le chemin entre deux points.
        // Réinitialisation du labyrinthe
        maze.reset();
        steps = 0;
        pathLength = 0;
        solution.clear();
        
        // Création d'une pile pour le DFS (LIFO) et initialisation avec la position de départ
        Stack<Position> stack = new Stack<>();
        // Tableau pour stocker le parent de chaque position
        Position[][] parent = new Position[maze.getHeight()][maze.getWidth()];
        
        // Point de départ
        Position start = maze.getStart();
        stack.push(start);
        
        // Boucle principale du DFS
        while (!stack.isEmpty()) {// tant que la pile n'est pas vide
            Position current = stack.pop();// retirer la position actuelle de la pile pour l'explorer
            steps++;// incrémenter le nombre d'étapes
            
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
                return true;// retourner true si le chemin solution a été trouvé
            }
            
            // Marquer la position comme visitée
            maze.markVisited(current);
            
            // Explorer les 4 directions (haut, droite, bas, gauche)
            for (int dir = 0; dir < 4; dir++) {
                Position next = current.move(dir);
                
                // Vérifier si la position est valide et non visitée
                if (maze.isValidPosition(next) && 
                    (maze.getCell(next) == Maze.PATH || maze.getCell(next) == Maze.EXIT)) {// vérifier si la position est un chemin ou la sortie
                    
                    stack.push(next);// ajouter la position à la pile pour l'explorer
                    // Enregistrer le parent pour la reconstruction du chemin 
                    parent[next.getY()][next.getX()] = current;
                }
            }
        }
        
        // Pas de solution trouvée
        return false;
    }
    
    @Override
    public int getSteps() {// obtenir le nombre d'étapes pour résoudre le labyrinthe 
        return steps;
    }
    
    @Override
    public int getPathLength() {// obtenir la longueur du chemin solution
        return pathLength;
    }
    
    // obtenir la liste des positions formant le chemin solution 
    public List<Position> getSolution() {
        return solution;
    }
}