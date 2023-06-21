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
 * Clase main
 */
public class HelloApplication extends Application {

    private static final int ROWS = 15;
    private static final int COLUMNS = 25;
    private static final int RECT_WIDTH = 25;
    private static final int RECT_HEIGHT = 30;
    private static final Color GRAY_COLOR = Color.GRAY;

    private static Random random;
    public static List<Nodo> nodes;
    private List<Circle> airplanes;
    public static List<Route> routes = new ArrayList<>();


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

    private Rectangle createRectangle() {
        Rectangle rectangle = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
        rectangle.setFill(Math.random() < 0.5 ? Color.GREEN : Color.BLUE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        return rectangle;
    }

    private static void selectAndDrawShapes(GridPane gridPane) {
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
        }System.out.println(nodes);
    }

    private static boolean isCoordinateSelected(String coordinate) {
        for (Nodo node : nodes) {
            String selectedCoordinate = node.getRow() + "-" + node.getCol();
            if (selectedCoordinate.equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    private static void drawShape(GridPane gridPane, int row, int col, int number) {
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

    private static Polygon createTriangle() {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 0.0,
                RECT_WIDTH / 2.0, (double) RECT_HEIGHT,
                (double) RECT_WIDTH, 0.0
        );
        triangle.setFill(GRAY_COLOR);
        return triangle;
    }

    private static Polygon createRhombus() {
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

    private static Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 10px;");
        return label;
    }

    private void drawAirplanes(GridPane gridPane) {
        airplanes = new ArrayList<>();
        for (Nodo node : nodes) {
            Circle airplane = createAirplane(node.getRow(), node.getCol());
            gridPane.add(airplane, node.getCol(), node.getRow());
            airplanes.add(airplane);
        }
    }

    private Circle createAirplane(int row, int col) {
        Circle airplane = new Circle(5, Color.YELLOWGREEN);
        airplane.setStroke(Color.BLACK);
        GridPane.setHalignment(airplane, HPos.CENTER);
        GridPane.setValignment(airplane, VPos.CENTER);
        return airplane;
    }

    private void drawRoutes(GridPane gridPane) {
        System.out.println("aca aca");
        /*Nodo node1 = routes.get(0).getStartNode();
        Nodo node2 =  routes.get(0).getEndNode();
        Line routeLine = createRouteLine(node1.getRow(), node1.getCol(), node2.getRow(), node2.getCol());
        //Line routeLine = createRouteLine(50, 50, 300, 300);
        GridPane.setConstraints(routeLine, (node1.getCol() + node2.getCol())/2 , (node1.getRow() + node2.getRow())/2, gridPane.getColumnCount(), gridPane.getRowCount(), HPos.LEFT, VPos.TOP);
        gridPane.getChildren().add(routeLine);*/
        for (Route route: routes) {
            Nodo node1 = route.getStartNode();
            Nodo node2 = route.getEndNode();
            Line routeLine = createRouteLine(node1.getRow(), node1.getCol(), node2.getRow(), node2.getCol());
            GridPane.setConstraints(routeLine, (node1.getCol() + node2.getCol())/2 , (node1.getRow() + node2.getRow())/2, gridPane.getColumnCount(), gridPane.getRowCount()); //HPos.CENTER, VPos.CENTER);
            gridPane.getChildren().add(routeLine);

        }


    }


    private Line createRouteLine(int row1, int col1, int row2, int col2) {
        double startX = col1 * RECT_WIDTH + RECT_WIDTH / 2.0;
        System.out.println("Inicio x" + startX);
        double startY = row1 * RECT_HEIGHT + RECT_HEIGHT / 2.0;
        System.out.println("Inicio y" + startY);
        double endX = col2 * RECT_WIDTH + RECT_WIDTH / 2.0;
        System.out.println("Final x" + endX);
        double endY = row2 * RECT_HEIGHT + RECT_HEIGHT / 2.0;
        System.out.println("Final y" + endY);

        Line routeLine = new Line();
        routeLine.setStartX(startX);
        routeLine.setStartY(startY);
        routeLine.setEndX(endX);
        routeLine.setEndY(endY);
        routeLine.setStroke(Color.ORANGE);
        return routeLine;
    }



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



    private List<Line> getRoutesForNode(Nodo node) {
        List<Line> routes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (i != nodes.indexOf(node)) {
                Nodo targetNode = nodes.get(i);
                //Line routeLine = createRouteLine(node.getRow(), node.getCol(), targetNode.getRow(), targetNode.getCol());
               // routes.add(routeLine);
            }
        }
        return routes;
    }

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



    private void printGraphInformation() {
        System.out.println("Figures:");
        for (Nodo node : nodes) {
            System.out.println(node.getName() + " at Row: " + node.getRow() + ", Col: " + node.getCol());
        }
    }

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