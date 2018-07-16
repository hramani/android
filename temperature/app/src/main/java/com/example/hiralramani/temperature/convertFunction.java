package com.example.hiralramani.temperature;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by hiral ramani on 2/3/2017.
 */

public class convertFunction {

    public static float ctof(float number)
    {


        return (float) ((number*9.0/5.0)+32.0);

    }

    public static float ftoc(float number)
    {
        DecimalFormat df = new DecimalFormat("#.#");

        df.setRoundingMode(RoundingMode.CEILING);
        float f = (float) ((number-32.0)*5.0/9.0);
        return Float.parseFloat(df.format(f));

    }

}
