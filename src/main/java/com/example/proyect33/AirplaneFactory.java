package com.example.proyect33;

import java.util.Arrays;

/**
 * Esta clase se permite crear aviones y realizar operaciones de clasificación de aviones.
 */
public class AirplaneFactory {
    /**
     * Crea un avión del tipo especificado en el aeropuerto dado.
     * @param type
     * @param airport
     * @return
     */
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

    /**
     *
     Ordena los aviones por nombre en orden ascendente.
     * @param airplanes
     */
    public void sortAirplanesByName(Airplane[] airplanes) {
        Arrays.sort(airplanes, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
    }

    /**
     *
     Ordena los aviones por velocidad en orden ascendente.
     * @param airplanes
     */
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

    /**
     *
     Ordena los aviones por combustible en orden ascendente.
     * @param airplanes
     */
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

    /**
     * Imprime los detalles de los aviones.
     * @param airplanes
     */
    public void printAirplanes(Airplane[] airplanes) {
        for (Airplane airplane : airplanes) {
            System.out.println(airplane.getName() + ": Speed - " + airplane.getSpeed() + ", Strength - " +
                    airplane.getStrength() + ", Fuel - " + airplane.getFuel());
        }
    }
}

