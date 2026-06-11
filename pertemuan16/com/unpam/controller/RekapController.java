package com.unpam.controller;

import com.unpam.model.Nilai;
import com.unpam.model.NilaiDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RekapController", urlPatterns = {"/RekapController"})
public class RekapController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cek Keamanan: Pastikan yang akses sudah login
        HttpSession session = request.getSession();
        if (session.getAttribute("adminLog") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Ambil data dari DAO
        NilaiDAO dao = new NilaiDAO();
        List<Nilai> daftarNilai = dao.tampilSemua();
        
        // Kirim data ke rekap.jsp
        request.setAttribute("daftarNilai", daftarNilai);
        request.getRequestDispatcher("rekap.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}