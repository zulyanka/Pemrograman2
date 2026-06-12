/* =========================================================
   SCRIPT.JS — Showroom Master (versi database)
   Bedanya dengan versi lama: data tidak lagi disimpan di
   localStorage, tapi dikirim/diambil dari server Node.js
   lewat fetch() ke alamat /api/... lalu server yang
   menyimpannya ke MySQL.
   ========================================================= */

const API = "/api";

// Data ditampung di sini setelah diambil dari server
let dataMobil = [];
let dataCustomer = [];
let dataTransaksi = [];

// Format Rupiah: 250000000 → Rp 250.000.000
function rupiah(angka) {
  return "Rp " + Number(angka).toLocaleString("id-ID");
}

/* ---------- AMBIL DATA DARI SERVER ---------- */
async function muatData() {
  try {
    // Ambil tiga jenis data sekaligus
    const [resMobil, resCustomer, resTransaksi] = await Promise.all([
      fetch(API + "/mobil"),
      fetch(API + "/customer"),
      fetch(API + "/transaksi")
    ]);

    dataMobil = await resMobil.json();
    dataCustomer = await resCustomer.json();
    dataTransaksi = await resTransaksi.json();

    tampilkanSemua();
  } catch (err) {
    alert("Tidak bisa terhubung ke server. Pastikan 'node server.js' sedang berjalan.");
    console.error(err);
  }
}

// Fungsi bantu untuk kirim data (POST) atau hapus (DELETE)
async function kirim(alamat, metode, isi) {
  const respon = await fetch(API + alamat, {
    method: metode,
    headers: { "Content-Type": "application/json" },
    body: isi ? JSON.stringify(isi) : undefined
  });
  const hasil = await respon.json();
  if (!respon.ok) {
    alert(hasil.pesan); // tampilkan pesan error dari server
    return false;
  }
  return true;
}

/* ---------- NAVIGASI SIDEBAR ---------- */
const tombolNav = document.querySelectorAll(".nav-btn");
const semuaHalaman = document.querySelectorAll(".page");

tombolNav.forEach(function (tombol) {
  tombol.addEventListener("click", function () {
    tombolNav.forEach(t => t.classList.remove("active"));
    semuaHalaman.forEach(h => h.classList.remove("active"));
    tombol.classList.add("active");
    document.getElementById(tombol.dataset.target).classList.add("active");
  });
});

/* ---------- DATA MOBIL ---------- */
document.getElementById("formMobil").addEventListener("submit", async function (e) {
  e.preventDefault();

  const sukses = await kirim("/mobil", "POST", {
    merk: document.getElementById("merkMobil").value,
    model: document.getElementById("modelMobil").value,
    tahun: document.getElementById("tahunMobil").value,
    warna: document.getElementById("warnaMobil").value,
    harga: Number(document.getElementById("hargaMobil").value)
  });

  if (sukses) { this.reset(); muatData(); }
});

async function hapusMobil(id) {
  if (!confirm("Hapus mobil ini?")) return;
  if (await kirim("/mobil/" + id, "DELETE")) muatData();
}

function renderTabelMobil() {
  const tbody = document.getElementById("tabelMobil");
  if (dataMobil.length === 0) {
    tbody.innerHTML = '<tr><td colspan="7" class="empty">Belum ada data mobil. Isi form di atas untuk menambahkan.</td></tr>';
    return;
  }
  tbody.innerHTML = dataMobil.map(function (m, i) {
    const status = m.terjual ? ' <span class="badge-terjual">Terjual</span>' : "";
    return `<tr>
      <td>${i + 1}</td>
      <td>${m.merk}</td>
      <td>${m.model}${status}</td>
      <td>${m.tahun}</td>
      <td>${m.warna}</td>
      <td>${rupiah(m.harga)}</td>
      <td><button class="btn-hapus" onclick="hapusMobil(${m.id})">Hapus</button></td>
    </tr>`;
  }).join("");
}

/* ---------- DATA CUSTOMER ---------- */
document.getElementById("formCustomer").addEventListener("submit", async function (e) {
  e.preventDefault();

  const sukses = await kirim("/customer", "POST", {
    nama: document.getElementById("namaCustomer").value,
    hp: document.getElementById("hpCustomer").value,
    email: document.getElementById("emailCustomer").value,
    alamat: document.getElementById("alamatCustomer").value
  });

  if (sukses) { this.reset(); muatData(); }
});

async function hapusCustomer(id) {
  if (!confirm("Hapus customer ini?")) return;
  if (await kirim("/customer/" + id, "DELETE")) muatData();
}

function renderTabelCustomer() {
  const tbody = document.getElementById("tabelCustomer");
  if (dataCustomer.length === 0) {
    tbody.innerHTML = '<tr><td colspan="6" class="empty">Belum ada data customer.</td></tr>';
    return;
  }
  tbody.innerHTML = dataCustomer.map(function (c, i) {
    return `<tr>
      <td>${i + 1}</td>
      <td>${c.nama}</td>
      <td>${c.hp}</td>
      <td>${c.email}</td>
      <td>${c.alamat}</td>
      <td><button class="btn-hapus" onclick="hapusCustomer(${c.id})">Hapus</button></td>
    </tr>`;
  }).join("");
}

/* ---------- TRANSAKSI ---------- */

// Mengisi dropdown pilihan mobil & customer
function isiDropdown() {
  const pilihMobil = document.getElementById("pilihMobil");
  const pilihCustomer = document.getElementById("pilihCustomer");

  pilihMobil.innerHTML = '<option value="">-- Pilih mobil --</option>' +
    dataMobil.filter(m => !m.terjual)
      .map(m => `<option value="${m.id}">${m.merk} ${m.model} (${m.tahun}) — ${rupiah(m.harga)}</option>`)
      .join("");

  pilihCustomer.innerHTML = '<option value="">-- Pilih customer --</option>' +
    dataCustomer
      .map(c => `<option value="${c.id}">${c.nama} (${c.hp})</option>`)
      .join("");
}

document.getElementById("formTransaksi").addEventListener("submit", async function (e) {
  e.preventDefault();

  const sukses = await kirim("/transaksi", "POST", {
    mobil_id: Number(document.getElementById("pilihMobil").value),
    customer_id: Number(document.getElementById("pilihCustomer").value),
    tanggal: document.getElementById("tanggalTransaksi").value,
    metode: document.getElementById("metodeBayar").value
  });

  if (sukses) {
    this.reset();
    muatData();
    alert("Transaksi berhasil disimpan! ✔");
  }
});

async function hapusTransaksi(id) {
  if (!confirm("Hapus transaksi ini? Mobil akan kembali berstatus tersedia.")) return;
  if (await kirim("/transaksi/" + id, "DELETE")) muatData();
}

function renderTabelTransaksi() {
  const tbody = document.getElementById("tabelTransaksi");
  if (dataTransaksi.length === 0) {
    tbody.innerHTML = '<tr><td colspan="7" class="empty">Belum ada transaksi.</td></tr>';
    return;
  }
  tbody.innerHTML = dataTransaksi.map(function (t, i) {
    return `<tr>
      <td>${i + 1}</td>
      <td>${t.tanggal}</td>
      <td>${t.namaCustomer}</td>
      <td>${t.namaMobil}</td>
      <td>${t.metode}</td>
      <td>${rupiah(t.harga)}</td>
      <td><button class="btn-hapus" onclick="hapusTransaksi(${t.id})">Hapus</button></td>
    </tr>`;
  }).join("");
}

/* ---------- DASHBOARD ---------- */
function renderDashboard() {
  document.getElementById("statMobil").textContent = dataMobil.length;
  document.getElementById("statCustomer").textContent = dataCustomer.length;
  document.getElementById("statTransaksi").textContent = dataTransaksi.length;

  const total = dataTransaksi.reduce((jumlah, t) => jumlah + Number(t.harga), 0);
  document.getElementById("statPendapatan").textContent = rupiah(total);

  const tbody = document.getElementById("tabelDashboard");
  if (dataTransaksi.length === 0) {
    tbody.innerHTML = '<tr><td colspan="4" class="empty">Belum ada transaksi. Mulai dari menu Data Mobil → Data Customer → Transaksi.</td></tr>';
    return;
  }
  tbody.innerHTML = dataTransaksi.slice(-5).reverse().map(t =>
    `<tr><td>${t.tanggal}</td><td>${t.namaCustomer}</td><td>${t.namaMobil}</td><td>${rupiah(t.harga)}</td></tr>`
  ).join("");
}

/* ---------- JALANKAN ---------- */
function tampilkanSemua() {
  renderTabelMobil();
  renderTabelCustomer();
  renderTabelTransaksi();
  renderDashboard();
  isiDropdown();
}

muatData(); // ambil data dari server saat halaman pertama dibuka
