package macifpb;

public class Vertice {
    private final String id;
    private final String nome;
    private final double x;
    private final double y;

    public Vertice(String id, String nome, double x, double y) {
        this.id = id;
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getX() { return x; }
    public double getY() { return y; }

    @Override
    public String toString() {
        return nome;
    }
}