import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mariadb://localhost:3306/pbo_uas";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("==================================");
            System.out.println("|    Aplikasi Katalog Product    | ");
            System.out.println("|       Dibuat oleh: Dihak       | ");
            System.out.println("==================================");

            viewProductList(connection);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Tambah Product");
                System.out.println("2. Edit Product");
                System.out.println("3. Hapus Product");
                System.out.println("4. Lihat Product");
                System.out.println("5. Keluar");
                System.out.print("Pilihan: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    addProduct(connection, scanner);
                } else if (choice == 2) {
                    editProduct(connection, scanner);
                } else if (choice == 3) {
                    deleteProduct(connection, scanner);
                } else if (choice == 4) {
                    viewProductList(connection);
                } else if (choice == 5) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewProductList(Connection connection) throws SQLException {
        String sql = "SELECT p.id, c.name as category, p.name, p.description, p.price, p.status FROM Product p INNER JOIN Category c ON p.category_id = c.id";
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String status = resultSet.getString("status");
                System.out.println("Id: " + id);
                System.out.println("Category: " + category);
                System.out.println("Name: " + name);
                System.out.println("Description: " + description);
                System.out.println("Price: " + price);
                System.out.println("Status: " + status);
                System.out.println();
            }
        }
    }
    
    private static void viewCategoryList(Connection connection) throws SQLException {
        String sql = "SELECT id, name FROM Category";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("ID\tName");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println(id + "\t" + name);
            }
        }
    }

    private static void addProduct(Connection connection, Scanner scanner) throws SQLException {
        viewCategoryList(connection);
        System.out.print("Masukkan ID kategori: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Masukkan nama: ");
        String name = scanner.nextLine();
        System.out.print("Masukkan deskripsi: ");
        String description = scanner.nextLine();
        System.out.print("Masukkan harga: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Masukkan status: ");
        String status = scanner.nextLine();

        String sql = "INSERT INTO Product (category_id, name, description, price, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setDouble(4, price);
            statement.setString(5, status);
            statement.executeUpdate();
        }
    }

    private static void editProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Masukkan ID product: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        viewCategoryList(connection);
        System.out.print("Masukkan ID kategori: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Masukkan nama: ");
        String name = scanner.nextLine();
        System.out.print("Masukkan deskripsi: ");
        String description = scanner.nextLine();
        System.out.print("Masukkan harga: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Masukkan status: ");
        String status = scanner.nextLine();

        String sql = "UPDATE Product SET category_id = ?, name = ?, description = ?, price = ?, status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setDouble(4, price);
            statement.setString(5, status);
            statement.setInt(6, productId);
            statement.executeUpdate();
        }
    }

    private static void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Masukkan ID product: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM Product WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }
}
