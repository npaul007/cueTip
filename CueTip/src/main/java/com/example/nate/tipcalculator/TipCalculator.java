package com.example.nate.tipcalculator;

public class TipCalculator {
    public double getTip(int percentage, double bill){
        return ((double) percentage/100) * bill;
    }

    public double getTotalToPay(int percentage, double bill){
        return this.getTip(percentage,bill) + bill;
    }

    public double getPerPerson(int people, double bill){
        if(people > 0){
            return bill / (double) people;
        }else{
            return 0.00;
        }
    }

    public double getTipPerPerson(int people, double tip){
        if(people > 0){
            return tip / (double) people;
        }else{
            return 0.00;
        }
    }


}
