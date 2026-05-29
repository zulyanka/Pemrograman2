<jsp:include page="layout/header.jsp" />
<jsp:include page="layout/sidebar.jsp" />

<main class='main-content'>
    <header class='topbar'>
        <div>
            <div class='title-kampus'>Universitas Pamulang</div>
            <div class='subtitle'>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan</div>
        </div>
        <div><i class='fas fa-user-circle' style='font-size: 24px; color: var(--main-color);'></i></div>
    </header>

    <section class='content-area'>
        <h1 class='welcome-text'>Selamat Datang di <span>Sistem Informasi</span></h1>
        <h2 style='margin-bottom: 20px; color: var(--text-muted);'>Halo, ${sessionScope.user}!</h2>
        
        <div class='id-card'>
            <div class='id-card-header'>
                <i class='fas fa-id-badge'></i>
                <h3>Profil Mahasiswa</h3>
            </div>
            <div class='id-card-body'>
                <div class='info-group'>
                    <div class='info-icon'><i class='fas fa-user'></i></div>
                    <div class='info-text'>
                        <p>Nama Lengkap</p>
                        <h4>I GEDE YOGA SETIAWAN</h4>
                    </div>
                </div>
                <div class='info-group'>
                    <div class='info-icon'><i class='fas fa-hashtag'></i></div>
                    <div class='info-text'>
                        <p>Nomor Induk Mahasiswa (NIM)</p>
                        <h4>231011401028</h4>
                    </div>
                </div>
                <div class='info-group'>
                    <div class='info-icon'><i class='fas fa-door-open'></i></div>
                    <div class='info-text'>
                        <p>Kelas</p>
                        <h4>06TPLE016</h4>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>
</body></html>