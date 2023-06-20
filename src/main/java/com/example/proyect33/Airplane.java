package com.example.proyect33;

import java.util.List;
import java.util.Random;


public class Airplane {

    private Random random;
    private List<Route> routes;
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
}
