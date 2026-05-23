package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;

    private CartaView cartaView;
    private JugadorView jugadorView;
    private MazoView mazoView;
    private PartidaView partidaView;
    private ConsultasView consultasView;

    public MainFrame() {
        setTitle("Magic Cards DAM");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cartaView     = new CartaView();
        jugadorView   = new JugadorView();
        mazoView      = new MazoView();
        partidaView   = new PartidaView();
        consultasView = new ConsultasView();

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.add(cartaView,     "cartas");
        panelContenido.add(jugadorView,   "jugadores");
        panelContenido.add(mazoView,      "mazos");
        panelContenido.add(partidaView,   "partidas");
        panelContenido.add(consultasView, "consultas");

        JMenuBar menuBar = new JMenuBar();

        JMenu menuGestion = new JMenu("Gestión");
        JMenuItem itemCartas     = new JMenuItem("Cartas");
        JMenuItem itemJugadores  = new JMenuItem("Jugadores");
        JMenuItem itemMazos      = new JMenuItem("Mazos");
        JMenuItem itemPartidas   = new JMenuItem("Partidas");
        JMenuItem itemConsultas  = new JMenuItem("Consultas avanzadas");

        itemCartas.addActionListener(e -> {
            cardLayout.show(panelContenido, "cartas");
            cartaView.cargarTabla();
        });
        itemJugadores.addActionListener(e -> {
            cardLayout.show(panelContenido, "jugadores");
            jugadorView.cargarTabla();
        });
        itemMazos.addActionListener(e -> {
            cardLayout.show(panelContenido, "mazos");
            mazoView.cargarTabla();
        });
        itemPartidas.addActionListener(e -> {
            cardLayout.show(panelContenido, "partidas");
            partidaView.cargarTabla();
        });
        itemConsultas.addActionListener(e -> {
            cardLayout.show(panelContenido, "consultas");
            consultasView.cargarTabla();
        });

        menuGestion.add(itemCartas);
        menuGestion.add(itemJugadores);
        menuGestion.add(itemMazos);
        menuGestion.add(itemPartidas);
        menuGestion.addSeparator();
        menuGestion.add(itemConsultas);
        menuBar.add(menuGestion);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Magic Cards DAM\nPráctica Final - Programación y Bases de Datos",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcerca);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
        add(panelContenido);

        cardLayout.show(panelContenido, "cartas");
        cartaView.cargarTabla();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Si Nimbus no está disponible, se usa el L&F por defecto
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
