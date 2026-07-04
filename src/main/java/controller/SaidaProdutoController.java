package controller;

import com.google.gson.Gson;
import dao.MonitoramentoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.MonitoramentoModel;

@WebServlet("/api/saida")
public class SaidaProdutoController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> resultado = new HashMap<>();

        try {
            String codigoBarras = request.getParameter("codigoBarras");
            String nomeProduto = request.getParameter("nomeProduto");
            String valor = request.getParameter("valor");
            String quantidadeStr = request.getParameter("quantidade");

            if (codigoBarras == null || codigoBarras.isBlank()
                    || nomeProduto == null || nomeProduto.isBlank()
                    || quantidadeStr == null || quantidadeStr.isBlank()) {
                resultado.put("erro", "Dados incompletos para registrar a saída");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(new Gson().toJson(resultado));
                return;
            }

            long quantidade = Long.parseLong(quantidadeStr);

            if (quantidade <= 0) {
                resultado.put("erro", "Quantidade deve ser maior que zero");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(new Gson().toJson(resultado));
                return;
            }

            MonitoramentoModel mov = new MonitoramentoModel();
            mov.setCodigoBarras(codigoBarras);
            mov.setNomeProduto(nomeProduto);
            mov.setTipoMovimentacao("saida");
            mov.setQuantidade(quantidade);
            mov.setValor(valor);

            String erro = new MonitoramentoDAO().registrarMonitoramento(mov);

            if (erro == null) {
                resultado.put("sucesso", true);
                response.getWriter().write(new Gson().toJson(resultado));
            } else {
                resultado.put("erro", erro); // ex: "Estoque insuficiente para essa saída"
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(new Gson().toJson(resultado));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}