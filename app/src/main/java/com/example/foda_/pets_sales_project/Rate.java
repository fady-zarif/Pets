package com.example.foda_.pets_sales_project;

/**
 * Created by foda_ on 2016-07-21.
 */
public class Rate {
    public String RatedPerson;
    public String SubmitPerson;
    public int Rate;
    public static  String FinalRate;
    public  static int value;

    public Rate()
    {}
    public Rate(String RatedPerson, String SubmitPerson, int Rate)
    {
        this.Rate=Rate;
        this.RatedPerson=RatedPerson;
        this.SubmitPerson=SubmitPerson;
    }

}
