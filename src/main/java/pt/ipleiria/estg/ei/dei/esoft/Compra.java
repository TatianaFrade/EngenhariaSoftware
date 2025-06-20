package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Classe que representa uma compra no sistema
 */
public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Date dataHora;
    private String idSessao;
    private String idLugar;
    private double precoBase;
    private List<Item> itensBar;
    private double precoTotal;
    private String metodoPagamento;
    private boolean confirmada;
    private String idUsuario; // ID do usuário que fez a compra
    /**
     * Construtor para compra apenas de itens do bar (sem bilhete)
     */
    public Compra(Sessao sessao, Lugar lugar, List<Item> itensBar, double precoTotal, String metodoPagamento, String idUsuario) {
        this.id = UUID.randomUUID().toString();
        this.dataHora = new Date();

        // Caso seja uma compra apenas de itens do bar (sem bilhete)
        if (sessao == null || lugar == null) {
            this.idSessao = null;
            this.idLugar = null;
            this.precoBase = 0.0;
        } else {
            this.idSessao = sessao.getId();
            this.idLugar = lugar.getIdentificacao();
            this.precoBase = lugar.calcularPreco(sessao.getPreco());
        }

        this.itensBar = itensBar;
        this.precoTotal = precoTotal;
        this.metodoPagamento = metodoPagamento;
        this.confirmada = metodoPagamento.equals("Cartão de Crédito"); // Pré-confirmada se for cartão
        this.idUsuario = idUsuario;
    }

    /**
     * Construtor para versões anteriores (compatibilidade)
     */
    public Compra(Sessao sessao, Lugar lugar, List<Item> itensBar, double precoTotal, String metodoPagamento) {
        this(sessao, lugar, itensBar, precoTotal, metodoPagamento, null);
    }

    // Getters
    public String getId() {
        return id;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public String getIdSessao() {
        return idSessao;
    }

    public String getIdLugar() {
        return idLugar;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public List<Item> getItensBar() {
        return itensBar;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    // Método para confirmar pagamento (usado para multibanco)
    public void confirmarPagamento() {
        this.confirmada = true;
    }

    // Calcular valor dos itens do bar
    public double getValorItensBar() {
        if (itensBar == null || itensBar.isEmpty()) {
            return 0.0;
        }

        return itensBar.stream().mapToDouble(Item::getPreco).sum();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    /**
     * Retorna um resumo dos itens de bar comprados
     * @return String com resumo dos itens ou mensagem indicando que não há itens
     */
    public String getResumoItensBar() {
        if (itensBar == null || itensBar.isEmpty()) {
            return "Nenhum item de bar";
        }

        // Contar quantidades de cada item e agrupar por categoria
        Map<String, Integer> contagem = new HashMap<>();
        Map<String, String> categorias = new HashMap<>();
        Map<String, Double> precoUnitario = new HashMap<>();

        for (Item item : itensBar) {
            contagem.put(item.getNome(), contagem.getOrDefault(item.getNome(), 0) + 1);
            categorias.put(item.getNome(), item.getCategoria()); // Guardar categoria do item
            precoUnitario.put(item.getNome(), item.getPreco()); // Guardar preço unitário
        }

        // Verificar se é uma compra apenas de itens do bar
        boolean compraApenasItensBar = (idSessao == null);

        // Criar resumo
        StringBuilder resumo = new StringBuilder();

        if (compraApenasItensBar && contagem.size() > 0) {
            // Formato mais detalhado para compras exclusivas de itens de bar
            resumo.append("Detalhes: ");

            // Agrupar por categoria
            Map<String, List<Map.Entry<String, Integer>>> itensPorCategoria = new HashMap<>();
            for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
                String categoria = categorias.get(entry.getKey());
                itensPorCategoria.putIfAbsent(categoria, new ArrayList<>());
                itensPorCategoria.get(categoria).add(entry);
            }

            // Adicionar itens agrupados por categoria
            boolean primeiraCategoria = true;
            for (Map.Entry<String, List<Map.Entry<String, Integer>>> categoriaEntry : itensPorCategoria.entrySet()) {
                if (!primeiraCategoria) {
                    resumo.append(" | ");
                }

                resumo.append(categoriaEntry.getKey()).append(": ");

                boolean primeiroItem = true;
                for (Map.Entry<String, Integer> itemEntry : categoriaEntry.getValue()) {
                    if (!primeiroItem) {
                        resumo.append(", ");
                    }
                    resumo.append(itemEntry.getValue()).append("x ").append(itemEntry.getKey());
                    primeiroItem = false;
                }

                primeiraCategoria = false;
            }
        } else {
            // Formato simplificado para compras normais com bilhete
            for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
                if (resumo.length() > 0) {
                    resumo.append(", ");
                }
                resumo.append(entry.getValue()).append("x ").append(entry.getKey());
            }
        }

        return resumo.toString();
    }
}
