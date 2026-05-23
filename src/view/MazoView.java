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
    private JTextField txtCantidad;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnAnadirCarta;
    private JButton btnQuitarCarta;

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

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lista de mazos
        String[] columnasMazos = {"ID", "Nombre", "Jugador"};
        modeloMazos = new DefaultTableModel(columnasMazos, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaMazos = new JTable(modeloMazos);
        tablaMazos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMazos.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaMazos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDetalleMazo();
        });

        JPanel panelIzq = new JPanel(new BorderLayout());
        panelIzq.setBorder(BorderFactory.createTitledBorder("Mazos"));
        panelIzq.add(new JScrollPane(tablaMazos), BorderLayout.CENTER);

        // Cartas del mazo
        String[] columnasCartas = {"ID", "Carta", "Tipo", "Cantidad"};
        modeloCartas = new DefaultTableModel(columnasCartas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaCartas = new JTable(modeloCartas);
        tablaCartas.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaCartas.getColumnModel().getColumn(3).setMaxWidth(70);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder("Cartas del mazo"));
        panelCentro.add(new JScrollPane(tablaCartas), BorderLayout.CENTER);

        JPanel panelAnadir = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cboCartaAnadir = new JComboBox<>();
        txtCantidad    = new JTextField("1", 4);
        btnAnadirCarta = new JButton("Añadir carta");
        btnQuitarCarta = new JButton("Quitar carta");
        panelAnadir.add(new JLabel("Carta:"));
        panelAnadir.add(cboCartaAnadir);
        panelAnadir.add(new JLabel("Cantidad:"));
        panelAnadir.add(txtCantidad);
        panelAnadir.add(btnAnadirCarta);
        panelAnadir.add(btnQuitarCarta);
        panelCentro.add(panelAnadir, BorderLayout.SOUTH);

        // Formulario mazo
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del mazo"));
        panelForm.setPreferredSize(new Dimension(260, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombreMazo = new JTextField(15);
        cboJugador    = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;       gbc.weightx = 0; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;                gbc.fill = GridBagConstraints.HORIZONTAL;  gbc.weightx = 1; panelForm.add(txtNombreMazo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;       gbc.weightx = 0; panelForm.add(new JLabel("Jugador:"), gbc);
        gbc.gridx = 1;                gbc.fill = GridBagConstraints.HORIZONTAL;  gbc.weightx = 1; panelForm.add(cboJugador, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnNuevo    = new JButton("Nuevo");
        btnGuardar  = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelForm.add(panelBotones, gbc);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzq, panelCentro);
        splitPane.setDividerLocation(300);
        add(splitPane,  BorderLayout.CENTER);
        add(panelForm,  BorderLayout.EAST);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnAnadirCarta.addActionListener(e -> anadirCarta());
        btnQuitarCarta.addActionListener(e -> quitarCarta());
    }

    public void cargarTabla() {
        cargarCombos();
        modeloMazos.setRowCount(0);
        List<Mazo> lista = mazoDAO.listarTodos();
        if (lista.isEmpty()) {
            modeloMazos.addRow(new Object[]{"-", "Sin mazos registrados", ""});
            return;
        }
        for (Mazo m : lista) {
            String jugador = m.getJugador() != null ? m.getJugador().getNombre() : "";
            modeloMazos.addRow(new Object[]{m.getIdMazo(), m.getNombre(), jugador});
        }
    }

    private void cargarDetalleMazo() {
        int fila = tablaMazos.getSelectedRow();
        if (fila == -1) return;
        idMazoSeleccionado = (int) modeloMazos.getValueAt(fila, 0);
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
            modeloCartas.addRow(new Object[]{"-", "Mazo vacío — añade cartas abajo", "", ""});
            return;
        }
        for (CartaMazo cm : lista) {
            String tipo = cm.getCarta().getTipoCarta() != null ? cm.getCarta().getTipoCarta().getNombre() : "";
            modeloCartas.addRow(new Object[]{
                cm.getCarta().getIdCarta(), cm.getCarta().getNombre(), tipo, cm.getCantidad()
            });
        }
    }

    private void guardar() {
        if (txtNombreMazo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del mazo es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
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
                "¿Seguro que quieres eliminar este mazo y todas sus cartas?", "Confirmar", JOptionPane.YES_NO_OPTION);
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
            JOptionPane.showMessageDialog(this, "Primero guarda o selecciona un mazo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Carta carta = (Carta) cboCartaAnadir.getSelectedItem();
        if (carta == null) return;
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Comprobar si la carta ya está en el mazo
        boolean yaExiste = false;
        for (int i = 0; i < modeloCartas.getRowCount(); i++) {
            if ((int) modeloCartas.getValueAt(i, 0) == carta.getIdCarta()) {
                yaExiste = true;
                // Actualizar cantidad
                CartaMazo cm = new CartaMazo();
                Mazo mazo = new Mazo();
                mazo.setIdMazo(idMazoSeleccionado);
                cm.setMazo(mazo);
                cm.setCarta(carta);
                cm.setCantidad(cantidad);
                cartaMazoDAO.actualizar(cm);
                break;
            }
        }
        if (!yaExiste) {
            CartaMazo cm = new CartaMazo();
            Mazo mazo = new Mazo();
            mazo.setIdMazo(idMazoSeleccionado);
            cm.setMazo(mazo);
            cm.setCarta(carta);
            cm.setCantidad(cantidad);
            boolean ok = cartaMazoDAO.insertar(cm);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Error al añadir la carta al mazo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        cargarCartasMazo();
    }

    private void quitarCarta() {
        int fila = tablaCartas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una carta para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCarta = (int) modeloCartas.getValueAt(fila, 0);
        cartaMazoDAO.eliminar(idMazoSeleccionado, idCarta);
        cargarCartasMazo();
    }

    private void limpiarFormulario() {
        idMazoSeleccionado = -1;
        txtNombreMazo.setText("");
        if (cboJugador.getItemCount() > 0) cboJugador.setSelectedIndex(0);
        modeloCartas.setRowCount(0);
        tablaMazos.clearSelection();
    }

    private void cargarCombos() {
        List<Jugador> jugadores = jugadorDAO.listarTodos();
        cboJugador.removeAllItems();
        for (Jugador j : jugadores) cboJugador.addItem(j);
        if (jugadores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay jugadores registrados. Ve a la sección Jugadores para añadir uno.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        List<Carta> cartas = cartaDAO.listarTodos();
        cboCartaAnadir.removeAllItems();
        for (Carta c : cartas) cboCartaAnadir.addItem(c);
        if (cartas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay cartas registradas. Ve a la sección Cartas para añadir una.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
