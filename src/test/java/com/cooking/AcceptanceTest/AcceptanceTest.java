package com.cooking.AcceptanceTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "features",monochrome = true, snippets = SnippetType.CAMELCASE, 
        glue = {"com.cooking.AcceptanceTest"})

public class AcceptanceTest {
	

}
