package view;

import dao.CartaDAO;
import dao.EdicionDAO;
import dao.TipoCartaDAO;
import model.Carta;
import model.Edicion;
import model.TipoCarta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartaView extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre;
    private JTextField txtCosteMana;
    private JTextField txtFuerza;
    private JTextField txtResistencia;
    private JTextArea txtTextoHabilidad;
    private JComboBox<String> cboRareza;
    private JCheckBox chkLegendario;
    private JComboBox<TipoCarta> cboTipo;
    private JComboBox<TipoCarta> cboTipoSec;
    private JComboBox<Edicion> cboEdicion;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private CartaDAO cartaDAO;
    private TipoCartaDAO tipoCartaDAO;
    private EdicionDAO edicionDAO;

    private int idCartaSeleccionada = -1;

    public CartaView() {
        cartaDAO = new CartaDAO();
        tipoCartaDAO = new TipoCartaDAO();
        edicionDAO = new EdicionDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla
        String[] columnas = {"ID", "Nombre", "Tipo", "Coste", "Rareza", "Edición"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormulario();
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(500, 0));
        add(scroll, BorderLayout.CENTER);

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la carta"));
        panelForm.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre = new JTextField(18);
        txtCosteMana = new JTextField(5);
        txtFuerza = new JTextField(5);
        txtResistencia = new JTextField(5);
        txtTextoHabilidad = new JTextArea(3, 18);
        txtTextoHabilidad.setLineWrap(true);
        cboRareza = new JComboBox<>(new String[]{"Común", "Infrecuente", "Rara", "Mítica"});
        chkLegendario = new JCheckBox("Legendario");
        cboTipo = new JComboBox<>();
        cboTipoSec = new JComboBox<>();
        cboEdicion = new JComboBox<>();

        int fila = 0;
        gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;                   gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(txtNombre, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Coste maná:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(txtCosteMana, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Fuerza:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(txtFuerza, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Resistencia:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(txtResistencia, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Rareza:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(cboRareza, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(cboTipo, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Tipo secundario:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(cboTipoSec, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Edición:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(cboEdicion, gbc);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0; panelForm.add(new JLabel("Habilidad:"), gbc);
        gbc.gridx = 1;                            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; panelForm.add(new JScrollPane(txtTextoHabilidad), gbc);

        fila++; gbc.gridx = 1; gbc.gridy = fila;  gbc.fill = GridBagConstraints.NONE; gbc.weightx = 1; panelForm.add(chkLegendario, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnNuevo = new JButton("Nueva");
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        fila++; gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        panelForm.add(panelBotones, gbc);

        add(panelForm, BorderLayout.EAST);

        // Buscador
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelBuscar.add(new JLabel("Buscar por nombre:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnMostrarTodos);
        add(panelBuscar, BorderLayout.NORTH);

        // Eventos
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());

        btnBuscar.addActionListener(e -> {
            String nombre = txtBuscar.getText().trim();
            if (nombre.isEmpty()) {
                cargarTabla();
            } else {
                List<Carta> lista = cartaDAO.buscarPorNombre(nombre);
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "No se encontraron cartas con el nombre \"" + nombre + "\".",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                }
                cargarTablaConLista(lista);
            }
        });

        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTabla();
        });

        cargarCombos();
    }

    public void cargarTabla() {
        List<Carta> lista = cartaDAO.listarTodos();
        cargarTablaConLista(lista);
    }

    private void cargarTablaConLista(List<Carta> lista) {
        modeloTabla.setRowCount(0);
        if (lista.isEmpty()) {
            modeloTabla.addRow(new Object[]{"-", "Sin resultados", "", "", "", ""});
            return;
        }
        for (Carta c : lista) {
            String tipo = c.getTipoCarta() != null ? c.getTipoCarta().getNombre() : "";
            String edicion = c.getEdicion() != null ? c.getEdicion().getNombre() : "";
            modeloTabla.addRow(new Object[]{
                c.getIdCarta(), c.getNombre(), tipo, c.getCosteMana(), c.getRareza(), edicion
            });
        }
    }

    private void cargarFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Carta c = cartaDAO.obtenerPorId(id);
        if (c == null) return;
        idCartaSeleccionada = c.getIdCarta();
        txtNombre.setText(c.getNombre());
        txtCosteMana.setText(String.valueOf(c.getCosteMana()));
        txtFuerza.setText(c.getFuerza() != null ? String.valueOf(c.getFuerza()) : "");
        txtResistencia.setText(c.getResistencia() != null ? String.valueOf(c.getResistencia()) : "");
        txtTextoHabilidad.setText(c.getTextoHabilidad());
        cboRareza.setSelectedItem(c.getRareza());
        chkLegendario.setSelected(c.isLegendario());
        seleccionarEnCombo(cboTipo, c.getTipoCarta() != null ? c.getTipoCarta().getIdTipo() : -1);
        seleccionarEnCombo(cboTipoSec, c.getTipoSecundario() != null ? c.getTipoSecundario().getIdTipo() : -1);
        seleccionarEnComboEdicion(cboEdicion, c.getEdicion() != null ? c.getEdicion().getIdEdicion() : -1);
    }

    private void guardar() {
        if (!validar()) return;
        Carta c = new Carta();
        c.setNombre(txtNombre.getText().trim());
        c.setCosteMana(Integer.parseInt(txtCosteMana.getText().trim()));
        String fuerza = txtFuerza.getText().trim();
        String resistencia = txtResistencia.getText().trim();
        c.setFuerza(fuerza.isEmpty() ? null : Integer.parseInt(fuerza));
        c.setResistencia(resistencia.isEmpty() ? null : Integer.parseInt(resistencia));
        c.setTextoHabilidad(txtTextoHabilidad.getText().trim());
        c.setRareza((String) cboRareza.getSelectedItem());
        c.setLegendario(chkLegendario.isSelected());
        c.setTipoCarta((TipoCarta) cboTipo.getSelectedItem());
        TipoCarta tipoSec = (TipoCarta) cboTipoSec.getSelectedItem();
        if (tipoSec != null && tipoSec.getIdTipo() != -1) c.setTipoSecundario(tipoSec);
        c.setEdicion((Edicion) cboEdicion.getSelectedItem());

        boolean ok;
        if (idCartaSeleccionada == -1) {
            ok = cartaDAO.insertar(c);
            if (ok) JOptionPane.showMessageDialog(this, "Carta añadida correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al añadir la carta.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            c.setIdCarta(idCartaSeleccionada);
            ok = cartaDAO.actualizar(c);
            if (ok) JOptionPane.showMessageDialog(this, "Carta actualizada correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al actualizar la carta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void eliminar() {
        if (idCartaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una carta para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar esta carta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = cartaDAO.eliminar(idCartaSeleccionada);
            if (ok) JOptionPane.showMessageDialog(this, "Carta eliminada.");
            else JOptionPane.showMessageDialog(this, "Error al eliminar la carta.\nPuede estar en uso en algún mazo.", "Error", JOptionPane.ERROR_MESSAGE);
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
        if (txtCosteMana.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El coste de maná es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtCosteMana.requestFocus();
            return false;
        }
        try {
            int coste = Integer.parseInt(txtCosteMana.getText().trim());
            if (coste < 0) {
                JOptionPane.showMessageDialog(this, "El coste de maná no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El coste de maná debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtFuerza.getText().trim().isEmpty()) {
            try { Integer.parseInt(txtFuerza.getText().trim()); }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La fuerza debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (!txtResistencia.getText().trim().isEmpty()) {
            try { Integer.parseInt(txtResistencia.getText().trim()); }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La resistencia debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (cboTipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un tipo de carta.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cboEdicion.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una edición.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        idCartaSeleccionada = -1;
        txtNombre.setText("");
        txtCosteMana.setText("");
        txtFuerza.setText("");
        txtResistencia.setText("");
        txtTextoHabilidad.setText("");
        cboRareza.setSelectedIndex(0);
        chkLegendario.setSelected(false);
        if (cboTipo.getItemCount() > 0) cboTipo.setSelectedIndex(0);
        if (cboTipoSec.getItemCount() > 0) cboTipoSec.setSelectedIndex(0);
        if (cboEdicion.getItemCount() > 0) cboEdicion.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void cargarCombos() {
        List<TipoCarta> tipos = tipoCartaDAO.listarTodos();
        cboTipo.removeAllItems();
        cboTipoSec.removeAllItems();
        TipoCarta sinTipo = new TipoCarta();
        sinTipo.setIdTipo(-1);
        sinTipo.setNombre("(ninguno)");
        cboTipoSec.addItem(sinTipo);
        for (TipoCarta t : tipos) {
            cboTipo.addItem(t);
            cboTipoSec.addItem(t);
        }
        if (tipos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay tipos de carta en la base de datos.\nEjecuta sql/seed.sql para cargar los datos de prueba.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        List<Edicion> ediciones = edicionDAO.listarTodas();
        cboEdicion.removeAllItems();
        for (Edicion e : ediciones) cboEdicion.addItem(e);
        if (ediciones.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay ediciones en la base de datos.\nEjecuta sql/seed.sql para cargar los datos de prueba.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarEnCombo(JComboBox<TipoCarta> combo, int idTipo) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdTipo() == idTipo) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarEnComboEdicion(JComboBox<Edicion> combo, int idEdicion) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdEdicion() == idEdicion) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}
