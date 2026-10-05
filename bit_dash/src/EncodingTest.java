import java.nio.charset.Charset;

public class EncodingTest {
    public static void main(String[] args) {
        System.out.println("✓");
        System.out.println(Charset.defaultCharset());
        System.out.println(System.getProperty("file.encoding"));
        System.out.println(System.getProperty("stdout.encoding"));
    }
}