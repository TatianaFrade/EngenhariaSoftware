package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class JanelaArmazem extends JDialog {

    public JanelaArmazem(JFrame parent) {
        super(parent, "📦 Armazém - Gestão de Stock", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        List<Stock> stocks = PersistenciaService.carregarStocks();

        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
        painelLista.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (stocks.isEmpty()) {
            painelLista.add(new JLabel("⚠️ Não há stocks registados."));
        } else {
            for (Stock s : stocks) {
                JPanel painelStock = new JPanel(new GridLayout(0, 1));
                painelStock.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                painelStock.setBackground(Color.WHITE);

                painelStock.add(new JLabel("📦 Item: " + s.getItem()));
                painelStock.add(new JLabel("🔢 Quantidade: " + s.getQuantidade()));
                painelStock.add(new JLabel("🧾 Fornecedor: " + s.getFornecedor()));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String dataFormatada = sdf.format(s.getDataValidade());
                painelStock.add(new JLabel("📅 Validade: " + dataFormatada));

                if (s.estaVencido()) {
                    JLabel vencido = new JLabel("⚠️ Expirado!");
                    vencido.setForeground(Color.RED);
                    painelStock.add(vencido);
                }

                painelStock.add(Box.createVerticalStrut(5));
                painelLista.add(painelStock);
                painelLista.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(painelLista);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);
    }
}
