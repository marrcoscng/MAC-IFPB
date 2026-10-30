package macifpb;

import java.util.*;

public class Grafo {
    private final List<Vertice> vertices = new ArrayList<>();
    private final List<Aresta> arestas = new ArrayList<>();
    private final Map<Vertice, List<Aresta>> adjacencias = new HashMap<>();

    public void adicionarVertice(Vertice v) {
        vertices.add(v);
        adjacencias.putIfAbsent(v, new ArrayList<>());
    }

    public void adicionarAresta(Vertice origem, Vertice destino, double peso) {
        Aresta a1 = new Aresta(origem, destino, peso);
        Aresta a2 = new Aresta(destino, origem, peso); // Mão dupla
        arestas.add(a1);
        adjacencias.get(origem).add(a1);
        adjacencias.get(destino).add(a2);
    }

    public List<Vertice> getVertices() { return vertices; }
    public List<Aresta> getArestas() { return arestas; }

    public ResultadoCaminho calcularDijkstra(Vertice inicio, Vertice fim) {
        Map<Vertice, Double> distancias = new HashMap<>();
        Map<Vertice, Vertice> anteriores = new HashMap<>();
        PriorityQueue<Vertice> fila = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        for (Vertice v : vertices) {
            distancias.put(v, Double.MAX_VALUE);
        }
        distancias.put(inicio, 0.0);
        fila.add(inicio);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();

            if (atual.equals(fim)) break;

            for (Aresta aresta : adjacencias.get(atual)) {
                Vertice vizinho = aresta.getDestino();
                double novaDist = distancias.get(atual) + aresta.getPeso();

                if (novaDist < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDist);
                    anteriores.put(vizinho, atual);
                    fila.remove(vizinho);
                    fila.add(vizinho);
                }
            }
        }

        List<Vertice> caminho = new ArrayList<>();
        Vertice pass = fim;
        if (distancias.get(fim) == Double.MAX_VALUE) {
            return new ResultadoCaminho(Collections.emptyList(), Double.MAX_VALUE);
        }

        while (pass != null) {
            caminho.add(0, pass);
            pass = anteriores.get(pass);
        }

        return new ResultadoCaminho(caminho, distancias.get(fim));
    }
}