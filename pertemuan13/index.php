<?php
// Mulai session sesuai ketentuan di PPT
session_start();

// Cek session (contoh set default user kalau baru pertama buka)
if (!isset($_SESSION['userName'])) {
    $_SESSION['userName'] = 'Administrator'; 
}
$userName = $_SESSION['userName'];

// Kredensial Database
$host = 'localhost'; 
$db   = 'root';
$user = 'root';
$pass = '';

// Inisialisasi Koneksi Database
$db_status = "";
try {
    $pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass);
    // Set error mode ke exception
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $db_status = "<span style='color: green;'>&#10003; Koneksi database berhasil!</span>";
} catch (PDOException $e) {
    // Kalau gagal, tangkap errornya biar halaman gak nge-blank
    $db_status = "<span style='color: red;'>&#10007; Koneksi database gagal: " . $e->getMessage() . "</span>";
}
?>

<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aplikasi Administrasi Nilai Web</title>
    <style>
        /* Reset & Base Styling */
        body {
            font-family: Arial, sans-serif;
            background-color: #f1f5f9;
            margin: 0;
            padding: 20px;
        }

        /* Layout Tabel (Sesuai ketentuan PPT) */
        table.layout {
            width: 80%;
            margin: 0 auto;
            background-color: #ffffff;
            border: 2px solid #cbd5e1;
            border-collapse: collapse;
        }
        table.layout td {
            border: 1px solid #cbd5e1;
            vertical-align: top;
        }

        .header {
            background-color: #f8fafc;
            padding: 20px;
            text-align: center;
        }
        
        .sidebar {
            width: 25%;
            padding: 20px;
            background-color: #eff6ff;
        }
        
        .content {
            width: 75%;
            padding: 20px;
        }

        .footer {
            background-color: #f8fafc;
            padding: 15px;
            text-align: center;
            font-size: 14px;
        }

        /* Styling CSS Vertical Menu (Sesuai PPT) */
        #menu {
            background: linear-gradient(#efefef, #bbbbbb);
            border-radius: 10px;
            list-style: none;
            padding: 10px;
            margin: 0;
        }
        #menu li {
            margin-bottom: 5px;
            font-size: 14px;
        }
        #menu a {
            text-decoration: none;
            color: #333;
            display: block;
            padding: 10px;
            border-radius: 5px;
            transition: 0.3s;
        }
        #menu a:hover {
            background-color: #697269;
            color: #FFFFFF;
        }

        /* Styling CSS Top Navigation (Sesuai PPT) */
        nav ul {
            list-style: none;
            padding: 0;
            margin: 0;
            background-color: #334155;
            border-radius: 5px;
        }
        nav ul li {
            float: left;
            position: relative;
        }
        nav ul li a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 12px 20px;
            font-size: 14px;
        }
        nav ul li a:hover {
            background-color: #475569;
        }
        /* Dropdown Setup */
        nav ul ul {
            display: none;
            position: absolute;
            top: 100%;
            left: 0;
            background-color: #475569;
            min-width: 160px;
            z-index: 99;
            border-radius: 0 0 5px 5px;
        }
        nav ul li:hover > ul {
            display: block;
        }
        nav ul:after {
            content: "";
            clear: both;
            display: table;
        }
        
        .welcome-text {
            color: #1e293b;
            border-bottom: 2px solid #e2e8f0;
            padding-bottom: 10px;
            margin-top: 30px;
        }
    </style>
</head>
<body>

    <table class="layout">
        <tr>
            <td colspan="2" class="header">
                <h2 style="margin:0; color:#1e293b;">Informasi Nilai Mahasiswa</h2>
            </td>
        </tr>
        
        <tr>
            <td class="sidebar">
                <ul id="menu">
                    <li style="padding: 10px; color: #1e293b;"><b>Master Data</b></li>
                    <li><a href="?page=mahasiswa">Data Mahasiswa</a></li>
                    <li><a href="?page=matkul">Data Mata Kuliah</a></li>
                    <li><a href="?page=dosen">Data Dosen</a></li>
                </ul>
            </td>
            
            <td class="content">
                <nav>
                    <ul>
                        <li><a href="index.php">Home</a></li>
                        <li><a href="#">Transaksi &raquo;</a>
                            <ul>
                                <li><a href="?page=input_nilai">Input Nilai</a></li>
                                <li><a href="?page=cetak_khs">Cetak KHS</a></li>
                            </ul>
                        </li>
                        <li><a href="?page=logout">Logout</a></li>
                    </ul>
                </nav>
                
                <div>
                    <?php
                    // Nampilin nama user dari session
                    if ($userName != null) {
                        echo "<h3 class='welcome-text'>Selamat Datang, " . htmlspecialchars($userName) . "!</h3>";
                    }
                    
                    // Nampilin status koneksi DB
                    echo "<p><strong>Status DB:</strong> " . $db_status . "</p>";

                    // Logic perpindahan halaman sederhana (Routing)
                    $page = isset($_GET['page']) ? $_GET['page'] : 'home';
                    
                    echo "<div style='margin-top:20px; line-height:1.6;'>";
                    switch ($page) {
                        case 'mahasiswa':
                            echo "<h4>Manajemen Data Mahasiswa</h4><p>Form dan tabel data mahasiswa akan muncul di sini.</p>";
                            break;
                        case 'matkul':
                            echo "<h4>Manajemen Mata Kuliah</h4><p>Form dan tabel data mata kuliah akan muncul di sini.</p>";
                            break;
                        case 'dosen':
                            echo "<h4>Manajemen Data Dosen</h4><p>Form dan tabel data dosen akan muncul di sini.</p>";
                            break;
                        case 'input_nilai':
                            echo "<h4>Input Nilai Mahasiswa</h4><p>Form input nilai per mata kuliah akan muncul di sini.</p>";
                            break;
                        case 'cetak_khs':
                            echo "<h4>Cetak KHS</h4><p>Laporan Kartu Hasil Studi mahasiswa akan muncul di sini.</p>";
                            break;
                        case 'logout':
                            session_destroy();
                            echo "<p>Anda telah logout. Silakan refresh halaman.</p>";
                            break;
                        default:
                            echo "<p>Ini adalah halaman utama (Home) dari Aplikasi Administrasi Nilai Web. Silakan navigasi melalui menu di sebelah kiri atau atas.</p>";
                            break;
                    }
                    echo "</div>";
                    ?>
                </div>
            </td>
        </tr>
        
        <tr>
            <td colspan="2" class="footer">
                Copyright &copy; 2026 - Aplikasi Administrasi Nilai Web
            </td>
        </tr>
    </table>

    <script>
        // Javascript opsional buat ngecek kalau DOM udah siap
        document.addEventListener("DOMContentLoaded", function() {
            console.log("Aplikasi Administrasi Nilai siap digunakan!");
        });
    </script>
</body>
</html>