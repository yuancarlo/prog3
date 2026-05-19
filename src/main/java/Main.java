/*import Utilidades.ListaEnlazadaDoble;

public class Main {
    public static void main(String[] args) {
        ListaEnlazadaDoble<Integer> listaDoble = new ListaEnlazadaDoble<>();
        /*listaDoble.preponer(3);
        listaDoble.preponer(5);
        listaDoble.preponer(6);*/
        /*listaDoble.adicionar(54);
        listaDoble.adicionar(44);
        listaDoble.preponer(34);
        listaDoble.adicionar(55);
        System.out.println(listaDoble);
        System.out.println(listaDoble.toStringDetallado());
    }
} */


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. JERARQUÍA DE FIGURAS (Simples para el dibujo)
// ==========================================
abstract class Figura {
    protected int x, y;
    public Figura(int x, int y) { this.x = x; this.y = y; }
    public abstract void dibujar(Graphics2D g2);
}

class Cuadrado extends Figura {
    private int ancho, alto;
    public Cuadrado(int x, int y, int ancho, int alto) {
        super(x, y); this.ancho = ancho; this.alto = alto;
    }
    @Override
    public void dibujar(Graphics2D g2) { g2.fillRect(x, y, ancho, alto); }
}

class Circulo extends Figura {
    private int diametro;
    public Circulo(int x, int y, int diametro) {
        super(x, y); this.diametro = diametro;
    }
    @Override
    public void dibujar(Graphics2D g2) { g2.fillOval(x, y, diametro, diametro); }
}

// ==========================================
// 2. TU MODELO (Adaptado con ArrayList para que sea ejecutable)
// ==========================================
class ModeloPizarra {
    private List<Figura> figurasPropias = new ArrayList<>();
    private List<Figura> figurasRemotas = new ArrayList<>();
    private java.beans.PropertyChangeSupport observado = new java.beans.PropertyChangeSupport(this);

    public void addObserver(PropertyChangeListener listener) { observado.addPropertyChangeListener(listener); }
    
    public void agregarFiguraPropia(Figura f) {
        figurasPropias.add(f);
        observado.firePropertyChange("NUEVA_FIGURA", null, f);
    }
    public void agregarFiguraRemota(Figura f) {
        figurasRemotas.add(f);
        observado.firePropertyChange("NUEVA_FIGURA", null, f);
    }
    public List<Figura> getListaPropia() { return figurasPropias; }
    public List<Figura> getListaRemota() { return figurasRemotas; }
}

// ==========================================
// 3. EL LIENZO DE DIBUJO (La Vista)
// ==========================================
class PanelPizarra extends JPanel implements PropertyChangeListener {
    private ModeloPizarra modelo;

    public PanelPizarra(ModeloPizarra modelo) {
        this.modelo = modelo;
        this.modelo.addObserver(this);
        this.setBackground(Color.WHITE); // Fondo blanco para la pizarra
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // REQUISITO: Dibujos propios en un color (Azul)
        g2.setColor(new Color(41, 128, 185)); 
        for (Figura f : modelo.getListaPropia()) {
            f.dibujar(g2);
        }

        // REQUISITO: Dibujos del otro en otro color (Rojo/Naranja)
        g2.setColor(new Color(231, 76, 60)); 
        for (Figura f : modelo.getListaRemota()) {
            f.dibujar(g2);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("NUEVA_FIGURA".equals(evt.getPropertyName())) {
            repaint(); // Redibujar la pantalla cuando el modelo cambie
        }
    }
}

// ==========================================
// 4. VENTANA PRINCIPAL (El Controlador de la UI)
// ==========================================
public class Main extends JFrame {
    private ModeloPizarra modelo;
    private PanelPizarra panelPizarra;
    
    // Estado de la herramienta actual
    private String tipoActual = "CUADRADO";

    public Main() {
        this.modelo = new ModeloPizarra();
        this.panelPizarra = new PanelPizarra(modelo);

        setTitle("Práctico 4 - Pizarra Interactiva Local");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Usamos BorderLayout para organizar las zonas
        setLayout(new BorderLayout());

        // --- ZONA NORTE: BARRA DE HERRAMIENTAS ---
        JPanel barraHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraHerramientas.setBackground(new Color(245, 245, 245));

        JRadioButton rbCuadrado = new JRadioButton("Cuadrado", true);
        JRadioButton rbCirculo = new JRadioButton("Círculo");
        
        // Agruparlos para que solo se pueda seleccionar uno a la vez
        ButtonGroup grupoHerramientas = new ButtonGroup();
        grupoHerramientas.add(rbCuadrado);
        grupoHerramientas.add(rbCirculo);

        // Inputs de tamaño con valores por defecto
        JLabel lblAncho = new JLabel("Ancho/Diámetro:");
        JTextField txtAncho = new JTextField("50", 4);
        JLabel lblAlto = new JLabel("Alto:");
        JTextField txtAlto = new JTextField("50", 4);

        // Botón mágico para simular eventos de red antes de programar los Sockets
        JButton btnSimularRed = new JButton("Simular Recibir por Red (Rojo)");
        btnSimularRed.setBackground(new Color(230, 126, 34));
        btnSimularRed.setForeground(Color.WHITE);

        // Escuchadores de la barra
        rbCuadrado.addActionListener(e -> { tipoActual = "CUADRADO"; txtAlto.setEnabled(true); });
        rbCirculo.addActionListener(e -> { tipoActual = "CIRCULO"; txtAlto.setEnabled(false); }); // Círculo solo usa diámetro
        
        btnSimularRed.addActionListener(e -> {
            // Simulamos que llegó un círculo remoto aleatorio en la coordenada (300, 200)
            int randomX = (int) (Math.random() * 400) + 50;
            int randomY = (int) (Math.random() * 200) + 50;
            modelo.agregarFiguraRemota(new Circulo(randomX, randomY, 60));
        });

        barraHerramientas.add(rbCuadrado);
        barraHerramientas.add(rbCirculo);
        barraHerramientas.add(lblAncho);
        barraHerramientas.add(txtAncho);
        barraHerramientas.add(lblAlto);
        barraHerramientas.add(txtAlto);
        barraHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        barraHerramientas.add(btnSimularRed);

        // --- ZONA CENTRO: LA PIZARRA ---
        // Escuchar los clics del mouse sobre el lienzo blanco
        panelPizarra.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int ancho = Integer.parseInt(txtAncho.getText());

                if (tipoActual.equals("CUADRADO")) {
                    int alto = Integer.parseInt(txtAlto.getText());
                    // Creamos cuadrado propio centrando el clic en el medio de la figura
                    modelo.agregarFiguraPropia(new Cuadrado(x - ancho/2, y - alto/2, ancho, alto));
                } else {
                    // Creamos círculo propio centrando el clic
                    modelo.agregarFiguraPropia(new Circulo(x - ancho/2, y - ancho/2, ancho));
                }
            }
        });

        // Agregar componentes al Frame
        add(barraHerramientas, BorderLayout.NORTH);
        add(panelPizarra, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
