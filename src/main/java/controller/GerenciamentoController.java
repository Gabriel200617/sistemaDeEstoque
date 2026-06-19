package controller;

import com.google.gson.Gson;
import connection.ConnectionFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet("/api/gerenciamento")
public class GerenciamentoController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String sqlResumo = """
             SELECT 
               COUNT(*) AS totalItens,
               SUM(quantidade) AS totalEstoque,
               SUM(CASE WHEN quantidade < 10 THEN 1 ELSE 0 END) AS estoqueBaixo,
               SUM(CASE WHEN status = 'entrada' THEN CAST(total AS DECIMAL(10,2))
                        WHEN status = 'saida'   THEN -CAST(total AS DECIMAL(10,2))
                        ELSE 0 END) AS valorTotal
             FROM produtos
             """;

        // Nova query que busca a lista dos produtos com estoque baixo
        String sqlBaixo = """
             SELECT codigo_barras, nome_produto, quantidade, data_vencimento
             FROM produtos
             WHERE quantidade < 10
             ORDER BY quantidade ASC
             """;

        try (Connection conn = ConnectionFactory.getConnection()) {

            Map<String, Object> resultado = new HashMap<>();
            Locale ptBr = Locale.forLanguageTag("pt-BR");
            NumberFormat moeda = NumberFormat.getCurrencyInstance(ptBr);

            // Resumo dos cards
            try (PreparedStatement stmt = conn.prepareStatement(sqlResumo);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    resultado.put("totalItens",    rs.getInt("totalItens"));
                    resultado.put("totalEstoque",  rs.getLong("totalEstoque"));
                    resultado.put("estoqueBaixo",  rs.getInt("estoqueBaixo"));
                    BigDecimal valor = rs.getBigDecimal("valorTotal");
                    resultado.put("valorTotal", valor != null ? moeda.format(valor) : moeda.format(BigDecimal.ZERO));
                } else {
                    resultado.put("totalItens", 0);
                    resultado.put("totalEstoque", 0);
                    resultado.put("estoqueBaixo", 0);
                    resultado.put("valorTotal", moeda.format(BigDecimal.ZERO));
                }
            }

            // Lista dos itens com estoque baixo
            List<Map<String, Object>> itensBaixo = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlBaixo);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("codigoBarras",   rs.getString("codigo_barras"));
                    item.put("nomeProduto",    rs.getString("nome_produto"));
                    item.put("quantidade",     rs.getInt("quantidade"));
                    // Data já como String para evitar bug de timezone no front
                    java.sql.Date venc = rs.getDate("data_vencimento");
                    item.put("dataVencimento", venc != null ? venc.toLocalDate().toString() : null);
                    itensBaixo.add(item);
                }
            }

            resultado.put("estoqueBaixoItens", itensBaixo);

            String json = new Gson().toJson(resultado);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
