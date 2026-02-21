/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class Login extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String usuario = request.getParameter("users");
            String senha = request.getParameter("passw");
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            
            if("admin".equals(usuario) && "gabriel29".equals(senha)) {
                response.sendRedirect("dashboard.html");
                // out.println("<h2>Login realizado.</h2>");
            } else {
                out.println("<h2>Usuário ou senha incorreto.</h2>");
            }
    }
}
