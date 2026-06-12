/* =========================================================
   SCRIPT.JS — ZW Rent Car (Frontend)
   Mengambil & mengirim data ke server lewat fetch(),
   menghitung estimasi biaya secara langsung, dan
   menyusun laporan transaksi yang bisa difilter & dicetak.
   ========================================================= */

const API = "/api";

let dataMobil = [];
let dataCustomer = [];
let dataSewa = [];

// Format Rupiah: 350000 → Rp 350.000
function rupiah(angka) {
  return "Rp " + Number(angka).toLocaleString("id-ID");
}

// Format tanggal Indonesia: 2026-06-12 → 12 Juni 2026
function tanggalID(str) {
  if (!str) return "-";
  return new Date(str).toLocaleDateString("id-ID", { day: "numeric", month: "long", year: "numeric" });
}

/* ---------- AMBIL DATA DARI SERVER ---------- */
async function muatData() {
  try {
    const [resMobil, resCustomer, resSewa] = await Promise.all([
      fetch(API + "/mobil"),
      fetch(API + "/customer"),
      fetch(API + "/sewa")
    ]);

    dataMobil = await resMobil.json();
    dataCustomer = await resCustomer.json();
    dataSewa = await resSewa.json();

    tampilkanSemua();
  } catch (err) {
    alert("Tidak bisa terhubung ke server. Pastikan 'node server.js' sedang berjalan.");
    console.error(err);
  }
}

// Fungsi bantu kirim data ke server. Mengembalikan hasil JSON, atau null kalau gagal.
async function kirim(alamat, metode, isi) {
  const respon = await fetch(API + alamat, {
    method: metode,
    headers: { "Content-Type": "application/json" },
    body: isi ? JSON.stringify(isi) : undefined
  });
  const hasil = await respon.json();
  if (!respon.ok) {
    alert(hasil.pesan);
    return null;
  }
  return hasil;
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

  const hasil = await kirim("/mobil", "POST", {
    merk: document.getElementById("merkMobil").value,
    model: document.getElementById("modelMobil").value,
    tahun: document.getElementById("tahunMobil").value,
    plat_nomor: document.getElementById("platMobil").value,
    harga_sewa: Number(document.getElementById("hargaMobil").value)
  });

  if (hasil) { this.reset(); muatData(); }
});

async function hapusMobil(id) {
  if (!confirm("Hapus mobil ini dari armada?")) return;
  if (await kirim("/mobil/" + id, "DELETE")) muatData();
}

function renderTabelMobil() {
  const tbody = document.getElementById("tabelMobil");
  if (dataMobil.length === 0) {
    tbody.innerHTML = '<tr><td colspan="8" class="empty">Belum ada armada. Tambahkan lewat form di atas.</td></tr>';
    return;
  }
  tbody.innerHTML = dataMobil.map(function (m, i) {
    const badge = m.status === "Tersedia"
      ? '<span class="badge b-tersedia">Tersedia</span>'
      : '<span class="badge b-disewa">Disewa</span>';
    return `<tr>
      <td>${i + 1}</td>
      <td>${m.merk}</td>
      <td>${m.model}</td>
      <td>${m.tahun}</td>
      <td>${m.plat_nomor}</td>
      <td>${rupiah(m.harga_sewa)}</td>
      <td>${badge}</td>
      <td><button class="btn-hapus" onclick="hapusMobil(${m.id})">Hapus</button></td>
    </tr>`;
  }).join("");
}

/* ---------- DATA CUSTOMER ---------- */
document.getElementById("formCustomer").addEventListener("submit", async function (e) {
  e.preventDefault();

  const hasil = await kirim("/customer", "POST", {
    nama: document.getElementById("namaCustomer").value,
    no_ktp: document.getElementById("ktpCustomer").value,
    hp: document.getElementById("hpCustomer").value,
    alamat: document.getElementById("alamatCustomer").value
  });

  if (hasil) { this.reset(); muatData(); }
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
      <td>${c.no_ktp}</td>
      <td>${c.hp}</td>
      <td>${c.alamat}</td>
      <td><button class="btn-hapus" onclick="hapusCustomer(${c.id})">Hapus</button></td>
    </tr>`;
  }).join("");
}

/* ---------- PENYEWAAN ---------- */

// Mengisi dropdown form sewa & form pengembalian
function isiDropdown() {
  const pilihMobil = document.getElementById("pilihMobilSewa");
  const pilihCustomer = document.getElementById("pilihCustomerSewa");
  const pilihSewa = document.getElementById("pilihSewaAktif");

  // Hanya mobil berstatus "Tersedia" yang bisa disewa
  pilihMobil.innerHTML = '<option value="">-- Pilih mobil --</option>' +
    dataMobil.filter(m => m.status === "Tersedia")
      .map(m => `<option value="${m.id}">${m.merk} ${m.model} · ${m.plat_nomor} — ${rupiah(m.harga_sewa)}/hari</option>`)
      .join("");

  pilihCustomer.innerHTML = '<option value="">-- Pilih customer --</option>' +
    dataCustomer
      .map(c => `<option value="${c.id}">${c.nama} (${c.hp})</option>`)
      .join("");

  // Hanya transaksi berstatus "Disewa" yang bisa dikembalikan
  pilihSewa.innerHTML = '<option value="">-- Pilih transaksi --</option>' +
    dataSewa.filter(s => s.status === "Disewa")
      .map(s => `<option value="${s.id}">${s.namaCustomer} — ${s.namaMobil}</option>`)
      .join("");
}

// Estimasi biaya muncul langsung saat mobil / lama sewa berubah
function hitungEstimasi() {
  const idMobil = Number(document.getElementById("pilihMobilSewa").value);
  const lama = Number(document.getElementById("lamaHari").value);
  const kotak = document.getElementById("estimasiBiaya");

  const mobil = dataMobil.find(m => m.id === idMobil);
  if (!mobil || !lama) {
    kotak.innerHTML = "Estimasi biaya akan muncul di sini setelah mobil &amp; lama sewa dipilih.";
    return;
  }
  kotak.innerHTML = `Estimasi biaya: <strong>${rupiah(lama * mobil.harga_sewa)}</strong> (${lama} hari × ${rupiah(mobil.harga_sewa)})`;
}

document.getElementById("pilihMobilSewa").addEventListener("change", hitungEstimasi);
document.getElementById("lamaHari").addEventListener("input", hitungEstimasi);

document.getElementById("formSewa").addEventListener("submit", async function (e) {
  e.preventDefault();

  const hasil = await kirim("/sewa", "POST", {
    mobil_id: Number(document.getElementById("pilihMobilSewa").value),
    customer_id: Number(document.getElementById("pilihCustomerSewa").value),
    tgl_sewa: document.getElementById("tglSewa").value,
    lama_hari: Number(document.getElementById("lamaHari").value)
  });

  if (hasil) {
    this.reset();
    hitungEstimasi();
    muatData();
    alert("Penyewaan berhasil! Total biaya: " + rupiah(hasil.total));
  }
});

async function hapusSewa(id) {
  if (!confirm("Hapus transaksi ini? Jika masih berjalan, mobil akan kembali tersedia.")) return;
  if (await kirim("/sewa/" + id, "DELETE")) muatData();
}

function renderTabelSewa() {
  const tbody = document.getElementById("tabelSewa");
  if (dataSewa.length === 0) {
    tbody.innerHTML = '<tr><td colspan="9" class="empty">Belum ada penyewaan.</td></tr>';
    return;
  }
  tbody.innerHTML = dataSewa.map(function (s, i) {
    const badge = s.status === "Disewa"
      ? '<span class="badge b-disewa">Disewa</span>'
      : '<span class="badge b-selesai">Selesai</span>';
    return `<tr>
      <td>${i + 1}</td>
      <td>${tanggalID(s.tgl_sewa)}</td>
      <td>${s.namaCustomer}</td>
      <td>${s.namaMobil}</td>
      <td>${s.lama_hari} hari</td>
      <td>${tanggalID(s.tgl_kembali_rencana)}</td>
      <td>${rupiah(s.total_biaya)}</td>
      <td>${badge}</td>
      <td><button class="btn-hapus" onclick="hapusSewa(${s.id})">Hapus</button></td>
    </tr>`;
  }).join("");
}

/* ---------- PENGEMBALIAN ---------- */

// Tampilkan jadwal kembali saat transaksi dipilih
document.getElementById("pilihSewaAktif").addEventListener("change", function () {
  const sewa = dataSewa.find(s => s.id === Number(this.value));
  const kotak = document.getElementById("infoKembali");
  if (!sewa) {
    kotak.innerHTML = "Pilih transaksi untuk melihat jadwal kembalinya.";
    return;
  }
  kotak.innerHTML = `Jadwal kembali: <strong>${tanggalID(sewa.tgl_kembali_rencana)}</strong>. Lewat dari tanggal itu dikenakan denda sebesar tarif sewa per hari keterlambatan.`;
});

document.getElementById("formKembali").addEventListener("submit", async function (e) {
  e.preventDefault();

  const hasil = await kirim("/kembali", "POST", {
    sewa_id: Number(document.getElementById("pilihSewaAktif").value),
    tgl_kembali: document.getElementById("tglKembali").value
  });

  if (hasil) {
    this.reset();
    document.getElementById("infoKembali").innerHTML = "Pilih transaksi untuk melihat jadwal kembalinya.";
    muatData();
    if (hasil.hariTelat > 0) {
      alert(`Mobil dikembalikan TERLAMBAT ${hasil.hariTelat} hari.\nDenda: ${rupiah(hasil.denda)}`);
    } else {
      alert("Mobil dikembalikan tepat waktu. Tidak ada denda. ✔");
    }
  }
});

function renderTabelKembali() {
  const tbody = document.getElementById("tabelKembali");
  const selesai = dataSewa.filter(s => s.status === "Selesai");
  if (selesai.length === 0) {
    tbody.innerHTML = '<tr><td colspan="6" class="empty">Belum ada pengembalian.</td></tr>';
    return;
  }
  tbody.innerHTML = selesai.map(function (s, i) {
    const denda = s.denda > 0
      ? `<span class="badge b-denda">${rupiah(s.denda)}</span>`
      : '<span class="badge b-tersedia">Tepat waktu</span>';
    return `<tr>
      <td>${i + 1}</td>
      <td>${s.namaCustomer}</td>
      <td>${s.namaMobil}</td>
      <td>${tanggalID(s.tgl_kembali_rencana)}</td>
      <td>${tanggalID(s.tgl_kembali_aktual)}</td>
      <td>${denda}</td>
    </tr>`;
  }).join("");
}

/* ---------- DASHBOARD ---------- */
function renderDashboard() {
  document.getElementById("statMobil").textContent = dataMobil.length;
  document.getElementById("statDisewa").textContent = dataMobil.filter(m => m.status === "Disewa").length;
  document.getElementById("statSewa").textContent = dataSewa.length;

  // Pendapatan = seluruh biaya sewa + seluruh denda
  const total = dataSewa.reduce((jml, s) => jml + Number(s.total_biaya) + Number(s.denda), 0);
  document.getElementById("statPendapatan").textContent = rupiah(total);

  const tbody = document.getElementById("tabelDashboard");
  const aktif = dataSewa.filter(s => s.status === "Disewa");
  if (aktif.length === 0) {
    tbody.innerHTML = '<tr><td colspan="4" class="empty">Tidak ada mobil yang sedang disewa. Semua armada di garasi. 🌙</td></tr>';
    return;
  }
  tbody.innerHTML = aktif.map(s =>
    `<tr><td>${tanggalID(s.tgl_sewa)}</td><td>${s.namaCustomer}</td><td>${s.namaMobil}</td><td>${tanggalID(s.tgl_kembali_rencana)}</td></tr>`
  ).join("");
}

/* ---------- LAPORAN TRANSAKSI ---------- */
function terapkanLaporan() {
  const dari = document.getElementById("lapDari").value;
  const sampai = document.getElementById("lapSampai").value;

  // Saring transaksi berdasarkan tanggal sewa
  let hasil = dataSewa;
  if (dari)   hasil = hasil.filter(s => s.tgl_sewa >= dari);
  if (sampai) hasil = hasil.filter(s => s.tgl_sewa <= sampai);

  // Teks periode di bawah judul laporan
  let periode = "Periode: Semua Transaksi";
  if (dari && sampai) periode = `Periode: ${tanggalID(dari)} s.d. ${tanggalID(sampai)}`;
  else if (dari)      periode = `Periode: sejak ${tanggalID(dari)}`;
  else if (sampai)    periode = `Periode: sampai ${tanggalID(sampai)}`;
  document.getElementById("periodeLaporan").textContent = periode;

  // Isi tabel laporan
  const tbody = document.getElementById("tabelLaporan");
  if (hasil.length === 0) {
    tbody.innerHTML = '<tr><td colspan="10" class="empty">Tidak ada transaksi pada periode ini.</td></tr>';
  } else {
    tbody.innerHTML = hasil.map((s, i) =>
      `<tr>
        <td style="text-align:center">${i + 1}</td>
        <td>${tanggalID(s.tgl_sewa)}</td>
        <td>${s.namaCustomer}</td>
        <td>${s.namaMobil}</td>
        <td style="text-align:center">${s.lama_hari} hari</td>
        <td>${tanggalID(s.tgl_kembali_rencana)}</td>
        <td>${tanggalID(s.tgl_kembali_aktual)}</td>
        <td style="text-align:center">${s.status}</td>
        <td style="text-align:right">${rupiah(s.denda)}</td>
        <td style="text-align:right">${rupiah(Number(s.total_biaya) + Number(s.denda))}</td>
      </tr>`
    ).join("");
  }

  // Ringkasan di bawah tabel
  const totalSewa = hasil.reduce((j, s) => j + Number(s.total_biaya), 0);
  const totalDenda = hasil.reduce((j, s) => j + Number(s.denda), 0);
  document.getElementById("ringkasanLaporan").innerHTML = `
    <strong>Jumlah Transaksi</strong>: ${hasil.length} transaksi<br>
    <strong>Total Biaya Sewa</strong>: ${rupiah(totalSewa)}<br>
    <strong>Total Denda</strong>: ${rupiah(totalDenda)}<br>
    <strong>Total Pendapatan</strong>: ${rupiah(totalSewa + totalDenda)}
  `;

  // Tanggal cetak otomatis hari ini
  document.getElementById("tglCetak").textContent =
    "Tangerang Selatan, " + new Date().toLocaleDateString("id-ID", { day: "numeric", month: "long", year: "numeric" });
}

/* ---------- JALANKAN ---------- */
function tampilkanSemua() {
  renderTabelMobil();
  renderTabelCustomer();
  renderTabelSewa();
  renderTabelKembali();
  renderDashboard();
  isiDropdown();
  terapkanLaporan();
}

muatData();
