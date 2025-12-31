import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class QuanLyNhanVien {

    public static void main(String[] args) {
        String inputFile = "nhanvien.txt";
        String outputFile = "danh_sach_nv.csv";

        List<String> dataList = new ArrayList<>();

        System.out.println("BẮT ĐẦU CHƯƠNG TRÌNH");

        Path pathInput = Paths.get(inputFile);

        if (!Files.exists(pathInput)) {
            System.err.println("LỖI: File '" + inputFile + "' không tồn tại!");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(pathInput, StandardCharsets.UTF_8)) {
            String line;
            System.out.println("\nNỘI DUNG FILE TEXT:");

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    System.out.println(line);
                    dataList.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!dataList.isEmpty()) {
            Path pathOutput = Paths.get(outputFile);

            try (BufferedWriter writer = Files.newBufferedWriter(pathOutput, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("ID,Ho Ten,Phong Ban");
                writer.newLine();

                for (String rawLine : dataList) {
                    writer.write(rawLine);
                    writer.newLine();
                }

                System.out.println("\nĐã ghi danh sách vào file CSV: " + outputFile);

            } catch (IOException e) {
                System.err.println("Lỗi khi ghi file: " + e.getMessage());
            }
        }
    }
}