import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {

    public static void main(String[] args) {
        try {
            // Read JSON input from file
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject jsonObject = new JSONObject(content);
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // List to hold the decoded points
            List<Point> points = new ArrayList<>();

            // Iterate through the keys in the JSON object
            for (int i = 1; i <= n; i++) {
                if (jsonObject.has(String.valueOf(i))) {
                    JSONObject point = jsonObject.getJSONObject(String.valueOf(i));
                    int x = i; // Key corresponds to x
                    int base = point.getInt("base");
                    String value = point.getString("value");

                    // Decode y value
                    int y = decodeValue(value, base);
                    points.add(new Point(x, y));
                }
            }

            // Calculate the constant term c using Lagrange interpolation
            double c = calculateConstantTerm(points, k);
            System.out.println("The constant term (c) is: " + c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to decode y value from a given base
    private static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Method to calculate the constant term using Lagrange interpolation
    private static double calculateConstantTerm(List<Point> points, int k) {
        double c = 0.0;
        for (int i = 0; i < k; i++) {
            double xi = points.get(i).x;
            double yi = points.get(i).y;
            double li = 1.0;

            // Lagrange basis polynomial
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    li *= (0 - points.get(j).x) / (xi - points.get(j).x);
                }
            }
            c += li * yi;
        }
        return c;
    }

    // Simple Point class to hold x and y coordinates
    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}