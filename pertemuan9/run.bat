@echo off
echo ============================================
echo  POS App - Build Script (Windows)
echo ============================================

set LIB=lib\mysql-connector-j-9.6.0.jar
set SRC_DIR=src
set OUT_DIR=out
set MAIN_CLASS=Main

echo [1] Membuat folder output...
if not exist %OUT_DIR% mkdir %OUT_DIR%

echo [2] Mengkompilasi source code...
javac -cp ".;%LIB%" -d %OUT_DIR% ^
  %SRC_DIR%\config\DatabaseConfig.java ^
  %SRC_DIR%\model\Barang.java ^
  %SRC_DIR%\model\Customer.java ^
  %SRC_DIR%\model\Supplier.java ^
  %SRC_DIR%\model\DetailTransaksi.java ^
  %SRC_DIR%\model\Transaksi.java ^
  %SRC_DIR%\controller\BarangController.java ^
  %SRC_DIR%\controller\CustomerController.java ^
  %SRC_DIR%\controller\SupplierController.java ^
  %SRC_DIR%\controller\TransaksiController.java ^
  %SRC_DIR%\view\UIHelper.java ^
  %SRC_DIR%\view\FormBarang.java ^
  %SRC_DIR%\view\FormCustomer.java ^
  %SRC_DIR%\view\FormSupplier.java ^
  %SRC_DIR%\view\FormTransaksi.java ^
  %SRC_DIR%\view\LaporanTransaksi.java ^
  %SRC_DIR%\view\LaporanStok.java ^
  %SRC_DIR%\view\MainFrame.java ^
  %SRC_DIR%\Main.java

if %errorlevel% neq 0 (
    echo [ERROR] Kompilasi GAGAL!
    pause
    exit /b 1
)

echo [3] Kompilasi BERHASIL!
echo [4] Menjalankan aplikasi...
java -cp ".;%OUT_DIR%;%LIB%" %MAIN_CLASS%
pause
