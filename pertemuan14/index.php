<?php
session_start();

if (!isset($_SESSION['userName'])) {
    $_SESSION['userName'] = 'Yofiandi Harya';
}

$userName = $_SESSION['userName'];

// DATABASE
$host = "localhost";
$db   = "231011401899_yofiandi_harya";
$user = "root";
$pass = "";

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $db_status = "Terhubung";
    $status_class = "success";
} catch(PDOException $e){
    $db_status = "Gagal Koneksi";
    $status_class = "danger";
}
?>

<!DOCTYPE html>
<html lang="id">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Sistem Akademik Mahasiswa</title>

<style>

*{
    margin:0;
    padding:0;
    box-sizing:border-box;
    font-family:'Segoe UI',sans-serif;
}

body{
    background:#eef2f7;
}

/* Layout */

.container{
    display:flex;
    min-height:100vh;
}

/* Sidebar */

.sidebar{
    width:260px;
    background:#111827;
    color:white;
    padding:25px;
}

.logo{
    text-align:center;
    margin-bottom:35px;
}

.logo h2{
    color:#60a5fa;
}

.logo p{
    font-size:13px;
    color:#cbd5e1;
}

.menu{
    list-style:none;
}

.menu li{
    margin-bottom:10px;
}

.menu a{
    text-decoration:none;
    color:white;
    display:block;
    padding:12px 15px;
    border-radius:10px;
    transition:.3s;
}

.menu a:hover{
    background:#2563eb;
}

/* Main */

.main{
    flex:1;
}

/* Topbar */

.topbar{
    background:white;
    padding:20px 30px;
    display:flex;
    justify-content:space-between;
    align-items:center;
    box-shadow:0 2px 10px rgba(0,0,0,.08);
}

.topbar h1{
    font-size:24px;
    color:#1e293b;
}

.user{
    font-weight:bold;
    color:#334155;
}

/* Content */

.content{
    padding:30px;
}

/* Cards */

.cards{
    display:grid;
    grid-template-columns:repeat(auto-fit,minmax(220px,1fr));
    gap:20px;
    margin-bottom:30px;
}

.card{
    background:white;
    padding:25px;
    border-radius:15px;
    box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.card h3{
    color:#64748b;
    font-size:14px;
    margin-bottom:10px;
}

.card .number{
    font-size:28px;
    font-weight:bold;
    color:#0f172a;
}

/* Status */

.success{
    color:green;
    font-weight:bold;
}

.danger{
    color:red;
    font-weight:bold;
}

/* Content Box */

.box{
    background:white;
    padding:25px;
    border-radius:15px;
    box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.box h2{
    margin-bottom:15px;
    color:#1e293b;
}

.footer{
    text-align:center;
    margin-top:25px;
    color:#64748b;
}

</style>
</head>

<body>

<div class="container">

    <!-- SIDEBAR -->
    <div class="sidebar">

        <div class="logo">
            <h2>SI Akademik</h2>
            <p>Universitas Pamulang</p>
        </div>

        <ul class="menu">
            <li><a href="index.php">🏠 Dashboard</a></li>
            <li><a href="?page=mahasiswa">👨‍🎓 Data Mahasiswa</a></li>
            <li><a href="?page=matkul">📚 Mata Kuliah</a></li>
            <li><a href="?page=dosen">👨‍🏫 Data Dosen</a></li>
            <li><a href="?page=inputnilai">📝 Input Nilai</a></li>
            <li><a href="?page=khs">📄 Cetak KHS</a></li>
            <li><a href="?page=logout">🚪 Logout</a></li>
        </ul>

    </div>

    <!-- MAIN -->
    <div class="main">

        <div class="topbar">
            <h1>Sistem Informasi Akademik</h1>
            <div class="user">
                <?= htmlspecialchars($userName); ?>
            </div>
        </div>

        <div class="content">

            <!-- CARD -->
            <div class="cards">

                <div class="card">
                    <h3>Nama Mahasiswa</h3>
                    <div class="number">Yofiandi Harya</div>
                </div>

                <div class="card">
                    <h3>NIM</h3>
                    <div class="number">231011401899</div>
                </div>

                <div class="card">
                    <h3>Status Database</h3>
                    <div class="<?= $status_class ?>">
                        <?= $db_status ?>
                    </div>
                </div>

            </div>

            <!-- KONTEN -->
            <div class="box">

            <?php

            $page = $_GET['page'] ?? 'home';

            switch($page){

                case 'mahasiswa':
                    echo "<h2>Data Mahasiswa</h2>";
                    echo "<p>Halaman pengelolaan data mahasiswa.</p>";
                break;

                case 'matkul':
                    echo "<h2>Data Mata Kuliah</h2>";
                    echo "<p>Halaman pengelolaan mata kuliah.</p>";
                break;

                case 'dosen':
                    echo "<h2>Data Dosen</h2>";
                    echo "<p>Halaman pengelolaan dosen.</p>";
                break;

                case 'inputnilai':
                    echo "<h2>Input Nilai</h2>";
                    echo "<p>Halaman input nilai mahasiswa.</p>";
                break;

                case 'khs':
                    echo "<h2>Cetak KHS</h2>";
                    echo "<p>Halaman cetak Kartu Hasil Studi.</p>";
                break;

                case 'logout':
                    session_destroy();
                    echo "<h2>Logout Berhasil</h2>";
                    echo "<p>Silakan refresh halaman.</p>";
                break;

                default:
                    echo "
                    <h2>Dashboard Akademik</h2>
                    <p>
                    Selamat datang di Sistem Informasi Akademik berbasis web.
                    Sistem ini digunakan untuk mengelola data mahasiswa,
                    dosen, mata kuliah, serta pengolahan nilai akademik.
                    </p>
                    ";
            }

            ?>

            </div>

            <div class="footer">
                Copyright © 2026 | YOFIANDI HARYA - 231011401899
            </div>

        </div>

    </div>

</div>

</body>
</html>