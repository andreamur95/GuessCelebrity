package org.example;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Converting Roman numbers to integers
 *
 * Andrea Murino
 */
@SpringBootApplication
public class App {
    AbstractMap.SimpleEntry<Integer,Integer> romanCharacter(char c) {
        if (c == 'I')
            return new AbstractMap.SimpleEntry<>(1,1);
        if (c == 'V')
            return new AbstractMap.SimpleEntry<>(2,5);
        if (c == 'X')
            return new AbstractMap.SimpleEntry<>(3,10);
        if (c == 'L')
            return new AbstractMap.SimpleEntry<>(4,50);
        if (c == 'C')
            return new AbstractMap.SimpleEntry<>(5,100);
        if (c == 'D')
            return new AbstractMap.SimpleEntry<>(6,500);
        if (c == 'M')
            return new AbstractMap.SimpleEntry<>(7,1000);
        // if the character is not recognized then the input is not valid -> Raise exception
        throw new RuntimeException("The input is not valid!");
    }

    //function to convert roman to integer
    public int convertRomanToInt(String s) {
        //variable to store the sum
        int sum = 0;

        //getting value from symbol s1[i]
        for (int i = 0; i < s.length(); i++) {
            Map.Entry<Integer,Integer> s1 = romanCharacter(s.charAt(i));

            if (i + 1 < s.length()) {
                Map.Entry<Integer,Integer> s2 = romanCharacter(s.charAt(i + 1));
                //comparing the current character from its right character
                if (s1.getValue() >= s2.getValue()) {
                    //if the value of current character is greater or equal to the next symbol
                    sum = sum + s1.getValue();
                } else {
                    // if the value of the current character is less than the next symbol
                    // ensure that it's a power of ten and that we are subtracting only from the next two higher "digits"
                    // e.g. IC, IL and VX are illegal roman numbers
                    if((Math.abs(s1.getKey() - s2.getKey()) > 2 || s1.getValue() % 10 != 0) && s1.getValue() != 1)
                        throw new RuntimeException("The input is not valid!");

                    sum = sum - s1.getValue();
                }
            } else {
                sum = sum + s1.getValue();
            }
        }

        return sum;
    }

    // function to convert Integer to roman
    public String convertIntToRoman(Integer num){
        // the requested number to convert exceed the symbolic representation
        if(num > 3999 || num < 1)
            throw new RuntimeException("The input is not valid!");
        int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanLetters = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        StringBuilder roman = new StringBuilder();
        for(int i=0;i<values.length;i++)
        {
            while(num >= values[i])
            {
                num = num - values[i];
                roman.append(romanLetters[i]);
            }
        }
        return roman.toString();
    }

    //function to find celebrity in a specific given year
    public String findCelebrityByYear(String value)  {
        boolean numberIsValid = false;
        Integer year = 0;
        String celebrities = null;
        try{
            year = Integer.parseInt(value);
            if(year < 1)
                throw new RuntimeException("Year can't be negative");
            numberIsValid = true;
        }catch(NumberFormatException e){
            // Could be a roman number
        }
        if(!numberIsValid) {
            try {
                year = convertRomanToInt(value);
            }catch (Exception e){
                throw new RuntimeException("Year is not valid");
            }
        }

        CloseableHttpClient client = HttpClients.custom().evictIdleConnections(40, TimeUnit.SECONDS).build();

        HttpUriRequest request = RequestBuilder.post()
                .setUri("https://www.famousbirthdays.com/year/"+year+".html")
                .setHeader(HttpHeaders.CONTENT_TYPE,"application/json")
                .build();

        try {
            HttpResponse response = client.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());
            celebrities = responseString.substring(responseString.indexOf("Celebrities born"),responseString.indexOf("more")+4).replace("including","are");
            if(celebrities.length()==0)
                return("No celebrities found from the datasource in "+year+"!");

        } catch (Exception e) {
            return("No celebrities found from the datasource in "+year+"!");
        }
        return celebrities + "\n\nCredits to https://www.famousbirthdays.com/";
    }

    public void func(){

    }



    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }




}
