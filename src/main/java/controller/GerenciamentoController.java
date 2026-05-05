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
import java.util.Locale;

@WebServlet("/api/gerenciamento")
public class GerenciamentoController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String sql = """
             SELECT 
               COUNT(*) AS totalItens,
               SUM(quantidade) AS totalEstoque,
               SUM(CASE WHEN quantidade < 10 THEN 1 ELSE 0 END) AS estoqueBaixo,
               SUM(CASE WHEN status = 'entrada' THEN CAST(total AS DECIMAL(10,2))
                        WHEN status = 'saida'   THEN -CAST(total AS DECIMAL(10,2))
                        ELSE 0 END) AS valorTotal
             FROM produtos
             """;

        try (Connection conn = ConnectionFactory.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery()) {

            Map<String, Object> resultado = new HashMap<>();

            Locale ptBr = Locale.forLanguageTag("pt-BR");
            NumberFormat moeda = NumberFormat.getCurrencyInstance(ptBr);

            if (rs.next()) {
                int totalItens = rs.getInt("totalItens");
                long totalEstoque = rs.getLong("totalEstoque");
                int estoqueBaixo = rs.getInt("estoqueBaixo");
                BigDecimal valor = rs.getBigDecimal("valorTotal");

                resultado.put("totalItens", totalItens);
                resultado.put("totalEstoque", totalEstoque);
                resultado.put("estoqueBaixo", estoqueBaixo);
                resultado.put("valorTotal", valor != null ? moeda.format(valor) : moeda.format(BigDecimal.ZERO));
            } else {
                resultado.put("totalItens", 0);
                resultado.put("totalEstoque", 0);
                resultado.put("estoqueBaixo", 0);
                resultado.put("valorTotal", moeda.format(BigDecimal.ZERO));
            }

            String json = new Gson().toJson(resultado);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
