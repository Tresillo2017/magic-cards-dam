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
    private JLabel lblFechaRegistro;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JugadorDAO jugadorDAO;
    private int idJugadorSeleccionado = -1;

    public JugadorView() {
        jugadorDAO = new JugadorDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla (incluye columna Nº mazos)
        String[] columnas = {"ID", "Nombre", "Email", "Fecha registro", "Mazos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(4).setMaxWidth(55);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormulario();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del jugador"));
        panelForm.setPreferredSize(new Dimension(300, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(18);
        txtEmail  = new JTextField(18);
        lblFechaRegistro = new JLabel("-");

        int fila = 0;
        gbc.gridx = 0; gbc.gridy = fila; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNombre, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panelForm.add(txtEmail, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; panelForm.add(new JLabel("Fecha registro:"), gbc);
        gbc.gridx = 1; panelForm.add(lblFechaRegistro, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnNuevo    = new JButton("Nuevo");
        btnGuardar  = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        panelForm.add(panelBotones, gbc);

        add(panelForm, BorderLayout.EAST);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Jugador> lista = jugadorDAO.listarTodosConMazos();
        for (Jugador j : lista) {
            modeloTabla.addRow(new Object[]{
                j.getIdJugador(), j.getNombre(), j.getEmail(),
                j.getFechaRegistro(), j.getNumMazos()
            });
        }
    }

    private void cargarFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        idJugadorSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        Jugador j = jugadorDAO.obtenerPorId(idJugadorSeleccionado);
        if (j == null) return;
        txtNombre.setText(j.getNombre());
        txtEmail.setText(j.getEmail());
        lblFechaRegistro.setText(j.getFechaRegistro() != null ? j.getFechaRegistro() : "-");
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
            else JOptionPane.showMessageDialog(this, "Error al añadir el jugador.\nEl email puede estar duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            j.setIdJugador(idJugadorSeleccionado);
            ok = jugadorDAO.actualizar(j);
            if (ok) JOptionPane.showMessageDialog(this, "Jugador actualizado correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al actualizar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void eliminar() {
        if (idJugadorSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar este jugador?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = jugadorDAO.eliminar(idJugadorSeleccionado);
            if (ok) JOptionPane.showMessageDialog(this, "Jugador eliminado.");
            else JOptionPane.showMessageDialog(this, "Error al eliminar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        }
    }

    private boolean validar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El email es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        if (!txtEmail.getText().trim().contains("@")) {
            JOptionPane.showMessageDialog(this, "El email no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        idJugadorSeleccionado = -1;
        txtNombre.setText("");
        txtEmail.setText("");
        lblFechaRegistro.setText("-");
        tabla.clearSelection();
    }
}
