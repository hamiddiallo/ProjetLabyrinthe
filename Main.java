import java.util.Scanner;


//Classe principale qui exécute le programme de résolution de labyrinthe en utilisant les solveurs DFS et BFS pour trouver le chemin entre deux points.
//Cette classe contient le menu principal pour charger un labyrinthe à partir d'un fichier, générer un labyrinthe aléatoire, résoudre le labyrinthe avec DFS, résoudre le labyrinthe avec BFS et comparer les performances de DFS et BFS.
//elle permet de faire la resolution en mode console.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Programme de résolution de labyrinthe");
        System.out.println("------------------------------------");
        
        Maze maze = null;
        
        while (true) {
            System.out.println("\nMenu principal:");
            System.out.println("1. Charger un labyrinthe depuis un fichier");
            System.out.println("2. Générer un labyrinthe aléatoire");
            System.out.println("3. Résoudre le labyrinthe avec DFS");
            System.out.println("4. Résoudre le labyrinthe avec BFS");
            System.out.println("5. Comparer les performances DFS et BFS");
            System.out.println("6. Quitter");
            System.out.print("Votre choix: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne
            
            switch (choice) {
                case 1:// charger un labyrinthe à partir d'un fichier
                    System.out.print("Entrez le chemin du fichier: ");
                    String filePath = scanner.nextLine();// lire le chemin du fichier
                    try {
                        maze = MazeLoader.loadFromFile(filePath);// charger le labyrinthe à partir du fichier 
                        System.out.println("Labyrinthe chargé avec succès:");
                        maze.display();// afficher le labyrinthe
                    } catch (Exception e) {
                        System.out.println("Erreur lors du chargement du fichier: " + e.getMessage());
                    }
                    break;
                    
                case 2:// générer un labyrinthe aléatoire 
                    System.out.print("Entrez la largeur du labyrinthe: ");
                    int width = scanner.nextInt();
                    System.out.print("Entrez la hauteur du labyrinthe: ");
                    int height = scanner.nextInt();
                    scanner.nextLine(); // Consommer le retour à la ligne
                    
                    maze = MazeGenerator.generateRandomMaze(width, height);// générer un labyrinthe aléatoire
                    System.out.println("Labyrinthe généré avec succès:");
                    maze.display();// afficher le labyrinthe
                    break;
                    
                case 3:// résoudre le labyrinthe avec DFS 
                    if (maze == null) {
                        System.out.println("Veuillez d'abord charger ou générer un labyrinthe");
                    } else {
                        MazeSolver dfs = new DFSSolver(maze);
                        long startTime = System.nanoTime();
                        boolean solved = dfs.solve();
                        long endTime = System.nanoTime();
                        
                        if (solved) {//si le labyrinthe est résolu alors afficher le chemin solution 
                            System.out.println("Labyrinthe résolu avec DFS:");
                            maze.displaySolution();// afficher le chemin solution
                            System.out.println("Temps d'exécution: " + ((endTime - startTime) / 1000000.0) + " ms");
                            System.out.println("Nombre d'étapes: " + dfs.getSteps());
                            System.out.println("Longueur du chemin: " + dfs.getPathLength());
                        } else {
                            System.out.println("Pas de solution trouvée avec DFS");
                        }
                    }
                    break;
                    
                case 4:// résoudre le labyrinthe avec BFS
                    if (maze == null) {
                        System.out.println("Veuillez d'abord charger ou générer un labyrinthe");
                    } else {
                        MazeSolver bfs = new BFSSolver(maze);
                        long startTime = System.nanoTime();
                        boolean solved = bfs.solve();
                        long endTime = System.nanoTime();// calculer le temps d'exécution
                        
                        if (solved) {// si le labyrinthe est résolu alors afficher le chemin solution
                            System.out.println("Labyrinthe résolu avec BFS:");
                            maze.displaySolution();
                            System.out.println("Temps d'exécution: " + ((endTime - startTime) / 1000000.0) + " ms");
                            System.out.println("Nombre d'étapes: " + bfs.getSteps());
                            System.out.println("Longueur du chemin: " + bfs.getPathLength());
                        } else {
                            System.out.println("Pas de solution trouvée avec BFS");
                        }
                    }
                    break;
                    
                case 5:// comparer les performances DFS et BFS
                    if (maze == null) {
                        System.out.println("Veuillez d'abord charger ou générer un labyrinthe");
                    } else {
                        // Sauvegarde de l'état original du labyrinthe
                        Maze mazeCopy = maze.clone();
                        
                        // Test DFS
                        MazeSolver dfs = new DFSSolver(maze);
                        long dfsStartTime = System.nanoTime();
                        boolean dfsSolved = dfs.solve();
                        long dfsEndTime = System.nanoTime();
                        double dfsTime = (dfsEndTime - dfsStartTime) / 1000000.0;
                        int dfsSteps = dfs.getSteps();
                        int dfsLength = dfs.getPathLength();
                        
                        // Restauration du labyrinthe pour le test BFS
                        maze = mazeCopy.clone();
                        
                        // Test BFS
                        MazeSolver bfs = new BFSSolver(maze);
                        long bfsStartTime = System.nanoTime();
                        boolean bfsSolved = bfs.solve();
                        long bfsEndTime = System.nanoTime();
                        double bfsTime = (bfsEndTime - bfsStartTime) / 1000000.0;
                        int bfsSteps = bfs.getSteps();
                        int bfsLength = bfs.getPathLength();
                        
                        // Affichage des résultats
                        System.out.println("\nComparaison des performances:");
                        System.out.println("============================");
                        System.out.println("DFS:");
                        System.out.println("  - Résolu: " + (dfsSolved ? "Oui" : "Non"));
                        System.out.println("  - Temps d'exécution: " + dfsTime + " ms");
                        System.out.println("  - Nombre d'étapes explorées: " + dfsSteps);
                        System.out.println("  - Longueur du chemin: " + dfsLength);
                        
                        System.out.println("\nBFS:");
                        System.out.println("  - Résolu: " + (bfsSolved ? "Oui" : "Non"));
                        System.out.println("  - Temps d'exécution: " + bfsTime + " ms");
                        System.out.println("  - Nombre d'étapes explorées: " + bfsSteps);
                        System.out.println("  - Longueur du chemin: " + bfsLength);
                        
                        // Analyse
                        System.out.println("\nAnalyse comparative:");
                        if (bfsTime < dfsTime) {
                            System.out.println("BFS est plus rapide de " + String.format("%.2f", (dfsTime / bfsTime)) + "x");
                        } else {
                            System.out.println("DFS est plus rapide de " + String.format("%.2f", (bfsTime / dfsTime)) + "x");
                        }
                        
                        if (bfsLength <= dfsLength && bfsSolved && dfsSolved) {
                            System.out.println("BFS a trouvé le chemin optimal (plus court)");
                        } else if (dfsSolved && bfsSolved) {
                            System.out.println("DFS a trouvé un chemin plus court que BFS (inhabituel)");
                        }
                        
                        // Restauration du labyrinthe résolu avec BFS (généralement optimal)
                        if (bfsSolved) {
                            maze = mazeCopy.clone();// restaurer le labyrinthe original
                            bfs = new BFSSolver(maze);// résoudre à nouveau le labyrinthe avec BFS pour afficher le chemin solution
                            bfs.solve();// résoudre le labyrinthe avec BFS
                        }
                    }
                    break;
                    
                case 6:// quitter le programme
                    System.out.println("Merci d'avoir utilisé notre programme de résolution de labyrinthe !");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Option invalide, veuillez réessayer");
            }
        }
    }
}