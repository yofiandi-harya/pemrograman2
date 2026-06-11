package com.unpam.controller;

import com.unpam.model.Koneksi;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LaporanNilaiController", urlPatterns = {"/LaporanNilaiController"})
public class LaporanNilaiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream out = response.getOutputStream();
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"LaporanNilai.pdf\"");
            
            Connection conn = Koneksi.getKoneksi();
            
            // Mengambil file jrxml dari folder reports
            InputStream reportStream = getServletContext().getResourceAsStream("/reports/NilaiReport.jrxml");
            
            if (reportStream != null) {
                // Compile jrxml menjadi report
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                
                // Map untuk parameter (kosong karena tidak ada parameter khusus)
                Map<String, Object> parameters = new HashMap<>();
                
                // Fill report dengan data dari koneksi database
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                
                // Ekspor ke format PDF dan tulis ke OutputStream browser
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                response.setContentType("text/html;charset=UTF-8");
                out.write("File laporan NilaiReport.jrxml tidak ditemukan di folder /reports/".getBytes());
            }
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "inline");
            try {
                out.write(("Terjadi error pada server saat generate PDF:\n" + e.toString() + "\n").getBytes());
                for (StackTraceElement el : e.getStackTrace()) {
                    out.write((el.toString() + "\n").getBytes());
                }
                if (e.getCause() != null) {
                    out.write(("\nCaused by:\n" + e.getCause().toString() + "\n").getBytes());
                    for (StackTraceElement el : e.getCause().getStackTrace()) {
                        out.write((el.toString() + "\n").getBytes());
                    }
                }
            } catch (Exception ex) {
                // ignore
            }
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
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
