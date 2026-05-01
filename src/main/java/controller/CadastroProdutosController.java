package controller;

import dao.CadastroProdutosDAO;
import dao.GerenciamentoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.CadastroProdutoModel;
import model.TotalModel;

@WebServlet("/cadastroProdutos")
public class CadastroProdutosController extends HttpServlet {

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
            
            CadastroProdutoModel produto = new CadastroProdutoModel();
            TotalModel total = new TotalModel();
            
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
            
            GerenciamentoDAO totalbusca = new GerenciamentoDAO();
            
            totalbusca.buscarTotalAtual();
            
            double valorJaExistente = (total.getTotal() != null) ? Double.parseDouble(total.getTotal()) : 0.0;
            double valorNovoProduto = Double.parseDouble(produto.getTotal());

            double totalNovo = valorJaExistente + valorNovoProduto;
            long quantidadeNovo = total.getQuantidadeTotal() + produto.getQuantidade();
            
            long estoqueBaixo = total.getEstoqueBaixo();
            if(produto.getQuantidade() < 10) {
                estoqueBaixo++;
            }
            
            total.setTotal(String.valueOf(totalNovo));
            total.setQuantidadeTotal(quantidadeNovo);
            total.setEstoqueBaixo(estoqueBaixo);
            
            CadastroProdutosDAO dao = new CadastroProdutosDAO();
            
            if(dao.salvar(produto,total)){
                response.sendRedirect("pages/dashboard.html");
            }else{
                response.sendRedirect("pages/cadastroProdutos.html");
                System.out.println("Chegou aqui");
            }
    }

}
