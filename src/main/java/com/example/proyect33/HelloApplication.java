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

    private static final int ROWS = 20;
    private static final int COLUMNS = 35;
    private static final int RECT_WIDTH = 35;
    private static final int RECT_HEIGHT = 40;
    private static final Color GRAY_COLOR = Color.GRAY;

    private Random random;
    public List<Nodo> nodes;
    private List<Circle> airplanes;

    public List<Route> routes;


    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = createGridPane();
        selectAndDrawShapes(gridPane);
        drawAirplanes(gridPane);
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

    private boolean isCoordinateSelected(String coordinate) {
        for (Nodo node : nodes) {
            String selectedCoordinate = node.getRow() + "-" + node.getCol();
            if (selectedCoordinate.equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

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

    private Label createLabel(String text) {
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
        Circle airplane = new Circle(5, Color.RED);
        airplane.setStroke(Color.BLACK);
        GridPane.setHalignment(airplane, HPos.CENTER);
        GridPane.setValignment(airplane, VPos.CENTER);
        return airplane;
    }

    private void drawRoutes(GridPane gridPane) {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Nodo node1 = nodes.get(i);
                Nodo node2 = nodes.get(j);
                Line routeLine = createRouteLine(node1.getRow(), node1.getCol(), node2.getRow(), node2.getCol());
                gridPane.getChildren().add(routeLine);
            }
        }
    }

    private Line createRouteLine(int row1, int col1, int row2, int col2) {
        double startX = col1 * RECT_WIDTH + RECT_WIDTH / 2.0;
        double startY = row1 * RECT_HEIGHT + RECT_HEIGHT / 2.0;
        double endX = col2 * RECT_WIDTH + RECT_WIDTH / 2.0;
        double endY = row2 * RECT_HEIGHT + RECT_HEIGHT / 2.0;

        Line routeLine = new Line(startX, startY, endX, endY);
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
                Line routeLine = createRouteLine(node.getRow(), node.getCol(), targetNode.getRow(), targetNode.getCol());
                routes.add(routeLine);
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

    /*public double calculateDistance(int row1, int col1, int row2, int col2) {
        int dx = Math.abs(col2 - col1);
        int dy = Math.abs(row2 - row1);
        return Math.sqrt(dx * dx + dy * dy);
    }*/
/*

    private static class Node {
        private final int row;
        private final int col;
        private final String name;
        private final String type;

        private int fuel;

        private int availableSlots;

        public Node(int row, int col, int number) {
            this.row = row;
            this.col = col;
            this.name = getNodeName(number);
            this.type = getRandomNodeType();
            this.fuel = 100;
            this.availableSlots = getRandomHangarSize();
        }

        public void setAvailableSlots(int availableSlots) {
            this.availableSlots = availableSlots;
        }

        public int getAvailableSlots() {
            return availableSlots;
        }

        public String getType() {
            return type;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public String getName() {
            return name;
        }

        public int getFuel() {
            return fuel;
        }

        public void setFuel(int fuel) {
            this.fuel = fuel;
        }

        private String getNodeName(int number) {
            return (number % 2 == 0) ? "Rhombo #" + number : "Triangle #" + number;
        }

        private String getRandomNodeType() {
            String[] nodeTypes = {"Aeropuerto", "Porta-Aviones"};
            Random random = new Random();
            int index = random.nextInt(nodeTypes.length);
            return nodeTypes[index];
        }

        private int getRandomHangarSize() {
            Random random = new Random();
            return random.nextInt(10);
        }
    }
*/
    /*
    private static class Route {
        private final Nodo startNode;
        private final Nodo endNode;
        private int weight;
        private final String type;
        private int danger;

        public Route(Nodo startNode, Nodo endNode, String type) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.weight = 0;
            this.type = type;
            this.danger = 0;

        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void increaseDanger() {
            this.danger += 1;
        }

        public void decreaseDanger() {
            this.danger += 1;
        }

        public Nodo getStartNode() {
            return startNode;
        }

        public Nodo getEndNode() {
            return endNode;
        }

        public int getWeight() {
            return weight;
        }

        public String getType() {
            return type;
        }
    }

    private List<Route> routes;

    public void addNode(Nodo node) {
        nodes.add(node);
    }

    public void generateRandomRoutes() {
        Random random = new Random();
        int numNodes = nodes.size();

        for (int i = 0; i < numNodes; i++) {
            Nodo startNode = nodes.get(i);

            for (int j = i + 1; j < numNodes; j++) {
                Nodo endNode = nodes.get(j);

                String type = getRandomRouteType();

                Route route = new Route(startNode, endNode, type);
                routes.add(route);
            }
        }
    }

    private String getRandomRouteType() {
        String[] routeTypes = {"Continental", "Interoceanica"};
        Random random = new Random();
        int index = random.nextInt(routeTypes.length);
        return routeTypes[index];
    }

    private void setWeights(Route route) {
        Nodo startNode = route.getStartNode();
        Nodo endNode = route.getEndNode();
        int currentWeight = (int) calculateDistance(startNode.getRow(), startNode.getCol(), endNode.getRow(), endNode.getCol());

        if (Objects.equals(route.getType(), "Continental")){
            currentWeight += 1;
        }
        else if (Objects.equals(route.getType(), "Interoceanica")){
            currentWeight += 3;
        }

        if (Objects.equals(route.getEndNode().getType(), "Aeropuerto")){
            currentWeight += 1;
        }
        else if (Objects.equals(route.getEndNode().getType(), "Porta-Aviones")){
            currentWeight += 3;
        }

        currentWeight += route.danger;

        route.setWeight(currentWeight);
    }

    public List<Nodo> findShortestPath(Nodo startNode, Nodo endNode) {
        // Initialize the distances map with infinity for all nodes except the start node
        Map<Nodo, Integer> distances = new HashMap<>();
        for (Nodo node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);

        // Initialize the previous nodes map
        Map<Nodo, Nodo> previousNodes = new HashMap<>();

        // Set of unvisited nodes
        Set<Nodo> unvisitedNodes = new HashSet<>(nodes);

        // Dijkstra's algorithm
        while (!unvisitedNodes.isEmpty()) {
            // Find the node with the minimum distance
            Nodo currentNode = null;
            int minDistance = Integer.MAX_VALUE;
            for (Nodo node : unvisitedNodes) {
                int distance = distances.get(node);
                if (distance < minDistance) {
                    currentNode = node;
                    minDistance = distance;
                }
            }

            if (currentNode == null) {
                break; // No reachable nodes left
            }

            unvisitedNodes.remove(currentNode);

            // Update distances to neighboring nodes
            List<Route> neighboringRoutes = getNeighboringRoutes(currentNode);
            for (Route route : neighboringRoutes) {
                Nodo neighborNode = route.getEndNode();
                int totalDistance = distances.get(currentNode) + route.getWeight();

                if (totalDistance < distances.get(neighborNode)) {
                    distances.put(neighborNode, totalDistance);
                    previousNodes.put(neighborNode, currentNode);
                }
            }
        }

        // Reconstruct the shortest path
        List<Nodo> shortestPath = new ArrayList<>();
        Nodo currentNode = endNode;
        while (currentNode != null) {
            shortestPath.add(0, currentNode);
            currentNode = previousNodes.get(currentNode);
        }

        return shortestPath;
    }

    private List<Route> getNeighboringRoutes(Node node) {
        List<Route> neighboringRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (route.getStartNode().equals(node)) {
                neighboringRoutes.add(route);
            }
        }
        return neighboringRoutes;
    }
*/
    /**
     * Clase AVION
     */
    public class Airplane {
        private final String name;
        private final Nodo airport;
        private final int speed;
        private final int strength;
        private int fuel;

        public Airplane(String name, int speed, int strength, int fuel, Nodo airport) {
            this.name = name;
            this.speed = speed;
            this.strength = strength;
            this.fuel = fuel;
            this.airport = airport;
        }

        public int getFuel() {
            return fuel;
        }

        public void setFuel(int fuel) {
            this.fuel = fuel;
        }

        public int getSpeed() {
            return speed;
        }

        public int getStrength() {
            return strength;
        }

        public String getName() {
            return name;
        }
        public void travelShortestPath(List<Nodo> shortestPath) throws InterruptedException {
            Random random = new Random();

            System.out.println("Starting the journey!");
            System.out.println("Initial fuel: " + fuel);

            for (int i = 1; i < shortestPath.size(); i++) {
                Nodo current = shortestPath.get(i);
                Nodo previous = shortestPath.get(i - 1);
                Route route = findRoute(previous, current);

                if (route != null) {
                    int distance = route.getWeight();
                    int travelTime = distance / speed;
                    int waitTime = random.nextInt(5) + 1; // Random wait time between 1 and 5 units

                    System.out.println("\nTraveling from " + previous.getName() + " to " + current.getName());
                    System.out.println("Fuel before travel: " + fuel);

                    if (fuel >= distance) {
                        fuel -= distance;

                        distributeFuel(previous, this);

                        System.out.println("Traveling for " + travelTime + " units of time.");
                        System.out.println("Waiting for " + waitTime + " units of time.");

                        fuel -= waitTime;

                        System.out.println("Fuel after travel and wait: " + fuel);
                        wait(waitTime);

                    } else {
                        System.out.println("Not enough fuel to complete the journey!");
                        route.increaseDanger();
                        break;
                    }
                }
            }

            System.out.println("\nJourney completed!");
            System.out.println("Remaining fuel: " + fuel);
        }
    }

    public class AirplaneFactory {
        public Airplane createAirplane(String type, Nodo airport) {
            if (type.equalsIgnoreCase("Airbus A380") && airport.getAvailableSlots() > 0) {
                airport.setAvailableSlots(airport.getAvailableSlots() - 1);
                return new Airplane("Airbus A380", 500, 100, 1000, airport);

            } else if (type.equalsIgnoreCase("Boeing 747") && airport.getAvailableSlots() > 0) {
                airport.setAvailableSlots(airport.getAvailableSlots() - 1);
                return new Airplane("Boeing 747", 600, 120, 1200, airport);

            } else if (type.equalsIgnoreCase("Airbus A350") && airport.getAvailableSlots() > 0) {
                airport.setAvailableSlots(airport.getAvailableSlots() - 1);
                return new Airplane("Airbus A350", 550, 110, 1100, airport);

            } else if (type.equalsIgnoreCase("Fighter aircraft") && airport.getAvailableSlots() > 0) {
                airport.setAvailableSlots(airport.getAvailableSlots() - 1);
                return new Airplane("Fighter aircraft", 450, 90, 900, airport);

            } else if (type.equalsIgnoreCase("Boeing 767") && airport.getAvailableSlots() > 0) {
                airport.setAvailableSlots(airport.getAvailableSlots() - 1);
                return new Airplane("Boeing 767", 700, 150, 1500, airport);

            } else {
                throw new IllegalArgumentException("Invalid airplane type: " + type);
            }
        }

        public void sortAirplanesByName(Airplane[] airplanes) {
            Arrays.sort(airplanes, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
        }

        public void sortAirplanesByVelocity(Airplane[] airplanes) {
            for (int i = 1; i < airplanes.length; i++) {
                Airplane key = airplanes[i];
                int j = i - 1;

                while (j >= 0 && airplanes[j].getSpeed() > key.getSpeed()) {
                    airplanes[j + 1] = airplanes[j];
                    j = j - 1;
                }

                airplanes[j + 1] = key;
            }
        }

        public void sortAirplanesByFuel(Airplane[] airplanes) {
            int n = airplanes.length;
            int gap = n / 2;

            while (gap > 0) {
                for (int i = gap; i < n; i++) {
                    Airplane temp = airplanes[i];
                    int j = i;

                    while (j >= gap && airplanes[j - gap].getFuel() > temp.getFuel()) {
                        airplanes[j] = airplanes[j - gap];
                        j -= gap;
                    }

                    airplanes[j] = temp;
                }

                gap /= 2;
            }
        }

        public void printAirplanes(Airplane[] airplanes) {
            for (Airplane airplane : airplanes) {
                System.out.println(airplane.getName() + ": Speed - " + airplane.getSpeed() + ", Strength - " +
                        airplane.getStrength() + ", Fuel - " + airplane.getFuel());
            }
        }
    }


    private Route findRoute(Nodo previous, Nodo current) {
        for (Route route : routes) {
            if (route.getStartNode().equals(previous) && route.getEndNode().equals(current)) {
                return route;
            }
        }
        return null;
    }

    private void distributeFuel(Nodo node, Airplane airplane) {
        int availableFuel = node.getFuel();

        // Distribute fuel evenly among the passing planes
        int totalPlanes = random.nextInt(20);

        if (totalPlanes > 0) {
            int fuelPerPlane = availableFuel / totalPlanes;

            System.out.println("Distributing fuel from " + node.getName() + " to passing planes.");

            // Update the fuel of each passing plane

            airplane.setFuel(airplane.getFuel() + fuelPerPlane);


            // Update the remaining fuel of the node
            node.setFuel(availableFuel % totalPlanes);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}