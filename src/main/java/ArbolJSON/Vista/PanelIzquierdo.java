package ArbolJSON.Vista;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class PanelIzquierdo extends JPanel {
    private static final Logger logger = LogManager.getRootLogger();
    private JTextArea txtJson;
    private JButton btnActualizar;
    private JButton btnSubirDocumento;

    public PanelIzquierdo() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("JSON"));

        txtJson = new JTextArea(20, 30);
        txtJson.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(txtJson), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnActualizar = new JButton("Actualizar Árbol ");
        btnSubirDocumento = new JButton("Subir Documento JSON");

        panelBotones.add(btnSubirDocumento);
        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void setJsonTexto(String json) { txtJson.setText(json); }
    public String getJsonTexto() { return txtJson.getText(); }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnSubirDocumento() { return btnSubirDocumento; }

    public void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                String contenido = new String(Files.readAllBytes(archivo.toPath()));
                txtJson.setText(contenido);
                logger.info("Archivo cargado exitosamente: {}", archivo.getName());
            } catch (Exception ex) {
                logger.error("Error al leer archivo JSON", ex);
                JOptionPane.showMessageDialog(this, "Error al leer archivo: " + ex.getMessage());
            }
        }
    }
}
