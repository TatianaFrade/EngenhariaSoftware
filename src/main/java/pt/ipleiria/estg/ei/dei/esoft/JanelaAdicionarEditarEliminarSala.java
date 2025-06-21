package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class JanelaAdicionarEditarEliminarSala extends JPanel {
    private JTextField txtNome;
    private JComboBox<String> cmbAcessibilidade;
    private JTextField txtFilas;
    private JTextField txtColunas;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnEliminar;

    public JanelaAdicionarEditarEliminarSala(Sala sala, OnSalvarSalaListener onSalvar, ActionListener onCancelar, ActionListener onEliminar) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(sala == null ? "Adicionar Sala" : "Editar Sala");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        formPanel.add(txtNome);

        formPanel.add(new JLabel("Acessibilidade:"));
        cmbAcessibilidade = new JComboBox<>(new String[]{"sim", "nao"});
        formPanel.add(cmbAcessibilidade);

        formPanel.add(new JLabel("Número de Filas:"));
        txtFilas = new JTextField();
        formPanel.add(txtFilas);

        formPanel.add(new JLabel("Número de Colunas:"));
        txtColunas = new JTextField();
        formPanel.add(txtColunas);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar");
        btnSalvar = new JButton("Salvar");
        btnEliminar = new JButton("Eliminar");

        btnCancelar.addActionListener(onCancelar);
        btnSalvar.addActionListener(e -> {
            if (onSalvar != null) {
                onSalvar.onSalvar(sala, getSalaFromFields());
            }
        });

        btnPanel.add(btnCancelar);
        if (sala != null) {
            btnPanel.add(btnEliminar);
            btnEliminar.addActionListener(onEliminar);
        }
        btnPanel.add(btnSalvar);

        add(btnPanel, BorderLayout.SOUTH);

        if (sala != null) {
            preencherCampos(sala);
        }

        setVisible(true);
    }

    private void preencherCampos(Sala sala) {
        txtNome.setText(sala.getNome());
        cmbAcessibilidade.setSelectedItem(sala.getAcessibilidade());
        int linhas = (int) Math.sqrt(sala.getTotalLugares()); // tentativa de deduzir dimensoes
        txtFilas.setText(String.valueOf(linhas));
        txtColunas.setText(String.valueOf(sala.getTotalLugares() / linhas));
    }

    public Sala getSalaFromFields() {
        String nome = txtNome.getText().trim();
        String acessibilidade = (String) cmbAcessibilidade.getSelectedItem();
        int filas = Integer.parseInt(txtFilas.getText().trim());
        int colunas = Integer.parseInt(txtColunas.getText().trim());
        return new Sala(nome, acessibilidade, filas, colunas);
    }
}
