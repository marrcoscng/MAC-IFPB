package macifpb;

import java.util.List;

public class ResultadoCaminho {
    private final List<Vertice> caminho;
    private final double distanciaTotal;

    public ResultadoCaminho(List<Vertice> caminho, double distanciaTotal) {
        this.caminho = caminho;
        this.distanciaTotal = distanciaTotal;
    }

    public List<Vertice> getCaminho() { return caminho; }
    public double getDistanciaTotal() { return distanciaTotal; }
}