package school.hei.helloworld.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class imageController {
    private static byte[] colorImage(BufferedImage image) throws IOException {

        for (int x = 0; x < image.getWidth(); ++x)
            for (int y = 0; y < image.getHeight(); ++y)
            {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                // Normalize and gamma correct:
                double rr = Math.pow(r / 255.0, 2.2);
                double gg = Math.pow(g / 255.0, 2.2);
                double bb = Math.pow(b / 255.0, 2.2);

                // Calculate luminance:
                double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                image.setRGB(x, y, gray);
            }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }
    @PostMapping(
            path ="/image",
            consumes = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_JPEG_VALUE},
            produces = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_JPEG_VALUE})
   /* public static byte[] image(@RequestParam("file") MultipartFile mfile) throws IOException {
        File file = new File("src/main/resources/targetFile.tmp");
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(mfile.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path source = Paths.get("src/main/resources/targetFile.tmp");
        BufferedImage bi = ImageIO.read(source.toFile());
        BufferedImage i = colorImage(bi);
        ImageIO.write(i, "png", new File("src/main/resources/response.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }*/

    public @ResponseBody byte[] image(@RequestBody byte[] image) throws IOException{
        ByteArrayInputStream baos = new ByteArrayInputStream(image);
        return colorImage(ImageIO.read(baos));
    }
}
