package com.example.nfc_parking1_project.helper;

public class ConvertTextLicense {
    public static String convertText(String text)
    {
        StringBuilder result = new StringBuilder();
        for(int i =0;i<text.length();i++)
        {
            int asci = (int) text.charAt(i);
            if( (asci>47 && asci<58)|| (asci>64 && asci <91) ) {
                if(result.length()==2)
                {
                    if (asci>64 && asci <91){
                        result.append(text.charAt(i));
                        continue;
                    }else
                        break;
                }else
                {
                    if(result.length()<4)
                    result.append(text.charAt(i));
                }


                if (result.length() == 4) {
                    result.append("-");
                }
                else if (result.length() >= 5)
                {
                    if(asci>47 && asci<58)
                    {
                        result.append(text.charAt(i));
                    }
                    else break;

                }



            }
        }

        return result.toString();
    }
}
