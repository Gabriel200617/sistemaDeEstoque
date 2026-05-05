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

        // Agrupa por produto e separa o que é entrada e o que é saída
        String sql = """
             SELECT 
               nome_produto, 
               SUM(CASE WHEN status = 'entrada' THEN quantidade ELSE 0 END) AS qtd_entrada,
               SUM(CASE WHEN status = 'saida' THEN quantidade ELSE 0 END) AS qtd_saida
             FROM produtos 
             GROUP BY nome_produto 
             ORDER BY (qtd_entrada + qtd_saida) DESC 
             LIMIT 5
             """;

        try (Connection conn = ConnectionFactory.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> dadosGrafico = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("nome", rs.getString("nome_produto"));
                item.put("entrada", rs.getInt("qtd_entrada"));
                item.put("saida", rs.getInt("qtd_saida"));
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