package org.example;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



@org.springframework.web.bind.annotation.RestController
public class RestController {

    @PostMapping(value = "romantoint")
    public @ResponseBody
    ResponseEntity<String> runRomanToInt(@RequestBody(required = true) String valueToConvert){

        return new ResponseEntity<>("The corresponding value is: "+ new App().convertRomanToInt(valueToConvert), HttpStatus.OK);
    }

    @PostMapping(value = "inttoroman")
    public @ResponseBody
    ResponseEntity<String> runIntToRoman(@RequestBody(required = true) Integer valueToConvert){

        return new ResponseEntity<>("The corresponding value is: "+ new App().convertIntToRoman(valueToConvert), HttpStatus.OK);
    }

    @GetMapping(value = "findCelebrityByYear")
    public @ResponseBody
    ResponseEntity<String> runFindCelebrity(@RequestBody(required = true) String value){

        return new ResponseEntity<>(""+ new App().findCelebrityByYear(value), HttpStatus.OK);
    }

}
