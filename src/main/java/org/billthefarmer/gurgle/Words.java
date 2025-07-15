////////////////////////////////////////////////////////////////////////////////
//
//  Gurgle - An android word game.
//
//  Copyright (C) 2022	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.gurgle;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class Words
{
    // TAG
    public static final String TAG = "Words";

    public static final String WORDS_FILE       = "Words.txt";
    public static final String ENGLISH_FILE     = "English.txt";
    public static final String ITALIAN_FILE     = "Italian.txt";
    public static final String ITALIAN_LONG     = "Italian-long.txt";
    public static final String SPANISH_FILE     = "Spanish.txt";
    public static final String SPANISH_LONG     = "Spanish-long.txt";
    public static final String CATALAN_FILE     = "Catalan.txt";
    public static final String FRENCH_FILE      = "French.txt";
    public static final String FRENCH_LONG      = "French-long.txt";
    public static final String PORTUGUESE_FILE  = "Portuguese.txt";
    public static final String PORTUGUESE_LONG  = "Portuguese-long.txt";
    public static final String GERMAN_FILE      = "German.txt";
    public static final String GERMAN_LONG      = "German-long.txt";
    public static final String DUTCH_FILE       = "Dutch.txt";
    public static final String AFRIKAANS_FILE   = "Afrikaans.txt";
    public static final String HUNGARIAN_FILE   = "Hungarian.txt";
    public static final String GREEK_FILE       = "Greek.txt";
    public static final String GREEK_LONG       = "Greek-long.txt";

    private static final int MAX_USED = 256;

    private static Random random;
    private static Deque<String> used;
    private static List<String> guess;
    private static Set<String> words;

    private Words() {}

    // getWord
    public static String getWord()
    {
        String word;

        if (random == null)
            random = new Random(new Date().getTime());

        for (word = guess.get(random.nextInt(guess.size()));
             hasBeenUsed(word); );

        return word.toUpperCase(Locale.getDefault());
    }

    // hasBeenUsed
    private static boolean hasBeenUsed(String word)
    {
        if (used == null)
            used = new ArrayDeque<String>();

        while (used.size() > MAX_USED)
            used.remove();

        if (used.contains(word))
            return true;

        used.add(word);
        return false;
    }

    public static void setLanguage(Context context, int l)
    {
        guess = new ArrayList<String>();

        switch (l)
        {
        case Gurgle.ENGLISH:
            readWords(context, WORDS_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, ENGLISH_FILE, words);
            break;

        case Gurgle.ITALIAN:
            readWords(context, ITALIAN_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, ITALIAN_LONG, words);
            break;

        case Gurgle.SPANISH:
            readWords(context, SPANISH_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, SPANISH_LONG, words);
            break;

        case Gurgle.CATALAN:
            readWords(context, CATALAN_FILE, guess);
            words = new HashSet<String>(guess);
            break;

        case Gurgle.FRENCH:
            readWords(context, FRENCH_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, FRENCH_LONG, words);
            break;

        case Gurgle.PORTUGUESE:
            readWords(context, PORTUGUESE_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, PORTUGUESE_LONG, words);
            break;

        case Gurgle.GERMAN:
            readWords(context, GERMAN_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, GERMAN_LONG, words);
            break;

        case Gurgle.DUTCH:
            readWords(context, DUTCH_FILE, guess);
            words = new HashSet<String>(guess);
            break;

        case Gurgle.AFRIKAANS:
            readWords(context, AFRIKAANS_FILE, guess);
            words = new HashSet<String>(guess);
            break;

        case Gurgle.HUNGARIAN:
            readWords(context, HUNGARIAN_FILE, guess);
            words = new HashSet<String>(guess);
            break;
        case Gurgle.GREEK:
            readWords(context, GREEK_FILE, guess);
            words = new HashSet<String>(guess);
            readWords(context, GREEK_LONG, words);
            break;
        }
    }

    // readWords
    private static void readWords(Context context, String file,
                                  Collection<String> collection)
    {
        try (BufferedReader reader = new BufferedReader
             (new InputStreamReader(context.getAssets().open(file))))
        {
            String line;
            while ((line = reader.readLine()) != null)
                collection.add(line);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // getCode
    public static String getCode()
    {
        long seed = new Date().getTime();

        random = new Random(seed);
        if (used != null)
            used.clear();

        byte bytes[] = String.valueOf(seed).getBytes();
        String code = Base64.encodeToString(bytes, Base64.DEFAULT);

        if (BuildConfig.DEBUG)
            Log.d(TAG, String.format(Locale.getDefault(),
                                     "%d, %s", seed, code));
        return code;
    }

    // setCode
    public static boolean setCode(String code)
    {
        try
        {
            byte bytes[] = Base64.decode(code, Base64.DEFAULT);
            long seed = Long.valueOf(new String(bytes));
            random = new Random(seed);

            if (BuildConfig.DEBUG)
                Log.d(TAG, String.format(Locale.getDefault(),
                                         "%d, %s", seed, code));
        }

        catch (Exception e)
        {
            return false;
        }

        if (used != null)
            used.clear();

        return true;
    }

    // isWord
    public static boolean isWord(String word)
    {
        return words.contains(word.toLowerCase(Locale.getDefault()));
    }
}
