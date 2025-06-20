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
    private List<Bilhete> bilhetes;
    private List<Item> itensBar;
    private double precoTotal;
    private String metodoPagamento;
    private boolean confirmada;
    private String idUsuario; // ID do usuário que fez a compra

    /**
     * Construtor para compra com bilhetes e/ou itens do bar
     */
    public Compra(List<Bilhete> bilhetes, List<Item> itensBar, String metodoPagamento, String idUsuario) {
        this.id = UUID.randomUUID().toString();
        this.dataHora = new Date();
        this.bilhetes = bilhetes != null ? bilhetes : new ArrayList<>();
        this.itensBar = itensBar != null ? itensBar : new ArrayList<>();
        this.metodoPagamento = metodoPagamento;
        this.idUsuario = idUsuario;
        this.confirmada = metodoPagamento.equals("Cartão de Crédito"); // Pré-confirmada se for cartão

        // Calcular preço total
        this.precoTotal = calcularPrecoTotal();
    }

    /**
     * Construtor para compatibilidade com versão anterior
     */
    public Compra(Sessao sessao, Lugar lugar, List<Item> itensBar, double precoTotal, String metodoPagamento, String idUsuario) {
        this.id = UUID.randomUUID().toString();
        this.dataHora = new Date();
        this.bilhetes = new ArrayList<>();

        // Se sessão e lugar não forem nulos, criar um bilhete
        if (sessao != null && lugar != null) {
            this.bilhetes.add(new Bilhete(sessao, lugar));
        }

        this.itensBar = itensBar != null ? itensBar : new ArrayList<>();
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

    /**
     * Calcula o preço total da compra (bilhetes + itens do bar)
     */
    public double calcularPrecoTotal() {
        double total = 0.0;

        // Somar preço de todos os bilhetes
        if (bilhetes != null) {
            for (Bilhete bilhete : bilhetes) {
                if (bilhete != null) {
                    total += bilhete.getPreco();
                }
            }
        }

        // Somar preço de todos os itens do bar
        if (itensBar != null) {
            for (Item item : itensBar) {
                if (item != null) {
                    total += item.getPreco();
                }
            }
        }

        return total;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public List<Bilhete> getBilhetes() {
        return bilhetes;
    }

    /**
     * Método de compatibilidade para versões anteriores
     * @return ID da primeira sessão se houver bilhetes, null caso contrário
     */
    public String getIdSessao() {
        if (bilhetes != null && !bilhetes.isEmpty() && bilhetes.get(0) != null) {
            return bilhetes.get(0).getIdSessao();
        }
        return null;
    }

    /**
     * Método de compatibilidade para versões anteriores
     * @return ID do primeiro lugar se houver bilhetes, null caso contrário
     */
    public String getIdLugar() {
        if (bilhetes != null && !bilhetes.isEmpty() && bilhetes.get(0) != null) {
            return bilhetes.get(0).getIdLugar();
        }
        return null;
    }

    /**
     * Método de compatibilidade para versões anteriores
     * @return Preço do primeiro bilhete se houver bilhetes, 0.0 caso contrário
     */
    public double getPrecoBase() {
        if (bilhetes != null && !bilhetes.isEmpty() && bilhetes.get(0) != null) {
            return bilhetes.get(0).getPreco();
        }
        return 0.0;
    }

    public List<Item> getItensBar() {
        return itensBar;
    }

    // Metodo para adicionar um item do bar a lista de itens do bar
    public void addItemBar(Item item) {
        if (itensBar == null) {
            itensBar = new ArrayList<>();
        }
        itensBar.add(item);
    }



    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
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
        // Verificar se é uma compra apenas de itens do bar (sem bilhetes)
        boolean compraApenasItensBar = (bilhetes == null || bilhetes.isEmpty());

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
