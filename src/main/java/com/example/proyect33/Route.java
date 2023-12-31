package com.example.proyect33;

import java.util.*;

/**
 * Clase que representa una Ruta de los aviones.
 */
public class Route {
    private final Nodo startNode;
    private final Nodo endNode;
    private int weight;
    private final String type;
    public static List<Nodo> nodes;
    private int danger;

    /**
     * constructor
     * @param startNode
     * @param endNode
     * @param type
     */
    public Route(Nodo startNode, Nodo endNode, String type) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = 0;
        this.type = type;
        this.danger = 0;

    }

    /**
     * Establece el peso de la Ruta.
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Incrementa el nivel de peligro de la Ruta.
     */
    public void increaseDanger() {
        this.danger += 1;
    }

    /**
     * Decrementa el nivel de peligro de la Ruta.
     */
    public void decreaseDanger() {
        this.danger += 1;
    }

    /**
     * Obtiene el Nodo de inicio de la Ruta.
     * @return
     */
    public Nodo getStartNode() {
        return startNode;
    }

    /**
     * Obtiene el Nodo de fin de la Ruta.
     * @return
     */
    public Nodo getEndNode() {
        return endNode;
    }

    /**
     * Obtiene el peso de la Ruta.
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Obtiene el tipo de la Ruta.
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Lista estática de rutas.
     */
    public static List<Route> routes;

    /**
     * Agrega un Nodo a la lista de nodos.
     * @param node
     */
    public void addNode(Nodo node) {
        nodes.add(node);
    }

    /**
     *
     Genera rutas aleatorias.
     */
    public static void generateRandomRoutes() {
        Random random = new Random();
        int min = 1;
        int randomNumber;
        int numNodes = HelloApplication.nodes.size();

        for (int i = 0; i < numNodes; i++) {
            randomNumber = random.nextInt(HelloApplication.nodes.size() - min +1);
            System.out.println("i:"+randomNumber);
            Nodo startNode = HelloApplication.nodes.get(randomNumber);

            for (int j = i + 1; j < numNodes; j++) {
                randomNumber = random.nextInt(HelloApplication.nodes.size() - min +1);
                System.out.println("j:"+randomNumber);
                Nodo endNode = HelloApplication.nodes.get(randomNumber);

                String type = getRandomRouteType();

                Route route = new Route(startNode, endNode, type);
                setWeights(route);
                HelloApplication.routes.add(route);
            }
        }
    }

    /**
     * Obtiene un tipo de ruta aleatorio.
     * @return
     */
    private static String getRandomRouteType() {
        String[] routeTypes = {"Continental", "Interoceanica"};
        Random random = new Random();
        int index = random.nextInt(routeTypes.length);
        return routeTypes[index];
    }

    /**
     * Calcula la distancia entre dos puntos en un plano.
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @return
     */
    public static double calculateDistance(int row1, int col1, int row2, int col2) {
        int dx = Math.abs(col2 - col1);
        int dy = Math.abs(row2 - row1);
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Establece los pesos de la Ruta.
     * @param route
     */
    public static void setWeights(Route route) {
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

    /**
     * Encuentra el camino más corto entre dos nodos
     * @param startNode
     * @param endNode
     * @return
     */
    public List<Nodo> findShortestPath(Nodo startNode, Nodo endNode) {
        // Initialize the distances map with infinity for all nodes except the start node
        Map<Nodo, Integer> distances = new HashMap<>();
        for (Nodo node : HelloApplication.nodes) {
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

    /**
     * Obtiene las rutas vecinas de un nodo.
     * @param node
     * @return
     */
    private List<Route> getNeighboringRoutes(Nodo node) {
        List<Route> neighboringRoutes = new ArrayList<>();
        for (Route route : HelloApplication.routes) {
            if (route.getStartNode().equals(node)) {
                neighboringRoutes.add(route);
            }
        }
        return neighboringRoutes;
    }

}

