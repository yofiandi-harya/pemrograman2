import java.awt.event.*;
import javax.swing.*;

public class kalkulator {

    public static void main(String[] args) {
        // Membuat window
        JFrame frame = new JFrame("Muhamad Fahrizal");

        // Membuat label
        JLabel label1 = new JLabel("Angka Pertama:");
        JLabel label2 = new JLabel("Angka Kedua:");
        JLabel label3 = new JLabel("Hasil:");

        // Membuat text field
        JTextField tf1 = new JTextField();
        JTextField tf2 = new JTextField();
        JTextField tfHasil = new JTextField();
        tfHasil.setEditable(false); // Hasil tidak bisa diedit

        // Membuat tombol
        JButton tambahButton = new JButton("Tambah");
        JButton hapusButton = new JButton("Hapus");
        JButton exitButton = new JButton("Exit");

        // Mengatur posisi label dan text field
        label1.setBounds(50, 30, 120, 25);
        tf1.setBounds(180, 30, 150, 25);

        label2.setBounds(50, 70, 120, 25);
        tf2.setBounds(180, 70, 150, 25);

        label3.setBounds(50, 110, 120, 25);
        tfHasil.setBounds(180, 110, 150, 25);

        // Mengatur posisi tombol
        tambahButton.setBounds(50, 150, 100, 30);
        hapusButton.setBounds(160, 150, 100, 30);
        exitButton.setBounds(270, 150, 100, 30);

        // Event tombol Tambah
        tambahButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double angka1 = Double.parseDouble(tf1.getText());
                    double angka2 = Double.parseDouble(tf2.getText());
                    double hasil = angka1 + angka2;
                    tfHasil.setText(String.valueOf(hasil));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Event tombol Hapus
        hapusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tf1.setText("");
                tf2.setText("");
                tfHasil.setText("");
            }
        });

        // Event tombol Exit
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Menambahkan komponen ke frame
        frame.add(label1);
        frame.add(tf1);
        frame.add(label2);
        frame.add(tf2);
        frame.add(label3);
        frame.add(tfHasil);
        frame.add(tambahButton);
        frame.add(hapusButton);
        frame.add(exitButton);

        // Pengaturan frame
        frame.setSize(450, 250);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}