package work.rabbi.afis.services;

import com.digitalpersona.uareu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class FingerprintService {
    private static Logger logger = LoggerFactory.getLogger(FingerprintService.class);
    private static Engine engine = UareUGlobal.GetEngine();
    private static Compression compressor = UareUGlobal.GetCompression();

    public static double compareMinexB64(String b64Minex1, String b64Minex2) throws IOException, UareUException {
        Fmd fmd1 = generateFmdFromMinexB64(b64Minex1);
        Fmd fmd2 = generateFmdFromMinexB64(b64Minex2);
        double score = matchFmd(fmd1, fmd2);

        System.out.println("Score: " + score);
        return score;
    }

    public static byte[] compressImageFileToWsq(String path) throws IOException, UareUException {
        // convert file to image
        BufferedImage image = ImageIO.read(new File(path));

        // convert image to wsq
        return compressImageToWsq(image);
    }

    public static byte[] compressImageToWsq(BufferedImage image) throws UareUException {
        // convert image bmp/jpeg/png to raw image
        byte[] rawImg = convertImageToRaw(image);

        // convert raw to wsq
        return compressToWsq(rawImg, image.getWidth(), image.getHeight());
    }

    public static byte[] convertImageToRaw(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        ColorConvertOp op = new ColorConvertOp(image.getColorModel().getColorSpace(), gray.getColorModel().getColorSpace(), null);

        op.filter(image, gray);

        WritableRaster raster = gray.getRaster();

        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return data.getData();
    }

    public static byte[] compressToWsq(byte[] raw, int width, int height) throws UareUException {
        try {
            compressor.Start();
            compressor.SetWsqBitrate(90, 0);
            return compressor.CompressRaw(raw, width, height, 500, 8, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);
        } finally {
            compressor.Finish();
        }
    }

    public static byte[] decompressWsqFileToImage(String path) throws IOException, UareUException {
        byte[] raw = Files.readAllBytes(Paths.get(path));

        // convert raw to wsq
        return decompressWsqToImage(raw);
    }

    public static byte[] decompressWsqToImage(byte[] wsq) throws UareUException {
        try {
            compressor.Start();
            // decompress wsq to raw
            Compression.RawImage raw = compressor.ExpandRaw(wsq, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);

            // convert raw image to bmp byte array
            return convertRawImageToByteArray(raw);
        } finally {
            compressor.Finish();
        }
    }

    public static byte[] convertRawImageToByteArray(Compression.RawImage raw) {
        // convert byte array back to BufferedImage
        BufferedImage bImageFromConvert = new BufferedImage(raw.width, raw.height, BufferedImage.TYPE_BYTE_GRAY);
        bImageFromConvert.getRaster().setDataElements(0, 0, raw.width, raw.height, raw.data);
        return imageToByteArray(bImageFromConvert);
    }

    public static byte[] compressToWsqFromFile(String path) throws UareUException, IOException {

        byte[] raw = convertFileToBytes(path);
        // public Fid ImportRaw(byte[] data, int width, int height, int dpi, int finger_position, int cbeff_id, Format out_format, int out_dpi, boolean rotate180)
        Fid fid1 = UareUGlobal.GetImporter().ImportRaw(raw, 320, 480, 500, 0, 0, Fid.Format.ANSI_381_2004, 500, true);

        byte[] raw1 = fid1.getData();
        System.out.println(raw1.length);

        compressor.Start();
        compressor.SetWsqBitrate(75, 0);
        byte[] o = compressor.CompressRaw(fid1.getData(), fid1.getViews()[0].getWidth(), fid1.getViews()[0].getHeight(), 500, 8, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);
//        Fid out = compressor.CompressFid(fid1, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);

        compressor.Finish();


        try (FileOutputStream fos = new FileOutputStream("input/b.wsq")) {
            fos.write(o);
//            fos.close(); //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
        return null;
    }

    public static byte[] convertImageToRaw(String path) throws IOException, UareUException {
        BufferedImage image = ImageIO.read(new File(path));

        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        ColorConvertOp op = new ColorConvertOp(image.getColorModel().getColorSpace(), gray.getColorModel().getColorSpace(), null);

        op.filter(image, gray);

        WritableRaster raster = gray.getRaster();

        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        byte[] rawPixels = data.getData();
//        Fid fid = UareUGlobal.GetImporter().ImportRaw(rawPixels, 320, 480, 500, 0, 0, Fid.Format.ANSI_381_2004, 500, true);

        compressor.Start();
        compressor.SetWsqBitrate(90, 0);
//        byte[] o = compressor.CompressRaw(fid.getData(), fid.getViews()[0].getWidth(), fid.getViews()[0].getHeight(), 500, 8, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);
//        Fid out = compressor.CompressFid(fid, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);

        byte[] out = compressor.CompressRaw(rawPixels, image.getWidth(), image.getHeight(), 500, 8, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);

        Compression.RawImage rawO = compressor.ExpandRaw(out, Compression.CompressionAlgorithm.COMPRESSION_WSQ_NIST);
        System.out.println(rawO.data.length);

        compressor.Finish();

        try (FileOutputStream fos = new FileOutputStream("input/b.wsq")) {
            fos.write(out);
//            fos.close(); //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }


        // convert byte array back to BufferedImage
        BufferedImage bImageFromConvert = new BufferedImage(rawO.width, rawO.height, BufferedImage.TYPE_BYTE_GRAY);
        bImageFromConvert.getRaster().setDataElements(0, 0, rawO.width, rawO.height, rawO.data);
        imageToFile("input/test_out.bmp", bImageFromConvert);

        return rawPixels;
    }

    public static Fmd generateFmdFromBmpFile(String path) throws IOException, UareUException {
        BufferedImage bimg = ImageIO.read(new File(path));
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        byte[] raw = imageToByteArray(bimg);
        return engine.CreateFmd(raw, width, height, 500, 0, 0, Fmd.Format.ANSI_378_2004);
    }

    public static Fmd generateFmdFromBytes(byte[] bmp) throws IOException, UareUException {
        InputStream targetStream = new ByteArrayInputStream(bmp);
        BufferedImage bimg = ImageIO.read(targetStream);
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        byte[] raw = convertImageToRaw(bimg);
        return generateFmdFromBytes(raw, width, height);
    }

    public static Fmd generateFmdFromBytes(byte[] raw, int width, int height) throws UareUException {
//        return engine.CreateFmd(raw, width, height, 500, 0, 0, Fmd.Format.ANSI_378_2004);
        return engine.CreateFmd(raw, width, height, 500, 0, 0, Fmd.Format.ANSI_378_2004);
    }

    public static Fmd generateFmdFromMinexB64(String b64) throws UareUException, IOException {
        byte[] minex = Base64.getDecoder().decode(b64);
        return generateFmdFromMinex(minex);
    }

    public static Fmd generateFmdFromMinexFile(String path) throws UareUException, IOException {
        byte[] minex = Files.readAllBytes(Paths.get(path));
        return generateFmdFromMinex(minex);
    }

    public static Fmd generateFmdFromMinex(byte[] minex) throws UareUException {
        return UareUGlobal.GetImporter().ImportFmd(minex, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
    }

    public static double matchFmd(Fmd fmd1, Fmd fmd2) throws UareUException {
        int falseMatchRate = engine.Compare(fmd1, 0, fmd2, 0);
        double score = ((double) Engine.PROBABILITY_ONE - (double) falseMatchRate) / (double) Engine.PROBABILITY_ONE;
        System.out.println(score);
        return score;
    }

    private static Fmd convertToRegFmd(byte[] raw) {

        try {
            return UareUGlobal.GetImporter().ImportFmd(raw, Fmd.Format.DP_REG_FEATURES, Fmd.Format.DP_REG_FEATURES);
        } catch (UareUException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean identifyFmdBytes(byte[] fmdData, List<byte[]> fmdDataList) throws UareUException {
        // input fmd as featureset
        Fmd inputFmd = UareUGlobal.GetImporter().ImportFmd(fmdData, Fmd.Format.DP_VER_FEATURES, Fmd.Format.DP_VER_FEATURES);


        // template list to fmd list
        Fmd[] fmds = fmdDataList
                .stream().map(FingerprintService::convertToRegFmd)
                .toArray(Fmd[]::new);

        return identifyFmd(inputFmd, fmds);
    }

    public static boolean identifyFmd(Fmd fmd1, Fmd[] fmds) throws UareUException {
        //target false positive identification rate: 0.00001
        //for a discussion of setting the threshold as well as the statistical validity of the dissimilarity score and error rates, consult the Developer Guide.
        int falsePositiveRate = Engine.PROBABILITY_ONE / 100000;
        int fingerCnt = 4; //how many fingerprints to collect for the identification

        Engine.Candidate[] vCandidates = engine.Identify(fmd1, 0, fmds, falsePositiveRate, fingerCnt);
        if (0 != vCandidates.length) {
            //optional: to get false match rate compare with the top candidate
            int falseMatchRate = engine.Compare(fmd1, 0, fmds[vCandidates[0].fmd_index], vCandidates[0].view_index);
            StringBuilder builder = new StringBuilder();
//            String str = String.format("Fingerprint identified, %s\n", m_vFingerNames[vCandidates[0].fmd_index]);
//            builder.append(str);
            String str = String.format("dissimilarity score: 0x%x.\n", falseMatchRate);
            builder.append(str);
            str = String.format("false match rate: %e.\n\n\n", (double) (falseMatchRate / Engine.PROBABILITY_ONE));
            builder.append(str);
            logger.warn("Match found! Details: " + builder.toString());
            return true;
        } else {
            logger.info("No match found!");
            return false; // no match found
        }
    }

    private static byte[] imageToByteArray(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "bmp", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void imageToFile(String file, BufferedImage image) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            ImageIO.write(image, "bmp", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertToBase64(String file) throws IOException {
        byte[] rawData = Files.readAllBytes(Paths.get(file));
        String s1 = Base64.getEncoder().encodeToString(rawData);
        System.out.println(s1);
    }

    public static byte[] convertFileToBytes(String file) throws IOException {
        return Files.readAllBytes(Paths.get(file));
    }
}
