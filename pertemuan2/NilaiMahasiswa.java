import java.util.*;

public class NilaiMahasiswa {

    // ─── Model ───────────────────────────────────────────────────────────────────

    static class Mahasiswa {
        private String nim;
        private String nama;
        private double nilaiTugas;
        private double nilaiUTS;
        private double nilaiUAS;

        public Mahasiswa(String nim, String nama, double nilaiTugas, double nilaiUTS, double nilaiUAS) {
            this.nim    = nim;
            this.nama   = nama;
            this.nilaiTugas = nilaiTugas;
            this.nilaiUTS   = nilaiUTS;
            this.nilaiUAS   = nilaiUAS;
        }

        /** Nilai akhir: Tugas 30%, UTS 30%, UAS 40% */
        public double getNilaiAkhir() {
            return (nilaiTugas * 0.30) + (nilaiUTS * 0.30) + (nilaiUAS * 0.40);
        }

        public String getGrade() {
            double na = getNilaiAkhir();
            if (na >= 85) return "A";
            if (na >= 75) return "B";
            if (na >= 65) return "C";
            if (na >= 55) return "D";
            return "E";
        }

        public String getKeterangan() {
            return getGrade().equals("E") || getGrade().equals("D") ? "Tidak Lulus" : "Lulus";
        }

        // Getters
        public String getNim()       { return nim; }
        public String getNama()      { return nama; }
        public double getNilaiTugas(){ return nilaiTugas; }
        public double getNilaiUTS()  { return nilaiUTS; }
        public double getNilaiUAS()  { return nilaiUAS; }
    }

    // ─── Tampilan ─────────────────────────────────────────────────────────────────

    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String CYAN   = "\u001B[36m";
    static final String GREEN  = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String RED    = "\u001B[31m";
    static final String BLUE   = "\u001B[34m";
    static final String MAGENTA= "\u001B[35m";
    static final String WHITE  = "\u001B[37m";

    static void clearLine() { System.out.println(); }

    static void cetakHeader() {
        System.out.println(CYAN + BOLD);
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         SISTEM MANAJEMEN NILAI MAHASISWA                 ║");
        System.out.println("║              Program Java - Console App                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    static void cetakMenu() {
        System.out.println(BOLD + YELLOW + "┌─────────────────────────────────┐");
        System.out.println("│           MENU UTAMA            │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Tambah Data Mahasiswa       │");
        System.out.println("│  2. Tampilkan Semua Nilai       │");
        System.out.println("│  3. Tampilkan Rata-rata Kelas   │");
        System.out.println("│  4. Cari Mahasiswa              │");
        System.out.println("│  5. Hapus Mahasiswa             │");
        System.out.println("│  6. Rekap Grade                 │");
        System.out.println("│  0. Keluar                      │");
        System.out.println("└─────────────────────────────────┘" + RESET);
        System.out.print(BOLD + "Pilihan: " + RESET);
    }

    static void cetakTabelNilai(List<Mahasiswa> daftarMahasiswa) {
        if (daftarMahasiswa.isEmpty()) {
            System.out.println(RED + "  ⚠  Belum ada data mahasiswa." + RESET);
            return;
        }

        System.out.println(CYAN + BOLD);
        System.out.printf("%-6s %-20s %8s %8s %8s %10s %6s %12s%n",
            "NIM", "NAMA", "TUGAS", "UTS", "UAS", "AKHIR", "GRADE", "KETERANGAN");
        System.out.println("─".repeat(82));
        System.out.print(RESET);

        for (Mahasiswa m : daftarMahasiswa) {
            String colorGrade;
            switch (m.getGrade()) {
                case "A": colorGrade = GREEN;   break;
                case "B": colorGrade = CYAN;    break;
                case "C": colorGrade = YELLOW;  break;
                default:  colorGrade = RED;     break;
            }

            System.out.printf(WHITE + "%-6s %-20s %8.1f %8.1f %8.1f " + RESET
                + colorGrade + "%10.2f %6s %12s" + RESET + "%n",
                m.getNim(),
                m.getNama(),
                m.getNilaiTugas(),
                m.getNilaiUTS(),
                m.getNilaiUAS(),
                m.getNilaiAkhir(),
                m.getGrade(),
                m.getKeterangan());
        }
        System.out.println(CYAN + "─".repeat(82) + RESET);
    }

    static void cetakRataRata(List<Mahasiswa> daftarMahasiswa) {
        if (daftarMahasiswa.isEmpty()) {
            System.out.println(RED + "  ⚠  Belum ada data mahasiswa." + RESET);
            return;
        }

        double sumTugas = 0, sumUTS = 0, sumUAS = 0, sumAkhir = 0;
        double maxNilai = Double.MIN_VALUE, minNilai = Double.MAX_VALUE;
        String maxNama = "", minNama = "";
        int lulus = 0, tidakLulus = 0;

        for (Mahasiswa m : daftarMahasiswa) {
            sumTugas  += m.getNilaiTugas();
            sumUTS    += m.getNilaiUTS();
            sumUAS    += m.getNilaiUAS();
            sumAkhir  += m.getNilaiAkhir();

            if (m.getNilaiAkhir() > maxNilai) {
                maxNilai = m.getNilaiAkhir();
                maxNama  = m.getNama();
            }
            if (m.getNilaiAkhir() < minNilai) {
                minNilai = m.getNilaiAkhir();
                minNama  = m.getNama();
            }
            if (m.getKeterangan().equals("Lulus")) lulus++;
            else tidakLulus++;
        }

        int n = daftarMahasiswa.size();
        System.out.println(MAGENTA + BOLD);
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         STATISTIK KELAS                  ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Jumlah Mahasiswa  : %-20d ║%n", n);
        System.out.printf( "║  Rata-rata Tugas   : %-20.2f ║%n", sumTugas / n);
        System.out.printf( "║  Rata-rata UTS     : %-20.2f ║%n", sumUTS   / n);
        System.out.printf( "║  Rata-rata UAS     : %-20.2f ║%n", sumUAS   / n);
        System.out.printf( "║  Rata-rata Akhir   : %-20.2f ║%n", sumAkhir / n);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Nilai Tertinggi   : %-5.2f (%s)%n", maxNilai, maxNama);
        System.out.printf( "║  Nilai Terendah    : %-5.2f (%s)%n", minNilai, minNama);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Lulus             : %-20d ║%n", lulus);
        System.out.printf( "║  Tidak Lulus       : %-20d ║%n", tidakLulus);
        System.out.printf( "║  Persentase Lulus  : %-19.1f%% ║%n", (lulus * 100.0) / n);
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print(RESET);
    }

    static void cetakRekapGrade(List<Mahasiswa> daftarMahasiswa) {
        Map<String, Integer> rekap = new LinkedHashMap<>();
        rekap.put("A", 0); rekap.put("B", 0); rekap.put("C", 0);
        rekap.put("D", 0); rekap.put("E", 0);

        for (Mahasiswa m : daftarMahasiswa) {
            rekap.put(m.getGrade(), rekap.get(m.getGrade()) + 1);
        }

        int total = daftarMahasiswa.size();
        System.out.println(YELLOW + BOLD + "\n  REKAP GRADE KELAS\n" + RESET);
        for (Map.Entry<String, Integer> e : rekap.entrySet()) {
            int count = e.getValue();
            double pct = total > 0 ? (count * 100.0) / total : 0;
            int bar = (int) pct / 4;
            String color = e.getKey().equals("A") ? GREEN :
                           e.getKey().equals("B") ? CYAN  :
                           e.getKey().equals("C") ? YELLOW: RED;

            System.out.printf(color + BOLD + "  Grade %s │ " + RESET, e.getKey());
            System.out.print(color + "█".repeat(bar) + RESET);
            System.out.printf("  %d mahasiswa (%.1f%%)%n", count, pct);
        }
        clearLine();
    }

    // ─── Input Helper ─────────────────────────────────────────────────────────────

    static double inputNilai(Scanner sc, String label) {
        double nilai = -1;
        while (nilai < 0 || nilai > 100) {
            System.out.printf("  Masukkan %s (0-100): ", label);
            try {
                nilai = Double.parseDouble(sc.nextLine().trim());
                if (nilai < 0 || nilai > 100)
                    System.out.println(RED + "  Nilai harus antara 0 - 100!" + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "  Input tidak valid, masukkan angka!" + RESET);
                nilai = -1;
            }
        }
        return nilai;
    }

    // ─── Main ─────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Mahasiswa> daftarMahasiswa = new ArrayList<>();

        // Data contoh bawaan
        daftarMahasiswa.add(new Mahasiswa("2021001", "Andi Pratama",     78, 80, 85));
        daftarMahasiswa.add(new Mahasiswa("2021002", "Budi Santoso",     60, 55, 50));
        daftarMahasiswa.add(new Mahasiswa("2021003", "Citra Dewi",       90, 88, 92));
        daftarMahasiswa.add(new Mahasiswa("2021004", "Dian Rahayu",      70, 72, 68));
        daftarMahasiswa.add(new Mahasiswa("2021005", "Eko Widodo",       55, 45, 40));

        cetakHeader();

        boolean running = true;
        while (running) {
            cetakMenu();
            String pilihan = sc.nextLine().trim();

            switch (pilihan) {

                // ── Tambah Mahasiswa ───────────────────────────────────────────
                case "1":
                    System.out.println(BOLD + "\n── TAMBAH MAHASISWA ──" + RESET);
                    System.out.print("  NIM   : "); String nim  = sc.nextLine().trim();
                    System.out.print("  Nama  : "); String nama = sc.nextLine().trim();

                    // Cek duplikat NIM
                    boolean duplicate = daftarMahasiswa.stream().anyMatch(m -> m.getNim().equals(nim));
                    if (duplicate) {
                        System.out.println(RED + "  NIM sudah terdaftar!" + RESET);
                        break;
                    }

                    double tugas = inputNilai(sc, "Nilai Tugas");
                    double uts   = inputNilai(sc, "Nilai UTS  ");
                    double uas   = inputNilai(sc, "Nilai UAS  ");

                    daftarMahasiswa.add(new Mahasiswa(nim, nama, tugas, uts, uas));
                    System.out.println(GREEN + "  ✔ Data berhasil ditambahkan!\n" + RESET);
                    break;

                // ── Tampilkan Semua Nilai ──────────────────────────────────────
                case "2":
                    System.out.println(BOLD + "\n── DAFTAR NILAI MAHASISWA ──\n" + RESET);
                    cetakTabelNilai(daftarMahasiswa);
                    clearLine();
                    break;

                // ── Rata-rata Kelas ────────────────────────────────────────────
                case "3":
                    cetakRataRata(daftarMahasiswa);
                    clearLine();
                    break;

                // ── Cari Mahasiswa ─────────────────────────────────────────────
                case "4":
                    System.out.print("\n  Cari (NIM / Nama): ");
                    String keyword = sc.nextLine().trim().toLowerCase();
                    List<Mahasiswa> hasil = new ArrayList<>();
                    for (Mahasiswa m : daftarMahasiswa) {
                        if (m.getNim().toLowerCase().contains(keyword) ||
                            m.getNama().toLowerCase().contains(keyword)) {
                            hasil.add(m);
                        }
                    }
                    if (hasil.isEmpty()) System.out.println(RED + "  Mahasiswa tidak ditemukan.\n" + RESET);
                    else { cetakTabelNilai(hasil); clearLine(); }
                    break;

                // ── Hapus Mahasiswa ────────────────────────────────────────────
                case "5":
                    System.out.print("\n  Masukkan NIM yang akan dihapus: ");
                    String nimHapus = sc.nextLine().trim();
                    boolean removed = daftarMahasiswa.removeIf(m -> m.getNim().equals(nimHapus));
                    System.out.println(removed
                        ? GREEN  + "  ✔ Data berhasil dihapus!\n"  + RESET
                        : RED    + "  ✘ NIM tidak ditemukan.\n"    + RESET);
                    break;

                // ── Rekap Grade ────────────────────────────────────────────────
                case "6":
                    cetakRekapGrade(daftarMahasiswa);
                    break;

                // ── Keluar ─────────────────────────────────────────────────────
                case "0":
                    System.out.println(CYAN + BOLD + "\n  Terima kasih! Program selesai.\n" + RESET);
                    running = false;
                    break;

                default:
                    System.out.println(RED + "  Pilihan tidak valid!\n" + RESET);
            }
        }
        sc.close();
    }
}