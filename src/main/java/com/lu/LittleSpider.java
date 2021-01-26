package com.lu;

import com.gargoylesoftware.htmlunit.WebClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LittleSpider {

    private Set<String> flies;
    private Set<String> babies_img;
    private Set<String> babies_mp4;
    private int generation;
    private int max;

    static {
        File f;
        if (!(f=new File("nest")).exists()) {
            System.out.println(f.mkdir()?"A NEST HAS BEEN FOUND!":"");
        }
    }

    public LittleSpider(String base_url) {
        flies = new HashSet<>();
        babies_img = new HashSet<>();
        babies_mp4 = new HashSet<>();
        flies.add(base_url);
    }

    public void setGeneration(int generation) {
        this.generation = generation - 1;
        max = generation - 1;
        rest();
    }

    public void cultivate() {
        System.out.print("generation " + (max - generation + 1) + " :\t\t" + flies.size()+"\t\t");
        System.out.print("babies_img :\t\t" +babies_img.size()+"\t\t");
        System.out.println("babies_mp4 :\t\t" +babies_mp4.size());
        String[] arr1 = new String[flies.size()] ;
        flies.toArray(arr1);
        String[] arr2 = new String[babies_img.size()] ;
        babies_img.toArray(arr2);
        String[] arr3 = new String[babies_mp4.size()] ;
        babies_mp4.toArray(arr3);
        settle( (max - generation + 1) ,arr1,arr2,arr3);
        if (generation <= 0) return;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(5));

        for (String x : arr1) {
            Runnable k= () -> {
                Document doc;
                try {
                    WebClient wb=new WebClient();
                    wb.getOptions().setJavaScriptEnabled(true);
                    wb.getOptions().setCssEnabled(false);
                    doc = Jsoup.connect(x)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                            .followRedirects(true)
                            .ignoreContentType(true)
                            .get();
                } catch (Exception e) {
                    flies.remove(x);
                    return;
                }
                for (String m : doc.select("a").eachAttr("href")) {
                    if(m.startsWith("http")){
                        if (m.endsWith(".mp4"))
                            babies_mp4.add(m);
                        else
                            flies.add(m);
                    }else {
                        if (m.endsWith(".html") || m.endsWith(".htm")||m.startsWith("/")) {
                            flies.add(x.substring(0, x.lastIndexOf("/")) + m);
                        }
                    }
                }
                babies_img.addAll(doc.select("img").eachAttr("src"));
                babies_mp4.addAll(doc.select("video").eachAttr("src"));
                flies.remove(x);
            };
            executor.execute(k);
        }
        executor.shutdown();
        while (true){
            if(executor.isTerminated()){
                generation--;
                cultivate();
                break;
            }

        }

    }
    File fs;
    public void rest(){
        for (int i=1;;i++){
            if (!(fs=new File("nest\\family"+i)).exists()) {
                if (fs.mkdir()) {
                    System.out.print(" ");
                }
                break;
            }
        }
    }
    public void settle(int generation,String[] urls,String[] img,String[] mp4){
        try {
            File m=new File(fs.getPath()+"\\generation"+generation+".txt");
            if (m.createNewFile()) {
                System.out.print(" ");
            }
            FileWriter fw=new FileWriter(m);
            fw.write("----------------------url--------------------\n");
            for (String x:urls)
                fw.write(x+"\n");
            fw.write("----------------------img--------------------\n");
            for (String x:img)
                fw.write(x+"\n");
            fw.write("----------------------mp4--------------------\n");
            for (String x:mp4)
                fw.write(x+"\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
