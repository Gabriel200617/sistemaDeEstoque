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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/pesquisa")
public class PesquisaController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String nomePesquisado = request.getParameter("nome_produto");

        String sql = """
             SELECT nome_produto,
                 codigo_barras,
                 nome_produto, 
                 fabricante, 
                 marca, 
                 data_fabricacao,
                 data_vencimento,
                 quantidade, 
                 valor, 
                 total,
                 status
             FROM produtos 
             WHERE nome_produto = ?
             """;

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, nomePesquisado);

            Map<String, Object> resultado = new HashMap<>();

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome_produto");
                String fabricante = rs.getString("fabricante");
                String marca = rs.getString("marca");
                int quantidade = rs.getInt("quantidade");
                BigDecimal valor = rs.getBigDecimal("valor");
                BigDecimal total = rs.getBigDecimal("total");
                Date data_fabricacao = rs.getDate("data_fabricacao");
                Date data_vencimento = rs.getDate("data_vencimento");
                String codigo_barras = rs.getString("codigo_barras");
                String status = rs.getString("status");


                resultado.put("nome_produto", nome);
                resultado.put("fabricante", fabricante);
                resultado.put("marca", marca);
                resultado.put("quantidade", quantidade);
                resultado.put("valor", valor);
                resultado.put("total", total);
                resultado.put("data_fabricacao", data_fabricacao != null ? data_fabricacao.toLocalDate().toString() : null);
                resultado.put("data_vencimento", data_vencimento != null ? data_vencimento.toLocalDate().toString() : null);
                resultado.put("codigo_barras", codigo_barras);
                resultado.put("status",status);
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
