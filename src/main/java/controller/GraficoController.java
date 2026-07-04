package controller;

import com.google.gson.Gson;
import connection.ConnectionFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/grafico")
public class GraficoController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // ALTERADO: "entrada" agora é o estoque ATUAL (produtos.quantidade,
        // já descontado pela trigger a cada saída). "saida" continua sendo
        // o histórico total de saídas (monitoramento), pra dar contexto de
        // quanto já foi retirado no total.
        String sql = """
             SELECT 
               p.nome_produto,
               p.quantidade AS estoque_atual,
               COALESCE(m.total_saida, 0) AS total_saida
             FROM produtos p
             LEFT JOIN (
                 SELECT nome_produto, SUM(quantidade) AS total_saida
                 FROM monitoramento
                 WHERE tipo_movimentacao = 'saida'
                 GROUP BY nome_produto
             ) m ON m.nome_produto = p.nome_produto
             ORDER BY (p.quantidade + COALESCE(m.total_saida, 0)) DESC
             LIMIT 5
             """;

        try (Connection conn = ConnectionFactory.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> dadosGrafico = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("nome", rs.getString("nome_produto"));
                item.put("entrada", rs.getInt("estoque_atual"));
                item.put("saida", rs.getInt("total_saida"));
                dadosGrafico.add(item);
            }

            String json = new Gson().toJson(dadosGrafico);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}