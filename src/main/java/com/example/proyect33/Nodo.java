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

    /**
     * Constructor
     * @param row
     * @param col
     * @param number
     */
    public Nodo (int row, int col, int number) {
        this.row = row;
        this.col = col;
        this.name = getNodeName(number);
        this.type = getRandomNodeType();
        this.fuel = 100;
        this.availableSlots = getRandomHangarSize();
    }

    /**
     * Establece la cantidad de espacios disponibles en el Nodo.
     * @param availableSlots
     */
    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    /**
     * Obtiene la cantidad de espacios disponibles en el Nodo.
     * @return
     */
    public int getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Obtiene el tipo del Nodo.
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Obtiene la fila del Nodo.
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna del Nodo.
     * @return
     */
    public int getCol() {
        return col;
    }

    /**
     * Obtiene el nombre del Nodo.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * obtiene el combustible del nodo
     * @return
     */
    public int getFuel() {
        return fuel;
    }

    /**
     * Establece el nivel de combustible del Nodo.
     * @param fuel
     */
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    /**
     * Genera el nombre del Nodo basado en su número
     * @param number
     * @return
     */
    private String getNodeName(int number) {
        return (number % 2 == 0) ? "Rhombo #" + number : "Triangle #" + number;
    }

    /**
     * Genera un tipo de Nodo aleatorio.
     * @return
     */
    private String getRandomNodeType() {
        String[] nodeTypes = {"Aeropuerto", "Porta-Aviones"};
        Random random = new Random();
        int index = random.nextInt(nodeTypes.length);
        return nodeTypes[index];
    }

    /**
     * Genera un tamaño de hangar aleatorio.
     * @return
     */
    private int getRandomHangarSize() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
