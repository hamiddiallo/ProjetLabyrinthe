import java.util.Random;
import java.util.Stack;

//Classe pour générer un labyrinthe aléatoire
public class MazeGenerator {
    private static final Random random = new Random();
    
    
    public static Maze generateRandomMaze(int width, int height) {// méthode pour générer un labyrinthe aléatoire. il prend en parametre la largeur et la hauteur du labyrinthe et retourne le labyrinthe généré
        // S'assurer que les dimensions sont impaires pour simplifier la génération
        if (width % 2 == 0) width++;// si la largeur est paire alors l'incrémenter
        if (height % 2 == 0) height++;// si la hauteur est paire alors l'incrémenter
        
        // Créer un labyrinthe rempli de murs
        Maze maze = new Maze(width, height);
        
        // Algorithme de génération par exploration récursive
        boolean[][] visited = new boolean[height/2][width/2];
        Stack<int[]> stack = new Stack<>();
        
        // Choisir un point de départ aléatoire (coordonnées en grille cellulaire)
        int startX = random.nextInt(width/2);
        int startY = random.nextInt(height/2);
        
        // Ajouter le point de départ à la pile
        stack.push(new int[]{startX, startY});
        visited[startY][startX] = true;
        
        // Convertir les coordonnées cellulaires en coordonnées de la grille
        int gridX = startX * 2 + 1;
        int gridY = startY * 2 + 1;
        maze.setCell(gridX, gridY, Maze.PATH);
        
        // Boucle principale de génération
        while (!stack.isEmpty()) {// tant que la pile n'est pas vide 
            int[] current = stack.peek();// obtenir la cellule actuelle
            int cx = current[0];
            int cy = current[1];
            
            // Liste des directions possibles
            int[][] directions = new int[4][2];
            int dirCount = 0;
            
            // Vérifier les 4 directions
            if (cy > 0 && !visited[cy-1][cx]) {             // Haut
                directions[dirCount++] = new int[]{cx, cy-1, 0, -1};
            }
            if (cx < width/2-1 && !visited[cy][cx+1]) {     // Droite
                directions[dirCount++] = new int[]{cx+1, cy, 1, 0};
            }
            if (cy < height/2-1 && !visited[cy+1][cx]) {    // Bas
                directions[dirCount++] = new int[]{cx, cy+1, 0, 1};
            }
            if (cx > 0 && !visited[cy][cx-1]) {             // Gauche
                directions[dirCount++] = new int[]{cx-1, cy, -1, 0};
            }
            
            if (dirCount > 0) {
                // Choisir une direction aléatoire
                int[] dir = directions[random.nextInt(dirCount)];
                
                // Marquer la nouvelle cellule comme visitée
                visited[dir[1]][dir[0]] = true;
                
                // Créer un passage
                int passageX = (cx * 2 + 1) + dir[2];
                int passageY = (cy * 2 + 1) + dir[3];
                maze.setCell(passageX, passageY, Maze.PATH);
                
                // Créer un chemin dans la nouvelle cellule
                int newCellX = (dir[0] * 2) + 1;
                int newCellY = (dir[1] * 2) + 1;
                maze.setCell(newCellX, newCellY, Maze.PATH);
                
                // Ajouter la nouvelle cellule à la pile
                stack.push(new int[]{dir[0], dir[1]});
            } else {
                // Backtrack
                stack.pop();
            }
        }
        
        // Placer le point de départ et la sortie
        placeStartAndExit(maze);
        
        return maze;
    }
    
    private static void placeStartAndExit(Maze maze) {// méthode pour placer le point de départ et la sortie dans le labyrinthe . il prend en parametre le labyrinthe généré
        int width = maze.getWidth();
        int height = maze.getHeight();
        
        // Trouver toutes les positions PATH sur les bords
        Stack<int[]> borderCells = new Stack<>();
        
        // Parcourir les bords du labyrinthe
        for (int x = 0; x < width; x++) {// parcourir les bords horizontaux
            if (maze.getCell(x, 0) == Maze.PATH) {// vérifier si la cellule est un chemin 
                borderCells.push(new int[]{x, 0});// ajouter la cellule à la pile
            }
            if (maze.getCell(x, height-1) == Maze.PATH) {
                borderCells.push(new int[]{x, height-1});
            }
        }
        
        for (int y = 1; y < height-1; y++) {// parcourir les bords verticaux 
            if (maze.getCell(0, y) == Maze.PATH) {
                borderCells.push(new int[]{0, y});
            }
            if (maze.getCell(width-1, y) == Maze.PATH) {
                borderCells.push(new int[]{width-1, y});
            }
        }
        
        // S'il n'y a pas assez de cellules de bordure, en créer
        if (borderCells.size() < 2) {
            // Trouver une cellule PATH près du bord supérieur
            for (int x = 1; x < width-1; x++) {
                for (int y = 1; y < 3; y++) {
                    if (maze.getCell(x, y) == Maze.PATH) {
                        maze.setCell(x, 0, Maze.PATH);
                        borderCells.push(new int[]{x, 0});
                        break;// si on a trouvé une cellule de bordure alors sortir de la boucle
                    }
                }
                if (borderCells.size() > 0) break;
            }
            
            // Trouver une cellule PATH près du bord inférieur
            for (int x = 1; x < width-1; x++) {
                for (int y = height-3; y < height-1; y++) {
                    if (maze.getCell(x, y) == Maze.PATH) {
                        maze.setCell(x, height-1, Maze.PATH);
                        borderCells.push(new int[]{x, height-1});
                        break;
                    }
                }
                if (borderCells.size() > 1) break;// si on a trouvé deux cellules de bordure alors sortir de la boucle
            }
        }
        
        // S'il n'y a toujours pas assez de cellules, créer des ouvertures aux coins
        if (borderCells.size() < 2) {
            maze.setCell(1, 0, Maze.PATH);
            borderCells.push(new int[]{1, 0});
            maze.setCell(width-2, height-1, Maze.PATH);
            borderCells.push(new int[]{width-2, height-1});
        }
        
        // Choisir deux cellules aléatoires sur les bords pour l'entrée et la sortie
        int[] startPos = borderCells.remove(random.nextInt(borderCells.size()));
        int[] exitPos = borderCells.remove(random.nextInt(borderCells.size()));
        
        // Définir l'entrée et la sortie dans le labyrinthe
        maze.setCell(startPos[0], startPos[1], Maze.START);
        maze.setCell(exitPos[0], exitPos[1], Maze.EXIT);
    }
}