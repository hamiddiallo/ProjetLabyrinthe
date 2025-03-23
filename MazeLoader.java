import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Classe pour charger un labyrinthe depuis un fichier
public class MazeLoader {
    //Charge un labyrinthe depuis un fichier texte prend en parametre filePath Le chemin du fichier et return Le labyrinthe chargé
    public static Maze loadFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        
        if (lines.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }
        
        int height = lines.size();
        int width = lines.get(0).length();
        
        // Vérifier que toutes les lignes ont la même longueur
        for (String line : lines) {
            if (line.length() != width) {
                throw new IOException("Toutes les lignes du labyrinthe doivent avoir la même longueur");
            }
        }
        
        // Créer la grille du labyrinthe
        char[][] grid = new char[height][width];
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                grid[y][x] = line.charAt(x);
            }
        }
        
        // Vérifier que le labyrinthe contient un point de départ et une sortie
        boolean hasStart = false;
        boolean hasExit = false;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == Maze.START) {
                    hasStart = true;
                } else if (grid[y][x] == Maze.EXIT) {
                    hasExit = true;
                }
            }
        }
        
        if (!hasStart || !hasExit) {
            throw new IOException("Le labyrinthe doit contenir un point de départ 'S' et une sortie 'E'");
        }
        
        return new Maze(grid);
    }
}