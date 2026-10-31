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
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // Painel Superior de Controlo
        HBox barControle = new HBox(15);
        barControle.setAlignment(Pos.CENTER_LEFT);
        barControle.setPadding(new Insets(15, 20, 15, 20));
        barControle.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);");

        Label lblOrigem = new Label("Origem:");
        lblOrigem.setStyle("-fx-text-fill: #f1f5f9; -fx-font-weight: bold; -fx-font-size: 13px;");
        comboOrigem = new ComboBox<>();
        comboOrigem.getItems().addAll(grafo.getVertices());
        comboOrigem.getSelectionModel().selectFirst();
        comboOrigem.setPrefWidth(180);

        Label lblDestino = new Label("Destino:");
        lblDestino.setStyle("-fx-text-fill: #f1f5f9; -fx-font-weight: bold; -fx-font-size: 13px;");
        comboDestino = new ComboBox<>();
        comboDestino.getItems().addAll(grafo.getVertices());
        if (grafo.getVertices().size() > 1) {
            comboDestino.getSelectionModel().select(1);
        }
        comboDestino.setPrefWidth(180);

        Button btnTracarRota = new Button("Calcular Rota");
        btnTracarRota.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6px; -fx-cursor: hand; -fx-padding: 8px 16px;");
        btnTracarRota.setOnAction(e -> desenharGrafoECalcular());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        labelDistancia = new Label("Distância total: -");
        labelDistancia.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 14px; -fx-font-weight: bold;");

        barControle.getChildren().addAll(lblOrigem, comboOrigem, lblDestino, comboDestino, btnTracarRota, spacer, labelDistancia);
        root.setTop(barControle);

        // Área Central do Mapa Visual ampliada
        paneMapa = new Pane();
        paneMapa.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10px; -fx-border-color: #e2e8f0; -fx-border-radius: 10px; -fx-border-width: 1px;");
        root.setCenter(paneMapa);

        BorderPane.setMargin(paneMapa, new Insets(15, 0, 0, 0));

        // Janela espaçosa (1300x850) para afastar bem os nós
        Scene scene = new Scene(root, 1300, 850);
        primaryStage.setTitle("Mini GPS - Algoritmo de Dijkstra");
        primaryStage.setScene(scene);
        primaryStage.show();

        desenharGrafoECalcular();
    }

    private void inicializarGrafoExemplo() {
        grafo = new Grafo();

        // 19 Vértices com coordenadas bem distantes e isoladas
        Vertice vA = new Vertice("A", "Centro", 250, 260);
        Vertice vB = new Vertice("B", "Bairro Norte", 520, 140);
        Vertice vC = new Vertice("C", "Zona Industrial", 920, 200);
        Vertice vD = new Vertice("D", "Bairro Sul", 320, 580);
        Vertice vE = new Vertice("E", "Terminal Urbano", 640, 520);
        Vertice vF = new Vertice("F", "Universidade", 1080, 680);

        Vertice vG = new Vertice("G", "Mercado Preço A", 460, 380);
        Vertice vH = new Vertice("H", "Cinema", 780, 280);
        Vertice vI = new Vertice("I", "Padaria", 120, 440);
        Vertice vJ = new Vertice("J", "Ginásio", 440, 750);

        Vertice vK = new Vertice("K", "Hospital Central", 580, 350);
        Vertice vL = new Vertice("L", "Parque Municipal", 120, 750);
        Vertice vM = new Vertice("M", "Shopping Boulevard", 840, 450);
        Vertice vN = new Vertice("N", "Posto de Gasolina", 360, 90);
        Vertice vO = new Vertice("O", "Estação Ferroviária", 1120, 420);
        Vertice vP = new Vertice("P", "Biblioteca Pública", 200, 130);
        Vertice vQ = new Vertice("Q", "Prefeitura", 520, 660);
        Vertice vR = new Vertice("R", "Aeroporto Regional", 1220, 750);
        Vertice vS = new Vertice("S", "Teatro Municipal", 760, 90);

        // Adicionar vértices ao grafo
        grafo.adicionarVertice(vA); grafo.adicionarVertice(vB); grafo.adicionarVertice(vC);
        grafo.adicionarVertice(vD); grafo.adicionarVertice(vE); grafo.adicionarVertice(vF);
        grafo.adicionarVertice(vG); grafo.adicionarVertice(vH); grafo.adicionarVertice(vI);
        grafo.adicionarVertice(vJ); grafo.adicionarVertice(vK); grafo.adicionarVertice(vL);
        grafo.adicionarVertice(vM); grafo.adicionarVertice(vN); grafo.adicionarVertice(vO);
        grafo.adicionarVertice(vP); grafo.adicionarVertice(vQ); grafo.adicionarVertice(vR);
        grafo.adicionarVertice(vS);

        // Conexões e pesos em km
        grafo.adicionarAresta(vA, vB, 5); grafo.adicionarAresta(vA, vD, 8); grafo.adicionarAresta(vA, vI, 3);
        grafo.adicionarAresta(vB, vC, 12); grafo.adicionarAresta(vB, vE, 6); grafo.adicionarAresta(vB, vG, 4);
        grafo.adicionarAresta(vC, vH, 5); grafo.adicionarAresta(vC, vF, 7);
        grafo.adicionarAresta(vD, vI, 4); grafo.adicionarAresta(vD, vE, 4); grafo.adicionarAresta(vD, vJ, 6);
        grafo.adicionarAresta(vE, vF, 9); grafo.adicionarAresta(vE, vH, 4); grafo.adicionarAresta(vE, vJ, 5);
        grafo.adicionarAresta(vG, vH, 6); grafo.adicionarAresta(vG, vA, 4);

        grafo.adicionarAresta(vN, vB, 3); grafo.adicionarAresta(vN, vA, 4);
        grafo.adicionarAresta(vP, vA, 2); grafo.adicionarAresta(vP, vI, 3);
        grafo.adicionarAresta(vK, vG, 5); grafo.adicionarAresta(vK, vE, 4);
        grafo.adicionarAresta(vS, vH, 3); grafo.adicionarAresta(vS, vC, 6);
        grafo.adicionarAresta(vM, vE, 5); grafo.adicionarAresta(vM, vC, 4);
        grafo.adicionarAresta(vQ, vE, 3); grafo.adicionarAresta(vQ, vJ, 4);
        grafo.adicionarAresta(vL, vD, 7);
        grafo.adicionarAresta(vO, vM, 5); grafo.adicionarAresta(vO, vR, 8);
        grafo.adicionarAresta(vR, vF, 6);
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

        // 1. Desenhar Arestas com maior espaçamento para os textos
        Set<String> arestasDesenhadas = new HashSet<>();
        for (Aresta a : grafo.getArestas()) {
            String chave = Math.min(a.getOrigem().hashCode(), a.getDestino().hashCode()) + "-" +
                    Math.max(a.getOrigem().hashCode(), a.getDestino().hashCode());

            if (arestasDesenhadas.contains(chave)) continue;
            arestasDesenhadas.add(chave);

            boolean fazParteDoCaminho = eArestaDoCaminho(a, caminho);

            double x1 = a.getOrigem().getX();
            double y1 = a.getOrigem().getY();
            double x2 = a.getDestino().getX();
            double y2 = a.getDestino().getY();

            Line linha = new Line(x1, y1, x2, y2);
            linha.setStroke(fazParteDoCaminho ? Color.web("#ef4444") : Color.web("#cbd5e1"));
            linha.setStrokeWidth(fazParteDoCaminho ? 4 : 2);

            // Afastamento maior do texto de km em relação à linha
            double posX = (x1 + x2) / 2;
            double posY = (y1 + y2) / 2;
            double dx = x2 - x1;
            double dy = y2 - y1;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > 0) {
                posX += (-dy / len) * 16;
                posY += (dx / len) * 16;
            }

            Text txtPeso = new Text(posX, posY, String.format("%.0f km", a.getPeso()));
            txtPeso.setStyle("-fx-font-size: 11px; -fx-fill: #475569; -fx-font-weight: bold;");

            paneMapa.getChildren().addAll(linha, txtPeso);
        }

        // 2. Desenhar Vértices
        for (Vertice v : grafo.getVertices()) {
            boolean noCaminho = caminho.contains(v);

            Circle circulo = new Circle(v.getX(), v.getY(), 16);
            if (v.equals(origem)) {
                circulo.setFill(Color.web("#10b981")); // Verde
            } else if (v.equals(destino)) {
                circulo.setFill(Color.web("#dc2626")); // Vermelho
            } else if (noCaminho) {
                circulo.setFill(Color.web("#f59e0b")); // Laranja
            } else {
                circulo.setFill(Color.web("#3b82f6")); // Azul
            }

            circulo.setStroke(Color.WHITE);
            circulo.setStrokeWidth(2.5);

            Text txtNome = new Text(v.getNome());
            txtNome.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #1e293b;");

            double textWidth = v.getNome().length() * 5.5;
            txtNome.setX(v.getX() - textWidth / 2);
            txtNome.setY(v.getY() + 30);

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