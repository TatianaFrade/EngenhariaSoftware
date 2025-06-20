package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class JanelaAdicionarEditarEliminarFilme extends JPanel {
    private JTextField txtNome;
    private JCheckBox chkLegendado;
    private JTextField txtDataLancamento;
    private JTextField txtRate;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnEliminar;

    public JanelaAdicionarEditarEliminarFilme(Filme filme, FilmeSaveListener onSalvar, ActionListener onCancelar, ActionListener onEliminar) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(filme == null ? "Adicionar Filme" : "Editar Filme");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        formPanel.add(txtNome);

        formPanel.add(new JLabel("Legendado:"));
        chkLegendado = new JCheckBox();
        formPanel.add(chkLegendado);

        formPanel.add(new JLabel("Data de Lançamento (YYYY-MM-DD):"));
        txtDataLancamento = new JTextField();
        formPanel.add(txtDataLancamento);

        formPanel.add(new JLabel("Rate (0-10):"));
        txtRate = new JTextField();
        formPanel.add(txtRate);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar");
        btnSalvar = new JButton("Salvar");
        btnEliminar = new JButton("Eliminar");

        btnCancelar.addActionListener(onCancelar);
        btnSalvar.addActionListener(e -> {
            if (onSalvar != null) {
                Filme filmeNovo = getFilmeFromFields();
                onSalvar.onSave(filme, filmeNovo);
            }
        });

        btnPanel.add(btnCancelar);
        if (filme != null) {
            btnPanel.add(btnEliminar);
            btnEliminar.addActionListener(onEliminar);
        }
        btnPanel.add(btnSalvar);

        add(btnPanel, BorderLayout.SOUTH);

        // Se estiver no modo edição, preencher os campos
        if (filme != null) {
            preencherCampos(filme);
        }

        setVisible(true);
    }

    private void preencherCampos(Filme filme) {
        txtNome.setText(filme.getNome());
        chkLegendado.setSelected(filme.isLegendado());
        txtDataLancamento.setText(filme.getDataLancamento());
        txtRate.setText(String.valueOf(filme.getRate()));
    }

    public Filme getFilmeFromFields() {
        String nome = txtNome.getText().trim();
        boolean legendado = chkLegendado.isSelected();
        String dataLancamento = txtDataLancamento.getText().trim();
        double rate = Double.parseDouble(txtRate.getText().trim());
        return new Filme(nome, legendado, dataLancamento, rate, null);
    }
}
