package view;

import dao.JugadorDAO;
import model.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JugadorView extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JLabel     lblFechaRegistro;
    private JLabel     lblEstado;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JugadorDAO jugadorDAO;
    private int idJugadorSeleccionado = -1;

    public JugadorView() {
        jugadorDAO = new JugadorDAO();
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Tabla ─────────────────────────────────────────────────
        String[] columnas = {"ID", "Nombre", "Email", "Fecha registro", "Mazos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        tabla.setAutoCreateRowSorter(true);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(4).setMaxWidth(60);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormulario();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ── Formulario ────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del jugador"));
        panelForm.setPreferredSize(new Dimension(280, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre        = new JTextField();
        txtEmail         = new JTextField();
        txtEmail.setToolTipText("Debe contener @ y ser único");
        lblFechaRegistro = new JLabel("-");

        int fila = 0;
        gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;       gbc.weightx = 0; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;                   gbc.fill = GridBagConstraints.HORIZONTAL;  gbc.weightx = 1; panelForm.add(txtNombre, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;       gbc.weightx = 0; panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL;  gbc.weightx = 1; panelForm.add(txtEmail, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; panelForm.add(new JLabel("Registro:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(lblFechaRegistro, gbc);

        btnNuevo    = new JButton("Nuevo");
        btnGuardar  = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        btnGuardar.setToolTipText("Guardar jugador (Enter)");
        btnEliminar.setToolTipText("Eliminar jugador seleccionado");

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        panelForm.add(panelBotones, gbc);

        add(panelForm, BorderLayout.EAST);

        // ── Estado ────────────────────────────────────────────────
        lblEstado = new JLabel(" ");
        lblEstado.setBorder(BorderFactory.createEtchedBorder());
        lblEstado.setFont(lblEstado.getFont().deriveFont(11f));
        add(lblEstado, BorderLayout.SOUTH);

        // ── Eventos ───────────────────────────────────────────────
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        txtNombre.addActionListener(e -> txtEmail.requestFocus());
        txtEmail.addActionListener(e -> guardar());
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Jugador> lista = jugadorDAO.listarTodosConMazos();
        if (lista.isEmpty()) {
            modeloTabla.addRow(new Object[]{"-", "Sin jugadores registrados", "", "", ""});
            lblEstado.setText("Sin jugadores registrados.");
            return;
        }
        for (Jugador j : lista) {
            modeloTabla.addRow(new Object[]{
                j.getIdJugador(), j.getNombre(), j.getEmail(), j.getFechaRegistro(), j.getNumMazos()
            });
        }
        lblEstado.setText(lista.size() + " jugadores cargados.");
    }

    private void cargarFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        Object val = modeloTabla.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        if (!(val instanceof Integer)) return;
        idJugadorSeleccionado = (int) val;
        Jugador j = jugadorDAO.obtenerPorId(idJugadorSeleccionado);
        if (j == null) return;
        txtNombre.setText(j.getNombre());
        txtEmail.setText(j.getEmail());
        lblFechaRegistro.setText(j.getFechaRegistro() != null ? j.getFechaRegistro() : "-");
        lblEstado.setText("Editando: " + j.getNombre());
    }

    private void guardar() {
        if (!validar()) return;
        Jugador j = new Jugador();
        j.setNombre(txtNombre.getText().trim());
        j.setEmail(txtEmail.getText().trim());
        boolean ok;
        if (idJugadorSeleccionado == -1) {
            ok = jugadorDAO.insertar(j);
            if (ok) JOptionPane.showMessageDialog(this, "Jugador añadido correctamente.");
            else    JOptionPane.showMessageDialog(this, "Error al añadir el jugador.\nEl email puede estar en uso.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            j.setIdJugador(idJugadorSeleccionado);
            ok = jugadorDAO.actualizar(j);
            if (ok) JOptionPane.showMessageDialog(this, "Jugador actualizado.");
            else    JOptionPane.showMessageDialog(this, "Error al actualizar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void eliminar() {
        if (idJugadorSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
            "¿Eliminar este jugador y todos sus mazos?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            boolean ok = jugadorDAO.eliminar(idJugadorSeleccionado);
            if (ok) JOptionPane.showMessageDialog(this, "Jugador eliminado.");
            else    JOptionPane.showMessageDialog(this, "Error al eliminar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        }
    }

    private boolean validar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus(); return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El email es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus(); return false;
        }
        if (!txtEmail.getText().trim().contains("@")) {
            JOptionPane.showMessageDialog(this, "El email no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus(); return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        idJugadorSeleccionado = -1;
        txtNombre.setText(""); txtEmail.setText("");
        lblFechaRegistro.setText("-");
        tabla.clearSelection();
        lblEstado.setText(" ");
        txtNombre.requestFocus();
    }
}
