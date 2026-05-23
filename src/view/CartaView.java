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
    private JTextArea  txtTextoHabilidad;
    private JComboBox<String>    cboRareza;
    private JCheckBox            chkLegendario;
    private JComboBox<TipoCarta> cboTipo;
    private JComboBox<TipoCarta> cboTipoSec;
    private JComboBox<Edicion>   cboEdicion;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTextField txtBuscar;
    private JLabel     lblEstado;

    private CartaDAO     cartaDAO;
    private TipoCartaDAO tipoCartaDAO;
    private EdicionDAO   edicionDAO;

    private int idCartaSeleccionada = -1;

    public CartaView() {
        cartaDAO     = new CartaDAO();
        tipoCartaDAO = new TipoCartaDAO();
        edicionDAO   = new EdicionDAO();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Barra de búsqueda ─────────────────────────────────────
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        txtBuscar = new JTextField(22);
        JButton btnBuscar      = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        txtBuscar.setToolTipText("Busca por nombre de carta (Enter para buscar)");
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnMostrarTodos);
        add(panelBuscar, BorderLayout.NORTH);

        // ── Tabla ─────────────────────────────────────────────────
        String[] columnas = {"ID", "Nombre", "Tipo", "Coste", "Rareza", "Edición"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(3).setMaxWidth(55);
        tabla.setAutoCreateRowSorter(true);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormulario();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ── Formulario ────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la carta"));
        panelForm.setPreferredSize(new Dimension(310, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre          = new JTextField();
        txtCosteMana       = new JTextField();
        txtFuerza          = new JTextField();
        txtResistencia     = new JTextField();
        txtTextoHabilidad  = new JTextArea(4, 16);
        txtTextoHabilidad.setLineWrap(true);
        txtTextoHabilidad.setWrapStyleWord(true);
        cboRareza     = new JComboBox<>(new String[]{"Común", "Infrecuente", "Rara", "Mítica"});
        chkLegendario = new JCheckBox("Legendario");
        cboTipo       = new JComboBox<>();
        cboTipoSec    = new JComboBox<>();
        cboEdicion    = new JComboBox<>();

        txtFuerza.setToolTipText("Solo para cartas de tipo Criatura");
        txtResistencia.setToolTipText("Solo para cartas de tipo Criatura");

        int f = 0;
        addFila(panelForm, gbc, f++, "Nombre:",          txtNombre);
        addFila(panelForm, gbc, f++, "Coste maná:",      txtCosteMana);
        addFila(panelForm, gbc, f++, "Fuerza:",          txtFuerza);
        addFila(panelForm, gbc, f++, "Resistencia:",     txtResistencia);
        addFila(panelForm, gbc, f++, "Rareza:",          cboRareza);
        addFila(panelForm, gbc, f++, "Tipo:",            cboTipo);
        addFila(panelForm, gbc, f++, "Tipo secundario:", cboTipoSec);
        addFila(panelForm, gbc, f++, "Edición:",         cboEdicion);
        addFila(panelForm, gbc, f++, "Habilidad:",       new JScrollPane(txtTextoHabilidad));

        gbc.gridx = 1; gbc.gridy = f++; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelForm.add(chkLegendario, gbc);

        btnNuevo    = new JButton("Nueva");
        btnGuardar  = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        btnGuardar.setToolTipText("Guardar carta (Enter)");
        btnEliminar.setToolTipText("Eliminar carta seleccionada");

        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 5, 0));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = f; gbc.gridwidth = 2;
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
        txtNombre.addActionListener(e -> guardar());

        btnBuscar.addActionListener(e -> buscar());
        txtBuscar.addActionListener(e -> buscar());
        btnMostrarTodos.addActionListener(e -> { txtBuscar.setText(""); cargarTabla(); });

        cargarCombos();
    }

    private void addFila(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = fila; gbc.fill = GridBagConstraints.NONE;       gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;                   gbc.fill = GridBagConstraints.HORIZONTAL;  gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    public void cargarTabla() {
        List<Carta> lista = cartaDAO.listarTodos();
        cargarTablaConLista(lista);
        lblEstado.setText(lista.isEmpty() ? "Sin cartas registradas." : lista.size() + " cartas cargadas.");
    }

    private void buscar() {
        String nombre = txtBuscar.getText().trim();
        if (nombre.isEmpty()) { cargarTabla(); return; }
        List<Carta> lista = cartaDAO.buscarPorNombre(nombre);
        cargarTablaConLista(lista);
        if (lista.isEmpty()) {
            lblEstado.setText("No se encontraron cartas con \"" + nombre + "\".");
            JOptionPane.showMessageDialog(this,
                "No se encontraron cartas con el nombre \"" + nombre + "\".",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblEstado.setText(lista.size() + " resultado(s) para \"" + nombre + "\".");
        }
    }

    private void cargarTablaConLista(List<Carta> lista) {
        modeloTabla.setRowCount(0);
        if (lista.isEmpty()) {
            modeloTabla.addRow(new Object[]{"-", "Sin resultados", "", "", "", ""});
            return;
        }
        for (Carta c : lista) {
            String tipo    = c.getTipoCarta() != null ? c.getTipoCarta().getNombre() : "";
            String edicion = c.getEdicion()   != null ? c.getEdicion().getNombre()   : "";
            modeloTabla.addRow(new Object[]{
                c.getIdCarta(), c.getNombre(), tipo, c.getCosteMana(), c.getRareza(), edicion
            });
        }
    }

    private void cargarFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        Object val = modeloTabla.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        if (!(val instanceof Integer)) return;
        int id = (int) val;
        Carta c = cartaDAO.obtenerPorId(id);
        if (c == null) return;
        idCartaSeleccionada = c.getIdCarta();
        txtNombre.setText(c.getNombre());
        txtCosteMana.setText(String.valueOf(c.getCosteMana()));
        txtFuerza.setText(c.getFuerza()      != null ? String.valueOf(c.getFuerza())      : "");
        txtResistencia.setText(c.getResistencia() != null ? String.valueOf(c.getResistencia()) : "");
        txtTextoHabilidad.setText(c.getTextoHabilidad());
        cboRareza.setSelectedItem(c.getRareza());
        chkLegendario.setSelected(c.isLegendario());
        seleccionarTipo(cboTipo,    c.getTipoCarta()      != null ? c.getTipoCarta().getIdTipo()      : -1);
        seleccionarTipo(cboTipoSec, c.getTipoSecundario() != null ? c.getTipoSecundario().getIdTipo() : -1);
        seleccionarEdicion(c.getEdicion() != null ? c.getEdicion().getIdEdicion() : -1);
        lblEstado.setText("Editando: " + c.getNombre());
    }

    private void guardar() {
        if (!validar()) return;
        Carta c = new Carta();
        c.setNombre(txtNombre.getText().trim());
        c.setCosteMana(Integer.parseInt(txtCosteMana.getText().trim()));
        String f = txtFuerza.getText().trim(), r = txtResistencia.getText().trim();
        c.setFuerza(f.isEmpty()      ? null : Integer.parseInt(f));
        c.setResistencia(r.isEmpty() ? null : Integer.parseInt(r));
        c.setTextoHabilidad(txtTextoHabilidad.getText().trim());
        c.setRareza((String) cboRareza.getSelectedItem());
        c.setLegendario(chkLegendario.isSelected());
        c.setTipoCarta((TipoCarta) cboTipo.getSelectedItem());
        TipoCarta sec = (TipoCarta) cboTipoSec.getSelectedItem();
        if (sec != null && sec.getIdTipo() != -1) c.setTipoSecundario(sec);
        c.setEdicion((Edicion) cboEdicion.getSelectedItem());

        boolean ok;
        if (idCartaSeleccionada == -1) {
            ok = cartaDAO.insertar(c);
            if (ok) JOptionPane.showMessageDialog(this, "Carta añadida correctamente.");
            else    JOptionPane.showMessageDialog(this, "Error al añadir la carta.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            c.setIdCarta(idCartaSeleccionada);
            ok = cartaDAO.actualizar(c);
            if (ok) JOptionPane.showMessageDialog(this, "Carta actualizada.");
            else    JOptionPane.showMessageDialog(this, "Error al actualizar la carta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void eliminar() {
        if (idCartaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una carta para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int op = JOptionPane.showConfirmDialog(this, "¿Eliminar esta carta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            boolean ok = cartaDAO.eliminar(idCartaSeleccionada);
            if (ok) JOptionPane.showMessageDialog(this, "Carta eliminada.");
            else    JOptionPane.showMessageDialog(this, "Error al eliminar.\nPuede estar en uso en algún mazo.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        }
    }

    private boolean validar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus(); return false;
        }
        try {
            int v = Integer.parseInt(txtCosteMana.getText().trim());
            if (v < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El coste de maná debe ser un número ≥ 0.", "Error", JOptionPane.ERROR_MESSAGE);
            txtCosteMana.requestFocus(); return false;
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
        txtNombre.setText(""); txtCosteMana.setText(""); txtFuerza.setText(""); txtResistencia.setText("");
        txtTextoHabilidad.setText("");
        cboRareza.setSelectedIndex(0);
        chkLegendario.setSelected(false);
        if (cboTipo.getItemCount()    > 0) cboTipo.setSelectedIndex(0);
        if (cboTipoSec.getItemCount() > 0) cboTipoSec.setSelectedIndex(0);
        if (cboEdicion.getItemCount() > 0) cboEdicion.setSelectedIndex(0);
        tabla.clearSelection();
        lblEstado.setText(" ");
        txtNombre.requestFocus();
    }

    private void cargarCombos() {
        List<TipoCarta> tipos = tipoCartaDAO.listarTodos();
        cboTipo.removeAllItems();
        cboTipoSec.removeAllItems();
        TipoCarta sinTipo = new TipoCarta(); sinTipo.setIdTipo(-1); sinTipo.setNombre("(ninguno)");
        cboTipoSec.addItem(sinTipo);
        for (TipoCarta t : tipos) { cboTipo.addItem(t); cboTipoSec.addItem(t); }
        if (tipos.isEmpty()) JOptionPane.showMessageDialog(this,
            "No hay tipos de carta. Ejecuta sql/seed.sql.", "Aviso", JOptionPane.WARNING_MESSAGE);

        List<Edicion> ediciones = edicionDAO.listarTodas();
        cboEdicion.removeAllItems();
        for (Edicion e : ediciones) cboEdicion.addItem(e);
        if (ediciones.isEmpty()) JOptionPane.showMessageDialog(this,
            "No hay ediciones. Ejecuta sql/seed.sql.", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void seleccionarTipo(JComboBox<TipoCarta> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdTipo() == id) { combo.setSelectedIndex(i); return; }
        }
    }

    private void seleccionarEdicion(int id) {
        for (int i = 0; i < cboEdicion.getItemCount(); i++) {
            if (cboEdicion.getItemAt(i).getIdEdicion() == id) { cboEdicion.setSelectedIndex(i); return; }
        }
    }
}
