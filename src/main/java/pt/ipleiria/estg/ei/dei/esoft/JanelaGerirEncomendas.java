package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class JanelaGerirEncomendas extends JDialog {

    private final List<Encomenda> encomendas;
    private JTable tabela;
    private JTextArea areaDetalhes;
    private JButton botaoConfirmar;
    private JButton botaoCancelar;

    public JanelaGerirEncomendas(JFrame parent, List<Encomenda> encomendas, List<Item> itens) {
        super(parent, "Gerir Encomendas", true);
        this.encomendas = new ArrayList<>(encomendas);

        setLayout(new BorderLayout(10, 10));
        setSize(700, 400);
        setLocationRelativeTo(parent);

        ordenarEncomendas();

        // Criar tabela com dados
        String[] colunas = {"Produto", "Fornecedor", "Quantidade"};
        Object[][] dados = new Object[this.encomendas.size()][3];
        for (int i = 0; i < this.encomendas.size(); i++) {
            Encomenda e = this.encomendas.get(i);
            dados[i][0] = e.getItemNome();
            dados[i][1] = e.getFornecedor();
            dados[i][2] = e.getQuantidade();
        }

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela só leitura
            }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(25);

        // Render customizado para colorir linhas conforme estado
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Encomenda e = JanelaGerirEncomendas.this.encomendas.get(row);
                if (e.isConcluido()) {
                    c.setBackground(isSelected ? new Color(144, 238, 144) : new Color(204, 255, 204)); // verde claro
                } else if (e.isCancelado()) {
                    c.setBackground(isSelected ? new Color(255, 182, 193) : new Color(255, 204, 204)); // rosa claro
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
                return c;
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabela);

        // Painel de detalhes à direita
        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes"));

        areaDetalhes = new JTextArea();
        areaDetalhes.setEditable(false);
        areaDetalhes.setLineWrap(true);
        areaDetalhes.setWrapStyleWord(true);

        painelDetalhes.add(new JScrollPane(areaDetalhes), BorderLayout.CENTER);
        painelDetalhes.setPreferredSize(new Dimension(250, 0));

        // Painel inferior com botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botaoConfirmar = new JButton("Confirmar Encomenda");
        botaoCancelar = new JButton("Cancelar Encomenda");
        botaoConfirmar.setEnabled(false);
        botaoCancelar.setEnabled(false);

        painelBotoes.add(botaoConfirmar);
        painelBotoes.add(botaoCancelar);

        add(scrollTabela, BorderLayout.CENTER);
        add(painelDetalhes, BorderLayout.EAST);
        add(painelBotoes, BorderLayout.SOUTH);

        // Listeners
        tabela.getSelectionModel().addListSelectionListener(e -> atualizarDetalhes());
        botaoConfirmar.addActionListener(e -> abrirDialogConfirmar(true, itens));
        botaoCancelar.addActionListener(e -> abrirDialogConfirmar(false, null));
    }

    private void ordenarEncomendas() {
        // Pendentes primeiro, depois confirmadas, depois canceladas
        encomendas.sort(Comparator.comparing(Encomenda::isCancelado)
                .thenComparing(Encomenda::isConcluido));
    }

    private void atualizarDetalhes() {
        int idx = tabela.getSelectedRow();
        if (idx < 0) {
            areaDetalhes.setText("");
            botaoConfirmar.setEnabled(false);
            botaoCancelar.setEnabled(false);
            return;
        }
        Encomenda e = encomendas.get(idx);
        StringBuilder sb = new StringBuilder();
        sb.append("Código: ").append(e.getCodigo()).append("\n");
        sb.append("Produto: ").append(e.getItemNome()).append("\n");
        sb.append("Fornecedor: ").append(e.getFornecedor()).append("\n");
        sb.append("Quantidade: ").append(e.getQuantidade()).append("\n");
        sb.append("Estado: ");
        if (e.isConcluido()) sb.append("Confirmado\n");
        else if (e.isCancelado()) sb.append("Cancelado\n");
        else sb.append("Pendente\n");
        sb.append("Mensagem:\n").append(e.getMessagem() != null ? e.getMessagem() : "");

        areaDetalhes.setText(sb.toString());

        // Botões só habilitados se encomenda pendente
        boolean pendente = !e.isConcluido() && !e.isCancelado();
        botaoConfirmar.setEnabled(pendente);
        botaoCancelar.setEnabled(pendente);
    }

    private void abrirDialogConfirmar(boolean confirmar, List<Item> itens) {
        int idx = tabela.getSelectedRow();
        if (idx < 0) return;

        Encomenda e = encomendas.get(idx);

        // Janela para inserir mensagem
        JTextArea campoMensagem = new JTextArea(5, 20);
        campoMensagem.setLineWrap(true);
        campoMensagem.setWrapStyleWord(true);
        campoMensagem.setText(e.getMessagem());

        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(campoMensagem),
                (confirmar ? "Confirmar Encomenda" : "Cancelar Encomenda"),
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            e.setMessagem(campoMensagem.getText().trim());
            if (confirmar) {
                e.setConcluido(true);
                e.setCancelado(false);
                for (Item item : itens) {
                    if (item.getNome().equals(e.getItemNome())) {
                        item.mudarQuantidade(e.getQuantidade());
                    }
                }
            } else {
                e.setCancelado(true);
                e.setConcluido(false);
            }
            // Atualizar interface
            atualizarDetalhes();
            tabela.repaint();
        }
    }
}