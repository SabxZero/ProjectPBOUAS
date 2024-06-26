import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HPdatabase {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/tugaspbo?useSSL=false";
    private static final String USER = "pma";
    private static final String PASSWORD = "";

    private Connection conn;

    public HPdatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Mencoba terhubung ke database...");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi berhasil!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Koneksi gagal!");
            e.printStackTrace();
        }
    }

    public void lihatTipeMerek() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tipemerekhp");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("nama_tipe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void lihatMerekHP(int tipeId) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM merekhp WHERE id_tipe = " + tipeId);
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("nama_merek"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void lihatInfoHP(int merekId) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM infohp WHERE id_merek = " + merekId);
            if (rs.next()) {
                System.out.println("Harga: " + rs.getDouble("harga"));
                System.out.println("Stok: " + rs.getInt("stok"));
                System.out.println("Rating: " + rs.getFloat("rating"));
            } else {
                System.out.println("Info tidak ditemukan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tambahTipeMerek(String tipeMerek) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO tipemerekhp (nama_tipe) VALUES ('" + tipeMerek + "')");
            System.out.println("Tipe merek berhasil ditambahkan!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tambahHargaHP(int merekId, double harga, int stok, float rating) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM infohp WHERE id_merek = " + merekId);
            if (rs.next()) {
                System.out.println("Harga sudah ditambahkan!");
            } else {
                stmt.executeUpdate("INSERT INTO infohp (id_merek, harga, stok, rating) VALUES (" + merekId + ", " + harga + ", " + stok + ", " + rating + ")");
                System.out.println("Harga berhasil ditambahkan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HPdatabase db = new HPdatabase();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Lihat Tipe Merek");
            System.out.println("2. Tambah Tipe Merek");
            System.out.println("3. Tambah Harga Merek HP");
            System.out.println("4. Keluar");
            System.out.print("Pilih opsi: ");
            int opsi = scanner.nextInt();
            scanner.nextLine();  // Clear buffer

            switch (opsi) {
                case 1:
                    db.lihatTipeMerek();
                    System.out.print("Pilih nomor tipe merek untuk melihat merek HP: ");
                    int tipeId = scanner.nextInt();
                    scanner.nextLine();  // Clear buffer
                    db.lihatMerekHP(tipeId);

                    System.out.print("Pilih nomor merek HP untuk melihat info: ");
                    int merekId = scanner.nextInt();
                    scanner.nextLine();  // Clear buffer
                    db.lihatInfoHP(merekId);

                    break;
                case 2:
                    System.out.print("Masukkan nama tipe merek: ");
                    String tipeMerek = scanner.nextLine();
                    db.tambahTipeMerek(tipeMerek);
                    break;
                case 3:
                    db.lihatTipeMerek();
                    System.out.print("Pilih nomor tipe merek untuk melihat merek HP: ");
                    tipeId = scanner.nextInt();
                    scanner.nextLine();  // Clear buffer
                    db.lihatMerekHP(tipeId);

                    System.out.print("Pilih nomor merek HP untuk menambah harga: ");
                    merekId = scanner.nextInt();
                    scanner.nextLine();  // Clear buffer

                    System.out.print("Masukkan harga: ");
                    double harga = scanner.nextDouble();
                    scanner.nextLine();  // Clear buffer

                    System.out.print("Masukkan stok: ");
                    int stok = scanner.nextInt();
                    scanner.nextLine();  // Clear buffer

                    System.out.print("Masukkan rating: ");
                    float rating = scanner.nextFloat();
                    scanner.nextLine();  // Clear buffer

                    db.tambahHargaHP(merekId, harga, stok, rating);
                    break;
                case 4:
                    System.out.println("Keluar dari program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opsi tidak valid! Silakan pilih lagi.");
            }
        }
    }
}
