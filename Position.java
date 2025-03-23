//Classe représentant une position dans le labyrinthe
public class Position {
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    //Obtenir la coordonnée X return La coordonnée X
    public int getX() {
        return x;
    }
    
    //Obtenir la coordonnée Y return La coordonnée Y
    public int getY() {
        return y;
    }
    
    //Définir la coordonnée X param x La nouvelle coordonnée X
    public void setX(int x) {
        this.x = x;
    }
    
    //Définir la coordonnée Y param y La nouvelle coordonnée Y
    public void setY(int y) {
        this.y = y;
    }
    
    //Créer une nouvelle position dans une direction donnée
    public Position move(int direction) {
        switch(direction) {
            case 0: return new Position(x, y - 1); // Haut
            case 1: return new Position(x + 1, y); // Droite
            case 2: return new Position(x, y + 1); // Bas
            case 3: return new Position(x - 1, y); // Gauche
            default: return new Position(x, y);    // Pas de mouvement
        }
    }
    
    //Comparer deux positions
     
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }
    
    //Générer le hashcode de la position
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    //Représentation textuelle de la position
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}