package com.unpam.controller;

import com.unpam.model.Koneksi;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String pesanError = "";

        try {
            Connection conn = Koneksi.getKoneksi();
            // Query untuk mencocokkan data dengan tabel admin
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, pass);
            
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Jika cocok, buat Session sebagai tanda bahwa admin sudah masuk
                HttpSession session = request.getSession();
                session.setAttribute("adminLog", user); // Menyimpan username di memori
                
                // Arahkan ke halaman utama aplikasi
                response.sendRedirect("index.jsp");
            } else {
                // Jika salah, kembalikan ke halaman login beserta pesan error
                pesanError = "Username atau Password salah!";
                request.setAttribute("pesanError", pesanError);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            pesanError = "Kesalahan Database: " + e.getMessage();
            request.setAttribute("pesanError", pesanError);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Jika user iseng mengetik URL LoginController di browser (metode GET), kembalikan ke form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}