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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.text.DateFormat;
import java.text.Normalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;

// Gurgle class
@SuppressWarnings("deprecation")
public class Gurgle extends Activity
{
    public static final String TAG = "Gurgle";
    public static final String ROW = "row";
    public static final String KEYS = "keys";
    public static final String DATA = "data";
    public static final String WORD = "word";
    public static final String LANG = "lang";
    public static final String SOLVED = "solved";
    public static final String LETTER = "letter";
    public static final String LOCKED = "locked";
    public static final String LETTERS = "letters";
    public static final String COLOURS = "colours";
    public static final String KEY_COLOURS = "keyColours";
    public static final String GURGLE_IMAGE = "Gurgle.png";
    public static final String CODE_IMAGE = "Code.png";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPG = "image/jpg";
    public static final String IMAGE_WILD = "image/*";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String PREF_THEME = "pref_theme";
    public static final String PREF_WRONG = "pref_wrong";
    public static final String PREF_CONF = "pref_conf";
    public static final String PREF_CONT = "pref_cont";
    public static final String PREF_CORR = "pref_corr";
    public static final String PREF_FARE = "pref_fare";
    public static final String PREF_LANG = "pref_lang";
    public static final String PREF_SWAP = "pref_swap";

    public static final String FILE_PROVIDER =
        "org.billthefarmer.gurgle.fileprovider";

    public static final String A_ACCENTS[] = {"A", "À", "Á", "Â"};
    public static final String C_ACCENTS[] = {"C", "Ç"};
    public static final String E_ACCENTS[] = {"E", "È", "É", "Ê"};
    public static final String I_ACCENTS[] = {"I", "Ì", "Í", "Î"};
    public static final String N_ACCENTS[] = {"N", "Ñ"};
    public static final String O_ACCENTS[] = {"O", "Ò", "Ó", "Ô", "Ö", "Ő"};
    public static final String U_ACCENTS[] = {"U", "Ù", "Ú", "Û", "Ü", "Ű"};

    public static final int GREY    = 0;
    public static final int DARK    = 1;
    public static final int BLUE    = 2;
    public static final int CYAN    = 3;
    public static final int GREEN   = 4;
    public static final int MAGENTA = 5;
    public static final int ORANGE  = 6;
    public static final int PURPLE  = 7;
    public static final int RED     = 8;
    public static final int YELLOW  = 9;
    public static final int BLACK   = 10;
    public static final int WHITE   = 11;
    public static final int LIGHT   = 12;

    public static final int REQUEST_IMAGE = 1;

    public static final int ENGLISH    = 0;
    public static final int ITALIAN    = 1;
    public static final int SPANISH    = 2;
    public static final int CATALAN    = 3;
    public static final int FRENCH     = 4;
    public static final int PORTUGUESE = 5;
    public static final int GERMAN     = 6;
    public static final int DUTCH      = 7;
    public static final int AFRIKAANS  = 8;

    public static final int HUNGARIAN  = 9;

    public static final int SIZE = 5;
    public static final int ROWS = 6;

    public static final int VERSION_CODE_S_V2 = 32;

    public static final int BITMAP_SCALE = 8;
    public static final int SWAP_DELAY = 100;

    private MediaPlayer mediaPlayer;

    private ActionMode.Callback actionModeCallback;
    private Map<String, TextView> keyboard;
    private KonfettiView konfettiView;
    private ActionMode actionMode;
    private TextView display[][];
    private TextView actionView;
    private Toast toast;
    private String word;
    private Party party;

    private boolean confetti;
    private boolean fanfare;
    private boolean locked[];
    private boolean solved;
    private boolean swap;

    private int language;
    private int contains;
    private int correct;
    private int letter;
    private int theme;
    private int wrong;
    private int row;

    // On create
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        confetti = preferences.getBoolean(PREF_CONF, true);
        contains = preferences.getInt(PREF_CONT, getColour(YELLOW));
        correct = preferences.getInt(PREF_CORR, getColour(GREEN));
        fanfare = preferences.getBoolean(PREF_FARE, true);
        language = preferences.getInt(PREF_LANG, ENGLISH);
        swap = preferences.getBoolean(PREF_SWAP, false);
        theme = preferences.getInt(PREF_THEME, DARK);
        wrong = preferences.getInt(PREF_WRONG, getColour(GREY));

        switch (theme)
        {
        default:
        case DARK:
            setTheme(R.style.AppTheme);
            break;

        case CYAN:
            setTheme(R.style.AppCyanTheme);
            break;

        case BLUE:
            setTheme(R.style.AppBlueTheme);
            break;

        case ORANGE:
            setTheme(R.style.AppOrangeTheme);
            break;

        case PURPLE:
            setTheme(R.style.AppPurpleTheme);
            break;

        case RED:
            setTheme(R.style.AppRedTheme);
            break;

        case BLACK:
            setTheme(R.style.AppBlackTheme);
            break;

        case LIGHT:
            setTheme(R.style.AppLightTheme);
            break;
        }

        setContentView(R.layout.main);

        setLanguage();

        keyboard = new HashMap<String, TextView>();
        ViewGroup rows = findViewById(R.id.keyboard);
        for (int r = 0; r < rows.getChildCount(); r++)
        {
            ViewGroup row = (ViewGroup) rows.getChildAt(r);
            for (int i = 0; i < row.getChildCount(); i++)
            {
                View view = row.getChildAt(i);
                if (view instanceof TextView)
                {
                    view.setOnClickListener((v) -> keyClicked(v));
                    keyboard.put(((TextView) view).getText().toString(),
                                 (TextView) view);
                }
            }
        }

        ImageView view = findViewById(R.id.enter);
        view.setImageResource(swap? R.drawable.ic_backspace:
                                    R.drawable.ic_enter);
        view.setOnClickListener(swap? (v) -> backspaceClicked(v):
                                      (v) -> enterClicked(v));
        view.setOnLongClickListener((v) -> swap(v));
        view = findViewById(R.id.back);
        view.setImageResource(swap? R.drawable.ic_enter:
                                    R.drawable.ic_backspace);
        view.setOnClickListener(swap? (v) -> enterClicked(v):
                                      (v) -> backspaceClicked(v));
        view.setOnLongClickListener((v) -> swap(v));

        display = new TextView[ROWS][];
        for (int i = 0; i < display.length; i++)
            display[i] = new TextView[SIZE];

        ViewGroup grid = findViewById(R.id.puzzle);
        for (int i = 0; i < grid.getChildCount(); i++)
        {
            display[i / SIZE][i % SIZE] = (TextView) grid.getChildAt(i);
            display[i / SIZE][i % SIZE].setOnClickListener((v) -> search(v));
            display[i / SIZE][i % SIZE].setOnLongClickListener((v) -> lock(v));
            registerForContextMenu(display[i / SIZE][i % SIZE]);
        }

        View layout = findViewById(R.id.layout);
        layout.setOnClickListener((v) ->
        {
            if (actionMode != null)
                actionMode.finish();
        });

        // Delay resizing
        grid.postDelayed(() ->
        {
            float scaleX = (float) layout.getWidth() / grid.getWidth();
            float scaleY = (float) (layout.getHeight() -
                                    rows.getHeight()) / grid.getHeight();
            float scale = Math.min(scaleX, scaleY);
            for (int i = 0; i < grid.getChildCount(); i++)
            {
                TextView v = (TextView) grid.getChildAt(i);
                v.setMinimumWidth(Math.round(v.getMinimumWidth() * scale));
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                              v.getTextSize() * scale);
            }
            scale = (float) layout.getWidth() / rows.getWidth();
            for (int r = 0; r < rows.getChildCount(); r++)
            {
                ViewGroup row = (ViewGroup) rows.getChildAt(r);
                for (int i = 0; i < row.getChildCount(); i++)
                {
                    View v = row.getChildAt(i);
                    if (v instanceof TextView)
                    {
                        v.setMinimumWidth
                            (Math.round(v.getMinimumWidth() * scale));
                        ((TextView) v).setTextSize
                            (TypedValue.COMPLEX_UNIT_PX,
                             ((TextView) v).getTextSize() * scale);
                    }
                }
            }
        }, SWAP_DELAY);

        konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new
            Emitter(5, TimeUnit.SECONDS).perSecond(50);
        party = new PartyFactory(emitterConfig)
            .angle(Angle.TOP)
            .spread(Spread.WIDE)
            .setSpeedBetween(10, 30)
            .build();

        actionModeCallback = new ActionMode.Callback()
        {
            // Called when the action mode is created;
            // startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                addAccents(actionView, menu);
                return true;
            }

            // Called each time the action mode is shown. Always
            // called after onCreateActionMode, but may be called
            // multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                actionView.setText(item.getTitle());
                mode.finish(); // Action picked, so close the CAB
                return true;
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode)
            {
                actionMode = null;
            }
        };

        if (savedInstanceState != null)
        {
            row = savedInstanceState.getInt(ROW);
            word = savedInstanceState.getString(WORD);
            letter = savedInstanceState.getInt(LETTER);
            solved = savedInstanceState.getBoolean(SOLVED);
            locked = savedInstanceState.getBooleanArray(LOCKED);

            List<String> letters =
                savedInstanceState.getStringArrayList(LETTERS);
            List<Integer> colours =
                savedInstanceState.getIntegerArrayList(COLOURS);

            for (int i = 0; i < letters.size(); i++)
            {
                TextView text = display[i / SIZE][i % SIZE];
                text.setText(letters.get(i));
                text.setTextColor(colours.get(i));
            }

            List<String> keys =
                savedInstanceState.getStringArrayList(KEYS);
            List<Integer> keyColours =
                savedInstanceState.getIntegerArrayList(KEY_COLOURS);

            for (int i = 0; i < keys.size(); i++)
            {
                TextView key = keyboard.get(keys.get(i));
                key.setTextColor(keyColours.get(i));
            }

            return;
        }

        Intent intent = getIntent();
        if (Intent.ACTION_SEND.contentEquals(intent.getAction()))
        {
            String type = intent.getType();
            if (IMAGE_PNG.contentEquals(type) ||
                IMAGE_JPG.contentEquals(type) ||
                IMAGE_WILD.contentEquals(type))
            {
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (uri != null)
                {
                    try (BufferedInputStream is = new BufferedInputStream
                        (getContentResolver().openInputStream(uri)))
                    {
                        BitmapDrawable drawable =
                            new BitmapDrawable(getResources(), is);
                        Bitmap bitmap = drawable.getBitmap();
                        decodeImage(bitmap);
                    }

                    catch (Exception e) {}
                }
            }

            else if (TEXT_PLAIN.contentEquals(type))
            {
                String code = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (Words.setCode(code))
                    showToast(R.string.newCode);

                else
                    showToast(R.string.notRecognised);
            }
        }

        word = Words.getWord();

        locked = new boolean[SIZE];
        solved = false;
        letter = 0;
        row = 0;
    }

    // onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        row = savedInstanceState.getInt(ROW);
        word = savedInstanceState.getString(WORD);
        letter = savedInstanceState.getInt(LETTER);
        solved = savedInstanceState.getBoolean(SOLVED);
        locked = savedInstanceState.getBooleanArray(LOCKED);

        List<String> letters =
            savedInstanceState.getStringArrayList(LETTERS);
        List<Integer> colours =
            savedInstanceState.getIntegerArrayList(COLOURS);

        for (int i = 0; i < letters.size(); i++)
        {
            TextView text = display[i / SIZE][i % SIZE];
            text.setText(letters.get(i));
            text.setTextColor(colours.get(i));
        }

        List<String> keys =
            savedInstanceState.getStringArrayList(KEYS);
        List<Integer> keyColours =
            savedInstanceState.getIntegerArrayList(KEY_COLOURS);

        for (int i = 0; i < keys.size(); i++)
        {
            TextView key = keyboard.get(keys.get(i));
            key.setTextColor(keyColours.get(i));
        }
    }

    // onPause
    @Override
    @SuppressWarnings("deprecation")
    public void onPause()
    {
        super.onPause();

        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREF_THEME, theme);
        editor.putInt(PREF_WRONG, wrong);
        editor.putInt(PREF_LANG, language);
        editor.putInt(PREF_CONT, contains);
        editor.putInt(PREF_CORR, correct);
        editor.putBoolean(PREF_CONF, confetti);
        editor.putBoolean(PREF_FARE, fanfare);
        editor.putBoolean(PREF_SWAP, swap);
        editor.apply();
    }

    // onSaveInstanceState
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt(ROW, row);
        outState.putString(WORD, word);
        outState.putInt(LETTER, letter);
        outState.putBoolean(SOLVED, solved);
        outState.putBooleanArray(LOCKED, locked);

        ArrayList<String> letterList = new ArrayList<String>();
        ArrayList<Integer> colourList = new ArrayList<Integer>();
        for (TextView a[]: display)
        {
            for (TextView text: a)
            {
                letterList.add(text.getText().toString());
                colourList.add(text.getTextColors().getDefaultColor());
            }
        }

        outState.putStringArrayList(LETTERS, letterList);
        outState.putIntegerArrayList(COLOURS, colourList);

        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<Integer> keyColours  = new ArrayList<Integer>();
        for (String key: keyboard.keySet().toArray(new String[0]))
        {
            keys.add(key);
            keyColours.add(keyboard.get(key).getTextColors().getDefaultColor());
        }

        outState.putStringArrayList(KEYS, keys);
        outState.putIntegerArrayList(KEY_COLOURS, keyColours);
    }

    // On create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it
        // is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    // onPrepareOptionsMenu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.confetti).setChecked(confetti);
        menu.findItem(R.id.fanfare).setChecked(fanfare);

        return true;
    }

    // onCreateContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        // Inflate a menu resource providing context menu items
        addAccents((TextView) v, menu);
    }

    // On options item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Get id
        int id = item.getItemId();
        switch (id)
        {
        case R.id.refresh:
            refresh();
            break;

        case R.id.image:
            shareImage();
            break;

        case R.id.code:
            showCode();
            break;

        case R.id.dark:
            theme(DARK);
            break;

        case R.id.cyan:
            theme(CYAN);
            break;

        case R.id.blue:
            theme(BLUE);
            break;

        case R.id.orange:
            theme(ORANGE);
            break;

        case R.id.purple:
            theme(PURPLE);
            break;

        case R.id.red:
            theme(RED);
            break;

        case R.id.black:
            theme(BLACK);
            break;

        case R.id.light:
            theme(LIGHT);
            break;

        case R.id.english:
            setLanguage(ENGLISH);
            break;

        case R.id.italian:
            setLanguage(ITALIAN);
            break;

        case R.id.spanish:
            setLanguage(SPANISH);
            break;

        case R.id.catalan:
            setLanguage(CATALAN);
            break;

        case R.id.french:
           setLanguage(FRENCH);
           break;

       case R.id.portuguese:
           setLanguage(PORTUGUESE);
           break;

        case R.id.german:
           setLanguage(GERMAN);
           break;

        case R.id.dutch:
           setLanguage(DUTCH);
           break;

        case R.id.afrikaans:
           setLanguage(AFRIKAANS);
           break;

        case R.id.hungarian:
            setLanguage(HUNGARIAN);
            break;

        case R.id.getText:
            getText();
            break;

        case R.id.getImage:
            getImage();
            break;

        case R.id.help:
            help();
            break;

        case R.id.confetti:
            confetti(item);
            break;

        case R.id.fanfare:
            fanfare(item);
            break;

        case R.id.highlight:
            highlight();
            break;

        case R.id.about:
            about();
            break;

        default:
            return false;
        }

        return true;
    }

    // onContextItemSelected
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        actionView.setText(item.getTitle());
        return true;
    }

    // onNewIntent
    @Override
    public void onNewIntent(Intent intent)
    {
        if (Intent.ACTION_SEND.contentEquals(intent.getAction()))
        {
            String type = intent.getType();
            if (IMAGE_PNG.contentEquals(type) ||
                IMAGE_JPG.contentEquals(type) ||
                IMAGE_WILD.contentEquals(type))
            {
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (uri != null)
                {
                    try (BufferedInputStream is = new BufferedInputStream
                        (getContentResolver().openInputStream(uri)))
                    {
                        BitmapDrawable drawable =
                            new BitmapDrawable(getResources(), is);
                        Bitmap bitmap = drawable.getBitmap();
                        if (decodeImage(bitmap) && row == 0 && letter == 0)
                            refresh();
                    }

                    catch (Exception e) {}
                }
            }

            else if (TEXT_PLAIN.contentEquals(type))
            {
                String code = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (Words.setCode(code))
                {
                    showToast(R.string.newCode);
                    if (row == 0 && letter == 0)
                        refresh();
                }

                else
                {
                    showToast(R.string.notRecognised);
                }
            }
        }
    }

    // onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK)
        {
            Bitmap bitmap = data.getParcelableExtra(DATA);
            decodeImage(bitmap);
        }
    }

    // addAccents
    private void addAccents(TextView textView, Menu menu)
    {
        char c = removeAccents(textView.getText()).charAt(0);

        String items[];
        switch (c)
        {
        case 'A':
            items = A_ACCENTS;
            break;

        case 'C':
            items = C_ACCENTS;
            break;

        case 'E':
            items = E_ACCENTS;
            break;

        case 'I':
            items = I_ACCENTS;
            break;

        case 'N':
            items = N_ACCENTS;
            break;

        case 'O':
            items = O_ACCENTS;
            break;

        case 'U':
            items = U_ACCENTS;
            break;

        default:
            menu.add(actionView.getText());
            return;
        }

        for (String item: items)
            menu.add(item);
    }

    // decodeImage
    private boolean decodeImage(Bitmap bitmap)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            showToast(R.string.notRecognised);
            return false;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pixels[] = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new
            RGBLuminanceSource(width, height, pixels);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap image = new BinaryBitmap(binarizer);
        QRCodeReader reader = new QRCodeReader();
        try
        {
            Result result = reader.decode(image);
            String code = result.getText();

            if (BuildConfig.DEBUG)
                Log.d(TAG, "Code " + code);

            if (Words.setCode(code))
            {
                showToast(R.string.newCode);
                return true;
            }

            else
                showToast(R.string.notRecognised);
        }

        catch (Exception e)
        {
            showToast(R.string.notRecognised);
        }

        return false;
    }

    // keyClicked
    public void keyClicked(View v)
    {
        if (actionMode != null)
            actionMode.finish();

        if (solved)
        {
            showToast(R.string.solved);
            return;
        }

        if (letter < SIZE)
        {
            while (letter < SIZE && locked[letter])
                letter++;

            if (letter < SIZE)
            {
                CharSequence s = ((TextView)v).getText();
                display[row][letter++].setText(s);
            }

            else
                showToast(R.string.press);
        }

        else
            showToast(R.string.press);
    }

    // enterClicked
    public void enterClicked(View v)
    {
        StringBuilder guess = new StringBuilder();
        for (TextView t: display[row])
            guess.append(t.getText());

        if (guess.length() != display[row].length)
        {
            showToast(R.string.finish);
            return;
        }

        if (!Words.isWord(guess.toString()))
        {
            showToast(R.string.notListed);
            return;
        }

        if (word.contentEquals(guess))
        {
            if (fanfare)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.fanfare);
                mediaPlayer.start();
            }

            if (confetti)
                konfettiView.start(party);

            showToast(R.string.congratulations, word);
            solved = true;
        }

        StringBuilder test = new StringBuilder(word);
        for (int i = 0; i < display[row].length; i++)
        {
            String testLetter = test.substring(i, i + 1);
            String guessLetter = guess.substring(i, i + 1);
            TextView key = keyboard.get(guessLetter);
            TextView let = display[row][i];

            if (testLetter.contentEquals(guessLetter))
            {
                let.setTextColor(correct);
                key.setTextColor(correct);
                test.replace(i, i + 1, ".");
            }
        }

        for (int i = 0; i < display[row].length; i++)
        {
            String guessLetter = guess.substring(i, i + 1);
            TextView key = keyboard.get(guessLetter);
            TextView let = display[row][i];

            if (test.toString().contains(guessLetter) &&
                let.getTextColors().getDefaultColor() != correct)
            {
                let.setTextColor(contains);
                int index = test.indexOf(guessLetter);
                test.replace(index, index + 1, ".");

                if (key.getTextColors().getDefaultColor() != correct)
                    key.setTextColor(contains);
            }

            else
            {
                if (let.getTextColors().getDefaultColor() != correct &&
                    let.getTextColors().getDefaultColor() != contains)
                    let.setTextColor(wrong);
                if (key.getTextColors().getDefaultColor() != correct &&
                    key.getTextColors().getDefaultColor() != contains)
                    key.setTextColor(wrong);
            }
        }

        letter = 0;
        locked = new boolean[SIZE];

        if (row < ROWS - 1)
            row++;

        else if (!solved)
            scroll();
    }

    // scroll
    private void scroll()
    {
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < display[i].length; j++)
            {
                display[i][j].setText(display[i + 1][j].getText());
                display[i][j].setTextColor
                    (display[i + 1][j].getTextColors().getDefaultColor());
            }
        }

        for (TextView t: display[row])
        {
            t.setText("");
            t.setTextColor(getColour(WHITE));
        }
    }

    // backspaceClicked
    public void backspaceClicked(View v)
    {
        if (letter > 0)
        {
            while (letter > 0 && locked[--letter]);

            if (!locked[letter])
                display[row][letter].setText("");
        }
    }

    // refresh
    private void refresh()
    {
        for (TextView r[]: display)
        {
            for (TextView t: r)
            {
                t.setText("");
                t.setTextColor(getColour(WHITE));
            }
        }

        for (TextView t: keyboard.values().toArray(new TextView[0]))
                t.setTextColor(getColour(WHITE));

        word = Words.getWord();

        locked = new boolean[SIZE];
        solved = false;
        letter = 0;
        row = 0;
    }

    // shareImage
    @SuppressWarnings("deprecation")
    private void shareImage()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title = getString(R.string.appName);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.setType(IMAGE_PNG);

        View root = findViewById(android.R.id.content).getRootView();
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);

        File image = new File(getCacheDir(), GURGLE_IMAGE);
        try (BufferedOutputStream out = new
             BufferedOutputStream(new FileOutputStream(image)))
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        }

        catch (Exception e) {}

        Uri imageUri = FileProvider
            .getUriForFile(this, FILE_PROVIDER, image);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        if (BuildConfig.DEBUG)
            intent.putExtra(Intent.EXTRA_TEXT, word);

        startActivity(Intent.createChooser(intent, null));
    }

    // showCode
    private void showCode()
    {
        String code = Words.getCode();
        codeDialog(code, (dialog, id)->
        {
            switch (id)
            {
            case DialogInterface.BUTTON_POSITIVE:
                shareCode(code);
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                shareQRCode(code);
                break;
            }
        });
    }

    // codeDialog
    private void codeDialog(String code,
                            DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.appName);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setMessage(code);
        builder.setPositiveButton(R.string.code, listener);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setNeutralButton(R.string.qrCode, listener);
        Dialog dialog = builder.show();

        TextView text = (TextView) dialog.findViewById(android.R.id.message);
        if (text != null)
        {
            ViewGroup.MarginLayoutParams layout =
                (ViewGroup.MarginLayoutParams) text.getLayoutParams();
            layout.topMargin += 32;
            text.setLayoutParams(layout);
            text.setGravity(Gravity.CENTER);
            text.setTextIsSelectable(true);
            Drawable drawable = getDrawable(code);
            text.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                                         null, drawable);
        }
    }

    // shareCode
    private void shareCode(String s)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title = getString(R.string.appName);
        String code = getString(R.string.code);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_SUBJECT, code);
        intent.putExtra(Intent.EXTRA_TEXT, s);
        intent.setType(TEXT_PLAIN);
        startActivity(Intent.createChooser(intent, null));
    }

    // shareQRCode
    private void shareQRCode(String s)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title = getString(R.string.appName);
        String code = getString(R.string.code);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_SUBJECT, code);
        intent.setType(IMAGE_PNG);

        Bitmap bitmap = getBitmap(s, BITMAP_SCALE);
        File image = new File(getCacheDir(), CODE_IMAGE);
        try (BufferedOutputStream out = new
             BufferedOutputStream(new FileOutputStream(image)))
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        }

        catch (Exception e) {}

        Uri imageUri = FileProvider
            .getUriForFile(this, FILE_PROVIDER, image);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, null));
    }

    // getDrawable
    private Drawable getDrawable(String code)
    {
        float density = getResources().getDisplayMetrics().density;
        int scale = (int) (BITMAP_SCALE * density);
        Bitmap bitmap = getBitmap(code, scale);
        return new BitmapDrawable(getResources(), bitmap);
    }

    // getBitmap
    private Bitmap getBitmap(String code, int scale)
    {
        try
        {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(code, BarcodeFormat.QR_CODE, 0, 0);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width * scale,
                                                height * scale,
                                                Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    if (matrix.get(x, y))
                        canvas.drawRect(x * scale, y * scale,
                                        (x * scale) + scale,
                                        (y * scale) + scale, paint);
            return bitmap;
        }

        catch (Exception e) {}

        return null;
    }

    // getText
    private void getText()
    {
        textDialog((dialog, id) ->
        {
            switch (id)
            {
            case DialogInterface.BUTTON_POSITIVE:
                TextView text = (TextView)
                    ((Dialog) dialog).findViewById(R.id.code);
                String code = text.getText().toString();

                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Code " + code);

                if (Words.setCode(code))
                {
                    showToast(R.string.newCode);
                    if (row == 0 && letter == 0)
                        refresh();
                }

                else
                    showToast(R.string.notRecognised);
                break;
            }
        });
    }

    // textDialog
    private void textDialog(DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.enterCode);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton(android.R.string.ok, listener);
        builder.setNegativeButton(android.R.string.cancel, null);

        // Create edit text
        EditText text = new EditText(builder.getContext());
        text.setId(R.id.code);
        text.setHint(R.string.code);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog dialog = builder.create();
        dialog.setView(text, 40, 0, 40, 0);
        dialog.show();
    }

    // getImage
    private void getImage()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            startActivityForResult(intent, REQUEST_IMAGE);
        }

        catch (Exception e) {}
    }

    // theme
    private void theme(int theme)
    {
        this.theme = theme;
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M)
            recreate();
    }

    // highlight
    private void highlight()
    {
        colourDialog();
    }

    // colourDialog
    private void colourDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectHighlight);
        builder.setIcon(R.drawable.ic_launcher);

        View view = ((LayoutInflater) builder.getContext()
                     .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.colours, null);
        builder.setView(view);

        builder.setNeutralButton(R.string.reset, (dialog, id) ->
        {
            contains = getColour(YELLOW);
            correct = getColour(GREEN);
            wrong = getColour(GREY);
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, (dialog, id) ->
        {
            ViewGroup those = (ViewGroup) ((Dialog) dialog)
                .findViewById(R.id.those);
            ViewGroup words = (ViewGroup) ((Dialog) dialog)
                .findViewById(R.id.words);
            contains = ((TextView) those.getChildAt(2)).getTextColors()
                .getDefaultColor();
            correct = ((TextView) words.getChildAt(0)).getTextColors()
                .getDefaultColor();
            wrong = ((TextView) those.getChildAt(0)).getTextColors()
                .getDefaultColor();
        });

        Dialog dialog = builder.show();

        int grey[] = {0, 1, 4};
        ViewGroup those = (ViewGroup) dialog.findViewById(R.id.those);
        for (int l: grey)
        {
            TextView t = (TextView) those.getChildAt(l);
            t.setTextColor(wrong);
        }

        int cont[] = {2, 3};
        for (int l: cont)
        {
            TextView t = (TextView) those.getChildAt(l);
            t.setTextColor(contains);
        }

        ViewGroup words = (ViewGroup) dialog.findViewById(R.id.words);
        for (int l = 0; l < words.getChildCount(); l++)
            ((TextView) words.getChildAt(l)).setTextColor(correct);

        View.OnTouchListener listener = (v, event) ->
        {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int width = v.getWidth();
            int height = v.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                                                Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            int colour = bitmap.getPixel(x, y);
            switch (v.getId())
            {
            case R.id.contains:
                ((TextView) those.getChildAt(2)).setTextColor(colour);
                ((TextView) those.getChildAt(3)).setTextColor(colour);
                break;

            case R.id.correct:
                for (int l = 0; l < words.getChildCount(); l++)
                    ((TextView) words.getChildAt(l)).setTextColor(colour);
                break;
            case R.id.wrong:
                ((TextView) those.getChildAt(0)).setTextColor(colour);
                ((TextView) those.getChildAt(1)).setTextColor(colour);
                ((TextView) those.getChildAt(4)).setTextColor(colour);
            }

            return false;
        };

        view = dialog.findViewById(R.id.contains);
        view.setOnTouchListener(listener);
        view = dialog.findViewById(R.id.correct);
        view.setOnTouchListener(listener);
        view = dialog.findViewById(R.id.wrong);
        view.setOnTouchListener(listener);
    }

    // getColour
    private int getColour(int c)
    {
        switch (c)
        {
        case WHITE:
            return 0xffffffff;

        case YELLOW:
            return 0xffffff00;

        case GREEN:
            return 0xff00ff00;

        case GREY:
            return 0x3fffffff;
        }

        return 0;
    }

    // languageToString
    private static String languageToString(int l)
    {
        switch (l)
        {
        default:
        case ENGLISH:
		return "en";

        case ITALIAN:
		return "it";

        case SPANISH:
		return "es";

        case CATALAN:
		return "ca";

        case FRENCH:
		return "fr";

        case PORTUGUESE:
		return "pt";

        case GERMAN:
		return "de";

        case DUTCH:
		return "nl";

        case AFRIKAANS:
		return "af";

        case HUNGARIAN:
        	return "hu";

        }
    }

    // setLanguage
    private void setLanguage(int l)
    {
        language = l;
        setLanguage();
        refresh();
    }

    // setLanguage
    private void setLanguage()
    {
        Words.setLanguage(this, language);

        switch (language)
        {
        default:
        case ENGLISH:
            getActionBar().setSubtitle(R.string.english);
            break;

        case ITALIAN:
            getActionBar().setSubtitle(R.string.italian);
            break;

        case SPANISH:
            getActionBar().setSubtitle(R.string.spanish);
            break;

        case CATALAN:
            getActionBar().setSubtitle(R.string.catalan);
            break;

        case FRENCH:
            getActionBar().setSubtitle(R.string.french);
            break;

        case PORTUGUESE:
            getActionBar().setSubtitle(R.string.portuguese);
            break;

        case GERMAN:
            getActionBar().setSubtitle(R.string.german);
            break;

        case DUTCH:
            getActionBar().setSubtitle(R.string.dutch);
            break;

        case AFRIKAANS:
            getActionBar().setSubtitle(R.string.afrikaans);
            break;

        case HUNGARIAN:
            getActionBar().setSubtitle(R.string.hungarian);
            break;
        }
    }

    // confetti
    private void confetti(MenuItem item)
    {
        confetti = !confetti;
        item.setChecked(confetti);
    }

    // fanfare
    private void fanfare(MenuItem item)
    {
        fanfare = !fanfare;
        item.setChecked(fanfare);
    }

    // help
    private void help()
    {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    // swap
    private boolean swap(View view)
    {
        swap = !swap;
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M)
            recreate();

        return true;
    }

    // lock
    private boolean lock(View view)
    {
        if (((TextView) view).length() == 0)
            return true;

        ViewGroup grid = findViewById(R.id.puzzle);
        if (grid.indexOfChild(view) / SIZE < row)
        {
            actionView = (TextView) view;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                return false;

            actionMode = view.startActionMode(actionModeCallback,
                                              ActionMode.TYPE_FLOATING);
            return true;
        }

        int col = grid.indexOfChild(view) % SIZE;
        locked[col] = !locked[col];

        ((TextView) view).setTextColor(locked[col]? getColour(GREY):
                                       getColour(WHITE));
        return true;
    }

    // search
    private void search(View view)
    {
        if (actionMode != null)
            actionMode.finish();

        StringBuilder guess = new StringBuilder();
        ViewGroup grid = findViewById(R.id.puzzle);
        int row = grid.indexOfChild(view) / SIZE;

        for (int col = 0; col < SIZE; col++)
            guess.append(((TextView) display[row][col]).getText());

        if (guess.length() != SIZE)
        {
            showToast(R.string.finish);
            return;
        }

        if (!Words.isWord(removeAccents(guess)))
        {
            showToast(R.string.notListed);
            return;
        }

        // Start the web search
        Intent intent = new Intent(this, Search.class);
        intent.putExtra(WORD, guess.toString().toLowerCase(Locale.getDefault()));
        intent.putExtra(LANG, languageToString(language));
        startActivity(intent);
    }

    // about
    @SuppressWarnings("deprecation")
    private void about()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.appName);
        builder.setIcon(R.drawable.ic_launcher);

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        SpannableStringBuilder spannable =
            new SpannableStringBuilder(getText(R.string.version));
        Pattern pattern = Pattern.compile("%s");
        Matcher matcher = pattern.matcher(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              BuildConfig.VERSION_NAME);
        matcher.reset(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              dateFormat.format(BuildConfig.BUILT));
        builder.setMessage(spannable);

        // Add the buttons
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNeutralButton(R.string.more, (d, i) ->
        {
            showToast(word);
            solved = true;
        });

        // Create the AlertDialog
        Dialog dialog = builder.show();

        // Set movement method
        TextView text = dialog.findViewById(android.R.id.message);
        if (text != null)
        {
            text.setTextAppearance(builder.getContext(),
                                   android.R.style.TextAppearance_Small);
            text.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    // removeAccents
    private static String removeAccents(CharSequence cs)
    {
        return Normalizer.normalize(cs, Normalizer.Form.NFKD)
            .replaceAll("\\p{M}", "");
    }

    // showToast
    private void showToast(int key, String s)
    {
        String format = getString(key);
        String text = String.format(format, s);
        showToast(text);
    }

    // showToast
    private void showToast(int key)
    {
        String text = getString(key);
        showToast(text);
    }

    // showToast
    @SuppressWarnings("deprecation")
    private void showToast(String text)
    {
        // Cancel the last one
        if (toast != null)
            toast.cancel();

        // Make a new one
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        // Fix for android 13
        View view = toast.getView();
        if (view != null && Build.VERSION.SDK_INT > VERSION_CODE_S_V2)
            view.setBackgroundResource(R.drawable.toast_frame);
        toast.show();
    }
}
