<?php
session_start();

// ==========================================
// 1. IDENTITAS & KONFIGURASI
// ==========================================
$nama_identitas = "Yofi12345";
$nim_identitas  = "231011401899";

$message = "";
$error_login = "";

// ==========================================
// 2. MOCK DATABASE (MENGGUNAKAN SESSION)
// ==========================================
// Inisialisasi data dummy jika session kosong (karena tidak pakai database)
if (!isset($_SESSION['db_mobil'])) {
    $_SESSION['db_mobil'] = [
        ['id_mobil' => 1, 'plat_nomor' => 'B 1234 ABC', 'merk_mobil' => 'Toyota Avanza', 'tahun_keluaran' => '2022', 'harga_sewa' => 350000, 'status' => 'Tersedia'],
        ['id_mobil' => 2, 'plat_nomor' => 'D 5678 DEF', 'merk_mobil' => 'Honda Brio', 'tahun_keluaran' => '2023', 'harga_sewa' => 250000, 'status' => 'Tersedia']
    ];
    $_SESSION['db_customer'] = [];
    $_SESSION['db_transaksi'] = [];
}

// ==========================================
// 3. PROSES LOGIN & LOGOUT
// ==========================================
if (isset($_GET['action']) && $_GET['action'] == 'logout') {
    session_destroy();
    header("Location: index.php");
    exit;
}

if (isset($_POST['login'])) {
    $username = $_POST['username'];
    $password = $_POST['password']; 

    // Hardcode Login sesuai permintaan (admin / admin123)
    if ($username === 'admin' && $password === 'admin123') {
        $_SESSION['admin_name'] = $nama_identitas;
        header("Location: index.php");
        exit;
    } else {
        $error_login = "Username atau Password salah!";
    }
}

// ==========================================
// 4. PROSES CRUD TANPA DATABASE
// ==========================================
if (isset($_SESSION['admin_name'])) {
    
    // -- SIMPAN MOBIL --
    if (isset($_POST['submit_mobil'])) {
        $new_id = count($_SESSION['db_mobil']) > 0 ? max(array_column($_SESSION['db_mobil'], 'id_mobil')) + 1 : 1;
        $_SESSION['db_mobil'][] = [
            'id_mobil'       => $new_id,
            'plat_nomor'     => htmlspecialchars($_POST['plat_nomor']),
            'merk_mobil'     => htmlspecialchars($_POST['merk_mobil']),
            'tahun_keluaran' => htmlspecialchars($_POST['tahun_keluaran']),
            'harga_sewa'     => htmlspecialchars($_POST['harga_sewa']),
            'status'         => 'Tersedia'
        ];
        $message = "Data Mobil berhasil ditambahkan!";
    }

    // -- SIMPAN CUSTOMER --
    if (isset($_POST['submit_customer'])) {
        $new_id = count($_SESSION['db_customer']) > 0 ? max(array_column($_SESSION['db_customer'], 'id_customer')) + 1 : 1;
        $_SESSION['db_customer'][] = [
            'id_customer'  => $new_id,
            'nik_ktp'      => htmlspecialchars($_POST['nik_ktp']),
            'nama_lengkap' => htmlspecialchars($_POST['nama_lengkap']),
            'no_telepon'   => htmlspecialchars($_POST['no_telepon']),
            'alamat'       => htmlspecialchars($_POST['alamat'])
        ];
        $message = "Data Customer berhasil ditambahkan!";
    }

    // -- SIMPAN TRANSAKSI --
    if (isset($_POST['submit_transaksi'])) {
        $id_customer = $_POST['id_customer'];
        $id_mobil    = $_POST['id_mobil'];
        
        $new_id = count($_SESSION['db_transaksi']) > 0 ? max(array_column($_SESSION['db_transaksi'], 'id_transaksi')) + 1 : 1;
        
        $_SESSION['db_transaksi'][] = [
            'id_transaksi'    => $new_id,
            'id_customer'     => $id_customer,
            'id_mobil'        => $id_mobil,
            'tanggal_sewa'    => $_POST['tanggal_sewa'],
            'tanggal_kembali' => $_POST['tanggal_kembali']
        ];

        // Update status mobil jadi disewa
        foreach ($_SESSION['db_mobil'] as $key => $mobil) {
            if ($mobil['id_mobil'] == $id_mobil) {
                $_SESSION['db_mobil'][$key]['status'] = 'Disewa';
                break;
            }
        }
        $message = "Transaksi Sewa berhasil diproses!";
    }

    // -- PERSIAPAN DATA DASHBOARD --
    $count_mobil = count($_SESSION['db_mobil']);
    $count_cust  = count($_SESSION['db_customer']);
    $count_trans = count($_SESSION['db_transaksi']);
}
?>

<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Yovie RENT | <?php echo $nama_identitas; ?></title>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --secondary: #0f172a;
            --bg-body: #f8fafc;
            --bg-card: #ffffff;
            --text-dark: #1e293b;
            --text-gray: #64748b;
            --border: #e2e8f0;
            --success: #10b981;
            --danger: #ef4444;
        }

        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Plus Jakarta Sans', sans-serif; }
        body { background-color: var(--bg-body); color: var(--text-dark); display: flex; min-height: 100vh; }

        /* ================= LOGIN ================= */
        .login-wrapper { display: flex; justify-content: center; align-items: center; width: 100%; min-height: 100vh; background: linear-gradient(135deg, var(--secondary) 0%, #1e293b 100%); }
        .login-card { background: var(--bg-card); padding: 50px 40px; border-radius: 20px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.5); width: 100%; max-width: 420px; text-align: center; }
        .login-card .logo { font-size: 48px; color: var(--primary); margin-bottom: 20px; }
        .login-card h2 { color: var(--secondary); font-weight: 700; margin-bottom: 5px; font-size: 26px; }
        .login-card p { color: var(--text-gray); margin-bottom: 30px; font-size: 14px; }
        .login-card .error-badge { background: #fee2e2; color: var(--danger); padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 14px; font-weight: 500; }
        .input-group { margin-bottom: 20px; text-align: left; }
        .input-group label { display: block; font-size: 13px; font-weight: 600; color: var(--text-dark); margin-bottom: 8px; }
        .input-group input { width: 100%; padding: 14px 16px; border: 1.5px solid var(--border); border-radius: 10px; font-size: 15px; transition: 0.3s; background: var(--bg-body); }
        .input-group input:focus { border-color: var(--primary); outline: none; background: #fff; box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.1); }
        .btn-login { width: 100%; padding: 14px; background: var(--primary); color: white; border: none; border-radius: 10px; font-weight: 600; font-size: 16px; cursor: pointer; transition: 0.3s; margin-top: 10px; }
        .btn-login:hover { background: var(--primary-hover); transform: translateY(-2px); }
        .login-footer { margin-top: 25px; font-size: 13px; color: var(--text-gray); border-top: 1px solid var(--border); padding-top: 15px; }

        /* ================= SIDEBAR ================= */
        .sidebar { width: 280px; background: var(--secondary); color: white; display: flex; flex-direction: column; transition: 0.3s; }
        .sidebar-brand { padding: 30px 25px; border-bottom: 1px solid rgba(255,255,255,0.1); }
        .sidebar-brand h3 { font-size: 22px; font-weight: 700; color: white; display: flex; align-items: center; gap: 12px; }
        .sidebar-brand h3 i { color: var(--primary); }
        .identity-badge { margin-top: 10px; background: rgba(255,255,255,0.1); padding: 8px 12px; border-radius: 8px; font-size: 12px; color: #cbd5e1; }
        .sidebar-nav { flex: 1; padding: 25px 15px; }
        .nav-item { padding: 14px 20px; display: flex; align-items: center; color: #94a3b8; text-decoration: none; cursor: pointer; transition: 0.3s; border-radius: 10px; margin-bottom: 8px; font-weight: 500; }
        .nav-item i { margin-right: 15px; font-size: 18px; width: 20px; text-align: center; }
        .nav-item:hover, .nav-item.active { background: var(--primary); color: white; }
        .sidebar-bottom { padding: 20px 15px; border-top: 1px solid rgba(255,255,255,0.1); }
        .btn-logout { display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%; padding: 12px; background: rgba(239, 68, 68, 0.1); color: #f87171; text-decoration: none; border-radius: 10px; font-weight: 600; transition: 0.3s; }
        .btn-logout:hover { background: var(--danger); color: white; }

        /* ================= MAIN CONTENT ================= */
        .main-wrapper { flex: 1; padding: 40px; overflow-y: auto; height: 100vh; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 40px; }
        .header h1 { font-size: 28px; font-weight: 700; color: var(--secondary); }
        .user-profile { display: flex; align-items: center; gap: 12px; background: white; padding: 8px 20px 8px 8px; border-radius: 50px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
        .user-profile .avatar { width: 40px; height: 40px; background: var(--primary); color: white; border-radius: 50%; display: flex; justify-content: center; align-items: center; font-weight: 700; }
        .user-profile span { font-weight: 600; font-size: 14px; color: var(--secondary); }

        /* Dashboard Stats */
        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 25px; margin-bottom: 40px; }
        .stat-card { background: white; padding: 25px; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); display: flex; align-items: center; gap: 20px; transition: 0.3s; border: 1px solid var(--border); }
        .stat-card:hover { transform: translateY(-5px); box-shadow: 0 10px 25px rgba(0,0,0,0.08); }
        .stat-icon { width: 60px; height: 60px; border-radius: 14px; display: flex; justify-content: center; align-items: center; font-size: 26px; color: white; }
        .stat-info h4 { font-size: 14px; color: var(--text-gray); font-weight: 600; margin-bottom: 5px; }
        .stat-info h2 { font-size: 32px; color: var(--secondary); font-weight: 700; }

        /* Content Sections */
        .section-content { display: none; animation: fadeUp 0.5s ease; }
        .section-content.active { display: block; }
        
        /* Forms & Tables */
        .content-card { background: white; padding: 30px; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); margin-bottom: 30px; border: 1px solid var(--border); }
        .content-card h2 { font-size: 20px; font-weight: 700; margin-bottom: 25px; display: flex; align-items: center; gap: 10px; color: var(--secondary); }
        
        .form-row { display: flex; gap: 25px; margin-bottom: 20px; }
        .form-group { flex: 1; }
        .form-group label { display: block; font-size: 14px; font-weight: 600; color: var(--text-dark); margin-bottom: 8px; }
        .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 14px; border: 1.5px solid var(--border); border-radius: 10px; font-size: 14px; background: var(--bg-body); transition: 0.3s; }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus { border-color: var(--primary); outline: none; background: white; box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.1); }
        .btn-submit { padding: 14px 28px; background: var(--primary); color: white; border: none; border-radius: 10px; font-weight: 600; font-size: 15px; cursor: pointer; transition: 0.3s; display: inline-flex; align-items: center; gap: 10px; }
        .btn-submit:hover { background: var(--primary-hover); }

        .table-wrapper { overflow-x: auto; margin-top: 20px; }
        table { width: 100%; border-collapse: collapse; white-space: nowrap; }
        th, td { padding: 16px 20px; text-align: left; border-bottom: 1px solid var(--border); font-size: 14px; }
        th { background: var(--bg-body); color: var(--text-gray); font-weight: 600; text-transform: uppercase; font-size: 12px; letter-spacing: 0.5px; }
        tbody tr { transition: 0.2s; }
        tbody tr:hover { background: var(--bg-body); }
        .status-badge { padding: 6px 14px; border-radius: 50px; font-size: 12px; font-weight: 700; }
        .status-tersedia { background: #d1fae5; color: #047857; }
        .status-disewa { background: #fee2e2; color: #b91c1c; }

        .alert-success { background: #d1fae5; color: #047857; padding: 16px 20px; border-radius: 10px; margin-bottom: 30px; font-weight: 600; display: flex; align-items: center; gap: 12px; border-left: 5px solid var(--success); }

        @keyframes fadeUp { from { opacity: 0; transform: translateY(15px); } to { opacity: 1; transform: translateY(0); } }
    </style>
</head>

<?php if (!isset($_SESSION['admin_name'])): ?>
<body>
    <div class="login-wrapper">
        <div class="login-card">
            <div class="logo"><i class="fa-solid fa-car-side"></i></div>
            <h2>Welcome Back</h2>
            <p>Login to Yovie Management System</p>

            <?php if (!empty($error_login)): ?>
                <div class="error-badge"><i class="fa-solid fa-circle-exclamation"></i> <?php echo $error_login; ?></div>
            <?php endif; ?>

            <form action="" method="POST">
                <div class="input-group">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="Masukkan username" required autofocus>
                </div>
                <div class="input-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Masukkan password" required>
                </div>
                <button type="submit" name="login" class="btn-login">Sign In</button>
            </form>

            <div class="login-footer">
                <p>Default Login: <b>admin</b> | Pass: <b>admin123</b></p>
                <div style="margin-top: 10px; font-weight: 600; color: var(--primary);">
                    Identity: <?php echo $nim_identitas; ?> - <?php echo $nama_identitas; ?>
                </div>
            </div>
        </div>
    </div>
</body>

<?php else: ?>
<body>
    <div class="sidebar">
        <div class="sidebar-brand">
            <h3><i class="fa-solid fa-car"></i> Yovie</h3>
            <div class="identity-badge">
                <i class="fa-solid fa-id-card"></i> <?php echo $nim_identitas; ?><br>
                <b><?php echo $nama_identitas; ?></b>
            </div>
        </div>
        <div class="sidebar-nav">
            <div class="nav-item active" onclick="switchTab(event, 'dashboard')">
                <i class="fa-solid fa-border-all"></i> Dashboard Overview
            </div>
            <div class="nav-item" onclick="switchTab(event, 'mobil')">
                <i class="fa-solid fa-car-side"></i> Manajemen Mobil
            </div>
            <div class="nav-item" onclick="switchTab(event, 'customer')">
                <i class="fa-solid fa-users"></i> Data Customer
            </div>
            <div class="nav-item" onclick="switchTab(event, 'transaksi')">
                <i class="fa-solid fa-file-invoice-dollar"></i> Transaksi Sewa
            </div>
        </div>
        <div class="sidebar-bottom">
            <a href="?action=logout" class="btn-logout"><i class="fa-solid fa-power-off"></i> Logout System</a>
        </div>
    </div>

    <div class="main-wrapper">
        <div class="header">
            <div>
                <h1>Sistem Manajemen Rental</h1>
                <p style="color: var(--text-gray); margin-top: 5px;">Kelola kendaraan, pelanggan, dan transaksi dengan mudah.</p>
            </div>
            <div class="user-profile">
                <div class="avatar"><?php echo substr($_SESSION['admin_name'], 0, 1); ?></div>
                <span><?php echo $_SESSION['admin_name']; ?></span>
            </div>
        </div>

        <?php if (!empty($message)): ?>
            <div class="alert-success" id="alert-msg">
                <i class="fa-solid fa-circle-check" style="font-size: 20px;"></i> <?php echo $message; ?>
            </div>
        <?php endif; ?>

        <div id="dashboard" class="section-content active">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background: var(--primary);"><i class="fa-solid fa-car"></i></div>
                    <div class="stat-info">
                        <h4>Total Kendaraan</h4>
                        <h2><?php echo $count_mobil; ?></h2>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background: #10b981;"><i class="fa-solid fa-users"></i></div>
                    <div class="stat-info">
                        <h4>Total Pelanggan</h4>
                        <h2><?php echo $count_cust; ?></h2>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background: #f59e0b;"><i class="fa-solid fa-file-signature"></i></div>
                    <div class="stat-info">
                        <h4>Total Transaksi</h4>
                        <h2><?php echo $count_trans; ?></h2>
                    </div>
                </div>
            </div>
            
            <div class="content-card">
                <h2><i class="fa-solid fa-circle-info"></i> Informasi Sistem</h2>
                <p style="color: var(--text-gray); line-height: 1.6;">
                    Sistem ini berjalan menggunakan metode penyimpanan sementara <b>(Session Based Database)</b> sesuai instruksi. Tidak memerlukan koneksi MySQL. Data akan hilang jika browser ditutup atau menekan tombol logout.
                </p>
            </div>
        </div>

        <div id="mobil" class="section-content">
            <div class="content-card">
                <h2><i class="fa-solid fa-plus"></i> Registrasi Mobil Baru</h2>
                <form action="" method="POST">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Plat Nomor</label>
                            <input type="text" name="plat_nomor" placeholder="Contoh: B 1234 XYZ" required>
                        </div>
                        <div class="form-group">
                            <label>Merk & Tipe Kendaraan</label>
                            <input type="text" name="merk_mobil" placeholder="Contoh: Honda HR-V" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Tahun Keluaran</label>
                            <input type="number" name="tahun_keluaran" placeholder="Contoh: 2023" required>
                        </div>
                        <div class="form-group">
                            <label>Harga Sewa per Hari (Rp)</label>
                            <input type="number" name="harga_sewa" placeholder="Contoh: 400000" required>
                        </div>
                    </div>
                    <button type="submit" name="submit_mobil" class="btn-submit"><i class="fa-solid fa-floppy-disk"></i> Simpan Data Mobil</button>
                </form>
            </div>

            <div class="content-card">
                <h2><i class="fa-solid fa-list"></i> Daftar Kendaraan</h2>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Plat Nomor</th>
                                <th>Merk Kendaraan</th>
                                <th>Tahun</th>
                                <th>Harga/Hari</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php foreach (array_reverse($_SESSION['db_mobil']) as $row): ?>
                            <tr>
                                <td><b>#MBL-<?php echo $row['id_mobil']; ?></b></td>
                                <td><?php echo $row['plat_nomor']; ?></td>
                                <td><?php echo $row['merk_mobil']; ?></td>
                                <td><?php echo $row['tahun_keluaran']; ?></td>
                                <td>Rp <?php echo number_format($row['harga_sewa'], 0, ',', '.'); ?></td>
                                <td>
                                    <?php if($row['status'] == 'Tersedia'): ?>
                                        <span class="status-badge status-tersedia">Tersedia</span>
                                    <?php else: ?>
                                        <span class="status-badge status-disewa">Sedang Disewa</span>
                                    <?php endif; ?>
                                </td>
                            </tr>
                            <?php endforeach; ?>
                            <?php if(empty($_SESSION['db_mobil'])) echo "<tr><td colspan='6' style='text-align:center;'>Belum ada data</td></tr>"; ?>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div id="customer" class="section-content">
            <div class="content-card">
                <h2><i class="fa-solid fa-user-plus"></i> Registrasi Pelanggan</h2>
                <form action="" method="POST">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Nomor Induk Kependudukan (NIK KTP)</label>
                            <input type="text" name="nik_ktp" placeholder="Masukkan 16 Digit NIK" required maxlength="16">
                        </div>
                        <div class="form-group">
                            <label>Nama Lengkap</label>
                            <input type="text" name="nama_lengkap" placeholder="Sesuai KTP" required>
                        </div>
                    </div>
                    <div class="form-group" style="margin-bottom:20px;">
                        <label>Nomor WhatsApp / Telepon</label>
                        <input type="text" name="no_telepon" placeholder="Contoh: 081234567890" required>
                    </div>
                    <div class="form-group" style="margin-bottom:20px;">
                        <label>Alamat Domisili Lengkap</label>
                        <textarea name="alamat" rows="3" placeholder="Tuliskan alamat lengkap..." required></textarea>
                    </div>
                    <button type="submit" name="submit_customer" class="btn-submit"><i class="fa-solid fa-floppy-disk"></i> Simpan Data Pelanggan</button>
                </form>
            </div>

            <div class="content-card">
                <h2><i class="fa-solid fa-users"></i> Daftar Pelanggan</h2>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>NIK KTP</th>
                                <th>Nama Lengkap</th>
                                <th>No. Telepon</th>
                                <th>Alamat</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php foreach (array_reverse($_SESSION['db_customer']) as $row): ?>
                            <tr>
                                <td><b>#CST-<?php echo $row['id_customer']; ?></b></td>
                                <td><?php echo $row['nik_ktp']; ?></td>
                                <td><b><?php echo $row['nama_lengkap']; ?></b></td>
                                <td><?php echo $row['no_telepon']; ?></td>
                                <td><?php echo $row['alamat']; ?></td>
                            </tr>
                            <?php endforeach; ?>
                            <?php if(empty($_SESSION['db_customer'])) echo "<tr><td colspan='5' style='text-align:center;'>Belum ada data pelanggan</td></tr>"; ?>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div id="transaksi" class="section-content">
            <div class="content-card">
                <h2><i class="fa-solid fa-handshake"></i> Buat Transaksi Rental</h2>
                <form action="" method="POST" onsubmit="return validateDate()">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Pilih Pelanggan</label>
                            <select name="id_customer" required>
                                <option value="" disabled selected>-- Pilih Pelanggan --</option>
                                <?php foreach ($_SESSION['db_customer'] as $cust): ?>
                                    <option value="<?php echo $cust['id_customer']; ?>">
                                        <?php echo $cust['nama_lengkap'] . " (" . $cust['nik_ktp'] . ")"; ?>
                                    </option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Pilih Kendaraan (Hanya yg Tersedia)</label>
                            <select name="id_mobil" required>
                                <option value="" disabled selected>-- Pilih Kendaraan --</option>
                                <?php 
                                $ada_mobil = false;
                                foreach ($_SESSION['db_mobil'] as $mobil): 
                                    if($mobil['status'] == 'Tersedia'):
                                        $ada_mobil = true;
                                ?>
                                    <option value="<?php echo $mobil['id_mobil']; ?>">
                                        <?php echo $mobil['merk_mobil'] . " - " . $mobil['plat_nomor']; ?>
                                    </option>
                                <?php endif; endforeach; ?>
                            </select>
                            <?php if(!$ada_mobil) echo "<small style='color:red;'>*Tidak ada mobil tersedia saat ini.</small>"; ?>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Tanggal Mulai Sewa</label>
                            <input type="date" name="tanggal_sewa" id="ts" required>
                        </div>
                        <div class="form-group">
                            <label>Tanggal Pengembalian</label>
                            <input type="date" name="tanggal_kembali" id="tk" required>
                        </div>
                    </div>
                    <button type="submit" name="submit_transaksi" class="btn-submit" <?php echo !$ada_mobil ? 'disabled' : ''; ?>><i class="fa-solid fa-check-double"></i> Proses Transaksi Sewa</button>
                </form>
            </div>

            <div class="content-card">
                <h2><i class="fa-solid fa-clock-rotate-left"></i> Riwayat Transaksi</h2>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>No. TRX</th>
                                <th>Pelanggan</th>
                                <th>Kendaraan</th>
                                <th>Tgl Sewa</th>
                                <th>Tgl Kembali</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php foreach (array_reverse($_SESSION['db_transaksi']) as $trx): 
                                // Cari nama customer
                                $nama_cust = "Unknown";
                                foreach($_SESSION['db_customer'] as $c) {
                                    if($c['id_customer'] == $trx['id_customer']) $nama_cust = $c['nama_lengkap'];
                                }
                                // Cari nama mobil
                                $nama_mobil = "Unknown";
                                foreach($_SESSION['db_mobil'] as $m) {
                                    if($m['id_mobil'] == $trx['id_mobil']) $nama_mobil = $m['merk_mobil'] . " (" . $m['plat_nomor'] . ")";
                                }
                            ?>
                            <tr>
                                <td><b>#TRX-<?php echo str_pad($trx['id_transaksi'], 4, '0', STR_PAD_LEFT); ?></b></td>
                                <td><?php echo $nama_cust; ?></td>
                                <td><?php echo $nama_mobil; ?></td>
                                <td><?php echo date('d M Y', strtotime($trx['tanggal_sewa'])); ?></td>
                                <td><?php echo date('d M Y', strtotime($trx['tanggal_kembali'])); ?></td>
                            </tr>
                            <?php endforeach; ?>
                            <?php if(empty($_SESSION['db_transaksi'])) echo "<tr><td colspan='5' style='text-align:center;'>Belum ada transaksi</td></tr>"; ?>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script>
        function switchTab(event, tabId) {
            document.querySelectorAll('.section-content').forEach(el => el.classList.remove('active'));
            document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
            document.getElementById(tabId).classList.add('active');
            event.currentTarget.classList.add('active');
            localStorage.setItem('activeTabYovie', tabId);
        }

        function validateDate() {
            const ts = new Date(document.getElementById('ts').value);
            const tk = new Date(document.getElementById('tk').value);
            if (tk < ts) {
                alert("Kesalahan: Tanggal pengembalian tidak boleh mendahului tanggal sewa!");
                return false;
            }
            return true;
        }

        window.onload = () => {
            const activeTab = localStorage.getItem('activeTabYovie');
            if (activeTab) {
                const targetBtn = Array.from(document.querySelectorAll('.nav-item'))
                                       .find(btn => btn.getAttribute('onclick').includes(activeTab));
                if (targetBtn) targetBtn.click();
            }

            const alertBox = document.getElementById('alert-msg');
            if (alertBox) {
                setTimeout(() => { alertBox.style.display = 'none'; }, 4000);
            }
        };
    </script>
</body>
<?php endif; ?>
</html>