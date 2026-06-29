package ServidorWeb.Vista;

import ServidorWeb.Modelo.ServidorWeb;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ServidorWebVentana extends JFrame implements PropertyChangeListener {
    private final ServidorWeb servidor;
    private final JButton btnIniciar, btnParar;
    private final JLabel lblStatus, lblClientes, lblTraficoIn, lblTraficoOut;

    public ServidorWebVentana() {
        // Fuerza a la ventana a usar el estilo visual estándar de tu sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        this.servidor = new ServidorWeb(8080);
        this.servidor.observador.addPropertyChangeListener(this);

        setTitle("Panel de Control del Servidor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Panel de Controles
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBotones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Controles del Servidor", TitledBorder.LEFT, TitledBorder.TOP));

        // Botones con su apariencia nativa por defecto
        btnIniciar = new JButton("Comenzar Servidor");
        btnParar = new JButton("Detener Servidor");
        btnParar.setEnabled(false);

        btnIniciar.addActionListener(e -> servidor.comenzar());
        btnParar.addActionListener(e -> servidor.pararServidor());

        pnlBotones.add(btnIniciar);
        pnlBotones.add(btnParar);

        // Panel de Métricas
        JPanel pnlMetricas = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlMetricas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Métricas en Tiempo Real", TitledBorder.LEFT, TitledBorder.TOP));

        lblStatus = new JLabel("Estado: PARADO", SwingConstants.LEFT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblClientes = new JLabel("Clientes Atendidos: 0", SwingConstants.LEFT);
        lblTraficoIn = new JLabel("Tráfico Entrada: 0 Bytes", SwingConstants.LEFT);
        lblTraficoOut = new JLabel("Tráfico Salida: 0 Bytes", SwingConstants.LEFT);

        pnlMetricas.add(lblStatus);
        pnlMetricas.add(lblClientes);
        pnlMetricas.add(lblTraficoIn);
        pnlMetricas.add(lblTraficoOut);

        add(pnlBotones, BorderLayout.NORTH);
        add(pnlMetricas, BorderLayout.CENTER);

        // Márgenes exteriores
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setSize(380, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            switch (evt.getPropertyName()) {
                case "ESTADO" -> {
                    boolean activo = "CORRIENDO".equals(evt.getNewValue());
                    btnIniciar.setEnabled(!activo);
                    btnParar.setEnabled(activo);
                    lblStatus.setText("Estado: " + (activo ? "EN LINEA (Puerto 8080)" : "PARADO"));
                }
                case "CLIENTES" -> lblClientes.setText("Clientes Atendidos: " + evt.getNewValue());
                case "TRAFICO" -> {
                    long[] trafico = (long[]) evt.getNewValue();
                    lblTraficoIn.setText(String.format("Tráfico Entrada: %,d Bytes", trafico[0]));
                    lblTraficoOut.setText(String.format("Tráfico Salida: %,d Bytes", trafico[1]));
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServidorWebVentana::new);
    }
}