import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EmptyBorder;
import java.util.concurrent.ExecutionException;

//Interface graphique pour l'application de labyrinthe

public class MazeGUI extends JFrame {
    private Maze maze;//Labyrinthe actuel
    private MazePanel mazePanel;//Panneau pour afficher le labyrinthe
    private JLabel statusLabel;//Étiquette pour afficher le statut de l'application
    private JButton loadButton;//Bouton pour charger un labyrinthe depuis un fichier
    private JButton generateButton;// Bouton pour générer un labyrinthe aléatoire
    private JButton solveDFSButton;// Bouton pour résoudre le labyrinthe en utilisant l'algorithme DFS
    private JButton solveBFSButton;// Bouton pour résoudre le labyrinthe en utilisant l'algorithme BFS
    private JButton compareButton;// Bouton pour comparer les solutions DFS et BFS
    
    // Constantes pour les paramètres de l'interface
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CELL_SIZE = 20;
    

    public MazeGUI() {
        super("Résolution de Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        
        // Créer les composants
        initComponents();
        
        // Organiser la disposition
        layoutComponents();
        
        // Ajouter les écouteurs d'événements
        addEventListeners();
        
        // Afficher la fenêtre
        setVisible(true);
    }
    
    //Initialise les composants de l'interface
    private void initComponents() {
        mazePanel = new MazePanel();
        statusLabel = new JLabel("Bienvenue dans l'application de résolution de labyrinthe");
        
        loadButton = new JButton("Charger un labyrinthe");
        generateButton = new JButton("Générer un labyrinthe");
        solveDFSButton = new JButton("Résoudre (DFS)");
        solveBFSButton = new JButton("Résoudre (BFS)");
        compareButton = new JButton("Comparer les algorithmes");
        
        // Désactiver les boutons de résolution au démarrage
        solveDFSButton.setEnabled(false);
        solveBFSButton.setEnabled(false);
        compareButton.setEnabled(false);
    }
    
    //Organise la disposition des composants
    private void layoutComponents() {
        // Créer un panneau principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Ajouter le panneau du labyrinthe au centre
        JScrollPane scrollPane = new JScrollPane(mazePanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Créer un panneau pour les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 5));
        buttonPanel.add(loadButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(solveDFSButton);
        buttonPanel.add(solveBFSButton);
        buttonPanel.add(compareButton);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        // Ajouter le panneau de boutons à droite
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Ajouter la barre d'état en bas
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Définir le panneau principal comme contenu de la fenêtre
        setContentPane(mainPanel);
    }
    
    //Ajoute les écouteurs d'événements
    private void addEventListeners() {
        // Charger un labyrinthe depuis un fichier
        loadButton.addActionListener(e -> loadMazeFromFile());
        
        // Générer un labyrinthe aléatoire
        generateButton.addActionListener(e -> generateRandomMaze());
        
        // Résoudre avec DFS
        solveDFSButton.addActionListener(e -> solveMaze("DFS"));
        
        // Résoudre avec BFS
        solveBFSButton.addActionListener(e -> solveMaze("BFS"));
        
        // Comparer les algorithmes
        compareButton.addActionListener(e -> compareAlgorithms());
    }
    
    //Charge un labyrinthe depuis un fichier
    private void loadMazeFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner un fichier de labyrinthe");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers texte", "txt"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                maze = MazeLoader.loadFromFile(selectedFile.getAbsolutePath());
                mazePanel.setMaze(maze);
                
                // Activer les boutons de résolution
                solveDFSButton.setEnabled(true);
                solveBFSButton.setEnabled(true);
                compareButton.setEnabled(true);
                
                statusLabel.setText("Labyrinthe chargé avec succès");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors du chargement du fichier: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //Génère un labyrinthe aléatoire
    private void generateRandomMaze() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 101, 2));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 101, 2));
        
        panel.add(new JLabel("Largeur:"));
        panel.add(widthSpinner);
        panel.add(new JLabel("Hauteur:"));
        panel.add(heightSpinner);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Dimensions du labyrinthe", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            int width = (int) widthSpinner.getValue();
            int height = (int) heightSpinner.getValue();
            
            // Générer le labyrinthe dans un SwingWorker pour ne pas bloquer l'UI
            statusLabel.setText("Génération du labyrinthe en cours...");
            
            SwingWorker<Maze, Void> worker = new SwingWorker<Maze, Void>() {
                @Override
                protected Maze doInBackground() throws Exception {
                    return MazeGenerator.generateRandomMaze(width, height);
                }
                
                @Override
                protected void done() {
                    try {
                        maze = get();
                        mazePanel.setMaze(maze);
                        
                        // Activer les boutons de résolution
                        solveDFSButton.setEnabled(true);
                        solveBFSButton.setEnabled(true);
                        compareButton.setEnabled(true);
                        
                        statusLabel.setText("Labyrinthe généré avec succès");
                    } catch (InterruptedException | ExecutionException ex) {
                        JOptionPane.showMessageDialog(MazeGUI.this, 
                            "Erreur lors de la génération du labyrinthe: " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
        }
    }
    
    //Résout le labyrinthe avec l'algorithme spécifié algorithm en parametre ("DFS" ou "BFS")
    private void solveMaze(String algorithm) {
        if (maze == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez d'abord charger ou générer un labyrinthe",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Créer une copie du labyrinthe pour la résolution
        Maze mazeCopy = maze.clone();
        
        // Résoudre le labyrinthe dans un SwingWorker
        statusLabel.setText("Résolution du labyrinthe avec " + algorithm + " en cours...");
        
        SwingWorker<SolveResult, Void> worker = new SwingWorker<SolveResult, Void>() {
            @Override
            protected SolveResult doInBackground() throws Exception {
                MazeSolver solver;
                if (algorithm.equals("DFS")) {
                    solver = new DFSSolver(mazeCopy);
                } else {
                    solver = new BFSSolver(mazeCopy);
                }
                
                long startTime = System.nanoTime();
                boolean solved = solver.solve();
                long endTime = System.nanoTime();
                double executionTime = (endTime - startTime) / 1000000.0;
                
                return new SolveResult(mazeCopy, solved, solver.getSteps(), 
                                     solver.getPathLength(), executionTime);
            }
            
            @Override
            protected void done() {
                try {
                    SolveResult result = get();
                    
                    if (result.solved) {
                        maze = result.maze;
                        mazePanel.setMaze(maze);
                        
                        statusLabel.setText(algorithm + ": Résolu en " + result.executionTime + 
                                         " ms, " + result.steps + " étapes, chemin de longueur " + 
                                         result.pathLength);
                    } else {
                        JOptionPane.showMessageDialog(MazeGUI.this, 
                            "Pas de solution trouvée avec " + algorithm,
                            "Résolution échouée", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    JOptionPane.showMessageDialog(MazeGUI.this, 
                        "Erreur lors de la résolution: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    //Compare les performances des algorithmes DFS et BFS
    private void compareAlgorithms() {
        if (maze == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez d'abord charger ou générer un labyrinthe",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Comparer les algorithmes dans un SwingWorker
        statusLabel.setText("Comparaison des algorithmes en cours...");
        
        SwingWorker<CompareResult, Void> worker = new SwingWorker<CompareResult, Void>() {
            @Override
            protected CompareResult doInBackground() throws Exception {
                // Test DFS
                Maze mazeCopyDFS = maze.clone();
                MazeSolver dfs = new DFSSolver(mazeCopyDFS);
                
                long dfsStartTime = System.nanoTime();
                boolean dfsSolved = dfs.solve();
                long dfsEndTime = System.nanoTime();
                double dfsTime = (dfsEndTime - dfsStartTime) / 1000000.0;
                
                // Test BFS
                Maze mazeCopyBFS = maze.clone();
                MazeSolver bfs = new BFSSolver(mazeCopyBFS);
                
                long bfsStartTime = System.nanoTime();
                boolean bfsSolved = bfs.solve();
                long bfsEndTime = System.nanoTime();
                double bfsTime = (bfsEndTime - bfsStartTime) / 1000000.0;
                
                return new CompareResult(
                    dfsSolved, dfs.getSteps(), dfs.getPathLength(), dfsTime,
                    bfsSolved, bfs.getSteps(), bfs.getPathLength(), bfsTime,
                    (bfsSolved ? mazeCopyBFS : mazeCopyDFS)  // Utiliser le labyrinthe résolu
                );
            }
            
            @Override
            protected void done() {
                try {
                    CompareResult result = get();
                    
                    // Afficher les résultats dans une boîte de dialogue
                    JTextArea textArea = new JTextArea(20, 40);
                    textArea.setEditable(false);
                    textArea.append("Comparaison des performances:\n");
                    textArea.append("============================\n\n");
                    
                    textArea.append("DFS:\n");
                    textArea.append("  - Résolu: " + (result.dfsSolved ? "Oui" : "Non") + "\n");
                    textArea.append("  - Temps d'exécution: " + result.dfsTime + " ms\n");
                    textArea.append("  - Nombre d'étapes explorées: " + result.dfsSteps + "\n");
                    textArea.append("  - Longueur du chemin: " + result.dfsPathLength + "\n\n");
                    
                    textArea.append("BFS:\n");
                    textArea.append("  - Résolu: " + (result.bfsSolved ? "Oui" : "Non") + "\n");
                    textArea.append("  - Temps d'exécution: " + result.bfsTime + " ms\n");
                    textArea.append("  - Nombre d'étapes explorées: " + result.bfsSteps + "\n");
                    textArea.append("  - Longueur du chemin: " + result.bfsPathLength + "\n\n");
                    
                    textArea.append("Analyse comparative:\n");
                    if (result.bfsTime < result.dfsTime) {
                        double ratio = result.dfsTime / result.bfsTime;
                        textArea.append("BFS est plus rapide de " + String.format("%.2f", ratio) + "x\n");
                    } else {
                        double ratio = result.bfsTime / result.dfsTime;
                        textArea.append("DFS est plus rapide de " + String.format("%.2f", ratio) + "x\n");
                    }
                    
                    if (result.bfsPathLength <= result.dfsPathLength && result.bfsSolved && result.dfsSolved) {
                        textArea.append("BFS a trouvé le chemin optimal (plus court)\n");
                    } else if (result.dfsSolved && result.bfsSolved) {
                        textArea.append("DFS a trouvé un chemin plus court que BFS (inhabituel)\n");
                    }
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    JOptionPane.showMessageDialog(MazeGUI.this, scrollPane, 
                        "Résultats de la comparaison", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Mettre à jour l'affichage du labyrinthe
                    maze = result.solvedMaze;
                    mazePanel.setMaze(maze);
                    
                    statusLabel.setText("Comparaison terminée");
                } catch (InterruptedException | ExecutionException ex) {
                    JOptionPane.showMessageDialog(MazeGUI.this, 
                        "Erreur lors de la comparaison: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    //Classe pour représenter le résultat d'une résolution
    private static class SolveResult {
        public final Maze maze;
        public final boolean solved;
        public final int steps;
        public final int pathLength;
        public final double executionTime;
        
        public SolveResult(Maze maze, boolean solved, int steps, int pathLength, double executionTime) {
            this.maze = maze;
            this.solved = solved;
            this.steps = steps;
            this.pathLength = pathLength;
            this.executionTime = executionTime;
        }
    }
    
    //Classe pour représenter le résultat d'une comparaison 
    private static class CompareResult {
        public final boolean dfsSolved;
        public final int dfsSteps;
        public final int dfsPathLength;
        public final double dfsTime;
        
        public final boolean bfsSolved;
        public final int bfsSteps;
        public final int bfsPathLength;
        public final double bfsTime;
        
        public final Maze solvedMaze;
        
        public CompareResult(boolean dfsSolved, int dfsSteps, int dfsPathLength, double dfsTime,
                            boolean bfsSolved, int bfsSteps, int bfsPathLength, double bfsTime,
                            Maze solvedMaze) {
            this.dfsSolved = dfsSolved;
            this.dfsSteps = dfsSteps;
            this.dfsPathLength = dfsPathLength;
            this.dfsTime = dfsTime;
            
            this.bfsSolved = bfsSolved;
            this.bfsSteps = bfsSteps;
            this.bfsPathLength = bfsPathLength;
            this.bfsTime = bfsTime;
            
            this.solvedMaze = solvedMaze;
        }
    }
    
    //Point d'entrée du programme en mode GUI
    public static void main(String[] args) {
        // Utiliser le look and feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Lancer l'interface graphique dans l'EDT
        SwingUtilities.invokeLater(() -> new MazeGUI());
    }
    
    //Panneau pour afficher le labyrinthe
    private class MazePanel extends JPanel {
        private Maze maze;
        
        public MazePanel() {
            setBackground(Color.WHITE);
        }
        
        public void setMaze(Maze maze) {
            this.maze = maze;
            
            if (maze != null) {
                // Ajuster la taille préférée du panneau
                setPreferredSize(new Dimension(
                    maze.getWidth() * CELL_SIZE,
                    maze.getHeight() * CELL_SIZE
                ));
            }
            
            // Redessiner le panneau
            revalidate();
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (maze != null) {
                Graphics2D g2d = (Graphics2D) g;
                
                // Améliorer la qualité du rendu
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dessiner chaque cellule du labyrinthe
                for (int y = 0; y < maze.getHeight(); y++) {
                    for (int x = 0; x < maze.getWidth(); x++) {
                        int posX = x * CELL_SIZE;
                        int posY = y * CELL_SIZE;
                        
                        switch (maze.getCell(x, y)) {
                            case Maze.WALL:
                                g2d.setColor(Color.BLACK);
                                g2d.fillRect(posX, posY, CELL_SIZE, CELL_SIZE);
                                break;
                            case Maze.PATH:
                                g2d.setColor(Color.WHITE);
                                g2d.fillRect(posX, posY, CELL_SIZE, CELL_SIZE);
                                break;
                            case Maze.START:
                                g2d.setColor(Color.GREEN);
                                g2d.fillRect(posX, posY, CELL_SIZE, CELL_SIZE);
                                g2d.setColor(Color.BLACK);
                                g2d.drawString("S", posX + CELL_SIZE/3, posY + 2*CELL_SIZE/3);
                                break;
                            case Maze.EXIT:
                                g2d.setColor(Color.RED);
                                g2d.fillRect(posX, posY, CELL_SIZE, CELL_SIZE);
                                g2d.setColor(Color.BLACK);
                                g2d.drawString("E", posX + CELL_SIZE/3, posY + 2*CELL_SIZE/3);
                                break;
                            case Maze.VISITED:
                                g2d.setColor(new Color(200, 200, 255)); // Bleu clair
                                g2d.fillRect(posX, posY, CELL_SIZE, CELL_SIZE);
                                break;
                            case Maze.SOLUTION:
                                g2d.setColor(new Color(255, 200, 200)); // Rouge clair
                                g2d.fillRect(posX, posY, CELL_SIZE, CELL_SIZE);
                                g2d.setColor(Color.RED);
                                g2d.drawString("+", posX + CELL_SIZE/3, posY + 2*CELL_SIZE/3);
                                break;
                        }
                        
                        // Dessiner une bordure pour chaque cellule
                        g2d.setColor(Color.GRAY);
                        g2d.drawRect(posX, posY, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }
}