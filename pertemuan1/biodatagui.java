import javax.swing.*;

public class biodatagui {

    public static void main(String[] args) {

        // Membuat window
        JFrame frame = new JFrame("Biodata Mahasiswa");

        // Membuat label nama
        JLabel namaLabel = new JLabel("Nama : Fahri Zal");

        // Membuat label NIM
        JLabel nimLabel = new JLabel("NIM : 231011402215");

        JLabel classLabel = new JLabel("Kelas : 06TPLE016");

         JLabel matkulLabel = new JLabel("Mata Kuliah : Pemprograman2");

        // Mengatur posisi teks
        namaLabel.setBounds(120,30,200,30);
        nimLabel.setBounds(120,60,200,30);
        classLabel.setBounds(120,100,200,30);
        matkulLabel.setBounds(120,120,200,30);

        // Menambahkan komponen ke frame
        frame.add(namaLabel);
        frame.add(nimLabel);
        frame.add(classLabel);
        frame.add(matkulLabel);

        // Pengaturan window
        frame.setSize(400,200);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}