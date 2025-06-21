package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JanelaAdicionarEditarEliminarSessao extends JPanel {
    private JComboBox<Filme> cmbFilme;
    private JComboBox<Sala> cmbSala;
    private JTextField txtDataHora;
    private JTextField txtPreco;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnEliminar;

    public interface OnSalvarSessaoListener {
        void onSalvar(Sessao original, Sessao nova);
    }

    public JanelaAdicionarEditarEliminarSessao(
            Sessao sessao,
            List<Filme> filmes,
            List<Sala> salas,
            OnSalvarSessaoListener onSalvar,
            ActionListener onCancelar,
            ActionListener onEliminar
    ) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(sessao == null ? "Adicionar Sessão" : "Editar Sessão");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Filme:"));
        cmbFilme = new JComboBox<>(filmes.toArray(new Filme[0]));
        formPanel.add(cmbFilme);

        formPanel.add(new JLabel("Sala:"));
        cmbSala = new JComboBox<>(salas.toArray(new Sala[0]));
        formPanel.add(cmbSala);

        formPanel.add(new JLabel("Data e Hora (dd-MM-yyyy HH:mm):"));
        txtDataHora = new JTextField();
        formPanel.add(txtDataHora);

        formPanel.add(new JLabel("Preço (€):"));
        txtPreco = new JTextField();
        formPanel.add(txtPreco);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar");
        btnSalvar = new JButton("Salvar");
        btnEliminar = new JButton("Eliminar");

        btnCancelar.addActionListener(onCancelar);
        btnSalvar.addActionListener(e -> {
            try {
                Sessao novaSessao = getSessaoFromFields();
                if (onSalvar != null) {
                    onSalvar.onSalvar(sessao, novaSessao);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnCancelar);
        if (sessao != null) {
            btnPanel.add(btnEliminar);
            btnEliminar.addActionListener(onEliminar);
        }
        btnPanel.add(btnSalvar);

        add(btnPanel, BorderLayout.SOUTH);

        if (sessao != null) {
            preencherCampos(sessao);
        }

        setVisible(true);
    }

    private void preencherCampos(Sessao sessao) {
        cmbFilme.setSelectedItem(sessao.getFilme());
        cmbSala.setSelectedItem(sessao.getSala());
        txtDataHora.setText(sessao.getDataHora().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        txtPreco.setText(String.format("%.2f", sessao.getPreco()));
    }

    public Sessao getSessaoFromFields() {
        Filme filme = (Filme) cmbFilme.getSelectedItem();
        Sala sala = (Sala) cmbSala.getSelectedItem();
        String dataHoraStr = txtDataHora.getText().trim();
        String precoStr = txtPreco.getText().trim();

        if (filme == null || sala == null || dataHoraStr.isEmpty() || precoStr.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos devem estar preenchidos.");
        }

        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        double preco = Double.parseDouble(precoStr.replace(",", "."));

        return new Sessao(filme, dataHora, sala, preco);
    }
}
