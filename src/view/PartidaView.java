package view;

import dao.JugadorDAO;
import dao.PartidaDAO;
import model.Jugador;
import model.Partida;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PartidaView extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<Jugador> cboJugador1;
    private JComboBox<Jugador> cboJugador2;
    private JComboBox<Jugador> cboGanador;

    private JButton btnNueva;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private PartidaDAO partidaDAO;
    private JugadorDAO jugadorDAO;

    private int idPartidaSeleccionada = -1;

    public PartidaView() {
        partidaDAO = new PartidaDAO();
        jugadorDAO = new JugadorDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Fecha", "Jugador 1", "Jugador 2", "Ganador"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormulario();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar partida"));
        panelForm.setPreferredSize(new Dimension(280, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboJugador1 = new JComboBox<>();
        cboJugador2 = new JComboBox<>();
        cboGanador  = new JComboBox<>();

        int fila = 0;
        gbc.gridx = 0; gbc.gridy = fila; panelForm.add(new JLabel("Jugador 1:"), gbc);
        gbc.gridx = 1; panelForm.add(cboJugador1, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; panelForm.add(new JLabel("Jugador 2:"), gbc);
        gbc.gridx = 1; panelForm.add(cboJugador2, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; panelForm.add(new JLabel("Ganador:"), gbc);
        gbc.gridx = 1; panelForm.add(cboGanador, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnNueva    = new JButton("Nueva");
        btnGuardar  = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        panelBotones.add(btnNueva);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        panelForm.add(panelBotones, gbc);

        add(panelForm, BorderLayout.EAST);

        btnNueva.addActionListener(e -> limpiarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    public void cargarTabla() {
        // Recarga combos para reflejar jugadores creados en JugadorView
        cargarCombos();
        modeloTabla.setRowCount(0);
        List<Partida> lista = partidaDAO.listarTodas();
        if (lista.isEmpty()) {
            modeloTabla.addRow(new Object[]{"-", "Sin partidas registradas", "", "", ""});
            return;
        }
        for (Partida p : lista) {
            String j1      = p.getJugador1() != null ? p.getJugador1().getNombre() : "";
            String j2      = p.getJugador2() != null ? p.getJugador2().getNombre() : "";
            String ganador = p.getGanador()  != null ? p.getGanador().getNombre()  : "En curso";
            modeloTabla.addRow(new Object[]{p.getIdPartida(), p.getFecha(), j1, j2, ganador});
        }
    }

    private void cargarFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        idPartidaSeleccionada = (int) modeloTabla.getValueAt(fila, 0);
        Partida p = partidaDAO.obtenerPorId(idPartidaSeleccionada);
        if (p == null) return;
        seleccionarJugadorEnCombo(cboJugador1, p.getJugador1());
        seleccionarJugadorEnCombo(cboJugador2, p.getJugador2());
        if (p.getGanador() != null) {
            seleccionarJugadorEnCombo(cboGanador, p.getGanador());
        } else {
            cboGanador.setSelectedIndex(0);
        }
    }

    private void guardar() {
        if (!validar()) return;
        Jugador j1      = (Jugador) cboJugador1.getSelectedItem();
        Jugador j2      = (Jugador) cboJugador2.getSelectedItem();
        Jugador ganador = (Jugador) cboGanador.getSelectedItem();
        boolean sinGanador = ganador == null || ganador.getIdJugador() == -1;

        boolean ok;
        if (idPartidaSeleccionada == -1) {
            Partida p = new Partida();
            p.setJugador1(j1);
            p.setJugador2(j2);
            if (!sinGanador) p.setGanador(ganador);
            ok = partidaDAO.insertar(p);
            if (ok) JOptionPane.showMessageDialog(this, "Partida registrada correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al registrar la partida.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (sinGanador) {
                ok = partidaDAO.quitarGanador(idPartidaSeleccionada);
            } else {
                ok = partidaDAO.registrarGanador(idPartidaSeleccionada, ganador.getIdJugador());
            }
            if (ok) JOptionPane.showMessageDialog(this, "Partida actualizada correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al actualizar la partida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void eliminar() {
        if (idPartidaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una partida para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar esta partida?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = partidaDAO.eliminar(idPartidaSeleccionada);
            if (ok) JOptionPane.showMessageDialog(this, "Partida eliminada.");
            else JOptionPane.showMessageDialog(this, "Error al eliminar la partida.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        }
    }

    private boolean validar() {
        Jugador j1      = (Jugador) cboJugador1.getSelectedItem();
        Jugador j2      = (Jugador) cboJugador2.getSelectedItem();
        Jugador ganador = (Jugador) cboGanador.getSelectedItem();
        if (j1 == null || j2 == null) {
            JOptionPane.showMessageDialog(this, "Selecciona los dos jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (j1.getIdJugador() == j2.getIdJugador()) {
            JOptionPane.showMessageDialog(this, "Los dos jugadores deben ser distintos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ganador != null && ganador.getIdJugador() != -1) {
            if (ganador.getIdJugador() != j1.getIdJugador() && ganador.getIdJugador() != j2.getIdJugador()) {
                JOptionPane.showMessageDialog(this, "El ganador debe ser uno de los dos jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void limpiarFormulario() {
        idPartidaSeleccionada = -1;
        if (cboJugador1.getItemCount() > 0) cboJugador1.setSelectedIndex(0);
        if (cboJugador2.getItemCount() > 0) cboJugador2.setSelectedIndex(0);
        if (cboGanador.getItemCount()  > 0) cboGanador.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void cargarCombos() {
        List<Jugador> jugadores = jugadorDAO.listarTodos();
        cboJugador1.removeAllItems();
        cboJugador2.removeAllItems();
        cboGanador.removeAllItems();
        Jugador sinGanador = new Jugador();
        sinGanador.setIdJugador(-1);
        sinGanador.setNombre("(en curso)");
        cboGanador.addItem(sinGanador);
        for (Jugador j : jugadores) {
            cboJugador1.addItem(j);
            cboJugador2.addItem(j);
            cboGanador.addItem(j);
        }
        if (jugadores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay jugadores registrados. Ve a la sección Jugadores para añadir uno.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarJugadorEnCombo(JComboBox<Jugador> combo, Jugador jugador) {
        if (jugador == null) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdJugador() == jugador.getIdJugador()) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}
