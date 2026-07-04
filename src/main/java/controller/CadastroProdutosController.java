package controller;

import Exception.ExceptionsHandles;
import Exception.ValidaçãoExceptions;
import dao.CadastroProdutosDAO;
import dao.MonitoramentoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import model.CadastroProdutoModel;
import model.MonitoramentoModel;

@WebServlet("/cadastroProdutos")
public class CadastroProdutosController extends HttpServlet {

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
     
    try {
        
        CadastroProdutoModel produto = new CadastroProdutoModel();

        produto.setCodigoBarras(request.getParameter("codigoBarras"));
        produto.setNomeProduto(request.getParameter("nomeProduto"));
        produto.setFabricante(request.getParameter("fabricante"));
        produto.setMarca(request.getParameter("marca"));
        produto.setDataFabricacao(request.getParameter("dataFabricacao"));
        produto.setDataVencimento(request.getParameter("dataVencimento"));
        produto.setQuantidade(Long.parseLong(request.getParameter("quantidade")));
        produto.setValor(request.getParameter("valor"));
        produto.setTotal(request.getParameter("total"));
        produto.setStatus(request.getParameter("status"));

            LocalDate dataVenci = LocalDate.parse(produto.getDataVencimento());
            LocalDate dataFab = LocalDate.parse(produto.getDataFabricacao());
            Long quantidade = produto.getQuantidade();
            double total = Double.parseDouble(produto.getTotal());

            ExceptionsHandles.validarData(dataVenci, dataFab);
            ExceptionsHandles.validarQuantidade(quantidade);
            ExceptionsHandles.validarTotal(total);

            CadastroProdutosDAO dao = new CadastroProdutosDAO();
            if (dao.salvar(produto)) {
                
                MonitoramentoModel monitoramento = new MonitoramentoModel();
                monitoramento.setCodigoBarras(produto.getCodigoBarras());
                monitoramento.setNomeProduto(produto.getNomeProduto());
                monitoramento.setTipoMovimentacao(produto.getStatus());
                monitoramento.setQuantidade(produto.getQuantidade());
                monitoramento.setValor(produto.getValor());
                
                new MonitoramentoDAO().registrarMonitoramento(monitoramento);
                response.sendRedirect("pages/dashboard.html");
            } else {
                request.setAttribute("erro", "Não foi possível salvar o produto");
                request.getRequestDispatcher("pages/cadastroProdutos.html").forward(request, response);
            }

        }catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno no servidor");
        }
    }

}
