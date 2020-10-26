/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.form.PDFieldTree;
import org.apache.pdfbox.pdmodel.interactive.measurement.PDMeasureDictionary;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFBox;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Example of a custom PDFGraphicsStreamEngine subclass. Allows text and graphics to be processed
 * in a custom manner. This example simply prints the operations to stdout.
 *
 * <p>See {@link PDFStreamEngine} for further methods which may be overridden.
 * 
 * @author John Hewson
 */
public class CustomGraphicsStreamEngine extends PDFGraphicsStreamEngine
{
    /**
     * Constructor.
     *
     * @param page PDF Page
     */
    protected CustomGraphicsStreamEngine(PDPage page)
    {
        super(page);
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) throws IOException
    {
        //File file = new File("D:\\test.pdf");
        //File file = new File("C:\\Users\\54286\\Desktop\\盘龙区地名志——正文.pdf");
        //C:/Users/54286/Desktop/盘龙区地名志——正文.pdf
        try {
            //PDDocument doc = PDDocument.load(file);

            /*
            获取地名pdf中的地名及其页码信息
             */
            /*
            System.out.println(doc.getNumberOfPages());
            PDFTextStripper stripper=new PDFTextStripper();
            stripper.setSortByPosition(true);
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\54286\\Desktop\\盘龙区地名志——正文.txt"));
            for (int i = 1; i < doc.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String content = stripper.getText(doc);

                String[] strs = content.split("\n");
                for (int j = 0; j < strs.length; j++) {
                    if (strs[j].indexOf("【") != -1) {
                        bw.write(strs[j].substring(0, strs[j].indexOf("【")));
                        bw.write(" ");
                        bw.write(String.valueOf(i));
                        bw.newLine();
                    }
                }
            }
            //关闭流
            bw.close();
            System.out.println("写入成功");
            */

            /*PDDocumentInformation info = doc.getDocumentInformation();
            List<PDAnnotation> list = doc.getPage(0).getAnnotations();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getAnnotationName());
            }

            COSDictionary d = doc.getPage(0).getCOSObject();
            Iterator ss = d.entrySet().iterator();
            while (ss.hasNext())
            {
                Map.Entry<COSName, COSBase> entry = (Map.Entry<COSName, COSBase>)ss.next();
                System.out.println(entry.getKey().getName() + ", " + entry.getValue());
            }*/
            /*Iterator<String> i = info.getMetadataKeys().iterator();
            while (i.hasNext())
            {
                String str = i.next();
                System.out.println(str);
                System.out.println(info.getPropertyStringValue(str));
            }

            System.out.println("标题:" + info.getTitle());
            System.out.println("主题:" + info.getSubject());
            System.out.println("作者:" + info.getAuthor());
            System.out.println("关键字:" + info.getKeywords());

            System.out.println("应用程序:" + info.getCreator());
            System.out.println("pdf 制作程序:" + info.getProducer());

            System.out.println("作者:" + info.getTrapped());

            System.out.println("创建时间:" + dateFormat(info.getCreationDate()));
            System.out.println("修改时间:" + dateFormat(info.getModificationDate()));
            //PDFieldTree tree = new PDFieldTree()
            PDFTextStripper stripper = new PDFTextStripper();
            String content = stripper.getText(doc);
            System.out.println(content);*/
            //PDFRenderer renderer = new PDFRenderer(doc);
            //BufferedImage bufferedImage = renderer.renderImage(0);

            //要遍历的路径
            String path = "C:\\Users\\54286\\Desktop\\临沧市地图集批量入库文件夹";
            //要遍历的路径
            String NewPath = "C:\\Users\\54286\\Desktop\\临沧市地图集";
            int MapTypeCount = 0;
            BufferedWriter bw = new BufferedWriter(new FileWriter(NewPath + "\\data.txt"));
            //获取其file对象
            File mfile = new File(path);
            ParseFiles(mfile, "", bw, MapTypeCount);
            /*
            //遍历path下的文件和目录，放在File数组中
            File[] fs = mfile.listFiles();
            //遍历File[]数组
            for(File f : fs){
                //若非目录(即文件)，则打印
                if (f.isDirectory()){
                    String DirectoryName = f.toString();
                    ParseFiles(f, DirectoryName.substring(DirectoryName.lastIndexOf("\\") + 1), bw, MapTypeCount);
                }
            }*/
            bw.close();

            /*ImageIO.write(bufferedImage, "png", new File("D:\\test.png"));
            JFrame frame = new ImageViewerFrame(new File("D:\\test.png"), bufferedImage.getWidth(), bufferedImage.getHeight());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);*/
        /*PDPage page = doc.getPage(0);
        CustomGraphicsStreamEngine engine = new CustomGraphicsStreamEngine(page);
        engine.run();*/
            //doc.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static void ParseFiles(File file, String FolderName, BufferedWriter bw, int MapTypeCount){
        try {
            System.out.println(FolderName);
            String ParentNodeName = FolderName.substring(FolderName.lastIndexOf("\\") + 1);
            int count = 1;
            File[] fs = file.listFiles();
            //遍历File[]数组
            for(File f : fs) {
                String FileName = f.toString();
                if (!f.isDirectory() && f.toString().contains(".pdf")) {
                    System.out.println(FileName);
                    PDDocument doc = PDDocument.load(f);
                    PDFRenderer renderer = new PDFRenderer(doc);
                    BufferedImage bufferedImage = renderer.renderImageWithDPI(0, 300, ImageType.RGB);
                    String pngName = FileName.substring(FileName.lastIndexOf(FolderName), FileName.lastIndexOf(".pdf")) + ".png";
                    ImageIO.write(bufferedImage, "png", new File("C:\\Users\\54286\\Desktop\\临沧市地图集\\" + pngName));
                    doc.close();
                    String PngName = pngName.substring(pngName.lastIndexOf("\\") + 1, pngName.lastIndexOf(".png")).trim();
                    //String MapType = GetMapType(ParentNodeName);
                    String PngPath = "C:\\Users\\54286\\Desktop\\临沧市地图集\\" + pngName;
                    //bw.write(ParentNodeName + "," + PngName + "," + MapType + "," + PngPath);
                    bw.write(ParentNodeName + "," + PngName + "," + MapTypeCount + "," + PngPath);
                    bw.newLine();
                }
                else{
                    bw.write(ParentNodeName + "," + FileName.substring(FileName.lastIndexOf("\\") + 1) + "," + MapTypeCount + "," + " ");
                    bw.newLine();
                    String DirectoryName = f.toString();
                    if (MapTypeCount == 0)
                        ParseFiles(f, DirectoryName.substring(DirectoryName.lastIndexOf("\\") + 1), bw, MapTypeCount + count++);
                    else
                        ParseFiles(f, FolderName + DirectoryName.substring(DirectoryName.lastIndexOf("\\")), bw, MapTypeCount + count++);
                }
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private static String GetMapType(String ParentNodeName){
        switch (ParentNodeName){
            case "序图组":
                return "1";
            case "资源与环境图组":
                return "2";
            case "社会经济图组":
                return "3";
            case "区域地理图组":
                return "4";
            case "县图":
                return "5";
            case "各县城区图":
                return "6";
            case "各县影像图":
                return "7";
            case "乡镇图":
                return "8";
            case "临翔区":
                return "9";
            case "凤庆县":
                return "10";
            case "云县":
                return "11";
            case "永德县":
                return "12";
            case "镇康县":
                return "13";
            case "双江县":
                return "14";
            case "耿马县":
                return "15";
            case "沧源县":
                return "16";
        }
        return "0";
    }

    public static String dateFormat( Calendar calendar ) throws Exception
    {
        if( null == calendar )
            return null;
        String date = null;
        try{
            String pattern = DATE_FORMAT;
            SimpleDateFormat format = new SimpleDateFormat( pattern );
            date = format.format( calendar.getTime() );
        }catch( Exception e )
        {
            throw e;
        }
        return date == null ? "" : date;
    }

    
    /**
     * Runs the engine on the current page.
     *
     * @throws IOException If there is an IO error while drawing the page.
     */
    public void run() throws IOException
    {
        processPage(getPage());

        for (PDAnnotation annotation : getPage().getAnnotations())
        {
            showAnnotation(annotation);
        }
    }
    
    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException
    {
        System.out.printf("appendRectangle %.2f %.2f, %.2f %.2f, %.2f %.2f, %.2f %.2f%n",
                p0.getX(), p0.getY(), p1.getX(), p1.getY(),
                p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }

    @Override
    public void drawImage(PDImage pdImage) throws IOException
    {
        System.out.println("drawImage");
    }

    @Override
    public void clip(int windingRule) throws IOException
    {
        System.out.println("clip");
    }

    @Override
    public void moveTo(float x, float y) throws IOException
    {
        System.out.printf("moveTo %.2f %.2f%n", x, y);
    }

    @Override
    public void lineTo(float x, float y) throws IOException
    {
        System.out.printf("lineTo %.2f %.2f%n", x, y);
    }

    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException
    {
        System.out.printf("curveTo %.2f %.2f, %.2f %.2f, %.2f %.2f%n", x1, y1, x2, y2, x3, y3);
    }

    @Override
    public Point2D getCurrentPoint() throws IOException
    {
        // if you want to build paths, you'll need to keep track of this like PageDrawer does
        return new Point2D.Float(0, 0);
    }

    @Override
    public void closePath() throws IOException
    {
        System.out.println("closePath");
    }

    @Override
    public void endPath() throws IOException
    {
        System.out.println("endPath");
    }

    @Override
    public void strokePath() throws IOException
    {
        System.out.println("strokePath");
    }

    @Override
    public void fillPath(int windingRule) throws IOException
    {
        System.out.println("fillPath");
    }

    @Override
    public void fillAndStrokePath(int windingRule) throws IOException
    {
        System.out.println("fillAndStrokePath");
    }

    @Override
    public void shadingFill(COSName shadingName) throws IOException
    {
        System.out.println("shadingFill " + shadingName.toString());
    }

    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    public void showTextString(byte[] string) throws IOException
    {
        System.out.print("showTextString \"");
        super.showTextString(string);
        System.out.println("\"");
    }

    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    public void showTextStrings(COSArray array) throws IOException
    {
        System.out.print("showTextStrings \"");
        super.showTextStrings(array);
        System.out.println("\"");
    }

    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, Vector displacement)
            throws IOException
    {
        System.out.print("showGlyph " + code);
        super.showGlyph(textRenderingMatrix, font, code, displacement);
    }
    
    // NOTE: there are may more methods in PDFStreamEngine which can be overridden here too.
}
