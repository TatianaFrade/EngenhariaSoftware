package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JanelaVerMenus extends JDialog {

    public JanelaVerMenus(JFrame parent, boolean isAdministrador, Usuario user) {
        super(parent, "Menus Disponíveis", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        List<Menu> menus = PersistenciaService.carregarMenus();

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        boolean encontrouMenus = false;

        for (Menu menu : menus) {
            if (isAdministrador || menu.isAtivo()) {
                JPanel painelMenu = new JPanel();
                painelMenu.setToolTipText("Clica com o botão direito para opções");
                painelMenu.setLayout(new BoxLayout(painelMenu, BoxLayout.Y_AXIS));
                painelMenu.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                painelMenu.setBackground(Color.WHITE);

                painelMenu.add(new JLabel("🍿 Nome: " + menu.getNome()));
                painelMenu.add(new JLabel("🎬 Filme: " + menu.getFilme()));
                painelMenu.add(new JLabel("💬 Descrição: " + menu.getDescricao()));
                painelMenu.add(new JLabel("💰 Preço: " + String.format("%.2f €", menu.getPreco())));

                String nomesItens = menu.getItens().isEmpty()
                        ? "Nenhum item"
                        : String.join(", ", menu.getItens());
                painelMenu.add(new JLabel("📦 Itens: " + nomesItens));

                if (!menu.isAtivo() && isAdministrador) {
                    painelMenu.setBackground(new Color(245, 245, 245));
                    painelMenu.add(new JLabel("⚠️ Estado: Inativo"));
                }

                JPopupMenu popup = new JPopupMenu();

                if (isAdministrador) {
                    JMenuItem editar = new JMenuItem("✏️ Editar");
                    editar.addActionListener(e -> {
                        Window parentWindow = SwingUtilities.getWindowAncestor(this);
                        new JanelaEditarMenu(parentWindow, menu, () -> {
                            dispose();
                            new JanelaVerMenus((JFrame) parentWindow, isAdministrador, user).setVisible(true);
                        }).setVisible(true);
                    });
                    popup.add(editar);

                    JMenuItem eliminar = new JMenuItem("🗑️ Eliminar");
                    eliminar.addActionListener(e -> {
                        int resposta = JOptionPane.showConfirmDialog(this,
                                "Tens a certeza que queres eliminar o menu \"" + menu.getNome() + "\"?",
                                "Confirmar eliminação", JOptionPane.YES_NO_OPTION);
                        if (resposta == JOptionPane.YES_OPTION) {
                            menus.remove(menu);
                            PersistenciaService.salvarMenus(menus);
                            JOptionPane.showMessageDialog(this, "Menu eliminado com sucesso.");
                            dispose();
                            new JanelaVerMenus(parent, isAdministrador, user).setVisible(true);
                        }
                    });
                    popup.add(eliminar);
                }

                JMenuItem comprar = new JMenuItem("🛒 Comprar");
                comprar.addActionListener(e -> {
                    if (!menu.isAtivo()) {
                        JOptionPane.showMessageDialog(this, "Este menu está inativo e não pode ser comprado.");
                        return;
                    }

                    Filme filmeDoMenu = UtilitariosMenu.obterFilmePorNome(menu.getFilme());
                    if (filmeDoMenu == null) {
                        JOptionPane.showMessageDialog(this, "Filme associado ao menu não encontrado.");
                        return;
                    }

                    // Abre o ecrã de opções de bilhete
                    JanelaOpcoesBilhete janelaOpcoes = new JanelaOpcoesBilhete(
                            (JFrame) SwingUtilities.getWindowAncestor(this),
                            menu, filmeDoMenu, user
                    );
                    janelaOpcoes.setVisible(true);
                });
                popup.add(comprar);

                painelMenu.setComponentPopupMenu(popup);
                painelMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                painelMenu.add(Box.createVerticalStrut(10));
                painel.add(painelMenu);
                painel.add(Box.createVerticalStrut(10));
                encontrouMenus = true;
            }
        }

        if (!encontrouMenus) {
            painel.add(new JLabel("⚠️ Nenhum menu disponível para visualizar."));
        }

        JScrollPane scroll = new JScrollPane(painel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        if (isAdministrador) {
            JButton btnNovoMenu = new JButton("➕ Criar Novo Menu");
            btnNovoMenu.addActionListener(e -> {
                new JanelaCriarMenu(this, () -> {
                    dispose();
                    new JanelaVerMenus(parent, true, user).setVisible(true);
                }).setVisible(true);
            });

            JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
            painelTopo.add(btnNovoMenu);
            painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        }

        add(painelPrincipal);
    }
}

class JanelaOpcoesBilhete extends JDialog {

    private Sessao sessaoSelecionada;
    private Lugar lugarSelecionado;

    private final JButton btnSelecionarSessao;
    private final JButton btnSelecionarLugar;

    public JanelaOpcoesBilhete(JFrame parent, Menu menu, Filme filme, Usuario user) {
        super(parent, "Opções do Bilhete", true);
        setSize(500, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Botão mostra sessão
        btnSelecionarSessao = new JButton("🎬 Sessão: ❌ nenhuma");
        btnSelecionarLugar = new JButton("🪑 Lugar: ❌ nenhum");
        btnSelecionarSessao.addActionListener(e -> {
            JanelaEscolherSessaoDialog dialog = new JanelaEscolherSessaoDialog(parent, filme);
            dialog.setVisible(true);
            Sessao selecionada = dialog.getSessaoSelecionada();
            if (selecionada != null) {
                sessaoSelecionada = selecionada;
                btnSelecionarSessao.setText("🎬 " + selecionada.getDataHoraFormatada() + " - Sala " + selecionada.getNomeSala());

                // Limpa lugar anterior
                lugarSelecionado = null;
                btnSelecionarLugar.setText("🪑 Lugar: ❌ nenhum");
            }
        });

        // Botão mostra lugar
        btnSelecionarLugar.addActionListener(e -> {
            if (sessaoSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Seleciona uma sessão primeiro.");
                return;
            }

            JanelaSelecionarLugar dialog = new JanelaSelecionarLugar(parent, sessaoSelecionada);
            dialog.setVisible(true);

            Lugar selecionado = dialog.getLugarSelecionado();
            if (selecionado != null) {
                lugarSelecionado = selecionado;
                btnSelecionarLugar.setText("🪑 Lugar: " + selecionado.getIdentificacao());
            }
        });

        // Itens do menu (como texto simples)
        String textoItens = menu.getItens().isEmpty()
                ? "Itens do Menu: (nenhum)"
                : "Itens do Menu: " + String.join(", ", menu.getItens());
        JLabel lblItens = new JLabel("🍿 " + textoItens);
        lblItens.setHorizontalAlignment(SwingConstants.CENTER);

        // Botão de pagamento
        JButton btnPagamento = new JButton("💳 Método de Pagamento");
        btnPagamento.addActionListener(e -> {
            if (sessaoSelecionada == null || lugarSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Seleciona a sessão e o lugar antes de continuar.");
                return;
            }

            Bilhete bilhete = new Bilhete(sessaoSelecionada, lugarSelecionado);
            JanelaPagamentoFinal janela = new JanelaPagamentoFinal(parent, bilhete, menu, user);
            janela.setVisible(true);

            // Fechar esta janela se a compra foi concluída
            if (janela.isCompraConcluida()) {
                dispose();
            }
        });

        // Organização visual
        JPanel painelTopo = new JPanel(new GridLayout(3, 1, 10, 10));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));
        painelTopo.add(btnSelecionarSessao);
        painelTopo.add(btnSelecionarLugar);
        painelTopo.add(lblItens);

        JPanel painelPagamento = new JPanel();
        painelPagamento.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        painelPagamento.add(btnPagamento);

        add(painelTopo, BorderLayout.CENTER);
        add(painelPagamento, BorderLayout.SOUTH);
    }
}

class JanelaEscolherSessaoDialog extends JDialog {
    private Sessao sessaoSelecionada;

    public JanelaEscolherSessaoDialog(JFrame parent, Filme filme) {
        super(parent, "Selecionar Sessão", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        List<Sessao> sessoes = PersistenciaService.carregarSessoes().stream()
                .filter(s -> s.getFilme().getNome().equalsIgnoreCase(filme.getNome()))
                .toList();

        DefaultListModel<Sessao> modelo = new DefaultListModel<>();
        for (Sessao s : sessoes) {
            modelo.addElement(s);
        }

        JList<Sessao> lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Sessao sessao) {
                    setText(sessao.getDataHoraFormatada() + " - Sala " + sessao.getNomeSala());
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
                return comp;
            }
        });


        lista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) {
                    sessaoSelecionada = lista.getSelectedValue();
                    dispose(); // Fecha logo ao clicar
                }
            }
        });

        add(new JLabel("📅 Seleciona uma sessão:", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(lista), BorderLayout.CENTER);
    }

    public Sessao getSessaoSelecionada() {
        return sessaoSelecionada;
    }
}

class JanelaSelecionarLugar extends JDialog {

    private final Sessao sessao;
    private Lugar lugarSelecionado;

    public JanelaSelecionarLugar(JFrame parent, Sessao sessao) {
        super(parent, "Selecionar Lugar", true);
        this.sessao = sessao;

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painel = criarPainelLugares();
        add(painel, BorderLayout.CENTER);
    }

    public Lugar getLugarSelecionado() {
        return lugarSelecionado;
    }

    private JPanel criarLugar(int linha, int coluna) {
        Sala sala = sessao.getSala();
        Lugar lugar = sala.getLugar(linha, coluna);

        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setToolTipText("Lugar " + getLugar(lugar).getIdentificacao());
        btn.setBackground(Color.LIGHT_GRAY);

        // Podes personalizar aqui se o lugar estiver ocupado
        if (lugar.isOcupado()) {
            btn.setBackground(Color.RED);
            btn.setEnabled(false);
        }

        btn.addActionListener(e -> {
            lugarSelecionado = lugar;
            JOptionPane.showMessageDialog(this, "Lugar selecionado: " + lugar.getIdentificacao());
            dispose();
        });

        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(35, 35));
        wrapper.add(btn);
        return wrapper;
    }

    private static Lugar getLugar(Lugar lugar) {
        return lugar;
    }

    private JPanel criarPainelLugares() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(0, 10));

        // Tela visual
        JPanel telaCinema = new JPanel();
        telaCinema.setPreferredSize(new Dimension(450, 25));
        telaCinema.setBackground(Color.DARK_GRAY);
        JLabel labelTela = new JLabel("TELA");
        labelTela.setForeground(Color.WHITE);
        telaCinema.add(labelTela);
        JPanel painelTela = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTela.add(telaCinema);
        painelPrincipal.add(painelTela, BorderLayout.NORTH);

        int filas = 8;
        int colunas = 10;

        JPanel painelGrid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Espaço vazio no canto
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelGrid.add(new JLabel(""), gbc);

        // Labels colunas
        for (int j = 0; j < colunas; j++) {
            gbc.gridx = j + 1;
            JLabel label = new JLabel(Integer.toString(j + 1), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(35, 20));
            painelGrid.add(label, gbc);
        }

        // Garantir lugares criados
        if (sessao != null && sessao.getSala() != null) {
            Sala sala = sessao.getSala();
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < colunas; j++) {
                    if (sala.getLugar(i, j) == null) {
                        sala.getLugares().add(Lugar.criarLugarPorPosicao(i, j));
                    }
                }
            }
        }

        for (int i = 0; i < filas; i++) {
            // Label de linha
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            char letra = (char) ('A' + i);
            JLabel label = new JLabel(String.valueOf(letra), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(15, 35));
            painelGrid.add(label, gbc);

            // Lugares
            for (int j = 0; j < colunas; j++) {
                gbc.gridx = j + 1;
                JPanel lugar = criarLugar(i, j);
                painelGrid.add(lugar, gbc);
            }
        }

        JPanel painelCentralizado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentralizado.add(painelGrid);
        painelPrincipal.add(painelCentralizado, BorderLayout.CENTER);

        return painelPrincipal;
    }
}

class JanelaPagamentoFinal extends JDialog {

    private final Bilhete bilhete;
    private final Menu menu;
    private MetodoPagamento metodoSelecionado;
    private boolean compraConcluida = false;

    public boolean isCompraConcluida() {
        return compraConcluida;
    }

    public JanelaPagamentoFinal(JFrame parent, Bilhete bilhete, Menu menu, Usuario user) {
        super(parent, "Finalizar Pagamento", true);
        this.bilhete = bilhete;
        this.menu = menu;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Resumo da compra
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        Sessao sessao = bilhete.getSessao();
        Lugar lugar = bilhete.getLugar();

        info.add(new JLabel("🎬 Filme: " + sessao.getFilme().getNome()));
        info.add(new JLabel("🕒 Sessão: " + sessao.getDataHoraFormatada() + " - Sala " + sessao.getNomeSala()));
        info.add(new JLabel("🪑 Lugar: " + lugar.getIdentificacao()));
        info.add(new JLabel(String.format("💰 Total: %.2f €", menu.getPreco())));

        add(info, BorderLayout.NORTH);

        // Métodos de pagamento
        JPanel metodos = new JPanel();
        metodos.setLayout(new BoxLayout(metodos, BoxLayout.Y_AXIS));
        metodos.setBorder(BorderFactory.createTitledBorder("Método de Pagamento"));
        ButtonGroup grupo = new ButtonGroup();

        for (MetodoPagamento mp : MetodoPagamento.getMetodosPagamentoPadrao()) {
            JRadioButton btn = new JRadioButton(mp.getNome());
            btn.addActionListener(e -> metodoSelecionado = mp);
            grupo.add(btn);
            metodos.add(btn);
        }

        add(metodos, BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelar = new JButton("Cancelar");
        JButton pagar = new JButton("Pagar");

        cancelar.addActionListener(e -> dispose());

        pagar.addActionListener(e -> {
            if (metodoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Seleciona um método de pagamento.");
                return;
            }

            if (metodoSelecionado.getNome().equalsIgnoreCase("Cartão de Crédito")) {
                Map<String, String> dadosCartao = coletarDadosCartao();
                if (dadosCartao == null) return;
            }

            // atualiza sala
            PersistenciaService.atualizarSalaSessao(lugar, sessao.getId());

            List<Bilhete> bilhetes = List.of(bilhete);
            List<Item> itensBar = UtilitariosMenu.obterItensPorNomes(menu.getItens());
            String idUsuario = (user != null) ? user.getNomeUsuario() : "";

            Compra compra = new Compra(bilhetes, itensBar, metodoSelecionado.getNome(), idUsuario);
            PersistenciaService.salvarCompra(compra);

            JOptionPane.showMessageDialog(this, "✅ Compra realizada com sucesso!");
            compraConcluida = true;
            dispose();
        });

        botoes.add(cancelar);
        botoes.add(pagar);
        add(botoes, BorderLayout.SOUTH);
    }

    private Map<String, String> coletarDadosCartao() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 8, 10));
        JTextField numero = new JTextField();
        JTextField nome = new JTextField();
        JTextField data = new JTextField();
        JPasswordField cvv = new JPasswordField();

        painel.add(new JLabel("Número:"));
        painel.add(numero);
        painel.add(new JLabel("Nome Titular:"));
        painel.add(nome);
        painel.add(new JLabel("Validade (MM/AA):"));
        painel.add(data);
        painel.add(new JLabel("CVV:"));
        painel.add(cvv);

        int res = JOptionPane.showConfirmDialog(this, painel, "Cartão de Crédito", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return null;

        StringBuilder erros = new StringBuilder();
        if (!numero.getText().matches("\\d{16}")) erros.append("- Número de cartão inválido.\n");
        if (nome.getText().isEmpty()) erros.append("- Nome do titular obrigatório.\n");
        if (!data.getText().matches("\\d{2}/\\d{2}")) erros.append("- Expiração inválida.\n");
        if (!new String(cvv.getPassword()).matches("\\d{3,4}")) erros.append("- CVV inválido.\n");

        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(this, "Corrige os erros:\n" + erros, "Erro", JOptionPane.ERROR_MESSAGE);
            return coletarDadosCartao(); // Repetir até estar correto
        }

        Map<String, String> dados = new HashMap<>();
        dados.put("numero", numero.getText());
        dados.put("nome", nome.getText());
        dados.put("expiracao", data.getText());
        dados.put("cvv", new String(cvv.getPassword()));
        return dados;
    }
}