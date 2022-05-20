package com.example.dietapp;

import android.content.Context;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Diet {
    String name;
    int durationInDays;
    int numOfMeals;
    int litresOfWater;
    String sportsActivity;
    String[][][] entireSchld;
    String[][][] valueOfFood;
    String[] advices;
    String description;

    public Diet(String name, int durationInDays, int numOfMeals, int litresOfWater, String sportsActivity, String[][][] entireSchld, String[][][] valueOfFood, String[] advices, String description) {
        this.name = name;
        this.durationInDays = durationInDays;
        this.numOfMeals = numOfMeals;
        this.litresOfWater = litresOfWater;
        this.sportsActivity = sportsActivity;
        this.entireSchld = entireSchld;
        this.valueOfFood = valueOfFood;
        this.advices = advices;
        this.description = description;
    }

    public static Diet[] createListOfDiets(Context context) {
        Diet[] listOfDiets = new Diet[valOfDiets(toArrayOfStrings(context))];
        String[] dietList = toArrayOfStrings(context);
        String name;
        int durationInDays;
        int numOfMeals;
        int litresOfWater;
        String sportsActivity;
        String[][][] entireSchld;
        String[][][] valueOfFood;
        String[] advices;
        String description;
        for (int i = 0; i < listOfDiets.length; i++) {
            name = readLineWFS(dietList[findDietByID(dietList, i + 1)]);
            durationInDays = Integer.parseInt(dietList[findDietByID(dietList, i + 1) + 1]);
            numOfMeals = Integer.parseInt(dietList[findDietByID(dietList, i + 1) + 2]);
            litresOfWater = Integer.parseInt(dietList[findDietByID(dietList, i + 1) + 3]);
            sportsActivity = dietList[findDietByID(dietList, i + 1) + 4];
            entireSchld = makeSchld(dietList, i + 1, numOfMeals, durationInDays);
            valueOfFood = makeValue(dietList, i + 1, numOfMeals, durationInDays);
            advices = makeAdvices(dietList, i + 1);
            description = makeDesc(dietList, i + 1);
            listOfDiets[i] = new Diet(name, durationInDays, numOfMeals, litresOfWater, sportsActivity, entireSchld, valueOfFood, advices, description);
        }
        return listOfDiets;
    }

    public static String[] getNames(Diet[] d) {
        String[] names = new String[d.length];
        for (int i = 0; i < d.length; i++) {
            names[i] = d[i].getName();
        }
        return names;
    }

    static String[] makeAdvices(String[] diets, int idOfDiet) {
        String[] advs = new String[valOfAdv(diets, idOfDiet)];
        int l = 0;
        boolean con = false;
        int start = findDietByID(diets, idOfDiet) + 1;
        if (advs.length != 0) {
            for (int i = start; i < diets.length; i++) {
                if (diets[i].charAt(0) == '[') {
                    break;
                } else if (diets[i].charAt(0) == '@' && diets[i].length() > 1) {
                    advs[l] = readLineWFS(diets[i]);
                    con = true;
                    l++;
                } else if (con) {
                    advs[l] = diets[i];
                    l++;
                }
            }
        } else {
            return new String[]{" "};
        }
        return advs;
    }

    static String makeDesc(String[] diets, int idOfDiet) {
        int start = findDietByID(diets, idOfDiet) + 1;
        for (int i = start; i < diets.length; i++) {
            if (diets[i].charAt(0) == '[') {
                return readLineWFS(diets[i]);
            }
        }
        return " ";
    }

    static int valOfAdv(String[] diets, int idOfDiet) {
        int start = findDietByID(diets, idOfDiet) + 1;
        int count = 0;
        boolean con = false;
        for (int i = start; i < diets.length; i++) {
            if (diets[i].charAt(0) == '[') {
                return count;
            } else if (diets[i].charAt(0) == '@' && diets[i].length() > 1) {
                con = true;
                count++;
            } else if (con) {
                count++;
            }
        }
        return count;
    }

    static String[][][] makeSchld(String[] diets, int idOfDiet, int num, int dur) {
        int start = findDietByID(diets, idOfDiet) + 5;
        int startOrig = start;
        String[][][] schld = new String[dur][num][6];
        for (int i = 0; i < dur; i++) {
            for (int j = 0; j < num; j++) {
                String[] set = reformatAStringToSchld(diets[start]).split(" ");
                for (int k = 0; k < set.length; k++) {
                    schld[i][j][k] = delUL(set[k]);
                }
                if (diets[start + 1].charAt(0) == '@') {
                    start = startOrig;
                } else {
                    start++;
                }
            }

        }
        return schld;
    }

    static String[][][] makeValue(String[] diets, int idOfDiet, int num, int dur) {
        int start = findDietByID(diets, idOfDiet) + 5;
        int startOrig = start;
        String[][][] value = new String[dur][num][6];
        for (int i = 0; i < dur; i++) {
            for (int j = 0; j < num; j++) {
                String[] set = reformatAStringToVal(diets[start]).split(" ");
                for (int k = 0; k < set.length; k++) {
                    value[i][j][k] = delUL(set[k]);
                }
                if (diets[start + 1].charAt(0) == '@') {
                    start = startOrig;
                } else {
                    start++;
                }
            }

        }
        return value;
    }

    static String delUL(String str) {
        char[] orig = str.toCharArray();
        for (int i = 0; i < orig.length; i++) {
            if (orig[i] == '_') {
                orig[i] = ' ';
            }
        }
        StringBuilder strBuilder = new StringBuilder();
        for (char c : orig) {
            strBuilder.append(c);
        }
        str = strBuilder.toString();
        return str;
    }

    static String reformatAStringToSchld(String orig) {
        if (orig.charAt(0) == '!') {
            orig = readLineWFS(orig);
        }
        String[] arr = orig.split(" ");
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 0) {
                res.append(" ").append(arr[i]);
            }
        }
        return readLineWFS(res.toString());
    }

    static String reformatAStringToVal(String orig) {
        if (orig.charAt(0) == '!') {
            orig = readLineWFS(orig);
        }
        String[] arr = orig.split(" ");
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 1 && Objects.equals(arr[i], "bos")) {
                res.append("_до_наступления_сытости");
            } else if (i % 2 == 1) {
                res.append(" ").append(arr[i]);
            }
        }
        return readLineWFS(res.toString());
    }

    static int findDietByID(String[] diets, int id) {
        if (id == 1) {
            return 0;
        }
        int c = 0;
        for (int i = 0; i < diets.length; i++) {
            if (diets[i].charAt(0) == '#') {
                c++;
            }
            if (c == id) {
                return i;
            }
        }
        return 0;
    }


    static String[] toArrayOfStrings(Context context) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("ListOfDiets"), StandardCharsets.UTF_8));
            ArrayList<String> strslist = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                strslist.add(line);
            }
            reader.close();
            String[] strs = new String[strslist.size()];
            for (int i = 0; i < strslist.size(); i++) {
                strs[i] = strslist.get(i);
            }
            return strs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    static int valOfDiets(String[] str) {
        int c = 0;
        for (String s : str) {
            if (s.charAt(0) == '#') {
                c++;
            }
        }
        return c;
    }

    static String readLineWFS(String s0) {
        char[] s = s0.toCharArray();
        StringBuilder sf = new StringBuilder();
        for (int i = 1; i < s.length; i++) {
            sf.append(s[i]);
        }
        return sf.toString();
    }


    public static void saveDiet(String idOfDiet, Context context) {
        Diet[] diets = createListOfDiets(context);
        Diet diet = null;
        for (Diet value : diets) {
            if (Objects.equals(value.getName(), idOfDiet)) {
                diet = value;
                break;
            }
        }
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)));
            // пишем данные
            assert diet != null;
            bw.flush();
            bw.write(diet.getName() + "\n" + getNormalDate());
            // закрываем поток
            bw.close();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    private final static String FILE_NAME = "CurrentDiet";


    static public String openText(Context context) {
        FileInputStream fin = null;
        try {
            fin = context.openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            if (!fin.toString().equals("")) {
                String res = "";
                res += new String(bytes);
                return res;
            } else {
                return "";
            }
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }

    public static boolean checkSaves(Context context) {
        return !openText(context).equals("");
    }

    public static String getNextFoodTime(String name, Context context) {
        int num = 0;
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(name, names[i])) {
                System.out.println(arr[i].getDurationInDays());
                num = arr[i].getNumOfMeals();
            }
        }
        int[] time = new int[]{Integer.parseInt(getNormalDate().split(" ")[3]), Integer.parseInt(getNormalDate().split(" ")[4])};
        if (time[0] < 8) {
            return rightDecl(8 - time[0], "час");
        } else if (time[0] == 8 && time[1] <= 20) {
            return "Сейчас";
        } else if (time[0] < 10 && num == 5) {
            return rightDecl(10 - time[0], "час");
        } else if (time[0] == 10 && time[1] <= 20 && num == 5) {
            return "Сейчас";
        } else if (time[0] < 13) {
            return rightDecl(13 - time[0], "час");
        } else if (time[0] == 13 && time[1] <= 20) {
            return "Сейчас";
        } else if (time[0] < 16 && (num == 5 || num == 4)) {
            return rightDecl(16 - time[0], "час");
        } else if (time[0] == 16 && time[1] <= 20 && (num == 5 || num == 4)) {
            return "Сейчас";
        } else if (time[0] < 18) {
            return rightDecl(18 - time[0], "час");
        } else if (time[0] == 18 && time[1] <= 20) {
            return "Сейчас";
        } else {
            return rightDecl(32 - time[0], "час");
        }
    }

    public static String getUntilItEnds(String dateStart0, int lengthDiet) {
        String[] dateStart = dateStart0.split(" ");
//        Calendar cal = new GregorianCalendar(Integer.parseInt(dateStart[0]), Integer.parseInt(dateStart[1]), Integer.parseInt(dateStart[2]), Integer.parseInt(dateStart[3]), Integer.parseInt(dateStart[4]));
//        cal.add(Calendar.DAY_OF_MONTH, lengthDiet);
        String[] dateEnd = plusDate(dateStart, lengthDiet);
//        dateEnd[1] = Integer.toString(Integer.parseInt(dateEnd[1]) - 1);
        String[] today = getNormalDate().split(" ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatTheDataRight(dateEnd);
        formatTheDataRight(today);
        LocalDate d1 = LocalDate.parse(dateEnd[0] + "-" + dateEnd[1] + "-" + dateEnd[2], dtf);
        LocalDate d2 = LocalDate.parse(today[0] + "-" + today[1] + "-" + today[2], dtf);
        Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
        int diffDays = (int) diff.toDays();
        if (diffDays == 0 && Objects.equals(dateStart[3], dateEnd[3])) {
            return rightDecl(Integer.parseInt(dateStart[4]) - Integer.parseInt(dateEnd[4]), "минута");
        } else if (diffDays == 0) {
            return rightDecl(Integer.parseInt(dateStart[3]) - Integer.parseInt(dateEnd[3]), "час");
        } else if (diffDays < 0) {
            return "Завершилось\nвыберите другую диету";
        }
        return rightDecl((diffDays), "день");
    }

    static String rightDecl(int num, String word) {
        boolean b = (num % 10 == 2 && num % 100 != 12) || (num % 10 == 3 && num % 100 != 13) || (num % 10 == 4 && num % 100 != 14);
        boolean a = num % 10 == 1 && num % 100 != 11;
        if (Objects.equals(word, "минута")) {
            if (a) {
                return num + " минута";
            } else if (b) {
                return num + " минуты";
            } else {
                return num + " минут";
            }
        } else if (Objects.equals(word, "час")) {
            if (a) {
                return num + " час";
            } else if (b) {
                return num + " часа";
            } else {
                return num + " часов";
            }
        } else {
            if (a) {
                return num + " день";
            } else if (b) {
                return num + " дня";
            } else {
                return num + " дней";
            }
        }
    }

    static void formatTheDataRight(String[] orig) {
        if (orig[1].length() == 1) {
            orig[1] = "0" + orig[1];
        }
        if (orig[2].length() == 1) {
            orig[2] = "0" + orig[2];
        }
    }

    public static int getLength(String name, Context context) {
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(name, names[i])) {
                return arr[i].getDurationInDays();
            }
        }
        return 0;
    }

    static String getNormalDate() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR) + " " + (calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE);
    }

    static String getNormalDate(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + " " + (calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE);
    }

    public static String getLitres(String name, Context context) {
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(name, names[i])) {
                return arr[i].getLitresOfWater() + " л.";
            }
        }
        return "2 л.";
    }

    public static String getSports(String name, Context context) {
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(name, names[i])) {
                return arr[i].getSportsActivity();
            }
        }
        return "- - -";
    }

    public static String getDesc(String name, Context context) {
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(name, names[i])) {
                return arr[i].getDescription();
            }
        }
        return "- - -";
    }

    static boolean isOver(String dateEnd1) {
        String[] dateEnd = dateEnd1.split(" ");
        String today1 = getNormalDate();
        String[] today = today1.split(" ");
        int yearT = Integer.parseInt(today[0]);
        int yearE = Integer.parseInt(dateEnd[0]);
        int monthT = Integer.parseInt(today[1]);
        int monthE = Integer.parseInt(dateEnd[1]);
        int dayT = Integer.parseInt(today[2]);
        int dayE = Integer.parseInt(dateEnd[2]);
        int hourT = Integer.parseInt(today[3]);
        int hourE = Integer.parseInt(dateEnd[3]);
        int minT = Integer.parseInt(today[4]);
        int minE = Integer.parseInt(dateEnd[4]);
        if (yearT > yearE) {
            return true;
        } else if (monthT > monthE) {
            return true;
        } else if (dayT > dayE) {
            return true;
        } else if (hourT > hourE) {
            return true;
        } else return minT > minE;
    }

    static String[] plusDate(String[] date, int dif) {
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]) + dif;
        int maxDay = howDays(month, year);
        while (day > maxDay) {
            day -= maxDay;
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
            maxDay = howDays(month, year);
        }
        return new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)};
    }

    static int howDays(int mon, int year) {
        boolean isVis = (year % 4 == 0 || year % 400 == 0) && year % 100 != 0;
        switch (mon) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (isVis) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return 30;
    }

    public static String[] getSchldAndValue(String dateStart0, int lengthDiet, String idOfDiet, Context context) {
        String[] dateStart = dateStart0.split(" ");
//        Calendar cal = new GregorianCalendar(Integer.parseInt(dateStart[0]), Integer.parseInt(dateStart[1]), Integer.parseInt(dateStart[2]), Integer.parseInt(dateStart[3]), Integer.parseInt(dateStart[4]));
//        cal.add(Calendar.DAY_OF_YEAR, lengthDiet);
        String[] dateEnd = plusDate(dateStart, lengthDiet);
//        dateEnd[1] = Integer.toString(Integer.parseInt(dateEnd[1]) - 1);
        String[] today = getNormalDate().split(" ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatTheDataRight(dateEnd);
        formatTheDataRight(today);
        LocalDate d1 = LocalDate.parse(dateEnd[0] + "-" + dateEnd[1] + "-" + dateEnd[2], dtf);
        LocalDate d2 = LocalDate.parse(today[0] + "-" + today[1] + "-" + today[2], dtf);
        Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
        int diffDays = (int) diff.toDays();
        diffDays = lengthDiet - diffDays;
        int num = 0;
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        String[][][] schld = null;
        String[][][] value = null;
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(idOfDiet, names[i])) {
                System.out.println(arr[i].getDurationInDays());
                num = arr[i].getNumOfMeals();
                schld = arr[i].getEntireSchld();
                value = arr[i].getValueOfFood();
                break;
            }
        }
        int[] time = new int[]{Integer.parseInt(getNormalDate().split(" ")[3]), Integer.parseInt(getNormalDate().split(" ")[4])};
        if (time[0] < 8) {
            num = 1;
        } else if (time[0] == 8 && time[1] <= 20) {
            num = 1;
        } else if (time[0] < 10 && num == 5) {
            num = 2;
        } else if (time[0] == 10 && time[1] <= 20 && num == 5) {
            num = 2;
        } else if (time[0] < 13) {
            if (num == 5) {
                num = 3;
            } else {
                num = 2;
            }
        } else if (time[0] == 13 && time[1] <= 20) {
            if (num == 5) {
                num = 3;
            } else {
                num = 2;
            }
        } else if (time[0] < 16 && (num == 5 || num == 4)) {
            if (num == 4) {
                num = 3;
            } else {
                num = 4;
            }
        } else if (time[0] == 16 && time[1] <= 20 && (num == 5 || num == 4)) {
            if (num == 4) {
                num = 3;
            } else {
                num = 4;
            }
        } else {
            num = 1;
            diffDays++;
        }
        String[] schldAndVal0 = new String[6];
        String[] schldAndVal1 = new String[6];
        assert schld != null;
        if (diffDays >= lengthDiet) {
            diffDays = lengthDiet - 1;
        } else if (diffDays < 0) {
            diffDays = 0;
        }
        System.arraycopy(schld[diffDays][num], 0, schldAndVal0, 0, Objects.requireNonNull(schld)[diffDays][num].length);
        for (int i = 0; i < schld[diffDays][num].length; i++) {
            assert value != null;
            schldAndVal1[i] = value[diffDays][num][i];
        }
        return glueTheArrays(schldAndVal0, schldAndVal1);
    }

    static String[] glueTheArrays(String[] ar1, String[] ar2) {
        String[] res = new String[12];
        res[0] = ar1[0];
        res[1] = ar2[0];
        if (ar1[1] != null) {
            res[2] = ar1[1];
            res[3] = ar2[1];
        }
        if (ar1[2] != null) {
            res[4] = ar1[2];
            res[5] = ar2[2];
        }
        if (ar1[3] != null) {
            res[6] = ar1[3];
            res[7] = ar2[3];
        }
        if (ar1[4] != null) {
            res[8] = ar1[4];
            res[9] = ar2[4];
        }
        if (ar1[5] != null) {
            res[10] = ar1[5];
            res[11] = ar2[5];
        }
        return res;
    }

    public static String[] getAdv(String name, Context context) {
        Diet[] arr = createListOfDiets(context);
        String[] names = getNames(arr);
        for (int i = 0; i < names.length; i++) {
            if (Objects.equals(name, names[i])) {
                return arr[i].getAdvices();
            }
        }
        return new String[]{""};
    }

    public String getName() {
        return name;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public int getNumOfMeals() {
        return numOfMeals;
    }

    public int getLitresOfWater() {
        return litresOfWater;
    }

    public String getSportsActivity() {
        return sportsActivity;
    }

    public String[][][] getEntireSchld() {
        return entireSchld;
    }

    public String[][][] getValueOfFood() {
        return valueOfFood;
    }

    public String[] getAdvices() {
        return advices;
    }

    public String getDescription() {
        return description;
    }
}
