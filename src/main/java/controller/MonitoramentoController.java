package controller;

import com.google.gson.Gson;
import dao.MonitoramentoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.MonitoramentoModel;

@WebServlet("/api/monitoramento")
public class MonitoramentoController extends HttpServlet{
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws IOException{
        
        String tipo = request.getParameter("filtroTipo");
        
        MonitoramentoDAO dao = new MonitoramentoDAO();  
        
        List<MonitoramentoModel> lista = (tipo !=null && !tipo.isEmpty())
                ? dao.listarPorItem(tipo)
                : dao.listarTodos();
        
        String json = new Gson().toJson(lista);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
