package controller;

import connection.ConnectionFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/api/produto")
public class DeleteProdutoController extends HttpServlet {

    protected void doDelete(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String codigoBarras = request.getParameter("codigo_barras");

        if (codigoBarras == null || codigoBarras.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\":\"codigo_barras obrigatorio\"}");
            return;
        }

        String sql = "DELETE FROM produtos WHERE codigo_barras = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoBarras);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (stmt.executeUpdate() > 0) {
                response.getWriter().write("{\"mensagem\":\"Produto deletado com sucesso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"erro\":\"Produto nao encontrado\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
