package com.unpam.controller;

import com.unpam.model.Mahasiswa;
import com.unpam.model.MataKuliah;
import com.unpam.model.Nilai;
import com.unpam.model.NilaiDAO;
import com.unpam.model.Koneksi;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@WebServlet(name = "NilaiController", urlPatterns = {"/NilaiController"})
public class NilaiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        NilaiDAO dao = new NilaiDAO();
        
        String action = request.getParameter("action");
        
        // Menangkap input dan langsung membersihkan spasi tersembunyi menggunakan .trim()
        String nimRaw = request.getParameter("nim");
        String kodeMkRaw = request.getParameter("kodeMk");
        
        String nim = (nimRaw != null) ? nimRaw.trim() : "";
        String kodeMk = (kodeMkRaw != null) ? kodeMkRaw.trim() : "";
        
        String pesan = "";

        try {
            if ("Cari NIM".equals(action)) {
                Mahasiswa mhs = dao.cariMahasiswa(nim);
                if (mhs != null) {
                    session.setAttribute("mhs", mhs);
                    pesan = "Data Mahasiswa ditemukan!";
                } else {
                    session.removeAttribute("mhs");
                    pesan = "Data Mahasiswa tidak ditemukan untuk NIM: '" + nim + "'";
                }

            } else if ("Cari MK".equals(action)) {
                // Mencetak ke jendela Output/Console NetBeans untuk debugging
                System.out.println("DEBUG KODE MK: -> '" + kodeMk + "'"); 

                MataKuliah mk = dao.cariMataKuliah(kodeMk);
                if (mk != null) {
                    session.setAttribute("mk", mk);
                    pesan = "Data Mata Kuliah ditemukan!";
                } else {
                    session.removeAttribute("mk");
                    // Pesan error diperbarui agar menampilkan apa yang sebenarnya dibaca sistem web
                    pesan = "Gagal! Sistem membaca Kode MK: '" + kodeMk + "'";
                }

            } else if ("Simpan".equals(action)) {
                String nTugasStr = request.getParameter("nilaiTugas");
                String nUtsStr = request.getParameter("nilaiUts");
                String nUasStr = request.getParameter("nilaiUas");

                if (nim.isEmpty() || kodeMk.isEmpty() || nTugasStr.isEmpty() || nUtsStr.isEmpty() || nUasStr.isEmpty()) {
                    pesan = "Harap lengkapi semua data sebelum menyimpan!";
                } else {
                    float nTugas = Float.parseFloat(nTugasStr);
                    float nUts = Float.parseFloat(nUtsStr);
                    float nUas = Float.parseFloat(nUasStr);

                    Nilai nilaiBaru = new Nilai(0, nim, kodeMk, nTugas, nUts, nUas);
                    if (dao.simpan(nilaiBaru)) {
                        pesan = "Data Nilai Berhasil Disimpan!";
                        session.removeAttribute("mhs");
                        session.removeAttribute("mk");
                    } else {
                        pesan = "Gagal menyimpan nilai ke database. Pastikan NIM dan Kode MK valid.";
                    }
                }

            } else if ("Hapus".equals(action)) {
                if (dao.hapus(nim, kodeMk)) {
                    pesan = "Data Nilai Berhasil Dihapus!";
                    session.removeAttribute("mhs");
                    session.removeAttribute("mk");
                } else {
                    pesan = "Gagal menghapus. Data tidak ditemukan.";
                }

            } else if ("Cetak".equals(action)) {
                String path = getServletContext().getRealPath("/WEB-INF/classes/reports/report_nilai.jrxml");
                
                Connection conn = Koneksi.getKoneksi();
                
                JasperReport jr = JasperCompileManager.compileReport(path);
                Map<String, Object> parameters = new HashMap<>(); 
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, conn);
                
                byte[] pdfReport = JasperExportManager.exportReportToPdf(jp);
                
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=LaporanNilai.pdf");
                response.getOutputStream().write(pdfReport);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                return; 
            }

        } catch (NumberFormatException e) {
            pesan = "Input nilai harus berupa angka!";
        } catch (Exception e) {
            pesan = "Kesalahan Sistem: " + e.getMessage();
            e.printStackTrace();
        }

        request.setAttribute("pesan", pesan);
        request.getRequestDispatcher("index.jsp").forward(request, response);
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