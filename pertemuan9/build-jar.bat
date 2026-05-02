@echo off
echo ============================================
echo  POS App - Build JAR (Windows)
echo ============================================

set LIB=lib\mysql-connector-j-9.6.0.jar
set OUT_DIR=out
set JAR_NAME=pos-app.jar

echo [1] Compile dulu (jalankan run.bat terlebih dahulu jika belum)...
call run.bat

echo [2] Extract MySQL connector ke folder sementara...
if not exist out\extracted mkdir out\extracted
cd out\extracted
jar xf ..\..\%LIB%
cd ..\..

echo [3] Membuat MANIFEST...
echo Main-Class: Main > out\MANIFEST.MF

echo [4] Build JAR...
jar cfm %JAR_NAME% out\MANIFEST.MF -C out . -C out\extracted .

echo [5] JAR berhasil dibuat: %JAR_NAME%
echo Jalankan dengan: java -jar %JAR_NAME%
pause
