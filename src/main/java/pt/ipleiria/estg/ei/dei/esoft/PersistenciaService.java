package pt.ipleiria.estg.ei.dei.esoft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Serviço responsável pela persistência de dados da aplicação de cinema
 */
public class PersistenciaService {
    private static final String DIRETORIO_DADOS = "dados/";
    private static final String ARQUIVO_FILMES = DIRETORIO_DADOS + "filmes.json";
    private static final String ARQUIVO_SALAS = DIRETORIO_DADOS + "salas.json";
    private static final String ARQUIVO_SESSOES = DIRETORIO_DADOS + "sessoes.json";
    private static final String ARQUIVO_COMPRAS = DIRETORIO_DADOS + "compras.json";
    private static final String ARQUIVO_ITENS = DIRETORIO_DADOS + "itens.json";

    // Configurar o Gson com adaptadores para tipos especiais como LocalDateTime
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(formatter.format(value));
                    }
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    String dateStr = in.nextString();
                    return (dateStr != null) ? LocalDateTime.parse(dateStr, formatter) : null;
                }
            })
            .create();

    // Inicializa o sistema de arquivos
    static {
        File diretorio = new File(DIRETORIO_DADOS);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

    // Métodos para Filmes
    public static void salvarFilmes(List<Filme> filmes) {
        try {
            File arquivo = new File(ARQUIVO_FILMES);
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            try (Writer writer = new FileWriter(arquivo)) {
                gson.toJson(filmes, writer);
                System.out.println("Filmes salvos com sucesso: " + filmes.size());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar filmes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Filme> carregarFilmes() {
        try {
            File arquivo = new File(ARQUIVO_FILMES);
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<Filme>>(){}.getType();
            try (Reader reader = new FileReader(arquivo)) {
                List<Filme> filmes = gson.fromJson(reader, tipoLista);
                return filmes != null ? filmes : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar filmes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Métodos para Salas
    public static void salvarSalas(List<Sala> salas) {
        try {
            File arquivo = new File(ARQUIVO_SALAS);
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            try (Writer writer = new FileWriter(arquivo)) {
                gson.toJson(salas, writer);
                System.out.println("Salas salvas com sucesso: " + salas.size());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar salas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Sala> carregarSalas() {
        try {
            File arquivo = new File(ARQUIVO_SALAS);
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<Sala>>(){}.getType();
            try (Reader reader = new FileReader(arquivo)) {
                List<Sala> salas = gson.fromJson(reader, tipoLista);
                return salas != null ? salas : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar salas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Métodos para Sessões
    public static void salvarSessoes(List<Sessao> sessoes) {
        try {
            // Vamos forçar a criação do arquivo mesmo que esteja vazio
            File arquivo = new File(ARQUIVO_SESSOES);
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs(); // Garante que o diretório existe
            }

            try (Writer writer = new FileWriter(arquivo)) {
                gson.toJson(sessoes, writer);
                System.out.println("Sessões salvas com sucesso em " + ARQUIVO_SESSOES + " - Total: " + sessoes.size());
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar sessões: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Sessao> carregarSessoes() {
        try {
            File arquivo = new File(ARQUIVO_SESSOES);
            if (!arquivo.exists()) {
                System.out.println("Arquivo de sessões não existe ainda: " + ARQUIVO_SESSOES);
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<Sessao>>(){}.getType();
            try (Reader reader = new FileReader(arquivo)) {
                List<Sessao> sessoes = gson.fromJson(reader, tipoLista);
                if (sessoes != null) {
                    System.out.println("Carregadas " + sessoes.size() + " sessões do arquivo");
                    return sessoes;
                }
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar sessões: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Salva a lista de itens do bar no arquivo JSON
     * @param itens Lista de itens para salvar
     */
    public static void salvarItens(List<Item> itens) {
        try {
            // Criar diretório se não existir
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Salvar itens no arquivo JSON
            File arquivo = new File(ARQUIVO_ITENS);
            try (Writer writer = new FileWriter(arquivo)) {
                gson.toJson(itens, writer);
            }
            System.out.println("Itens salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar itens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de itens do bar do arquivo JSON
     * @return Lista de itens ou lista vazia se o arquivo não existir
     */
    public static List<Item> carregarItens() {
        try {
            File arquivo = new File(ARQUIVO_ITENS);            if (!arquivo.exists()) {
                System.out.println("Arquivo de itens não encontrado. Usando itens padrão.");
                List<Item> itensPadrao = Item.getItensPadrao();
                salvarItens(itensPadrao); // Salva os itens padrão para uso futuro
                return itensPadrao;
            }
            try (Reader reader = new FileReader(arquivo)) {
                Type tipoLista = new TypeToken<ArrayList<Item>>(){}.getType();
                ArrayList<Item> itens = gson.fromJson(reader, tipoLista);
                return itens != null ? itens : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar itens: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Métodos para salvar e carregar compras
    public static void salvarCompra(Compra compra) {
        try {
            List<Compra> compras = carregarCompras();
            compras.add(compra);

            File arquivo = new File(ARQUIVO_COMPRAS);
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            try (Writer writer = new FileWriter(arquivo)) {
                gson.toJson(compras, writer);
                System.out.println("Compra salva com sucesso! ID: " + compra.getId());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar compra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Compra> carregarCompras() {
        try {
            File arquivo = new File(ARQUIVO_COMPRAS);
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<Compra>>(){}.getType();
            try (Reader reader = new FileReader(arquivo)) {
                List<Compra> compras = gson.fromJson(reader, tipoLista);
                return compras != null ? compras : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar compras: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retorna todas as datas únicas que têm sessões agendadas
     * @return Set de LocalDateTime com as datas únicas
     */
    public static Set<LocalDateTime> getDatasSessoes() {
        List<Sessao> sessoes = carregarSessoes();
        Set<LocalDateTime> datas = new TreeSet<>(); // TreeSet para manter ordenado
        for (Sessao sessao : sessoes) {
            // Removendo a parte do tempo para comparar apenas as datas
            LocalDateTime dataApenasDia = sessao.getDataHora()
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
            datas.add(dataApenasDia);
        }
        return datas;
    }

    /**
     * Retorna todas as sessões para uma data específica
     * @param data A data para filtrar as sessões
     * @return Lista de sessões na data especificada
     */
    public static List<Sessao> getSessoesPorData(LocalDateTime data) {
        List<Sessao> todasSessoes = carregarSessoes();
        List<Sessao> sessoesNaData = new ArrayList<>();

        for (Sessao sessao : todasSessoes) {
            if (sessao.getDataHora().toLocalDate().equals(data.toLocalDate())) {
                sessoesNaData.add(sessao);
            }
        }

        // Ordenar por hora
        sessoesNaData.sort(Comparator.comparing(Sessao::getDataHora));
        return sessoesNaData;
    }
}
