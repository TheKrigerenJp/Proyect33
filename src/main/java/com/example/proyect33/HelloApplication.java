package com.example.proyect33;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * Genera una interfaz gráfica basada en cuadrículas con formas, rutas y aviones animados.
 */
public class HelloApplication extends Application {

    private static final int ROWS = 15;
    private static final int COLUMNS = 25;
    private static final int RECT_WIDTH = 35;
    private static final int RECT_HEIGHT = 40;
    private static final Color GRAY_COLOR = Color.GRAY;

    private Random random;
    public static List<Nodo> nodes;
    private List<Circle> airplanes;

    public static List<Route> routes = new ArrayList<>();

    /**
     *El punto de entrada de la aplicación JavaFX.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = createGridPane();
        selectAndDrawShapes(gridPane);
        drawAirplanes(gridPane);
        Route.generateRandomRoutes();
        drawRoutes(gridPane);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        animateAirplanes();
        printGraphInformation();
        printGraphPaths();


    }

    /**
     * Crea el gridPane
     * @return
     */
    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle rectangle = createRectangle();
                gridPane.add(rectangle, col, row);
            }
        }

        return gridPane;
    }

    /**
     * Crea un rectángulo con color de relleno aleatorio
     * @return
     */
    private Rectangle createRectangle() {
        Rectangle rectangle = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
        rectangle.setFill(Math.random() < 0.5 ? Color.GREEN : Color.BLUE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        return rectangle;
    }

    /**
     *
     Selecciona y dibuja formas en el panel de cuadrícula.
     * @param gridPane
     */
    private void selectAndDrawShapes(GridPane gridPane) {
        random = new Random();
        int selectedCells = random.nextInt(10 - 6 + 1) + 6;

        nodes = new ArrayList<>();
        for (int i = 0; i < selectedCells; i++) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLUMNS);
            String cellCoordinate = row + "-" + col;
            if (!isCoordinateSelected(cellCoordinate)) {
                drawShape(gridPane, row, col, i + 1);
                nodes.add(new Nodo(row, col, i + 1));
            } else {
                i--;
            }
        }
    }

    /**
     * Comprueba si ya se ha seleccionado una coordenada para una forma.
     * @param coordinate
     * @return
     */
    private boolean isCoordinateSelected(String coordinate) {
        for (Nodo node : nodes) {
            String selectedCoordinate = node.getRow() + "-" + node.getCol();
            if (selectedCoordinate.equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dibuja una forma en el Gridpane.
     * @param gridPane
     * @param row
     * @param col
     * @param number
     */
    private void drawShape(GridPane gridPane, int row, int col, int number) {
        Rectangle rectangle = (Rectangle) gridPane.getChildren().get(row * COLUMNS + col);

        if (rectangle.getFill().equals(Color.GREEN)) {
            Polygon triangle = createTriangle();
            gridPane.add(triangle, col, row);
        } else if (rectangle.getFill().equals(Color.BLUE)) {
            Polygon rhombus = createRhombus();
            gridPane.add(rhombus, col, row);
        }

        Label label = createLabel(String.valueOf(number));
        gridPane.add(label, col, row);
    }

    /**
     * crea un triangulo
     * @return
     */
    private Polygon createTriangle() {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 0.0,
                RECT_WIDTH / 2.0, (double) RECT_HEIGHT,
                (double) RECT_WIDTH, 0.0
        );
        triangle.setFill(GRAY_COLOR);
        return triangle;
    }

    /**
     * crea un rombo
     * @return
     */
    private Polygon createRhombus() {
        Polygon rhombus = new Polygon();
        rhombus.getPoints().addAll(
                RECT_WIDTH / 2.0, 0.0,
                (double) RECT_WIDTH, RECT_HEIGHT / 2.0,
                RECT_WIDTH / 2.0, (double) RECT_HEIGHT,
                0.0, RECT_HEIGHT / 2.0
        );
        rhombus.setFill(GRAY_COLOR);
        return rhombus;
    }

    /**
     * crea un texto con el texto recibido
     * @param text
     * @return
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 10px;");
        return label;
    }

    /**
     * Se ponen los aviones vizualmente en el gridPane
     * @param gridPane
     */
    private void drawAirplanes(GridPane gridPane) {
        airplanes = new ArrayList<>();
        for (Nodo node : nodes) {
            Circle airplane = createAirplane(node.getRow(), node.getCol());
            gridPane.add(airplane, node.getCol(), node.getRow());
            airplanes.add(airplane);
        }
    }

    /**
     * Crea los aviones
     * @param row
     * @param col
     * @return
     */
    private Circle createAirplane(int row, int col) {
        Circle airplane = new Circle(5, Color.YELLOWGREEN);
        airplane.setStroke(Color.BLACK);
        GridPane.setHalignment(airplane, HPos.CENTER);
        GridPane.setValignment(airplane, VPos.CENTER);
        return airplane;
    }

    /**
     * dibuja las rutas en el gridpane
     * @param gridPane
     */
    private void drawRoutes(GridPane gridPane) {
        for (Route route: routes) {
            Nodo node1 = route.getStartNode();
            Nodo node2 = route.getEndNode();
            Line routeLine = createRouteLine(node1.getRow(), node1.getCol(), node2.getRow(), node2.getCol());
            GridPane.setConstraints(routeLine, 0, 0, gridPane.getColumnCount(), gridPane.getRowCount(), HPos.CENTER, VPos.CENTER);
            gridPane.getChildren().add(routeLine);
        }
    }

    /**
     * Crea una línea que representa una ruta entre dos nodos.
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @return
     */
    private Line createRouteLine(int row1, int col1, int row2, int col2) {
        double startX = col1 * RECT_WIDTH + RECT_WIDTH / 2.0;
        double startY = row1 * RECT_HEIGHT + RECT_HEIGHT / 2.0;
        double endX = col2 * RECT_WIDTH + RECT_WIDTH / 2.0;
        double endY = row2 * RECT_HEIGHT + RECT_HEIGHT / 2.0;

        Line routeLine = new Line(startX, startY, endX, endY);
        routeLine.setStroke(Color.ORANGE);
        return routeLine;
    }

    /**
     * Anima los aviones a lo largo de sus rutas.
     */
    private void animateAirplanes() {
        for (int i = 0; i < nodes.size(); i++) {
            Nodo node = nodes.get(i);
            Circle airplane = airplanes.get(i);
            List<Line> routes = getRoutesForNode(node);

            if (routes.size() > 0) {
                Timeline timeline = createAnimationTimeline(airplane, routes);
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        }
    }


    /**
     *
     Recupera las rutas para un nodo determinado.
     * @param node
     * @return
     */
    private List<Line> getRoutesForNode(Nodo node) {
        List<Line> routes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (i != nodes.indexOf(node)) {
                Nodo targetNode = nodes.get(i);
                Line routeLine = createRouteLine(node.getRow(), node.getCol(), targetNode.getRow(), targetNode.getCol());
                routes.add(routeLine);
            }
        }
        return routes;
    }

    /**
     * Crea una línea de tiempo para animar un avión a lo largo de sus rutas.
     * @param airplane
     * @param routes
     * @return
     */
    private Timeline createAnimationTimeline(Circle airplane, List<Line> routes) {
        Timeline timeline = new Timeline();

        for (Line route : routes) {
            double startX = route.getStartX();
            double startY = route.getStartY();
            double endX = route.getEndX();
            double endY = route.getEndY();

            KeyFrame startFrame = new KeyFrame(Duration.ZERO, new KeyValue(airplane.centerXProperty(), startX),
                    new KeyValue(airplane.centerYProperty(), startY));
            KeyFrame endFrame = new KeyFrame(Duration.seconds(3), new KeyValue(airplane.centerXProperty(), endX),
                    new KeyValue(airplane.centerYProperty(), endY));

            timeline.getKeyFrames().addAll(startFrame, endFrame);
        }

        return timeline;
    }

    /**
     *
     Imprime información sobre las figuras
     */

    private void printGraphInformation() {
        System.out.println("Figures:");
        for (Nodo node : nodes) {
            System.out.println(node.getName() + " at Row: " + node.getRow() + ", Col: " + node.getCol());
        }
    }

    /**
     * Imprime información sobre las rutas.
     */
    private void printGraphPaths() {
        System.out.println("Paths:");
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Nodo node1 = nodes.get(i);
                Nodo node2 = nodes.get(j);
                double distance = Route.calculateDistance(node1.getRow(), node1.getCol(), node2.getRow(), node2.getCol());
                String path = "Route " + (i + 1) + ": From " + node1.getName() + " to " + node2.getName();
                System.out.println(path + ", Distance: " + distance);
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}