package view;

import dao.ConsultasDAO;
import dao.EdicionDAO;
import dao.JugadorDAO;
import dao.MazoDAO;
import model.Edicion;
import model.Jugador;
import model.Mazo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultasView extends JPanel {

    private ConsultasDAO consultasDAO;
    private EdicionDAO   edicionDAO;
    private JugadorDAO   jugadorDAO;
    private MazoDAO      mazoDAO;

    // Panel de resultados compartido
    private JTable tablaResultados;
    private DefaultTableModel modeloResultados;
    private JLabel lblInfo;

    // Controles de las consultas con parámetros
    private JComboBox<Edicion>  cboEdicion;
    private JComboBox<Mazo>     cboMazo;
    private JComboBox<Jugador>  cboJugador1Proc;
    private JComboBox<Jugador>  cboJugador2Proc;
    private JComboBox<Jugador>  cboGanadorProc;

    public ConsultasView() {
        consultasDAO = new ConsultasDAO();
        edicionDAO   = new EdicionDAO();
        jugadorDAO   = new JugadorDAO();
        mazoDAO      = new MazoDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: botones de consultas
        JPanel panelConsultas = new JPanel(new GridLayout(0, 1, 5, 5));
        panelConsultas.setBorder(BorderFactory.createTitledBorder("Consultas disponibles"));

        // --- Consulta 1 ---
        JButton btnC1 = new JButton("1. Cartas por tipo y color");
        btnC1.addActionListener(e -> ejecutarConsulta1());
        panelConsultas.add(btnC1);

        // --- Consulta 2 ---
        JButton btnC2 = new JButton("2. Mazos con más cartas por jugador");
        btnC2.addActionListener(e -> ejecutarConsulta2());
        panelConsultas.add(btnC2);

        // --- Consulta 3 ---
        JButton btnC3 = new JButton("3. Jugadores con más victorias");
        btnC3.addActionListener(e -> ejecutarConsulta3());
        panelConsultas.add(btnC3);

        // --- Consulta 4 (con parámetro: edición) ---
        JPanel panelC4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        cboEdicion = new JComboBox<>();
        JButton btnC4 = new JButton("4. Cartas sobre la media de coste (edición):");
        btnC4.addActionListener(e -> ejecutarConsulta4());
        panelC4.add(btnC4);
        panelC4.add(cboEdicion);
        panelConsultas.add(panelC4);

        // --- Función SQL: coste total de un mazo ---
        JPanel panelFun = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        cboMazo = new JComboBox<>();
        JButton btnFun = new JButton("5. Coste total de maná del mazo:");
        btnFun.addActionListener(e -> ejecutarFuncion());
        panelFun.add(btnFun);
        panelFun.add(cboMazo);
        panelConsultas.add(panelFun);

        // --- Procedimiento: registrar partida ---
        JPanel panelProc = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        cboJugador1Proc = new JComboBox<>();
        cboJugador2Proc = new JComboBox<>();
        cboGanadorProc  = new JComboBox<>();
        JButton btnProc = new JButton("6. Registrar partida (procedimiento)  J1:");
        btnProc.addActionListener(e -> ejecutarProcedimiento());
        panelProc.add(btnProc);
        panelProc.add(cboJugador1Proc);
        panelProc.add(new JLabel("J2:"));
        panelProc.add(cboJugador2Proc);
        panelProc.add(new JLabel("Ganador:"));
        panelProc.add(cboGanadorProc);
        panelConsultas.add(panelProc);

        add(panelConsultas, BorderLayout.NORTH);

        // Panel central: tabla de resultados
        modeloResultados = new DefaultTableModel();
        tablaResultados  = new JTable(modeloResultados);
        tablaResultados.setEnabled(false);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);

        // Barra de estado
        lblInfo = new JLabel(" ");
        lblInfo.setBorder(BorderFactory.createEtchedBorder());
        add(lblInfo, BorderLayout.SOUTH);

        cargarCombos();
    }

    public void cargarTabla() {
        cargarCombos();
        modeloResultados.setRowCount(0);
        modeloResultados.setColumnCount(0);
        lblInfo.setText(" ");
    }

    // -------------------------------------------------------
    // Consulta 1: cartas por tipo y color
    // -------------------------------------------------------
    private void ejecutarConsulta1() {
        List<String[]> datos = consultasDAO.cartasPorTipoYColor();
        String[] cols = {"Carta", "Tipo", "Color", "Rareza"};
        mostrarResultados(cols, datos);
        if (datos.isEmpty()) lblInfo.setText("Consulta 1 — Sin resultados. Comprueba que hay cartas en la BD.");
        else lblInfo.setText("Consulta 1 — " + datos.size() + " registros encontrados.");
    }

    // -------------------------------------------------------
    // Consulta 2: mazos con más cartas por jugador
    // -------------------------------------------------------
    private void ejecutarConsulta2() {
        List<String[]> datos = consultasDAO.mazosConMasCartasPorJugador();
        String[] cols = {"Jugador", "Mazo", "Total cartas"};
        mostrarResultados(cols, datos);
        if (datos.isEmpty()) lblInfo.setText("Consulta 2 — Sin resultados. No hay mazos con cartas asignadas.");
        else lblInfo.setText("Consulta 2 — " + datos.size() + " mazos encontrados.");
    }

    // -------------------------------------------------------
    // Consulta 3: jugadores con más victorias
    // -------------------------------------------------------
    private void ejecutarConsulta3() {
        List<String[]> datos = consultasDAO.jugadoresConMasVictorias();
        String[] cols = {"Jugador", "Victorias"};
        mostrarResultados(cols, datos);
        if (datos.isEmpty()) lblInfo.setText("Consulta 3 — Sin jugadores registrados.");
        else lblInfo.setText("Consulta 3 — " + datos.size() + " jugadores.");
    }

    // -------------------------------------------------------
    // Consulta 4: cartas sobre la media por edición
    // -------------------------------------------------------
    private void ejecutarConsulta4() {
        Edicion edicion = (Edicion) cboEdicion.getSelectedItem();
        if (edicion == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una edición.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<String[]> datos = consultasDAO.cartasSobreMediaPorEdicion(edicion.getIdEdicion());
        String[] cols = {"Carta", "Coste maná", "Tipo", "Rareza"};
        mostrarResultados(cols, datos);
        if (datos.isEmpty()) lblInfo.setText("Consulta 4 — Edición: " + edicion.getNombre() + " — Ninguna carta supera la media de coste.");
        else lblInfo.setText("Consulta 4 — Edición: " + edicion.getNombre() + " — " + datos.size() + " cartas sobre la media.");
    }

    // -------------------------------------------------------
    // Función SQL: coste total del mazo
    // -------------------------------------------------------
    private void ejecutarFuncion() {
        Mazo mazo = (Mazo) cboMazo.getSelectedItem();
        if (mazo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un mazo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int total = consultasDAO.costeTotalMazo(mazo.getIdMazo());
        modeloResultados.setRowCount(0);
        modeloResultados.setColumnIdentifiers(new String[]{"Mazo", "Coste total de maná"});
        modeloResultados.addRow(new Object[]{mazo.getNombre(), total});
        lblInfo.setText("Función coste_total_mazo — Mazo: " + mazo.getNombre() + " → " + total + " de maná.");
    }

    // -------------------------------------------------------
    // Procedimiento: registrar partida
    // -------------------------------------------------------
    private void ejecutarProcedimiento() {
        Jugador j1      = (Jugador) cboJugador1Proc.getSelectedItem();
        Jugador j2      = (Jugador) cboJugador2Proc.getSelectedItem();
        Jugador ganador = (Jugador) cboGanadorProc.getSelectedItem();

        if (j1 == null || j2 == null) {
            JOptionPane.showMessageDialog(this, "Selecciona los dos jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (j1.getIdJugador() == j2.getIdJugador()) {
            JOptionPane.showMessageDialog(this, "Los jugadores deben ser distintos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ganador != null && ganador.getIdJugador() != -1) {
            if (ganador.getIdJugador() != j1.getIdJugador() && ganador.getIdJugador() != j2.getIdJugador()) {
                JOptionPane.showMessageDialog(this, "El ganador debe ser uno de los dos jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int idGanador = (ganador != null && ganador.getIdJugador() != -1) ? ganador.getIdJugador() : -1;
        int idNueva   = consultasDAO.registrarPartidaProcedimiento(j1.getIdJugador(), j2.getIdJugador(), idGanador);

        if (idNueva != -1) {
            modeloResultados.setRowCount(0);
            modeloResultados.setColumnIdentifiers(new String[]{"Resultado"});
            modeloResultados.addRow(new Object[]{"Partida registrada con ID: " + idNueva});
            lblInfo.setText("Procedimiento registrar_partida — Nueva partida ID: " + idNueva);
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la partida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------
    private void mostrarResultados(String[] columnas, List<String[]> datos) {
        modeloResultados.setRowCount(0);
        modeloResultados.setColumnIdentifiers(columnas);
        for (String[] fila : datos) {
            modeloResultados.addRow(fila);
        }
    }

    private void cargarCombos() {
        List<Edicion> ediciones = edicionDAO.listarTodas();
        cboEdicion.removeAllItems();
        for (Edicion e : ediciones) cboEdicion.addItem(e);

        List<Mazo> mazos = mazoDAO.listarTodos();
        cboMazo.removeAllItems();
        for (Mazo m : mazos) cboMazo.addItem(m);

        List<Jugador> jugadores = jugadorDAO.listarTodos();
        cboJugador1Proc.removeAllItems();
        cboJugador2Proc.removeAllItems();
        cboGanadorProc.removeAllItems();
        Jugador sinGanador = new Jugador();
        sinGanador.setIdJugador(-1);
        sinGanador.setNombre("(en curso)");
        cboGanadorProc.addItem(sinGanador);
        for (Jugador j : jugadores) {
            cboJugador1Proc.addItem(j);
            cboJugador2Proc.addItem(j);
            cboGanadorProc.addItem(j);
        }
    }
}
