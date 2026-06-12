package com.app.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.model.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDB {
    private static final String DB_DIR = System.getProperty("user.home") + "/AppNilaiData/";
    private static final Gson gson = new Gson();

    static {
        File dir = new File(DB_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // --- GENERIC READ/WRITE METHOD ---
    private static <T> List<T> read(String filename, Type type) {
        try (Reader reader = new FileReader(DB_DIR + filename)) {
            List<T> list = gson.fromJson(reader, type);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) { return new ArrayList<>(); }
    }
    private static <T> void write(String filename, List<T> list) {
        try (Writer writer = new FileWriter(DB_DIR + filename)) {
            gson.toJson(list, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- MAHASISWA ---
    public static List<Mahasiswa> getMahasiswa() {
        return read("mahasiswa.json", new TypeToken<ArrayList<Mahasiswa>>(){}.getType());
    }
    public static void saveMahasiswa(List<Mahasiswa> list) { write("mahasiswa.json", list); }

    // --- MATA KULIAH ---
    public static List<MataKuliah> getMataKuliah() {
        return read("matakuliah.json", new TypeToken<ArrayList<MataKuliah>>(){}.getType());
    }
    public static void saveMataKuliah(List<MataKuliah> list) { write("matakuliah.json", list); }

    // --- NILAI ---
    public static List<Nilai> getNilai() {
        return read("nilai.json", new TypeToken<ArrayList<Nilai>>(){}.getType());
    }
    public static void saveNilai(List<Nilai> list) { write("nilai.json", list); }
    
    // --- TAMBAH DATA NILAI ---
    public static void addNilai(Nilai nilaiBaru) {
        // 1. Ambil data nilai yang sudah ada
        List<Nilai> listNilai = getNilai(); 
        
        // 2. Tambahkan data baru
        listNilai.add(nilaiBaru);
        
        // 3. Simpan menggunakan method saveNilai yang sudah ada!
        saveNilai(listNilai);
    }
}