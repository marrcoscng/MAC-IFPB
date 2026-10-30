package macifpb;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class MainApp extends Application {

    private Grafo grafo;
    private Pane paneMapa;
    private ComboBox<Vertice> comboOrigem;
    private ComboBox<Vertice> comboDestino;
    private Label labelDistancia;

    @Override
    public void start(Stage primaryStage) {
        inicializarGrafoExemplo();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Painel Superior de Controle
        HBox barControle = new HBox(15);
        barControle.setAlignment(Pos.CENTER_LEFT);
        barControle.setPadding(new Insets(10));
        barControle.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 8px;");

        Label lblOrigem = new Label("Origem:");
        lblOrigem.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        comboOrigem = new ComboBox<>();
        comboOrigem.getItems().addAll(grafo.getVertices());
        comboOrigem.getSelectionModel().selectFirst();

        Label lblDestino = new Label("Destino:");
        lblDestino.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        comboDestino = new ComboBox<>();
        comboDestino.getItems().addAll(grafo.getVertices());
        if (grafo.getVertices().size() > 1) {
            comboDestino.getSelectionModel().select(1);
        }

        Button btnTraçarRota = new Button("Calcular Rota");
        btnTraçarRota.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnTraçarRota.setOnAction(e -> desenharGrafoECalcular());

        labelDistancia = new Label("Distância total: -");
        labelDistancia.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-font-weight: bold;");

        barControle.getChildren().addAll(lblOrigem, comboOrigem, lblDestino, comboDestino, btnTraçarRota, labelDistancia);
        root.setTop(barControle);

        // Área Central do Mapa Visual
        paneMapa = new Pane();
        paneMapa.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 8px;");
        root.setCenter(paneMapa);

        BorderPane.setMargin(paneMapa, new Insets(10, 0, 0, 0));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Mini GPS - Algoritmo de Dijkstra");
        primaryStage.setScene(scene);
        primaryStage.show();

        desenharGrafoECalcular();
    }

    private void inicializarGrafoExemplo() {
        grafo = new Grafo();

        Vertice vA = new Vertice("A", "Centro", 150, 150);
        Vertice vB = new Vertice("B", "Bairro Norte", 350, 100);
        Vertice vC = new Vertice("C", "Zona Industrial", 600, 150);
        Vertice vD = new Vertice("D", "Bairro Sul", 200, 380);
        Vertice vE = new Vertice("E", "Terminal Urbano", 450, 350);
        Vertice vF = new Vertice("F", "Universidade", 650, 420);

        // Novos vértices com coordenadas ÚNICAS (espalhadas pelo mapa)
        Vertice vG = new Vertice("G", "Mercado Preço A", 300, 240);
        Vertice vH = new Vertice("H", "Cinema", 500, 180);
        Vertice vI = new Vertice("I", "Padaria", 150, 270);
        Vertice vJ = new Vertice("J", "Ginásio", 300, 480);

        // Adicionando TODOS os vértices ao grafo
        grafo.adicionarVertice(vA);
        grafo.adicionarVertice(vB);
        grafo.adicionarVertice(vC);
        grafo.adicionarVertice(vD);
        grafo.adicionarVertice(vE);
        grafo.adicionarVertice(vF);
        grafo.adicionarVertice(vG);
        grafo.adicionarVertice(vH);
        grafo.adicionarVertice(vI);
        grafo.adicionarVertice(vJ);

        grafo.adicionarAresta(vA, vB, 5);
        grafo.adicionarAresta(vA, vD, 8);
        grafo.adicionarAresta(vA, vI, 3);

        grafo.adicionarAresta(vB, vC, 12);
        grafo.adicionarAresta(vB, vE, 6);
        grafo.adicionarAresta(vB, vG, 4);

        grafo.adicionarAresta(vC, vH, 5);
        grafo.adicionarAresta(vC, vF, 7);

        grafo.adicionarAresta(vD, vI, 4);
        grafo.adicionarAresta(vD, vE, 4);
        grafo.adicionarAresta(vD, vJ, 6);

        grafo.adicionarAresta(vE, vF, 9);
        grafo.adicionarAresta(vE, vH, 4);
        grafo.adicionarAresta(vE, vJ, 5);

        grafo.adicionarAresta(vG, vH, 6);
        grafo.adicionarAresta(vG, vA, 4);
    }

    private void desenharGrafoECalcular() {
        paneMapa.getChildren().clear();

        Vertice origem = comboOrigem.getValue();
        Vertice destino = comboDestino.getValue();

        ResultadoCaminho resultado = null;
        List<Vertice> caminho = Collections.emptyList();

        if (origem != null && destino != null) {
            resultado = grafo.calcularDijkstra(origem, destino);
            caminho = resultado.getCaminho();

            if (resultado.getDistanciaTotal() == Double.MAX_VALUE) {
                labelDistancia.setText("Sem Rota Possível");
            } else {
                labelDistancia.setText(String.format("Distância Total: %.1f km", resultado.getDistanciaTotal()));
            }
        }

        // Desenhar Arestas
        Set<String> arestasDesenhadas = new HashSet<>();
        for (Aresta a : grafo.getArestas()) {
            String chave = Math.min(a.getOrigem().hashCode(), a.getDestino().hashCode()) + "-" +
                    Math.max(a.getOrigem().hashCode(), a.getDestino().hashCode());

            if (arestasDesenhadas.contains(chave)) continue;
            arestasDesenhadas.add(chave);

            boolean fazParteDoCaminho = eArestaDoCaminho(a, caminho);

            Line linha = new Line(a.getOrigem().getX(), a.getOrigem().getY(), a.getDestino().getX(), a.getDestino().getY());
            linha.setStroke(fazParteDoCaminho ? Color.web("#e74c3c") : Color.web("#bdc3c7"));
            linha.setStrokeWidth(fazParteDoCaminho ? 4 : 2);

            double posX = (a.getOrigem().getX() + a.getDestino().getX()) / 2;
            double posY = (a.getOrigem().getY() + a.getDestino().getY()) / 2;
            Text txtPeso = new Text(posX, posY - 5, String.format("%.0f km", a.getPeso()));
            txtPeso.setStyle("-fx-font-size: 11px; -fx-fill: #7f8c8d; -fx-font-weight: bold;");

            paneMapa.getChildren().addAll(linha, txtPeso);
        }

        // Desenhar Vértices
        for (Vertice v : grafo.getVertices()) {
            boolean noCaminho = caminho.contains(v);

            Circle circulo = new Circle(v.getX(), v.getY(), 18);
            if (v.equals(origem)) {
                circulo.setFill(Color.web("#27ae60"));
            } else if (v.equals(destino)) {
                circulo.setFill(Color.web("#c0392b"));
            } else if (noCaminho) {
                circulo.setFill(Color.web("#f39c12"));
            } else {
                circulo.setFill(Color.web("#3498db"));
            }

            circulo.setStroke(Color.WHITE);
            circulo.setStrokeWidth(2);

            Text txtNome = new Text(v.getX() - 25, v.getY() + 32, v.getNome());
            txtNome.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2c3e50;");

            paneMapa.getChildren().addAll(circulo, txtNome);
        }
    }

    private boolean eArestaDoCaminho(Aresta a, List<Vertice> caminho) {
        for (int i = 0; i < caminho.size() - 1; i++) {
            Vertice v1 = caminho.get(i);
            Vertice v2 = caminho.get(i + 1);
            if ((a.getOrigem().equals(v1) && a.getDestino().equals(v2)) ||
                    (a.getOrigem().equals(v2) && a.getDestino().equals(v1))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}