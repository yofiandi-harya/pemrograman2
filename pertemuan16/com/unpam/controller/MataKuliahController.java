package com.unpam.controller;

import com.unpam.model.MataKuliahDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MataKuliahController", urlPatterns = {"/MataKuliahController"})
public class MataKuliahController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (request.getSession().getAttribute("adminLog") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        request.setAttribute("listMataKuliah", new MataKuliahDAO().tampilSemua());
        request.getRequestDispatcher("matakuliah.jsp").forward(request, response);
    }
}