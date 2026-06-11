package com.unpam.controller;

import com.unpam.model.MahasiswaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MahasiswaController", urlPatterns = {"/MahasiswaController"})
public class MahasiswaController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (request.getSession().getAttribute("adminLog") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        request.setAttribute("listMahasiswa", new MahasiswaDAO().tampilSemua());
        request.getRequestDispatcher("mahasiswa.jsp").forward(request, response);
    }
}