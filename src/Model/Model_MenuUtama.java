/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author AJI-PC
 */
public class Model_MenuUtama {
    private static String nama;

    public String getNama() {
        System.err.println("susah getNama"+nama);
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
        System.err.println("susah setNama"+this.nama);
    }
    
}
