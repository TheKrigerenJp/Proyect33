package com.example.proyect33;
import javax.swing.*;

public class AirplaneFactoryGUI {
    private JFrame frame;
    private AirplaneFactory airplaneFactory;
    private Airplane[] airplanes;
    private JTextArea outputTextArea;

    public AirplaneFactoryGUI() {
        airplaneFactory = new AirplaneFactory();

        // Crear la ventana
        frame = new JFrame("Airplane Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Crear los componentes de la interfaz
        JPanel panel = new JPanel();
        JButton createButton = new JButton("Crear avión");
        JButton sortByNameButton = new JButton("Ordenar por nombre");
        JButton sortByVelocityButton = new JButton("Ordenar por velocidad");
        JButton sortByFuelButton = new JButton("Ordenar por combustible");
        JButton printButton = new JButton("Imprimir aviones");
        outputTextArea = new JTextArea(10, 30);

        // Agregar el ActionListener al botón "Crear avión"
        createButton.addActionListener(e -> {
            String type = JOptionPane.showInputDialog(frame, "Ingrese el tipo de avión:");
            Nodo aeropuerto = new Nodo(1, 1, 1); // Crear un aeropuerto específico con los parámetros deseados
            Airplane airplane = airplaneFactory.createAirplane(type, aeropuerto);
            outputTextArea.append("Avión creado:\n" + airplane.toString() + "\n");
        });

        // Definir método para mostrar los aviones creados por el usuario en el JTextArea
        printButton.addActionListener(e -> {
            showUserCreatedAirplanes();
        });

        // Agregar los componentes al panel
        panel.add(createButton);
        panel.add(sortByNameButton);
        panel.add(sortByVelocityButton);
        panel.add(sortByFuelButton);
        panel.add(printButton);

        // Agregar el JTextArea al panel usando JScrollPane
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        panel.add(scrollPane);

        // Agregar el panel a la ventana
        frame.add(panel);

        // Mostrar la ventana
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.setVisible(true);
    }

    private void showUserCreatedAirplanes() {
        outputTextArea.append("Aviones creados por el usuario:\n");
        for (Airplane airplane : airplanes) {
            if (airplane != null) {
                outputTextArea.append(airplane.toString() + "\n");
            }
        }
        outputTextArea.append("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AirplaneFactoryGUI gui = new AirplaneFactoryGUI();
                gui.airplanes = new Airplane[5];
            }
        });
    }
}

