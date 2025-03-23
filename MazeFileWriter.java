import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//Classe pour sauvegarder un labyrinthe dans un fichier
public class MazeFileWriter {
   //Sauvegarde un labyrinthe dans un fichier . il prend en parametre Le labyrinthe à sauvegarderet Le chemin du fichier
    public static void saveToFile(Maze maze, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int y = 0; y < maze.getHeight(); y++) {
                for (int x = 0; x < maze.getWidth(); x++) {
                    writer.write(maze.getCell(x, y));
                }
                writer.newLine();
            }
            writer.flush();
        }
    }
}