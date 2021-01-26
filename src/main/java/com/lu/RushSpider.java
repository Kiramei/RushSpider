package com.lu;

public class RushSpider {
    public static void main(String[] args) {
        if (args.length == 4 && args[2].equals("-g") && args[0].equals("-url")) {
            int a;
            try{
                a=Integer.parseInt(args[3]);
            }catch (Exception e){
                System.out.println("generation is an integer!");
                return;
            }
            if (a<=0){
                System.out.println("generation must be bigger than 0!");
                return;
            }
            if (!args[1].startsWith("http")){
                System.out.println("url must start with 'http'!");
                return;
            }
            LittleSpider t = new LittleSpider(args[1]);
            t.setGeneration(a);
            System.out.println("--------------------------------------Start------------------------------------------");
            t.cultivate();
            System.out.println("--------------------------------------Finish-----------------------------------------");
        }else{
            System.out.println("RushSpider usage: -url [your url] -g [count of generations]");
        }
    }
}