package view;

import dao.CartaMazoDAO;
import dao.CartaDAO;
import dao.JugadorDAO;
import dao.MazoDAO;
import model.Carta;
import model.CartaMazo;
import model.Jugador;
import model.Mazo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MazoView extends JPanel {

    private JTable tablaMazos;
    private DefaultTableModel modeloMazos;

    private JTable tablaCartas;
    private DefaultTableModel modeloCartas;

    private JTextField txtNombreMazo;
    private JComboBox<Jugador> cboJugador;
    private JComboBox<Carta>   cboCartaAnadir;
    private JSpinner spinCantidad;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnAnadirCarta;
    private JButton btnQuitarCarta;

    private JLabel lblEstado;

    private MazoDAO      mazoDAO;
    private JugadorDAO   jugadorDAO;
    private CartaDAO     cartaDAO;
    private CartaMazoDAO cartaMazoDAO;

    private int idMazoSeleccionado = -1;

    public MazoView() {
        mazoDAO      = new MazoDAO();
        jugadorDAO   = new JugadorDAO();
        cartaDAO     = new CartaDAO();
        cartaMazoDAO = new CartaMazoDAO();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Lista de mazos (izquierda) ──────────────────────────
        String[] columnasMazos = {"ID", "Nombre", "Jugador"};
        modeloMazos = new DefaultTableModel(columnasMazos, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaMazos = new JTable(modeloMazos);
        tablaMazos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMazos.setRowHeight(22);
        tablaMazos.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaMazos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDetalleMazo();
        });

        JPanel panelIzq = new JPanel(new BorderLayout(0, 4));
        panelIzq.setBorder(BorderFactory.createTitledBorder("Mazos"));
        panelIzq.add(new JScrollPane(tablaMazos), BorderLayout.CENTER);

        // ── Cartas del mazo (centro) ────────────────────────────
        String[] columnasCartas = {"ID", "Carta", "Tipo", "Cant."};
        modeloCartas = new DefaultTableModel(columnasCartas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaCartas = new JTable(modeloCartas);
        tablaCartas.setRowHeight(22);
        tablaCartas.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaCartas.getColumnModel().getColumn(3).setMaxWidth(55);

        // Panel añadir carta — GridBagLayout para que no se corte
        JPanel panelAnadir = new JPanel(new GridBagLayout());
        panelAnadir.setBorder(BorderFactory.createTitledBorder("Añadir carta al mazo"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        cboCartaAnadir = new JComboBox<>();
        spinCantidad   = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        ((JSpinner.DefaultEditor) spinCantidad.getEditor()).getTextField().setColumns(3);
        btnAnadirCarta = new JButton("Añadir");
        btnQuitarCarta = new JButton("Quitar seleccionada");
        btnAnadirCarta.setToolTipText("Añadir la carta seleccionada al mazo");
        btnQuitarCarta.setToolTipText("Quitar la carta seleccionada en la tabla del mazo");

        g.gridx = 0; g.gridy = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        panelAnadir.add(new JLabel("Carta:"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.gridwidth = 3;
        panelAnadir.add(cboCartaAnadir, g);

        g.gridwidth = 1;
        g.gridx = 0; g.gridy = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        panelAnadir.add(new JLabel("Cantidad:"), g);
        g.gridx = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        panelAnadir.add(spinCantidad, g);
        g.gridx = 2; g.weightx = 0.5; g.fill = GridBagConstraints.HORIZONTAL;
        panelAnadir.add(btnAnadirCarta, g);
        g.gridx = 3; g.weightx = 0.5;
        panelAnadir.add(btnQuitarCarta, g);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 4));
        panelCentro.setBorder(BorderFactory.createTitledBorder("Cartas del mazo"));
        panelCentro.add(new JScrollPane(tablaCartas), BorderLayout.CENTER);
        panelCentro.add(panelAnadir, BorderLayout.SOUTH);

        // ── Formulario mazo (derecha) ────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del mazo"));
        panelForm.setPreferredSize(new Dimension(230, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombreMazo = new JTextField();
        cboJugador    = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        panelForm.add(txtNombreMazo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;      gbc.weightx = 0;
        panelForm.add(new JLabel("Jugador:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        panelForm.add(cboJugador, gbc);

        btnNuevo    = new JButton("Nuevo");
        btnGuardar  = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        for (JButton b : new JButton[]{btnNuevo, btnGuardar, btnEliminar, btnLimpiar}) {
            b.setPreferredSize(new Dimension(90, 26));
        }
        btnGuardar.setToolTipText("Guardar el mazo (Enter)");
        btnEliminar.setToolTipText("Eliminar el mazo seleccionado");

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(panelBotones, gbc);

        // ── Estado ────────────────────────────────────────────────
        lblEstado = new JLabel(" ");
        lblEstado.setBorder(BorderFactory.createEtchedBorder());
        lblEstado.setFont(lblEstado.getFont().deriveFont(11f));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzq, panelCentro);
        splitPane.setDividerLocation(280);
        splitPane.setResizeWeight(0.35);

        add(splitPane,  BorderLayout.CENTER);
        add(panelForm,  BorderLayout.EAST);
        add(lblEstado,  BorderLayout.SOUTH);

        // ── Eventos ───────────────────────────────────────────────
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnAnadirCarta.addActionListener(e -> anadirCarta());
        btnQuitarCarta.addActionListener(e -> quitarCarta());

        txtNombreMazo.addActionListener(e -> guardar());
    }

    public void cargarTabla() {
        cargarCombos();
        modeloMazos.setRowCount(0);
        List<Mazo> lista = mazoDAO.listarTodos();
        if (lista.isEmpty()) {
            modeloMazos.addRow(new Object[]{"-", "Sin mazos registrados", ""});
            lblEstado.setText("Sin mazos registrados.");
            return;
        }
        for (Mazo m : lista) {
            String jugador = m.getJugador() != null ? m.getJugador().getNombre() : "";
            modeloMazos.addRow(new Object[]{m.getIdMazo(), m.getNombre(), jugador});
        }
        lblEstado.setText(lista.size() + " mazos cargados.");
    }

    private void cargarDetalleMazo() {
        int fila = tablaMazos.getSelectedRow();
        if (fila == -1) return;
        Object val = modeloMazos.getValueAt(fila, 0);
        if (!(val instanceof Integer)) return;
        idMazoSeleccionado = (int) val;
        Mazo m = mazoDAO.obtenerPorId(idMazoSeleccionado);
        if (m == null) return;
        txtNombreMazo.setText(m.getNombre());
        for (int i = 0; i < cboJugador.getItemCount(); i++) {
            if (cboJugador.getItemAt(i).getIdJugador() == m.getJugador().getIdJugador()) {
                cboJugador.setSelectedIndex(i);
                break;
            }
        }
        cargarCartasMazo();
    }

    private void cargarCartasMazo() {
        modeloCartas.setRowCount(0);
        if (idMazoSeleccionado == -1) return;
        List<CartaMazo> lista = cartaMazoDAO.listarPorMazo(idMazoSeleccionado);
        if (lista.isEmpty()) {
            modeloCartas.addRow(new Object[]{"-", "Mazo vacío", "", ""});
            lblEstado.setText("Mazo seleccionado sin cartas. Usa el panel inferior para añadir.");
            return;
        }
        for (CartaMazo cm : lista) {
            String tipo = cm.getCarta().getTipoCarta() != null ? cm.getCarta().getTipoCarta().getNombre() : "";
            modeloCartas.addRow(new Object[]{
                cm.getCarta().getIdCarta(), cm.getCarta().getNombre(), tipo, cm.getCantidad()
            });
        }
        lblEstado.setText("Mazo con " + lista.size() + " carta(s) diferente(s).");
    }

    private void guardar() {
        if (txtNombreMazo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del mazo es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombreMazo.requestFocus();
            return;
        }
        if (cboJugador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Mazo m = new Mazo();
        m.setNombre(txtNombreMazo.getText().trim());
        m.setJugador((Jugador) cboJugador.getSelectedItem());
        boolean ok;
        if (idMazoSeleccionado == -1) {
            ok = mazoDAO.insertar(m);
            if (ok) JOptionPane.showMessageDialog(this, "Mazo creado correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al crear el mazo.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            m.setIdMazo(idMazoSeleccionado);
            ok = mazoDAO.actualizar(m);
            if (ok) JOptionPane.showMessageDialog(this, "Mazo actualizado correctamente.");
            else JOptionPane.showMessageDialog(this, "Error al actualizar el mazo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void eliminar() {
        if (idMazoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un mazo para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el mazo y todas sus cartas?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            cartaMazoDAO.eliminarPorMazo(idMazoSeleccionado);
            boolean ok = mazoDAO.eliminar(idMazoSeleccionado);
            if (ok) JOptionPane.showMessageDialog(this, "Mazo eliminado.");
            else JOptionPane.showMessageDialog(this, "Error al eliminar el mazo.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        }
    }

    private void anadirCarta() {
        if (idMazoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un mazo de la lista primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Carta carta = (Carta) cboCartaAnadir.getSelectedItem();
        if (carta == null) return;
        int cantidad = (int) spinCantidad.getValue();

        boolean yaExiste = false;
        for (int i = 0; i < modeloCartas.getRowCount(); i++) {
            Object val = modeloCartas.getValueAt(i, 0);
            if (!(val instanceof Integer)) continue;
            if ((int) val == carta.getIdCarta()) {
                yaExiste = true;
                CartaMazo cm = new CartaMazo();
                Mazo mazo = new Mazo(); mazo.setIdMazo(idMazoSeleccionado);
                cm.setMazo(mazo); cm.setCarta(carta); cm.setCantidad(cantidad);
                cartaMazoDAO.actualizar(cm);
                break;
            }
        }
        if (!yaExiste) {
            CartaMazo cm = new CartaMazo();
            Mazo mazo = new Mazo(); mazo.setIdMazo(idMazoSeleccionado);
            cm.setMazo(mazo); cm.setCarta(carta); cm.setCantidad(cantidad);
            boolean ok = cartaMazoDAO.insertar(cm);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Error al añadir la carta al mazo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        cargarCartasMazo();
        spinCantidad.setValue(1);
    }

    private void quitarCarta() {
        int fila = tablaCartas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una carta de la tabla para quitarla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object val = modeloCartas.getValueAt(fila, 0);
        if (!(val instanceof Integer)) return;
        int idCarta = (int) val;
        cartaMazoDAO.eliminar(idMazoSeleccionado, idCarta);
        cargarCartasMazo();
    }

    private void limpiarFormulario() {
        idMazoSeleccionado = -1;
        txtNombreMazo.setText("");
        if (cboJugador.getItemCount() > 0) cboJugador.setSelectedIndex(0);
        modeloCartas.setRowCount(0);
        tablaMazos.clearSelection();
        lblEstado.setText(" ");
    }

    private void cargarCombos() {
        List<Jugador> jugadores = jugadorDAO.listarTodos();
        cboJugador.removeAllItems();
        for (Jugador j : jugadores) cboJugador.addItem(j);
        if (jugadores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay jugadores registrados. Ve a Jugadores para añadir uno.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        List<Carta> cartas = cartaDAO.listarTodos();
        cboCartaAnadir.removeAllItems();
        for (Carta c : cartas) cboCartaAnadir.addItem(c);
        if (cartas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay cartas registradas. Ve a Cartas para añadir una.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
