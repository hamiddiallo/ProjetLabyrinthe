// Classe représentant un labyrinthe 
public class Maze {
    // Constantes pour les éléments du labyrinthe
    public static final char WALL = '#';// mur
    public static final char PATH = '=';// chemin
    public static final char START = 'E';// point de départ
    public static final char EXIT = 'S';// sortie
    public static final char VISITED = '+';// visité
    public static final char SOLUTION = '*';// solution
    
    private char[][] grid;// grille du labyrinthe
    private int width;// largeur du labyrinthe
    private int height;// hauteur du labyrinthe
    private Position start;// position de départ
    private Position exit;// position de sortie
    
   
    //Constructeur pour un labyrinthe vide de dimensions données
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        
        // Initialisation avec des murs partout
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = WALL;
            }
        }
    }
    
   
    // Constructeur à partir d'une grille existante
    public Maze(char[][] grid) {
        this.height = grid.length;
        this.width = grid[0].length;
        this.grid = new char[height][width];
        
        // Copie de la grille et recherche du départ et de la sortie
        for (int y = 0; y < height; y++) {// parcourir les lignes de la grille
            for (int x = 0; x < width; x++) {// parcourir les colonnes de la grille 
                this.grid[y][x] = grid[y][x];// copier la valeur de la cellule
                
                if (grid[y][x] == START) {// si la cellule est le point de départ alors enregistrer la position
                    this.start = new Position(x, y);
                } else if (grid[y][x] == EXIT) {// si la cellule est la sortie alors enregistrer la position
                    this.exit = new Position(x, y);
                }
            }
        }
    }
    
    
    public Maze clone() {// méthode pour cloner un labyrinthe 
        char[][] newGrid = new char[height][width];// créer une nouvelle grille pour le clone 
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newGrid[y][x] = this.grid[y][x];// copier les valeurs de la grille
            }
        }
        Maze clone = new Maze(newGrid);// créer un nouveau labyrinthe à partir de la grille clonée
        return clone;// retourner le clone du labyrinthe
    }
    
    
    //Définir une cellule du labyrinthe
    public void setCell(int x, int y, char value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = value;
            
            // Mise à jour des positions de départ et sortie si nécessaire
            if (value == START) {
                start = new Position(x, y);
            } else if (value == EXIT) {
                exit = new Position(x, y);
            }
        }
    }
    

    //Renvoit la valeur d'une cellule
    public char getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return WALL; // Hors limites = mur
    }
    
    
    //Renvoit la valeur d'une cellule à partir d'une position
    public char getCell(Position pos) {
        return getCell(pos.getX(), pos.getY());
    }
    
    
    //Vérifie si une position est valide (dans les limites et pas un mur)
    public boolean isValidPosition(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            char cell = grid[y][x];
            return cell != WALL;
        }
        return false;
    }
    
    
    //Vérifie si une position est valide 
    public boolean isValidPosition(Position pos) {
        return isValidPosition(pos.getX(), pos.getY());// retourner true si la position est valide
    }
    
    //Marque une cellule comme visitée 
    public void markVisited(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if (grid[y][x] != START && grid[y][x] != EXIT) {
                grid[y][x] = VISITED;
            }
        }
    }
    
    //Marque une cellule comme visitée
    public void markVisited(Position pos) {
        markVisited(pos.getX(), pos.getY());
    }
    
    //Marque une cellule comme faisant partie de la solution
    public void markSolution(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if (grid[y][x] != START && grid[y][x] != EXIT) {
                grid[y][x] = SOLUTION;
            }
        }
    }
    
    //Marque une cellule comme faisant partie de la solution
    public void markSolution(Position pos) {
        markSolution(pos.getX(), pos.getY());
    }
    
    
    //Vérifie si une position est la sortie
    public boolean isExit(int x, int y) {
        return x == exit.getX() && y == exit.getY(); //retourne true si la position est la sortie
    }
    
   
    //Vérifie si une position est la sortie
    public boolean isExit(Position pos) {
        return isExit(pos.getX(), pos.getY());// retourne true si la position est la sortie
    }
    
    //Renvoit la position de départ
    public Position getStart() {
        return start;
    }
    
   
    //Renvoit la position de sortie
    public Position getExit() {
        return exit;
    }
    
    //Renvoit la largeur du labyrinthe
    public int getWidth() {
        return width;
    }
    
    //Renvoit la hauteur du labyrinthe
    public int getHeight() {
        return height;
    }
    
    //Réinitialise le labyrinthe (supprime les marques de visite et solution)
    
    public void reset() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == VISITED || grid[y][x] == SOLUTION) {
                    grid[y][x] = PATH;
                }
            }
        }
    }
    
    //Affiche le labyrinthe dans la console
    public void display() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }
    
    //Affiche le labyrinthe résolu dans la console
    public void displaySolution() {
        display();
    }
}