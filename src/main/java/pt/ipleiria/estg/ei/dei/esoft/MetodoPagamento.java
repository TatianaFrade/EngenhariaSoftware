package pt.ipleiria.estg.ei.dei.esoft;

/**
 * Classe que representa um método de pagamento no sistema
 */
public class MetodoPagamento {
    private String nome;
    private String descricao;
    private boolean requerReferencia;

    /**
     * Construtor da classe
     * @param nome Nome do método de pagamento
     * @param descricao Descrição do método de pagamento
     * @param requerReferencia Se requer referência para completar pagamento
     */
    public MetodoPagamento(String nome, String descricao, boolean requerReferencia) {
        this.nome = nome;
        this.descricao = descricao;
        this.requerReferencia = requerReferencia;
    }

    /**
     * @return Nome do método de pagamento
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return Descrição do método de pagamento
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return Se requer referência para completar o pagamento
     */
    public boolean isRequerReferencia() {
        return requerReferencia;
    }

    @Override
    public String toString() {
        return nome;
    }

    /**
     * Cria instâncias dos métodos de pagamento padrão do sistema
     * @return Array com os métodos de pagamento disponíveis
     */
    public static MetodoPagamento[] getMetodosPagamentoPadrao() {
        return new MetodoPagamento[]{
                new MetodoPagamento(
                        "Cartão de Crédito",
                        "Pagamento imediato com cartão de crédito/débito",
                        false
                ),
                new MetodoPagamento(
                        "Multibanco",
                        "Será gerada uma referência para pagamento",
                        true
                )
        };
    }
}
