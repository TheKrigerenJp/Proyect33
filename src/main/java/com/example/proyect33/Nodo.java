package com.example.proyect33;

import java.util.Random;

/**
 * Clase NODO
 */
public class Nodo {
    private final int row;
    private final int col;
    private final String name;
    private final String type;

    private int fuel;

    private int availableSlots;

    public Nodo (int row, int col, int number) {
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
