package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Janela para seleção de método de pagamento
 * Exibe valores e permite escolher entre cartão de crédito e multibanco
 */
public class JanelaPagamento extends JPanel {    private JButton btnPagar;
    private JButton btnVoltar;

    private Sessao sessao;
    private Lugar lugar;
    private double precoTotal;
    private double desconto;
    private double subtotal;
    private MetodoPagamento[] metodosDisponiveis;
    private Map<JRadioButton, MetodoPagamento> mapaBotoesPagamento;
    private ButtonGroup grupoMetodosPagamento;
    private MetodoPagamento metodoPagamentoSelecionado;

    private List<Item> itensSelecionados; // Lista de itens do bar

    /**
     * Construtor da janela de pagamento
     * @param sessao Sessão selecionada
     * @param lugar Lugar selecionado
     * @param precoTotal Preço total (incluindo lugar VIP se aplicável)
     * @param actionListenerVoltar Listener para o botão voltar
     * @param actionListenerProximo Listener para o botão próximo
     */    public JanelaPagamento(Sessao sessao, Lugar lugar, double precoTotal,
                                  ActionListener actionListenerVoltar, ActionListener actionListenerProximo) {
        this.sessao = sessao;
        this.lugar = lugar;
        this.precoTotal = precoTotal;

        // Inicializa os métodos de pagamento disponíveis
        this.metodosDisponiveis = MetodoPagamento.getMetodosPagamentoPadrao();
        this.mapaBotoesPagamento = new HashMap<>();

        // Cálculo do desconto (por exemplo, 10% para filmes em dias de semana)
        // Este é apenas um exemplo, você pode implementar sua própria lógica de desconto
        this.desconto = calcularDesconto();
        this.subtotal = precoTotal - desconto;

        setLayout(new BorderLayout(10, 10));

        // Painel de título e detalhes de pagamento
        configurarPainelTitulo();

        // Painel central com os métodos de pagamento
        configurarPainelMetodosPagamento();

        // Painel inferior com os botões
        configurarPainelBotoes(actionListenerVoltar, actionListenerProximo);
    }
    /**
     * Construtor para pagamento com itens do bar selecionados
     *
     * @param sessao Sessão selecionada
     * @param lugar Lugar selecionado
     * @param precoTotal Preço total (incluindo lugar VIP se aplicável)
     * @param itensSelecionados Lista de itens do bar selecionados
     * @param actionListenerVoltar Listener para o botão voltar
     * @param actionListenerProximo Listener para o botão próximo
     */
    public JanelaPagamento(Sessao sessao, Lugar lugar, double precoTotal, List<Item> itensSelecionados,
                           ActionListener actionListenerVoltar, ActionListener actionListenerProximo) {
        this(sessao, lugar, precoTotal, actionListenerVoltar, actionListenerProximo);
        this.itensSelecionados = itensSelecionados;
    }

    /**
     * Calcula o desconto baseado em alguma regra de negócio
     * (Por especificação, o desconto deve ser sempre 0)
     */
    private double calcularDesconto() {
        // Conforme requisito, o desconto deve ser sempre 0
        return 0.0;
    }

    private void configurarPainelTitulo() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel labelTitulo = new JLabel("Finalizar Pagamento");
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.BOLD, 18));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelTitulo);

        painelTitulo.add(Box.createVerticalStrut(20));

        // Informações do filme
        JLabel labelFilme = new JLabel("Filme: " + sessao.getFilme().getNome());
        labelFilme.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelFilme);

        // Informações da sessão
        JLabel labelSessao = new JLabel("Sessão: " + sessao.getDataHoraFormatada() +
                " - Sala " + sessao.getNomeSala());
        labelSessao.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelSessao);

        // Informações do lugar
        JLabel labelLugar = new JLabel("Lugar: " + lugar.getIdentificacao());
        labelLugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelLugar);

        painelTitulo.add(Box.createVerticalStrut(30));

        // Painel para os valores
        JPanel painelValores = new JPanel(new GridLayout(3, 2, 10, 10));
        painelValores.setBorder(BorderFactory.createTitledBorder("Detalhes do Pagamento"));

        DecimalFormat df = new DecimalFormat("0.00 €");

        // Subtotal
        JLabel labelSubtotalTexto = new JLabel("Subtotal:");
        labelSubtotalTexto.setFont(new Font(labelSubtotalTexto.getFont().getName(), Font.BOLD, 14));
        JLabel labelSubtotalValor = new JLabel(df.format(precoTotal));
        labelSubtotalValor.setHorizontalAlignment(SwingConstants.RIGHT);
        painelValores.add(labelSubtotalTexto);
        painelValores.add(labelSubtotalValor);

        // Desconto
        JLabel labelDescontoTexto = new JLabel("Desconto:");
        labelDescontoTexto.setFont(new Font(labelDescontoTexto.getFont().getName(), Font.BOLD, 14));
        JLabel labelDescontoValor = new JLabel(df.format(desconto));
        labelDescontoValor.setHorizontalAlignment(SwingConstants.RIGHT);
        labelDescontoValor.setForeground(new Color(0, 128, 0)); // Verde para desconto
        painelValores.add(labelDescontoTexto);
        painelValores.add(labelDescontoValor);

        // Total
        JLabel labelTotalTexto = new JLabel("TOTAL:");
        labelTotalTexto.setFont(new Font(labelTotalTexto.getFont().getName(), Font.BOLD, 16));
        JLabel labelTotalValor = new JLabel(df.format(subtotal));
        labelTotalValor.setHorizontalAlignment(SwingConstants.RIGHT);
        labelTotalValor.setFont(new Font(labelTotalValor.getFont().getName(), Font.BOLD, 16));
        painelValores.add(labelTotalTexto);
        painelValores.add(labelTotalValor);

        painelValores.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelValores.setMaximumSize(new Dimension(400, 120));

        painelTitulo.add(painelValores);

        add(painelTitulo, BorderLayout.NORTH);
    }    private void configurarPainelMetodosPagamento() {
        // Painel principal com borda e título
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                        "Método de Pagamento",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.BOLD, 14)),
                BorderFactory.createEmptyBorder(15, 20, 20, 20)
        ));

        // Painel central para os métodos de pagamento
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

        // Título da seção mais destacado
        JLabel labelMetodoPagamento = new JLabel("Selecione o método de pagamento:");
        labelMetodoPagamento.setFont(new Font("Arial", Font.BOLD, 16));
        labelMetodoPagamento.setForeground(new Color(0, 0, 100));
        labelMetodoPagamento.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCentral.add(labelMetodoPagamento);

        painelCentral.add(Box.createVerticalStrut(25));

        // Grupo de radio buttons para os métodos de pagamento
        grupoMetodosPagamento = new ButtonGroup();

        // Painel para organizar os métodos de pagamento
        JPanel painelMetodos = new JPanel();
        painelMetodos.setLayout(new BoxLayout(painelMetodos, BoxLayout.Y_AXIS));
        painelMetodos.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        painelMetodos.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Criar radio buttons para cada método de pagamento
        for (MetodoPagamento metodo : metodosDisponiveis) {
            // Painel para cada método de pagamento
            JPanel painelMetodo = new JPanel();
            painelMetodo.setLayout(new BoxLayout(painelMetodo, BoxLayout.Y_AXIS));
            painelMetodo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));
            painelMetodo.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Criar o radio button para o método com fonte maior
            JRadioButton radioButton = new JRadioButton(metodo.getNome());
            radioButton.setFont(new Font("Arial", Font.BOLD, 16));
            radioButton.setSelected(false); // Não seleciona por padrão
            radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Adicionar listener para habilitar o botão Próximo quando um método for selecionado
            radioButton.addActionListener(e -> {
                metodoPagamentoSelecionado = metodo;
                btnPagar.setEnabled(true);
            });

            // Guardar a associação entre o botão e o método
            mapaBotoesPagamento.put(radioButton, metodo);

            // Adicionar ao grupo
            grupoMetodosPagamento.add(radioButton);
            painelMetodo.add(radioButton);

            // Pequena descrição para o método com estilo melhorado
            JLabel labelDesc = new JLabel("   " + metodo.getDescricao());
            labelDesc.setFont(new Font("Arial", Font.ITALIC, 14));
            labelDesc.setForeground(new Color(80, 80, 80));
            labelDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelDesc.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 0));
            painelMetodo.add(labelDesc);

            // Adicionar o painel do método ao painel de métodos
            painelMetodos.add(painelMetodo);
            painelMetodos.add(Box.createVerticalStrut(15));
        }

        // Adicionar o painel de métodos ao painel central
        painelCentral.add(painelMetodos);

        // Adicionar espaço flexível para empurrar os elementos para cima
        painelCentral.add(Box.createVerticalGlue());

        // Adicionar o painel central ao painel principal
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);

        // Adicionar ao painel da janela
        add(painelPrincipal, BorderLayout.CENTER);
    }    private void configurarPainelBotoes(ActionListener actionListenerVoltar, ActionListener actionListenerProximo) {
        // Usar o mesmo layout de JanelaSelecaoLugar
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnPagar = new JButton("Finalizar Pagamento");

        // Desativar o botão Pagar inicialmente - será ativado quando um método de pagamento for selecionado
        btnPagar.setEnabled(false);

        // Adicionar listeners
        btnVoltar.addActionListener(actionListenerVoltar);
        btnPagar.addActionListener(actionListenerProximo);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnPagar);

        add(painelBotoes, BorderLayout.SOUTH);
    }
    /**
     * @return true se Cartão de Crédito estiver selecionado, false se Multibanco
     */
    public boolean isCartaoCreditoSelecionado() {
        // Verifica se o método selecionado é o Cartão de Crédito baseado no nome
        if (metodoPagamentoSelecionado != null) {
            return metodoPagamentoSelecionado.getNome().equals("Cartão de Crédito");
        }
        return false;
    }

    /**
     * @return O método de pagamento selecionado como String
     */
    public String getMetodoPagamentoSelecionado() {
        return metodoPagamentoSelecionado != null ? metodoPagamentoSelecionado.getNome() : "";
    }

    /**
     * @return O objeto MetodoPagamento selecionado
     */
    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamentoSelecionado;
    }

    /**
     * @return Valor do desconto aplicado
     */
    public double getDesconto() {
        return desconto;
    }

    /**
     * @return Valor subtotal (preço final após desconto)
     */
    public double getSubtotal() {
        return subtotal;
    }
    // Getters para os botões
    public JButton getBtnProximo() {
        return btnPagar;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    /**
     * Obtém uma referência ao botão de pagamento.
     * @return Botão de finalizar pagamento
     */
    public JButton getBtnPagar() {
        return btnPagar;
    }

    /**
     * @return Lista de itens selecionados do bar
     */
    public List<Item> getItensSelecionados() {
        return itensSelecionados;
    }
    /**
     * Exibe um diálogo para coletar informações do cartão de crédito
     * @return Map com os dados do cartão ou null se cancelado
     */
    public java.util.Map<String, String> coletarDadosCartao() {
        // Painel para o formulário de cartão de crédito - usar layout mais flexível
        JPanel painelCartao = new JPanel();
        painelCartao.setLayout(new BoxLayout(painelCartao, BoxLayout.Y_AXIS));
        painelCartao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ícone de cartão de crédito (opcional - apenas para ilustração)
        JPanel painelIcone = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelIcone = new JLabel("💳 Pagamento com Cartão de Crédito");
        labelIcone.setFont(new Font("Arial", Font.BOLD, 16));
        painelIcone.add(labelIcone);
        painelCartao.add(painelIcone);

        // Espaçamento
        painelCartao.add(Box.createVerticalStrut(15));

        // Painel do formulário
        JPanel painelFormulario = new JPanel(new GridLayout(4, 2, 8, 12));

        // Campos do formulário com formatação melhorada
        JTextField campoNumeroCartao = new JTextField(16);
        JTextField campoNomeTitular = new JTextField(20);
        JTextField campoDataExpiracao = new JTextField(7);
        JPasswordField campoCVV = new JPasswordField(3);

        // Adicionar dicas de formatação
        campoNumeroCartao.setToolTipText("Digite os 16 dígitos do cartão sem espaços");
        campoDataExpiracao.setToolTipText("Formato: MM/AA");
        campoCVV.setToolTipText("3 ou 4 dígitos no verso do cartão");

        // Adicionar os componentes ao painel do formulário
        painelFormulario.add(new JLabel("Número do Cartão:"));
        painelFormulario.add(campoNumeroCartao);
        painelFormulario.add(new JLabel("Nome do Titular:"));
        painelFormulario.add(campoNomeTitular);
        painelFormulario.add(new JLabel("Data de Expiração (MM/AA):"));
        painelFormulario.add(campoDataExpiracao);
        painelFormulario.add(new JLabel("CVV:"));
        painelFormulario.add(campoCVV);

        // Adicionar o formulário ao painel principal
        painelCartao.add(painelFormulario);

        // Adicionar texto de segurança
        JPanel painelSeguranca = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelSeguranca = new JLabel("🔒 Suas informações são protegidas e criptografadas");
        labelSeguranca.setFont(new Font("Arial", Font.ITALIC, 11));
        labelSeguranca.setForeground(new Color(100, 100, 100));
        painelSeguranca.add(labelSeguranca);

        painelCartao.add(Box.createVerticalStrut(15));
        painelCartao.add(painelSeguranca);

        // Exibir o diálogo
        int resultado = JOptionPane.showConfirmDialog(
                this,
                painelCartao,
                "Dados do Cartão de Crédito",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // Se o usuário clicar em OK, retornar os dados
        if (resultado == JOptionPane.OK_OPTION) {
            // Obter os valores dos campos
            String numeroCartao = campoNumeroCartao.getText().trim();
            String nomeTitular = campoNomeTitular.getText().trim();
            String dataExpiracao = campoDataExpiracao.getText().trim();
            String cvv = new String(campoCVV.getPassword()).trim();

            // Validar os campos
            StringBuilder erros = new StringBuilder();

            // Validar número do cartão (exatamente 16 dígitos)
            if (numeroCartao.isEmpty()) {
                erros.append("- O número do cartão é obrigatório.\n");
            } else if (!numeroCartao.matches("\\d{16}")) {
                erros.append("- Número de cartão inválido. Deve ter exatamente 16 dígitos.\n");
            }

            // Validar nome do titular
            if (nomeTitular.isEmpty()) {
                erros.append("- O nome do titular é obrigatório.\n");
            }

            // Validar data de expiração e verificar se não está expirado
            if (dataExpiracao.isEmpty()) {
                erros.append("- A data de expiração é obrigatória.\n");
            } else if (!dataExpiracao.matches("\\d{2}/\\d{2}")) {
                erros.append("- Data de expiração inválida. Use o formato MM/AA.\n");
            } else {
                // Verificar se o cartão não está expirado
                try {
                    int mes = Integer.parseInt(dataExpiracao.substring(0, 2));
                    int ano = Integer.parseInt(dataExpiracao.substring(3, 5));

                    // Converter para formato completo do ano (20XX)
                    ano += 2000;

                    // Obter a data atual
                    java.util.Calendar calAtual = java.util.Calendar.getInstance();
                    int mesAtual = calAtual.get(java.util.Calendar.MONTH) + 1; // +1 porque janeiro é 0
                    int anoAtual = calAtual.get(java.util.Calendar.YEAR);

                    // Verificar se está expirado
                    if (anoAtual > ano || (anoAtual == ano && mesAtual > mes)) {
                        erros.append("- Cartão expirado. Por favor, use um cartão válido.\n");
                    }

                    // Verificar se o mês é válido (entre 1 e 12)
                    if (mes < 1 || mes > 12) {
                        erros.append("- Mês de expiração inválido. Deve ser entre 01 e 12.\n");
                    }
                } catch (NumberFormatException e) {
                    erros.append("- Data de expiração inválida. Use o formato MM/AA.\n");
                }
            }

            // Validar CVV
            if (cvv.isEmpty()) {
                erros.append("- O código de segurança (CVV) é obrigatório.\n");
            } else if (!cvv.matches("\\d{3,4}")) {
                erros.append("- CVV inválido. Deve ter 3 ou 4 dígitos.\n");
            }

            // Se houver erros, mostrar mensagem e tentar novamente
            if (erros.length() > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Por favor, corrija os seguintes problemas:\n" + erros.toString(),
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE
                );
                return coletarDadosCartao(); // Chamada recursiva para tentar novamente
            }

            // Criar mapa com os dados
            java.util.Map<String, String> dadosCartao = new java.util.HashMap<>();
            dadosCartao.put("numeroCartao", numeroCartao);
            dadosCartao.put("nomeTitular", nomeTitular);
            dadosCartao.put("dataExpiracao", dataExpiracao);
            dadosCartao.put("cvv", cvv);

            return dadosCartao;
        }

        return null;
    }
    /**
     * Adiciona detalhes sobre itens do bar ao painel de título
     *
     * @param itens Lista de itens do bar selecionados
     * @param valorItensBar Valor total dos itens do bar
     */
    public void adicionarDetalhesItensBar(List<Item> itens, double valorItensBar) {
        if (itens == null || itens.isEmpty()) {
            return;
        }

        // Criar um painel para os itens do bar
        JPanel painelItensBar = new JPanel();
        painelItensBar.setLayout(new BoxLayout(painelItensBar, BoxLayout.Y_AXIS));
        painelItensBar.setBorder(BorderFactory.createTitledBorder("Itens do Bar"));

        // Adicionar cada item à lista
        for (Item item : itens) {
            JLabel labelItem = new JLabel(String.format("%s - %.2f €", item.getNome(), item.getPreco()));
            labelItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelItensBar.add(labelItem);
        }

        // Adicionar o total dos itens
        JLabel labelTotal = new JLabel(String.format("Total itens: %.2f €", valorItensBar));
        labelTotal.setFont(new Font(labelTotal.getFont().getName(), Font.BOLD, 12));
        labelTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelItensBar.add(labelTotal);

        // Adicionar o painel ao painel de título
        // Como o painel de título está no NORTH do BorderLayout, precisamos adicioná-lo no fim
        // do componente que já está lá, que é o painelTitulo
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel && comp == getComponent(0)) { // O primeiro componente é o painel de título
                JPanel painelTitulo = (JPanel) comp;
                painelTitulo.add(Box.createVerticalStrut(15));
                painelTitulo.add(painelItensBar);
                break;
            }
        }

        // Atualizar a interface
        revalidate();
        repaint();
    }
}
