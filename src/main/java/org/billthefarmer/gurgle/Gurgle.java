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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Gurgle class
public class Gurgle extends Activity
{
    public static final String TAG = "Gurgle";
    public static final String ROW = "row";
    public static final String KEYS = "keys";
    public static final String DATA = "data";
    public static final String WORD = "word";
    public static final String SOLVED = "solved";
    public static final String LETTER = "letter";
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
    public static final String PREF_LANG = "pref_lang";
    public static final String PREF_CONT = "pref_cont";
    public static final String PREF_CORR = "pref_corr";
    public static final String FILE_PROVIDER =
        "org.billthefarmer.gurgle.fileprovider";

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

    public static final int REQUEST_IMAGE = 1;

    public static final int KEYBOARD[] =
    {
        R.id.keys1, R.id.keys2, R.id.keys3
    };

    public static final int ROWS[] =
    {
        R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6
    };

    public static final int ENGLISH    = 0;
    public static final int ITALIAN    = 1;
    public static final int SPANISH    = 2;
    public static final int CATALAN    = 3;
    public static final int FRENCH     = 4;
    public static final int PORTUGUESE = 5;
    public static final int GERMAN     = 6;

    public static final int BITMAP_SCALE = 8;

    private TextView display[][];
    private Map<String, TextView> keyboard;
    private Toast toast;
    private String word;
    private boolean solved;
    private int language;
    private int contains;
    private int correct;
    private int letter;
    private int theme;
    private int mode;
    private int row;

    // On create
    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        theme = preferences.getInt(PREF_THEME, DARK);
        language = preferences.getInt(PREF_LANG, ENGLISH);
        contains = preferences.getInt(PREF_CONT, getColour(YELLOW));
        correct = preferences.getInt(PREF_CORR, getColour(GREEN));

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
        }

        setContentView(R.layout.main);

        setLanguage();

        keyboard = new HashMap<String, TextView>();
        for (int id: KEYBOARD)
        {
            ViewGroup group = (ViewGroup) findViewById(id);
            for (int i = 0; i < group.getChildCount(); i++)
            {
                View view = group.getChildAt(i);
                if (view instanceof TextView)
                {
                    view.setOnClickListener((v) -> keyClicked(v));
                    keyboard.put(((TextView) view).getText().toString(),
                                 (TextView) view);
                }
            }
        }

        View view = findViewById(R.id.enter);
        view.setOnClickListener((v) -> enterClicked(v));
        view = findViewById(R.id.back);
        view.setOnClickListener((v) -> backspaceClicked(v));

        display = new TextView[ROWS.length][];
        int row = 0;
        for (int id: ROWS)
        {
            ViewGroup group = (ViewGroup) findViewById(id);
            display[row] = new TextView[group.getChildCount()];
            for (int i = 0; i < group.getChildCount(); i++)
            {
                display[row][i] = (TextView) group.getChildAt(i);
                display[row][i].setOnClickListener((v) -> search());
            }

            row++;
        }

        if (savedInstanceState != null)
        {
            row = savedInstanceState.getInt(ROW);
            word = savedInstanceState.getString(WORD);
            letter = savedInstanceState.getInt(LETTER);
            solved = savedInstanceState.getBoolean(SOLVED);

            List<String> letters =
                savedInstanceState.getStringArrayList(LETTERS);
            List<Integer> colours =
                savedInstanceState.getIntegerArrayList(COLOURS);

            for (int i = 0; i < letters.size(); i++)
            {
                TextView text = display[i % display.length][i / display.length];
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

        List<String> letters =
            savedInstanceState.getStringArrayList(LETTERS);
        List<Integer> colours =
            savedInstanceState.getIntegerArrayList(COLOURS);

        for (int i = 0; i < letters.size(); i++)
        {
            TextView text = display[i / display[0].length]
                [i % display[0].length];
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
    public void onPause()
    {
        super.onPause();

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREF_THEME, theme);
        editor.putInt(PREF_LANG, language);
        editor.putInt(PREF_CONT, contains);
        editor.putInt(PREF_CORR, correct);
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

        case R.id.getText:
            getText();
            break;

        case R.id.getImage:
            getImage();
            break;

        case R.id.help:
            help();
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

    // decodeImage
    private boolean decodeImage(Bitmap bitmap)
    {
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
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
        if (letter < display[row].length)
        {
            CharSequence s = ((TextView)v).getText();
            display[row][letter++].setText(s);
        }

        else
            showToast(R.string.press);
    }

    // enterClicked
    public void enterClicked(View v)
    {
        if (letter != display[row].length)
        {
            showToast(R.string.finish);
            return;
        }

        StringBuilder guess = new StringBuilder();
        for (TextView t: display[row])
            guess.append(t.getText());

        if (!Words.isWord(guess.toString()))
        {
            showToast(R.string.notListed);
            return;
        }

        if (word.contentEquals(guess))
            solved = true;

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
                    let.setTextColor(getColour(GREY));
                if (key.getTextColors().getDefaultColor() != correct &&
                    key.getTextColors().getDefaultColor() != contains)
                    key.setTextColor(getColour(GREY));
            }
        }

        letter = 0;
        row = (row + 1) % ROWS.length;
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
            display[row][--letter].setText("");
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
    private void theme(int c)
    {
        theme = c;
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
        });

        Dialog dialog = builder.show();

        int grey[] = {0, 1, 4};
        ViewGroup those = (ViewGroup) dialog.findViewById(R.id.those);
        for (int l: grey)
        {
            TextView t = (TextView) those.getChildAt(l);
            t.setTextColor(getColour(GREY));
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
            }

            return false;
        };

        view = dialog.findViewById(R.id.contains);
        view.setOnTouchListener(listener);
        view = dialog.findViewById(R.id.correct);
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
            return 0x7fffffff;
        }

        return 0;
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
        }
    }

    // help
    private void help()
    {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    // search
    private void search()
    {
        if (!solved)
        {
            showToast(R.string.guess);
            return;
        }

        // Start the web search
        Intent intent = new Intent(this, Search.class);
        intent.putExtra(WORD, word.toLowerCase(Locale.getDefault()));
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

        // Add the button
        builder.setPositiveButton(android.R.string.ok, null);

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

    // showToast
    void showToast(int key)
    {
        String text = getString(key);

        // Cancel the last one
        if (toast != null)
            toast.cancel();

        // Make a new one
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
